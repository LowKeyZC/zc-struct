package com.example.struct.controller;

import com.example.struct.result.ResultEnum;
import com.example.struct.result.ZcResult;
import com.example.struct.schedule.QuartzService;
import com.example.struct.common.Constant;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledFuture;

/**
 * 启动停止定时任务
 * @author zhouchao
 */
@RestController
@RequestMapping(value = "dynamicTask")
public class DynamicTaskController {

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 启动任务1
     * @param cron cron字符串
     * @return
     */
    @RequestMapping("start1")
    public ZcResult start1(String cron) {
        // 判断定时任务不存在或被取消
        if (!QuartzService.scheduledFutureMap.containsKey(Constant.DYNAMIC_TASK1) ||
                QuartzService.scheduledFutureMap.get(Constant.DYNAMIC_TASK1).isCancelled()) {
            ScheduledFuture future = threadPoolTaskScheduler.schedule(new QuartzService.MyRunnable1(), new CronTrigger(cron));
            QuartzService.scheduledFutureMap.put(Constant.DYNAMIC_TASK1, future);
            return ZcResult.success();
        }
        return ZcResult.result(ResultEnum.TaskStarted);
    }

    /**
     * 取消定时任务1
     * @return
     */
    @RequestMapping("stop1")
    public ZcResult stop1() {
        ScheduledFuture future = QuartzService.scheduledFutureMap.get(Constant.DYNAMIC_TASK1);
        if (future != null) {
            future.cancel(true);
        }
        return ZcResult.success();
    }

    @RequestMapping("start2")
    public ZcResult start2(String cron) {
        if (!QuartzService.scheduledFutureMap.containsKey(Constant.DYNAMIC_TASK2) ||
                QuartzService.scheduledFutureMap.get(Constant.DYNAMIC_TASK2).isCancelled()) {
            ScheduledFuture future = threadPoolTaskScheduler.schedule(new QuartzService.MyRunnable2(), new CronTrigger(cron));
            QuartzService.scheduledFutureMap.put(Constant.DYNAMIC_TASK2, future);
            return ZcResult.success();
        }
        return ZcResult.result(ResultEnum.TaskStarted);
    }

    @RequestMapping("stop2")
    public ZcResult stop2() {
        ScheduledFuture future = QuartzService.scheduledFutureMap.get(Constant.DYNAMIC_TASK2);
        if (future != null) {
            future.cancel(true);
        }
        return ZcResult.success();
    }
}
