package com.example.struct.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

public class DateUtil {

    /**
     * 计算原时间对应偏移量后的时间
     * @param sourceDate 原时间
     * @param seconds 偏移秒数
     * @return 偏移后的时间
     */
    public static Date dateOffset(Date sourceDate, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDate);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * 计算原时间当天对应时分秒的时间
     * @param sourceDate 原时间
     * @param hour 时
     * @param minute 分
     * @param second 秒
     * @return 当天对应时分秒的时间
     */
    public static Date getDayTime(Date sourceDate, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        System.out.println(dateOffset(new Date(), 24 * 60 * 60));
    }
}
