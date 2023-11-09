package com.isycores.driver.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class DateUtils {
  public static final Date MAX_DATE = new Date(Long.MAX_VALUE);

  public static final String YEAR_FORMAT = "yyyy-";
  public static final String MONTH_FORMAT = "yyyyMM";
  public static final String MONTH_FORMAT2 = "yyyy-MM";
  public static final String MONTH_FORMAT3 = "yyyy年MM月";

  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATE_FORMAT2 = "yyyyMMdd";
  public static final String DATE_FORMAT3 = "yyMMdd";
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_TIME_FORMAT2 = "yyyyMMddHHmmss";
  public static final String DATE_TIME_FORMAT3 = "yyyyMMddHHmmssSSS";
  public static final String DATE_TIME_FORMAT4 = "yyyyMMddHHmm";
  public static final String DATE_TIME_FORMAT5 = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String DATE_TIME_FORMAT6 = "yyyy/MM/dd";

  public static final String DATE_TIME_FORMAT7 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  public static final String DATE_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";

  public static final String YEAR_PATTERN = "\\d{4}-";
  public static final String DATE_PATTERN = "\\d{4}(-\\d\\d){2}";
  public static final String DATE_TIME_PATTERN = "\\d{4}(-\\d\\d){2} \\d\\d(:\\d\\d){2}";
  public static final String DATE_PATTERN2 = "\\d{8}";
  public static final String MONTH_PATTERN2 = "\\d{4}-\\d\\d";
  public static final String DATE_TIME_PATTERN2 = "\\d{14}";
  public static final String DATE_TIME_PATTERN3 = "[19|20]\\d{10}";//以19xx或20xx开头的12位数字
  public static final String DATE_TIME_PATTERN4 = "\\d{10}";
  public static final String DATE_TIME_PATTERN7 = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}\\+\\d{4}";
  public static final String MONTH_PATTERN = "\\d{6}";
  public static final String DATE_MINUTE_PATTERN = "\\d{4}(-\\d\\d){2} \\d\\d:\\d\\d";
  public static final String DATE_MILLISECOND_PATTERN = "\\d{4}(-\\d\\d){2} \\d\\d:\\d\\d:\\d\\d.\\d\\d\\d";
  public static final String DATE_TIMESTAMP = "^[-|+]?\\d+$";// 正号或者负号开头的整数
  private static final String ERROR = "日期转换失败:\n";

  private DateUtils() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * 获取传入日期所在的周日
   *
   * @return
   * @wujiang
   */
  public static Date getSundayOfWeek(Date date) {
    Calendar calendar = Calendar.getInstance();
    if (date != null) {
      calendar.setTime(date);
    }
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    if (dayOfWeek == 0) {
      dayOfWeek = 7;
    }
    calendar.add(Calendar.DATE, -dayOfWeek + 7);
    return calendar.getTime();
  }


  /**
   * 获取指定日期的开始时间
   *
   * @return
   * @wujiang
   */
  public static Date getSpecifyDate(Integer day) {
    return getStartOfDay(addDay(new Date(), day));
  }


  /**
   * 获取当前时间之前x日的00:00:00.
   *
   * @param day 应为负数
   */
  public static Date minus(int day) {
    if (day == 1) {
      day = 0;
    }
    day = -day;
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, day);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  public static Date add(int day) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, day);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  /**
   * 获取昨日时间.
   *
   * @return 昨日时间
   */
  public static Date getYesterday() {
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, -1);
    return cal.getTime();

  }

  /**
   * 计算N分钟的时间.
   *
   * @param date 时间
   * @param minute 分钟
   * @return
   */
  public static Date addMinute(Date date, int minute) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MINUTE, minute);
    return cal.getTime();
  }

  public static Date addMinute(Date date, Long minute) {
    return addMinute(date, minute.intValue());
  }

  /**
   * 获取传入日期所在的周一
   *
   * @return
   */
  public static Date getMondayOfWeek(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date != null ? date : new Date());
    int week = calendar.get(Calendar.DAY_OF_WEEK);
    if (1 == week) {
      week = -6;
    } else {
      week = 2 - week;
    }
    calendar.add(Calendar.DAY_OF_MONTH, week);
    return calendar.getTime();
  }

  /**
   * 获取本周的周一.
   *
   * @return 本周周一的date
   */
  public static Date getMondayOfWeek() {
    return getMondayOfWeek(new Date());
  }

  /**
   * 获取上周的周一.
   *
   * @return 上周周一的date
   */
  public static Date getLastMondayOfWeek() {
    Calendar calendar = Calendar.getInstance();
    int week = calendar.get(Calendar.DAY_OF_WEEK);
    if (1 == week) {
      week = -6;
    } else {
      week = 2 - week;
    }
    calendar.add(Calendar.DAY_OF_MONTH, week);
    calendar.add(Calendar.DATE, -7);
    return calendar.getTime();
  }

  /**
   * 获取本周周末.
   *
   * @return 本周周末
   */
  public static Date getSundayOfWeek() {
    Calendar calendar = Calendar.getInstance();
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    if (dayOfWeek == 0) {
      dayOfWeek = 7;
    }
    calendar.add(Calendar.DATE, -dayOfWeek + 7);
    return calendar.getTime();
  }

  /**
   * 获取当年第一天.
   *
   * @param date 时间
   * @return 当年第一天
   */
  public static Date getStartOfYear(Date date) {
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DATE));
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);// 设置毫秒数为0，防止被进位
    return cal.getTime();
  }


  /**
   * 获取当年最后一天.
   *
   * @param date 时间
   * @return 当年最后一天
   */
  public static Date getEndOfYear(Date date) {
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  /**
   * 获取当月第一天.
   *
   * @param date 时间
   * @return 当月第一天
   */
  public static Date getStartOfMonth(Date date) {
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DATE));
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);// 设置毫秒数为0，防止被进位
    return cal.getTime();
  }


  /**
   * 获取传入时间N月前的时间.
   *
   * @param date 时间
   * @param month 月数
   * @return
   */
  public static Date getStartOfMonth(Date date, int month) {
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, -month);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DATE));
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    return cal.getTime();
  }

  /**
   * 过去6个月得日期.
   *
   * @param date the date
   * @return the last 6 date
   */
  public static Date getLast6Date(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, -6);
    return cal.getTime();
  }

  /**
   * 获取当月最后一天.
   *
   * @param date 时间
   * @return 当月最后一天
   */
  public static Date getEndOfMonth(Date date) {
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  /**
   * 获取一天开始.
   *
   * @param date 时间
   * @return 一天开始时间（00:00:00）
   */
  public static Date getStartOfDay(Date date) {
    if (date == null) {
      return null;
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  /**
   * 获取明天的开始.
   *
   * @param date 时间
   * @return 昨天结束时间（23:59:59）
   */
  public static Date getStartOfTomorrow(Date date) {
    return getStartOfDay(addDay(date, 1));
  }

  /**
   * 获取一天结束.
   *
   * @param date 时间
   * @return 一天结束时间（23:59:59）
   */
  public static Date getEndOfDay(Date date) {
    if (date == null) {
      return null;
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 0);// 设置毫秒数为0，防止被进位
    return cal.getTime();
  }

  /**
   * 获取昨天的结束.
   *
   * @param date 时间
   * @return 昨天结束时间（23:59:59）
   */
  public static Date getEndOfYesterday(Date date) {
    return getEndOfDay(addDay(date, -1));
  }

  /**
   * 获取前天结束时间
   *
   * @param date 时间
   * @return 昨天结束时间（23:59:59）
   */
  public static Date getStartOfYesterday(Date date) {
    return getStartOfDay(addDay(date, -1));
  }

  /**
   * 获取本小时开始.
   *
   * @param date 时间
   * @return 本小时开始时间（xx:00:00）
   */
  public static Date getStartOfHour(Date date) {
    if (date == null) {
      return null;
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);// 设置毫秒数为0，防止被进位
    return cal.getTime();
  }

  /**
   * 获取本小时结束.
   *
   * @param date 时间
   * @return 本小时结束时间（xx:59:59）
   */
  public static Date getEndOfHour(Date date) {
    if (date == null) {
      return null;
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  /**
   * 获取今天是周几
   *
   * @param date
   * @return 周一为1，周日为7
   */
  public static int getDayOfWeek(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    // 一周第一天是否为星期天
    boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
    // 获取周几
    int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
    // 若一周第一天为星期天，则-1
    if (isFirstSunday) {
      weekDay = weekDay - 1;
      if (weekDay == 0) {
        weekDay = 7;
      }
    }
    return weekDay;
  }

  /**
   * 获取一段时间内的工作日天数（只排除周末）
   *
   * @param from
   * @param to
   * @return
   */
  public static int getWeekdays(Date from, Date to) {
    int days = 0;
    if (from == null || to == null) {
      return -1;
    }

    while (!getStartOfDay(from).after(getStartOfDay(to))) {
      if (getDayOfWeek(from) < 6) {
        days++;
      }
      from = addDay(from, 1);
    }
    return days;
  }

  /**
   * 获取一段时间内的周末天数
   *
   * @param from
   * @param to
   * @return
   */
  public static int getWeekends(Date from, Date to) {
    int days = 0;
    if (from == null || to == null) {
      return -1;
    }

    while (!getStartOfDay(from).after(getStartOfDay(to))) {
      if (getDayOfWeek(from) >= 6) {
        days++;
      }
      from = addDay(from, 1);
    }
    return days;
  }

  /**
   * 求出两个date之间的天数，如2018-01-01到2018-01-02为1天.
   *
   * @param from 开始时间
   * @param to 结束时间
   * @return 两个date相隔的时间。单位：天
   */
  public static int differDays(Date from, Date to) {
    if (from == null || to == null) {
      return -1;
    }
    return differSeconds(getStartOfDay(from), getStartOfDay(to)) / 60 / 60 / 24;
  }

  /**
   * 传入两个时间计算出相差几年几月几日（2年11月23天）
   *
   * @param beginDate
   * @param endDate
   * @return
   */
  public static String differYearMonthDays(Date beginDate, Date endDate) {
    StringBuffer data=new StringBuffer();
    Calendar begin = Calendar.getInstance();
    begin.setTime(beginDate);
    Calendar end = Calendar.getInstance();
    end.setTime(endDate);
    int day = end.get(Calendar.DAY_OF_MONTH) - begin.get(Calendar.DAY_OF_MONTH);
    int month = end.get(Calendar.MONTH) - begin.get(Calendar.MONTH);
    int year = end.get(Calendar.YEAR) - begin.get(Calendar.YEAR);
//按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
    if(day<0){
      month -= 1;
      end.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。
      day = day + end.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    if(month<0){
      month = (month+12)%12;
      year-=1;
    }
    if (year>0){
      data.append(year+"年");
    }
    if (month>0){
      data.append(month+"月");
    }
    if (day>0){
      data.append(day+"天");
    }
    return data.toString();
  }

  /**
   * 求出两个date相隔的小时数.
   *
   * @param from 开始时间
   * @param to 结束时间
   * @return 两个date相隔的时间。单位：分钟
   */
  public static int differHours(Date from, Date to) {
    if (from == null || to == null) {
      return -1;
    }
    return differSeconds(from, to) / 60 / 60;
  }

  /**
   * 求出两个date相隔的分钟数.
   *
   * @param from 开始时间
   * @param to 结束时间
   * @return 两个date相隔的时间。单位：分钟
   */
  public static int differMinutes(Date from, Date to) {
    if (from == null || to == null) {
      return -1;
    }
    return differSeconds(from, to) / 60;
  }

  /**
   * 求出两个date相隔的秒数.
   *
   * @param from 开始时间
   * @param to 结束时间
   * @return 两个date相隔的时间。单位：秒
   */
  public static int differSeconds(Date from, Date to) {
    if (from == null || to == null) {
      return -1;
    }
    Double result = Math.ceil((to.getTime() - from.getTime()) / 1000.0);
    return result.intValue();
  }

  /**
   * 秒数格式化为 hh:mm:ss.
   *
   * @param totalSecond 总秒数
   * @return 格式化后的时间字符串
   */
  public static String formatSecond(int totalSecond) {
    DecimalFormat dff = new DecimalFormat("00");
    int hour = totalSecond / (60 * 60);
    int min = (totalSecond - (hour * 60 * 60)) / 60;
    int second = totalSecond % 60;
    return dff.format(hour) + ":" + dff.format(min) + ":" + dff.format(second);
  }

  /**
   * 获取上月的第一天.
   *
   * @return
   */
  public static Date getStartOfLastMonth() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MONTH, -1);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  /**
   * 获取上月的最后一天.
   *
   * @return
   */
  public static Date getEndOfLastMonth() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.add(Calendar.DATE, -1);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  /**
   * 增加固定天数.
   *
   * @param date 日期
   * @param day 增加的天数
   * @return
   */
  public static Date addDay(Date date, int day) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, day);
    return cal.getTime();
  }

  /**
   * 增加固定月数（自然月）.
   *
   * @param date 日期
   * @param month 增加的月数
   * @return
   */
  public static Date addMonth(Date date, int month) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, month);
    return cal.getTime();
  }

  /**
   * 增加固定年数（自然年）.
   *
   * @param date 日期
   * @param year 增加的年数
   * @return
   */
  public static Date addYear(Date date, int year) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.YEAR, year);
    return cal.getTime();
  }

  /**
   * 获取月份的天数.
   *
   * @param date 当前时间
   * @return 当前时间是本月的第几天
   */
  public static int getDaysOfMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  /**
   * 获取当天时间段(上午/下午).
   *
   * @param date 当前时间
   * @return 当前时间是本月的第几天
   */
  public static String getPeriodOfDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(Calendar.HOUR_OF_DAY) < 12 ? "上午" : "下午";
  }

  /**
   * 把字符串转为date.
   *
   * @param date 时间字符串
   * @return date
   */
  public static Date string2Date(String date) {
    TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    if (Pattern.matches(DATE_PATTERN2, date)) {
      try {
        return new SimpleDateFormat(DATE_FORMAT2).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    } else if (Pattern.matches(MONTH_PATTERN, date)) {
      try {
        return new SimpleDateFormat(MONTH_FORMAT).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    } else if (Pattern.matches(MONTH_PATTERN2, date)) {
      try {
        return new SimpleDateFormat(MONTH_FORMAT2).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    } else if (Pattern.matches(DATE_TIME_PATTERN2, date)) {
      try {
        return new SimpleDateFormat(DATE_TIME_FORMAT2).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    } else if (Pattern.matches(DATE_PATTERN, date)) {
      try {
        return new SimpleDateFormat(DATE_FORMAT).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    } else if (Pattern.matches(DATE_TIME_PATTERN, date)) {
      try {
        return new SimpleDateFormat(DATE_TIME_FORMAT).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    } else if (Pattern.matches(DATE_MINUTE_PATTERN, date)) {
      try {
        return new SimpleDateFormat(DATE_MINUTE_FORMAT).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    } else if (Pattern.matches(DATE_TIME_PATTERN3, date)) {
      try {
        return new SimpleDateFormat(DATE_TIME_FORMAT4).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    } else if (Pattern.matches(DATE_TIME_PATTERN4, date)) {
      date += "000";
      return new Date(Long.parseLong(date));
    } else if (Pattern.matches(YEAR_PATTERN, date)) {
      try {
        return new SimpleDateFormat(YEAR_FORMAT).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    } else if (Pattern.matches(DATE_MILLISECOND_PATTERN, date)) {
      try {
        return new SimpleDateFormat(DATE_TIME_FORMAT5).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    } else if (Pattern.matches(DATE_TIMESTAMP, date)) {
      return new Date(Long.parseLong(date));
    } else if (Pattern.matches(DATE_TIME_PATTERN7, date)) {
      try {
        return new SimpleDateFormat(DATE_TIME_FORMAT7).parse(date);
      } catch (ParseException ex) {
        return null;
      }
    }
    return null;

  }

  public static boolean isToday(Date date) {
    return org.apache.commons.lang3.time.DateUtils.isSameDay(new Date(), date);
  }


  /*
   * lwr
   */
  // 获取时间所在周 的周一至 周日 的 7个时间
  public static Map<String, Date> getWeekStartAndEnd(Date time) {
    try {
      Preconditions.checkArgument(time != null, "传入时间不可为空");
      Map<String, Date> map = new HashMap<>();
      Calendar cal = Calendar.getInstance();
      cal.setTime(time);
      int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
      if (1 == dayWeek) {
        cal.add(Calendar.DAY_OF_MONTH, -1);
      }
      cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
      int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
      cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
      // 开始时间 周一
      map.put("week1", cal.getTime());
      // 循环获取周一到周日的时间
      for (int i = 1; i < 7; i++) {
        cal.add(Calendar.DATE, 1);
        // week1 周一 week2 周二 从 周二开始
        map.put("week" + (i + 1), cal.getTime());
      }
      return map;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 根据字符串的日期获取其一天的开始时间（00:00:00）
   *
   * @param date 时间字符串
   */
  public static String getStartTime(String date) {
    if (StringUtils.isBlank(date)) {
      return null;
    }
    SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_FORMAT);
    return format.format(DateUtils.getStartOfDay(DateUtils.string2Date(date)));
  }

  /**
   * 根据字符串的日期获取其一天的结束时间（23:59:59）
   *
   * @param date 时间字符串
   */
  public static String getEndTime(String date) {
    if (StringUtils.isBlank(date)) {
      return null;
    }
    SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_FORMAT);
    return format.format(DateUtils.getEndOfDay(DateUtils.string2Date(date)));
  }

  /**
   * 根据传入的时间 计算 至今多少年（比如计算年龄 工龄）.
   *
   * @param date 日期
   * @return
   */
  public static int getYearByDate(Date date) {
    return getYearByDate(date, new Date());
  }

  /**
   * 计算传入的开始时间<i>date</i>到<i>截止时间now</i>相差多少年（比如计算年龄 工龄）.
   *
   * @param date 开始时间
   * @param now 截止时间
   * @return
   */
  public static int getYearByDate(Date date, Date now) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(now);
    if (cal.before(date)) {
      return -1;
    }
    int yearNow = cal.get(Calendar.YEAR);
    int monthNow = cal.get(Calendar.MONTH);
    int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

    cal.setTime(date);
    int yearBirth = cal.get(Calendar.YEAR);
    int monthBirth = cal.get(Calendar.MONTH);
    int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
    int age = yearNow - yearBirth;
    if (monthNow < monthBirth) {
      age--;
    }
    if (monthNow == monthBirth && dayOfMonthNow < dayOfMonthBirth) {
      age--;
    }
    return age;
  }

  /**
   * 日期相加减
   *
   * @param time 时间字符串 yyyy-MM-dd HH:mm:ss
   * @param year 年
   * @param month 月
   * @param day 日
   * @return 加上相应的数量的年的日期
   */
  public static Date calculateTheDate(Date time, Integer year, Integer month, Integer day) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    calendar.add(Calendar.YEAR, year);
    calendar.add(Calendar.MONTH, month);
    calendar.add(Calendar.DAY_OF_MONTH, day);
    return calendar.getTime();
  }

  /**
   * 指定日期的 开始时间
   *
   * @param date
   * @param dateTime
   * @return
   */
  public static Date getBeginDateByDateTime(String date, Date dateTime) {
    Date beginDate = null;
    switch (date) {
      case "day":
        beginDate = DateUtils.getStartOfDay(dateTime);
        break;
      case "week":
        beginDate = DateUtils.getStartOfDay(DateUtils.getMondayOfWeek(dateTime));
        break;
      case "month":
        beginDate = DateUtils.getStartOfMonth(dateTime);
        break;
      case "quarter":
        beginDate = DateUtils.getCurrentQuarterStartTime(dateTime);
        break;
      case "year":
        beginDate = DateUtils.getStartOfYear(dateTime);
        break;
      default:
        beginDate = DateUtils.getStartOfDay(DateUtils.getYesterday());
        break;
    }
    return beginDate;
  }

  /**
   * 指定时间的季度开始时间
   *
   * @param date
   * @return
   */
  public static Date getCurrentQuarterStartTime(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int currentMonth = c.get(Calendar.MONTH) + 1;
    try {
      if (currentMonth >= 1 && currentMonth <= 3) {
        c.set(Calendar.MONTH, 0);
      } else if (currentMonth >= 4 && currentMonth <= 6) {
        c.set(Calendar.MONTH, 3);
      } else if (currentMonth >= 7 && currentMonth <= 9) {
        c.set(Calendar.MONTH, 6);

      } else if (currentMonth >= 10 && currentMonth <= 12) {
        c.set(Calendar.MONTH, 9);
      }
      c.set(Calendar.DATE, 1);
      c.set(Calendar.HOUR_OF_DAY, 0);
      c.set(Calendar.MINUTE, 0);
      c.set(Calendar.SECOND, 0);
      c.set(Calendar.MILLISECOND, 0);
    } catch (Exception e) {
    }
    return c.getTime();
  }

  /**
   * 获取当前时间最近的这次日期
   *
   * @param date 日期
   * @param day 天数
   * @return 离本次最近的day日期
   */
  public static Date getLastThisDay(Date date, String day) {
    if (!StringUtils.isNumeric(day)) {
      return DateUtils.getStartOfMonth(date);
    }
    int dayInt = Integer.parseInt(day);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    if (cal.get(Calendar.DAY_OF_MONTH) < dayInt) {
      cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
    }
    cal.set(Calendar.DAY_OF_MONTH, dayInt);
    return DateUtils.getStartOfDay(cal.getTime());
  }


  /**
   * 开始，结束时间有多少个月份.
   *
   * @param startDate 开始日期
   * @param endDate 结束日期
   * @return the months
   * @throws ParseException the parse exception
   */
  public static List<Date> getMonths(Date startDate, Date endDate) {

    List<Date> result = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat(MONTH_FORMAT2);// 格式化为年月

    Calendar min = Calendar.getInstance();
    Calendar max = Calendar.getInstance();

    try {
      min.setTime(sdf.parse(sdf.format(startDate)));
      max.setTime(sdf.parse(sdf.format(endDate)));
    } catch (ParseException e) {
    }
    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 1);
    Calendar curr = min;
    while (curr.before(max)) {
      result.add(curr.getTime());
      curr.add(Calendar.MONTH, 1);
    }
    return result;
  }

  public static Date getMaxDate(Date dateFirst, Date dateSecond) {
    if (dateFirst != null && dateSecond != null) {
      return dateFirst.after(dateSecond) ? dateFirst : dateSecond;
    }
    if (dateFirst != null) {
      return dateFirst;
    }
    return dateSecond;
  }

  public static boolean isThisWeek(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    int currentYear = calendar.get(Calendar.YEAR);
    int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
    calendar.setTime(date);
    int paramYear = calendar.get(Calendar.YEAR);
    int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
    return paramYear == currentYear && paramWeek == currentWeek;
  }
public static String getTime(Date date,Long sjc){
  SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
  // 将传入进来的时间转化为时间戳 ，然后再当前时间戳上加60000ms（1min=60000ms）
  long endTimeStamp = date.getTime() + sjc;
  // 转化计算得到的时间戳
  String endDateTemp = sdf.format(endTimeStamp);
return endDateTemp;
}
  /**
   * 开始，结束时间有多少天
   *
   * @param startDate 开始日期
   * @param endDate 结束日期
   * @return
   */
  public static List<Date> getDays(Date startDate, Date endDate) {

    List<Date> result = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);// 格式化为年月

    Calendar min = Calendar.getInstance();
    Calendar max = Calendar.getInstance();

    try {
      min.setTime(sdf.parse(sdf.format(startDate)));
      max.setTime(sdf.parse(sdf.format(endDate)));
    } catch (ParseException e) {
    }
    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), min.get(Calendar.DAY_OF_MONTH));
    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), max.get(Calendar.DAY_OF_MONTH));
    max.add(Calendar.DAY_OF_MONTH, 1);
    Calendar curr = min;
    while (curr.before(max)) {
      result.add(curr.getTime());
      curr.add(Calendar.DAY_OF_MONTH, 1);
    }
    return result;
  }

  public static String format(Date date, String dateFormat) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    return simpleDateFormat.format(date);
  }

  public static String date2String(Date date) {
    if (date == null) {
      return null;
    }
    return new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT).format(date);
  }

  /**
   * 判断两个时间段是否有交集
   *
   * @param startDate1 开始日期1
   * @param endDate1 结束日期1
   * @param startDate2 开始日期2
   * @param endDate2 结束日期2
   * @return
   */

  public static Boolean overlapped(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
    return (startDate1 == null || endDate2 == null || (!startDate1.after(endDate2))) && (endDate1 == null
        || startDate2 == null || (!endDate1.before(startDate2)));
  }

  // 宋师傅：小于等于16未成年
  //2020年7月2日宋师傅说小于等于17岁未成年
  public static int getAge(Date birthDay) {
    // 获取当前系统时间
    Calendar cal = Calendar.getInstance();
    // 如果出生日期大于当前时间，则抛出异常
    if (cal.before(birthDay)) {
      throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
    }
    // 取出系统当前时间的年、月、日部分
    int yearNow = cal.get(Calendar.YEAR);
    int monthNow = cal.get(Calendar.MONTH);
    int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

    // 将日期设置为出生日期
    cal.setTime(birthDay);
    // 取出出生日期的年、月、日部分
    int yearBirth = cal.get(Calendar.YEAR);
    int monthBirth = cal.get(Calendar.MONTH);
    int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
    // 当前年份与出生年份相减，初步计算年龄
    int age = yearNow - yearBirth;
    // 当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
    if (monthNow <= monthBirth) {
      // 如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周岁
      if (monthNow == monthBirth) {
        if (dayOfMonthNow < dayOfMonthBirth) {
          age--;
        }
      } else {
        age--;
      }
    }
    return age;
  }

}

