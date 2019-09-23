package com.example.struct.controller;

import com.example.struct.enums.EnumDefine.DynamicTask;
import com.example.struct.result.ResultEnum;
import com.example.struct.result.ZcResult;
import com.example.struct.schedule.QuartzService;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * 启动停止定时任务
 * @author zhouchao
 */
@RestController
@RequestMapping(value = "dynamicTask")
public class DynamicTaskController {

    private static final Logger logger = LoggerFactory.getLogger(DynamicTaskController.class);

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 启动任务
     *
     * @param cron cron字符串
     * @return
     */
    @RequestMapping("start")
    public ZcResult start1(String taskName, String cron) {
        // TODO 验证工具有问题！！！
        /*if (!CronSequenceGenerator.isValidExpression(cron)) {
            logger.error("illegal cron:{}", cron);
            return ZcResult.result(ResultEnum.ERROR_CRON);
        }*/
        DynamicTask task = EnumUtils.getEnum(DynamicTask.class,taskName);
        if (Objects.isNull(task)) {
            return ZcResult.result(ResultEnum.NOTFOUND_TASK);
        }
        // 判断定时任务不存在或被取消
        if (!QuartzService.scheduledFutureMap.containsKey(taskName) ||
            QuartzService.scheduledFutureMap.get(taskName).isCancelled()) {
            ScheduledFuture future = threadPoolTaskScheduler.schedule(
                Objects.requireNonNull(QuartzService.initRunnable(taskName)), new CronTrigger(cron));
            QuartzService.scheduledFutureMap.put(taskName, future);
            return ZcResult.success();
        }
        return ZcResult.result(ResultEnum.TaskStarted);
    }

    /**
     * 取消定时任务
     * @return
     */
    @RequestMapping("stop")
    public ZcResult stop1(String taskName) {
        ScheduledFuture future = QuartzService.scheduledFutureMap.get(taskName);
        if (future != null) {
            future.cancel(true);
        }
        return ZcResult.success();
    }
}
