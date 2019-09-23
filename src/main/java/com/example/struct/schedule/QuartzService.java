package com.example.struct.schedule;

import com.example.struct.common.Constant;
import com.example.struct.enums.EnumDefine;
import com.example.struct.enums.EnumDefine.DynamicTask;
import com.example.struct.enums.RedisDomainEnum;
import com.example.struct.util.RandomUtil;
import com.example.struct.util.RedisUtil;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static com.example.struct.enums.EnumDefine.DynamicTask.TASK_2;

/**
 * 配置说明:https://www.cnblogs.com/linjiqin/archive/2013/07/08/3178452.html
 * Seconds Minutes Hours DayofMonth Month DayofWeek Year或
 * Seconds Minutes Hours DayofMonth Month DayofWeek
 *
 0 0 10,14,16 * * ? 每天上午10点，下午2点，4点
 0 0/30 9-17 * * ? 朝九晚五工作时间内每半小时
 0 0 12 ? * WED 表示每个星期三中午12点
 "0 0 12 * * ?" 每天中午12点触发
 "0 15 10 ? * *" 每天上午10:15触发
 "0 15 10 * * ?" 每天上午10:15触发
 "0 15 10 * * ? *" 每天上午10:15触发
 "0 15 10 * * ? 2005" 2005年的每天上午10:15触发
 "0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
 "0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
 "0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
 "0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
 "0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
 "0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发
 "0 15 10 15 * ?" 每月15日上午10:15触发
 "0 15 10 L * ?" 每月最后一日的上午10:15触发
 "0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发
 "0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
 "0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发
 * @author zhouchao
 */
@Component
public class QuartzService {
    @Resource
    private RedisUtil redisUtil;

    // 动态定时任务运行结果Map,用于根据任务名动态停止定时任务
    public static Map<String, ScheduledFuture> scheduledFutureMap = new HashMap<>();

    /**
     * 通过redis分布式锁实现定时任务只执行一次
     * 每隔900秒触发一次
     */
//    @Scheduled(cron = "0/3 * * * * ?")
    public void task01() {
        System.out.println("=====进入task01");
        if (redisUtil.tryLock(Constant.LOCK_1, 600)) {
            try {
                System.out.println(new Date() + "=====task01抢到锁");
                System.out.println("nowTime=" + new Date() + "--task01执行任务");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                redisUtil.releaseLock(Constant.LOCK_1);
                System.out.println("=====task01释放锁");
            }
        } else {
            System.out.println(new Date() + "抢锁失败！");
        }
    }

    /**
     * 动态定时任务运行实例1
     */
    public static class MyRunnable1 implements Runnable {
        @Override
        public void run() {
            System.out.println("动态定时任务运行实例1,time=" + new Date());
        }
    }

    /**
     * 动态定时任务运行实例2
     */
    public static class MyRunnable2 implements Runnable {
        @Override
        public void run() {
            System.out.println("动态定时任务运行实例2,time=" + new Date());
        }
    }

    public static Runnable initRunnable(String taskName) {
        DynamicTask task = EnumUtils.getEnum(DynamicTask.class, taskName);
        /* 任务方法映射 */
        switch (task) {
            case TASK_1:
                return new MyRunnable1();
            case TASK_2:
                return new MyRunnable2();
            default:
                return null;
        }
    }

    public static class DelayTask1 implements Runnable {
        @Override
        public void run() {
            System.out.println("delaytask:" + new Date());
        }
    }

}