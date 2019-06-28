package com.example.struct.controller;

import com.example.struct.result.ZcResult;
import com.example.struct.schedule.QuartzService;
import com.example.struct.schedule.ScheduleFutures;
import org.springframework.jca.context.SpringContextResourceAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledFuture;

/**
 * todo future 每次请求会被更新，无法停止之前的定时任务
 * @author zhouchao
 */
@RestController
@RequestMapping(value = "dynamicTask")
public class DynamicTaskController {

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Resource
    private ScheduleFutures scheduleFutures;

    @RequestMapping("start1")
    public ZcResult start1(String cron) {
        ScheduledFuture future = threadPoolTaskScheduler.schedule(new QuartzService.MyRunnable1(), new CronTrigger(cron));
        scheduleFutures.setDynamicTaskFuture1(future);
        return ZcResult.success();
    }

    @RequestMapping("stop1")
    public ZcResult stop1() {
        ScheduledFuture future = scheduleFutures.getDynamicTaskFuture1();
        if (future != null) {
            future.cancel(true);
        }
        return ZcResult.success();
    }

    @RequestMapping("start2")
    public ZcResult start2(String cron) {
        ScheduledFuture future = threadPoolTaskScheduler.schedule(new QuartzService.MyRunnable2(), new CronTrigger(cron));
        scheduleFutures.setDynamicTaskFuture2(future);
        return ZcResult.success();
    }

    @RequestMapping("stop2")
    public ZcResult stop2() {
        ScheduledFuture future = scheduleFutures.getDynamicTaskFuture2();
        if (future != null) {
            future.cancel(true);
        }
        return ZcResult.success();
    }
}
