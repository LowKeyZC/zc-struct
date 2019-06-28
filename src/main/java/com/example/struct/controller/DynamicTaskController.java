package com.example.struct.controller;

import com.example.struct.result.ResultEnum;
import com.example.struct.result.ZcResult;
import com.example.struct.schedule.QuartzService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledFuture;

/**
 * @author zhouchao
 */
@RestController
@RequestMapping(value = "dynamicTask")
public class DynamicTaskController {

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @RequestMapping("start1")
    public ZcResult start1(String cron) {
        if (QuartzService.dynamicTaskFuture1 == null) {
            QuartzService.dynamicTaskFuture1 = threadPoolTaskScheduler.schedule(new QuartzService.MyRunnable1(), new CronTrigger(cron));
            return ZcResult.success();
        }
        return ZcResult.result(ResultEnum.TaskStarted);
    }

    @RequestMapping("stop1")
    public ZcResult stop1() {
        ScheduledFuture future = QuartzService.dynamicTaskFuture1;
        if (future != null) {
            future.cancel(true);
        }
        return ZcResult.success();
    }

    @RequestMapping("start2")
    public ZcResult start2(String cron) {
        if (QuartzService.dynamicTaskFuture2 == null) {
            QuartzService.dynamicTaskFuture2 = threadPoolTaskScheduler.schedule(new QuartzService.MyRunnable2(), new CronTrigger(cron));
            return ZcResult.success();
        }
        return ZcResult.result(ResultEnum.TaskStarted);
    }

    @RequestMapping("stop2")
    public ZcResult stop2() {
        ScheduledFuture future = QuartzService.dynamicTaskFuture2;
        if (future != null) {
            future.cancel(true);
        }
        return ZcResult.success();
    }
}
