package com.example.struct.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateHelper {
  private static Logger logger = LoggerFactory.getLogger(DateHelper.class);
  private static DateHelper.MySimpleDateFormat y4M2d2_minus = new DateHelper.MySimpleDateFormat("yyyy-MM-dd");
  private static DateHelper.MySimpleDateFormat y4M2d2 = new DateHelper.MySimpleDateFormat("yyyyMMdd");
  private static DateHelper.MySimpleDateFormat y4m2d2h2m2s2 = new DateHelper.MySimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static DateHelper.MySimpleDateFormat M2d2h2m2 = new DateHelper.MySimpleDateFormat("MM-dd HH:mm");
  private static DateHelper.MySimpleDateFormat y4M2d2h2m2s = new DateHelper.MySimpleDateFormat("yyyyMMddHHmmss");
  public static final long ONE_MINUTE_MS = 60000L;
  public static final long ONE_HOUR_MS = 3600000L;
  public static final long ONE_DAY_MS = 86400000L;
  public static final long ONE_YEAR_MS = 31536000000L;

  public DateHelper() {
  }

  public static Date str2date_4y2M2d_minus(String date) {
    if (StringUtils.isEmpty(date)) {
      return null;
    } else {
      try {
        return y4M2d2_minus.parse(date);
      } catch (Exception var2) {
        var2.printStackTrace();
        logger.error("convert date:" + date, var2);
        return null;
      }
    }
  }

  public static Date str2date_4y2M2d(String date) {
    if (StringUtils.isEmpty(date)) {
      return null;
    } else {
      try {
        return y4M2d2.parse(date);
      } catch (Exception var2) {
        var2.printStackTrace();
        logger.error("convert date:" + date, var2);
        return null;
      }
    }
  }

  public static Date str2date_4y2M2dH2m2s2_minus(String date) {
    if (StringUtils.isEmpty(date)) {
      return null;
    } else {
      try {
        return y4m2d2h2m2s2.parse(date);
      } catch (Exception var2) {
        var2.printStackTrace();
        logger.error("convert date:" + date, var2);
        return null;
      }
    }
  }

  public static String date2Str_4y2M2d(Date date) {
    return date != null ? y4M2d2.formatLocal(date) : y4M2d2.formatLocal(new Date());
  }

  public static String date2Str_2M2d2h2m(Date date) {
    return date != null ? M2d2h2m2.formatLocal(date) : M2d2h2m2.formatLocal(new Date());
  }

  public static String date2Str_2M2d2h2m2s(Date date) {
    return date != null ? y4M2d2h2m2s.formatLocal(date) : y4M2d2h2m2s.formatLocal(new Date());
  }

  public static String date2Str_4y2M2d_minus(Date date) {
    return date != null ? y4M2d2_minus.formatLocal(date) : y4M2d2_minus.formatLocal(new Date());
  }

  public static String date2Str_4y2M2d2H2m2s(Date date) {
    return date != null ? y4m2d2h2m2s2.formatLocal(date) : y4m2d2h2m2s2.formatLocal(new Date());
  }

  public static Date dateTrunc_4y(Date date) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    cl.set(Calendar.MONTH, 0);
    cl.set(Calendar.DATE, 0);
    cl.set(Calendar.HOUR_OF_DAY, 0);
    cl.set(Calendar.MINUTE, 0);
    cl.set(Calendar.SECOND, 0);
    cl.set(Calendar.MILLISECOND, 0);
    return cl.getTime();
  }

  public static Date dateTruncDayBegin(Date date) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    cl.set(Calendar.HOUR_OF_DAY, 0);
    cl.set(Calendar.MINUTE, 0);
    cl.set(Calendar.SECOND, 0);
    cl.set(Calendar.MILLISECOND, 0);
    return cl.getTime();
  }

  public static Date dateTruncDayEnd(Date date) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    cl.set(Calendar.HOUR_OF_DAY, 0);
    cl.set(Calendar.MINUTE, 0);
    cl.set(Calendar.SECOND, 0);
    cl.set(Calendar.MILLISECOND, 0);
    cl.add(5, 1);
    return cl.getTime();
  }

  public static Date dateAddDay(Date date, int day) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    cl.add(Calendar.DATE, day);
    return cl.getTime();
  }

  public static Date dateAddMonth(Date date, int month) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    cl.add(Calendar.MONTH, month);
    return cl.getTime();
  }

  public static Date dateAddHourOfDay(Date date, int hour) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    cl.set(Calendar.HOUR_OF_DAY, 0);
    cl.set(Calendar.MINUTE, 0);
    cl.set(Calendar.SECOND, 0);
    cl.set(Calendar.MILLISECOND, 0);
    cl.add(Calendar.HOUR_OF_DAY, hour);
    return cl.getTime();
  }

  public static Date dateAddHour(Date date, int hour) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    cl.add(Calendar.HOUR, hour);
    return cl.getTime();
  }

  public static Date str2nextDateBegin4y2M2d_minus(String dateStr) {
    return dateTruncDayEnd(str2date_4y2M2d_minus(dateStr));
  }

  public static Date dateAddMinute(Date date, int minute) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    cl.add(Calendar.MINUTE, minute);
    return cl.getTime();
  }

  public static int getWeekday(Date date) {
    Calendar cl = Calendar.getInstance();
    cl.setTime(date);
    int day = cl.get(Calendar.DAY_OF_WEEK) - 1;
    return day == 0 ? 7 : day;
  }

  private static class MySimpleDateFormat extends SimpleDateFormat {
    private static final long serialVersionUID = -3573203452927518511L;

    public MySimpleDateFormat(String format) {
      super(format);
    }

    @Override
    public Date parse(String format) throws ParseException {
      Class var2 = DateHelper.class;
      synchronized(DateHelper.class) {
        return super.parse(format);
      }
    }

    public String formatLocal(Date date) {
      Class var2 = DateHelper.class;
      synchronized(DateHelper.class) {
        return super.format(date);
      }
    }
  }
}
