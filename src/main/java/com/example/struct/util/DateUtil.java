package com.example.struct.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /**
     * 判断当前时间在输入时间之间，支持:yyyy/MM/dd HH:mm:ss, HH:mm:ss, yyyy/MM/dd
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @throws ParseException
     */
    public static boolean isBetweenTime(String startTime, String endTime) throws ParseException {
        Calendar now = Calendar.getInstance();
        String formatStr;
        if (startTime.length() == 19) {
            formatStr = "yyyy/MM/dd HH:mm:ss";
        } else if (startTime.length() == 8) {
            formatStr = "HH:mm:ss";
        } else if (startTime.length() == 10){
            formatStr = "yyyy/MM/dd";
        } else {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        now.setTime(sdf.parse(sdf.format(new Date())));
        Calendar start = Calendar.getInstance();
        start.setTime(sdf.parse(startTime));
        Calendar end = Calendar.getInstance();
        end.setTime(sdf.parse(endTime));
        return now.after(start) && now.before(end);
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(isBetweenTime("2019/06/11", "2019/06/16"));
    }
}
