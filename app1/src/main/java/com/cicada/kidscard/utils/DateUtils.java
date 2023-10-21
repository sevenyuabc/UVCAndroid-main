package com.cicada.kidscard.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期控件工具类
 * <p/>
 * 创建时间: 2014年12月18日 上午10:34:56 <br/>
 *
 * @author hwp
 * @since v0.0.1
 */
public class DateUtils {
    private static final String TAG = DateUtils.class.getSimpleName();
    /**
     * 1天=86400000毫秒
     */
    public static final long DAY_OF_MILLISECOND = 24 * 60 * 60 * 1000;
    /**
     * 1小时=3600000毫秒
     */
    public static final long HOUR_OF_MILLISECOND = 60 * 60 * 1000;
    /**
     * 1分钟=60000毫秒
     */
    public static final long MINUTE_OF_MILLISECOND = 60 * 1000;
    /**
     * 1秒钟=1000毫秒
     */
    public static final long SECOND_OF_MILLISECOND = 1000;

    /**
     * 获取－年
     */
    public static String format_yyyy = "yyyy";
    /**
     * 获取－月
     */
    public static String format_MM = "MM";
    /**
     * 获取－天
     */
    public static String format_dd = "dd";
    /**
     * 获取－时
     */
    public static String format_HH = "HH";
    /**
     * 获取－分
     */
    public static String format_mm = "mm";
    /**
     * 获取－秒
     */
    public static String format_ss = "ss";

    /**
     * 英文时分秒日期格式：2014-01-01 6:30:30
     */
    public static String format_yyyy_MM_dd_HH_mm_ss_12_EN = "yyyy-MM-dd hh:mm:ss";
    /**
     * 英文时分秒日期格式：2014-01-01 18:30:30
     */
    public static String format_yyyy_MM_dd_HH_mm_ss_24_EN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 中文时分秒日期格式：2014年01月01日 6时30分30秒
     */
    public static String format_yyyy_MM_dd_HH_mm_ss_12_CN = "yyyy年MM月dd日 hh时mm分ss秒";
    /**
     * 中文时分秒日期格式：2014年01月01日 18时30分30秒
     */
    public static String format_yyyy_MM_dd_HH_mm_ss_24_CN = "yyyy年MM月dd日 HH时mm分ss秒";

    /**
     * 英文时分日期格式：2014-01-01 6:30
     */
    public static String format_yyyy_MM_dd_HH_mm_12_EN = "yyyy-MM-dd hh:mm";
    /**
     * 英文时分日期格式：2014-01-01 18:30
     */
    public static String format_yyyy_MM_dd_HH_mm_24_EN = "yyyy-MM-dd HH:mm";
    /**
     * 中文时分日期格式：2014年01月01日 6时30分
     */
    public static String format_yyyy_MM_dd_HH_mm_12_CN = "yyyy年MM月dd日 hh时mm分";
    /**
     * 中文时分日期格式：2014年01月01日 18时30分
     */
    public static String format_yyyy_MM_dd_HH_mm_24_CN = "yyyy年MM月dd日 HH时mm分";

    /**
     * 英文日期格式：2014-01-01
     */
    public static String format_yyyy_MM_dd_EN = "yyyy-MM-dd";
    /**
     * 中文日期格式：2014年01月01日
     */
    public static String format_yyyy_MM_dd_CN = "yyyy年MM月dd日";

    /**
     * 英文格式：01-01
     */
    public static String format_MM_dd_EN = "MM-dd";
    /**
     * 中文格式：01月01日
     */
    public static String format_MM_dd_CN = "MM月dd日";

    /**
     * 英文格式：2014
     */
    public static String format_YYYY_EN = "yyyy";
    /**
     * 中文格式：2014年
     */
    public static String format_YYYY_CN = "yyyy年";

    /**
     * 英文格式：01
     */
    public static String format_MM_EN = "MM";
    /**
     * 中文格式：01月
     */
    public static String format_MM_CN = "MM月";

    /**
     * 英文格式：01
     */
    public static String format_dd_EN = "dd";
    /**
     * 中文格式：01日
     */
    public static String format_dd_CN = "dd日";

    /**
     * 英文时间格式：6:30:30
     */
    public static String format_HH_mm_ss_12_EN = "hh:mm:ss";
    /**
     * 英文时间格式：18:30:30
     */
    public static String format_HH_mm_ss_24_EN = "HH:mm:ss";
    /**
     * 中文时间格式：6时30分30秒
     */
    public static String format_HH_mm_ss_12_CN = "hh时mm分ss秒";
    /**
     * 中文时间格式：18时30分30秒
     */
    public static String format_HH_mm_ss_24_CN = "HH时mm分ss秒";

    /**
     * 英文时间格式：6:30
     */
    public static String format_HH_mm_12_EN = "hh:mm";
    /**
     * 英文时间格式：18:30
     */
    public static String format_HH_mm_24_EN = "HH:mm";
    /**
     * 中文时间格式：6时30分
     */
    public static String format_HH_mm_12_CN = "hh时mm分";
    /**
     * 中文时间格式：18时30分
     */
    public static String format_HH_mm_24_CN = "HH时mm分";

    private static final Calendar mCalendar = Calendar.getInstance();

    /**
     * 日期
     */
    public static final String JUSTNOW = "刚刚";
    public static final String MINUTE_AGO = "分钟前";
    public static final String HOUR_AGO = "小时前";

    public static final String TODAY = "今天";
    public static final String YESTERDAY = "昨天";
    public static final String BEFORE_YESTERDAY = "前天";

    public static final String TOMORROW = "明天";
    public static final String AFTER_TOMORROW = "后天";

    public static final String SECOND_AFTER = "秒后";
    public static final String MINUTE_AFTER = "分钟后";
    public static final String HOUR_AFTER = "小时后";

    public static final String MONDAY_EN = "MON";
    public static final String TUESDAY_EN = "TUE";
    public static final String WEDNESDAY_EN = "WED";
    public static final String THURSDAY_EN = "THU";
    public static final String FRIDAY_EN = "FRI";
    public static final String SATURDAY_EN = "SAT";
    public static final String SUNDAY_EN = "SUN";

    public static final String MONDAY_CN = "星期一";
    public static final String TUESDAY_CN = "星期二";
    public static final String WEDNESDAY_CN = "星期三";
    public static final String THURSDAY_CN = "星期四";
    public static final String FRIDAY_CN = "星期五";
    public static final String SATURDAY_CN = "星期六";
    public static final String SUNDAY_CN = "星期日";

    /**
     * 获取设备采用的时间制式(12小时制式或者24小时制式) <br>
     * 注意: 在模拟器上获取的时间制式为空
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getTIME_12_24(Context context) {
        String strTimeFormat_12_24 = "";
        try {
            ContentResolver cv = context.getContentResolver();
            strTimeFormat_12_24 = android.provider.Settings.System.getString(cv, android.provider.Settings.System.TIME_12_24);
            if (strTimeFormat_12_24 != null && "24".equals(strTimeFormat_12_24)) {
                return 24;
            }
            if (strTimeFormat_12_24 != null && "12".equals(strTimeFormat_12_24)) {
                return 12;
            }
            return 24;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return 24;
        }
    }

    /**
     * 获取时间显示制式：上午／下午 or AM／PM <br>
     * 如果是24制，则返回""
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTIME_AM_PM(Date dateIn) {
        String am_pm_String;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        if (calendar.get(Calendar.AM_PM) == 0) {
            // 中文显示格式
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("zh")) {
                am_pm_String = "上午";
            } else {
                am_pm_String = "AM";
            }
        } else {
            // 中文显示格式
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("zh")) {
                am_pm_String = "下午";
            } else {
                am_pm_String = "PM";
            }
        }
        return am_pm_String;
    }

    /**
     * 获取今天的日期
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static Date getDateNow() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 判断给定日期是否是今天
     *
     * @param time
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean isToday(Date dateIn) {
        return isSameDayOfDate(getDateNow(), dateIn);
    }

    /**
     * 判断给定日期是否是昨天
     *
     * @param time
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean isYesterday(Date dateIn) {
        Calendar compared = Calendar.getInstance();
        compared.setTime(dateIn);

        return isSameDayOfDate(Calendar.getInstance(), compared);
    }

    /**
     * 判断给定日期是否是今天
     *
     * @param time
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean isTomorrow(Date dateIn) {
        Calendar compared = Calendar.getInstance();
        compared.setTime(dateIn);

        return isSameDayOfDate(Calendar.getInstance(), compared);
    }

    /**
     * 加减n年后的日期
     *
     * @param dateIn
     * @param intYear
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static Date getDateAddYears(Date dateIn, int intYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        calendar.add(Calendar.YEAR, intYear);
        return calendar.getTime();
    }

    /**
     * 加减n月后的日期
     *
     * @param dateIn
     * @param intMonth
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static Date getDateAddMonths(Date dateIn, int intMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        calendar.add(Calendar.MONTH, intMonth);
        return calendar.getTime();
    }

    /**
     * 加减n天后的日期
     *
     * @param dateIn
     * @param intDay
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static Date getDateAddDays(Date dateIn, int intDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        calendar.add(Calendar.DAY_OF_YEAR, intDay);
        return calendar.getTime();
    }

    /**
     * 加减n小时后的日期
     *
     * @param dateIn
     * @param intMinute
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static Date getDateAddHours(Date dateIn, int intHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        calendar.add(Calendar.HOUR, intHour);
        return calendar.getTime();
    }

    /**
     * 加减n分钟后的日期
     *
     * @param dateIn
     * @param intMinute
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static Date getDateAddMinutes(Date dateIn, int intMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        calendar.add(Calendar.MINUTE, intMinute);
        return calendar.getTime();
    }

    /**
     * 加减n秒后的日期
     *
     * @param dateIn
     * @param intSecond
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static Date getDateAddSeconds(Date dateIn, int intSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        calendar.add(Calendar.SECOND, intSecond);
        return calendar.getTime();
    }

    /**
     * 判断两个日期是否在同一天.
     *
     * @param dateLeft
     * @param dateRight
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean isSameDayOfDate(Date dateLeft, Date dateRight) {
        Calendar ltime = Calendar.getInstance();
        ltime.setTime(dateLeft);
        Calendar rTime = Calendar.getInstance();
        rTime.setTime(dateRight);
        return isSameDayOfDate(ltime, rTime);
    }

    /**
     * 判断两个日期是否在同一天.
     *
     * @param calendarLeft
     * @param calendarRight
     * @return
     */
    public static boolean isSameDayOfDate(Calendar calendarLeft, Calendar calendarRight) {
        if (calendarLeft == null || calendarRight == null)
            return false;

        if (Math.abs(calendarLeft.getTimeInMillis() - calendarRight.getTimeInMillis()) > DAY_OF_MILLISECOND) {
            return false;
        }

        return calendarLeft.get(Calendar.YEAR) == calendarRight.get(Calendar.YEAR) && calendarLeft.get(Calendar.MONTH) == calendarRight.get(Calendar.MONTH) && calendarLeft.get(Calendar.DAY_OF_MONTH) == calendarRight.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期是所在年份的第几月
     *
     * @param date
     * @return
     */
    public static int getMonthOfYear(Date dateIn) {
        mCalendar.setTime(dateIn);
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期是所在年份的第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date dateIn) {
        mCalendar.setTime(dateIn);
        return mCalendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取日期是所在月份的第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfMonth(Date dateIn) {
        mCalendar.setTime(dateIn);
        return mCalendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取日期是所在年份的第几天
     *
     * @param date
     * @return
     */
    public static int getDayOfYear(Date dateIn) {
        mCalendar.setTime(dateIn);
        return mCalendar.get(Calendar.DAY_OF_YEAR);

    }

    /**
     * 获取日期是所在月份的第几天
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date dateIn) {
        mCalendar.setTime(dateIn);
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期是星期几
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDayOfWeekEN(Date dateIn) {
        Calendar target = Calendar.getInstance();
        target.setTime(dateIn);
        int dayForWeek = 0;
        dayForWeek = target.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek) {
            case 1:
                return SUNDAY_EN;
            case 2:
                return MONDAY_EN;
            case 3:
                return TUESDAY_EN;
            case 4:
                return WEDNESDAY_EN;
            case 5:
                return THURSDAY_EN;
            case 6:
                return FRIDAY_EN;
            case 7:
                return SATURDAY_EN;
            default:
                return "NA";
        }
    }

    /**
     * 获取日期是星期几
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDayOfWeekCN(Date dateIn) {
        Calendar target = Calendar.getInstance();
        target.setTime(dateIn);
        int dayForWeek = 0;
        dayForWeek = target.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek) {
            case 1:
                return SUNDAY_CN;
            case 2:
                return MONDAY_CN;
            case 3:
                return TUESDAY_CN;
            case 4:
                return WEDNESDAY_CN;
            case 5:
                return THURSDAY_CN;
            case 6:
                return FRIDAY_CN;
            case 7:
                return SATURDAY_CN;
            default:
                return "NA";
        }
    }

    /**
     * 获取日期所在月份的第一天日期
     *
     * @param dateIn 指定日期
     * @return 月份的第一天
     */
    public static Date getDayOfMonthFirst(Date dateIn) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取日期所在月份的最后一天日期
     *
     * @param dateIn 指定日期
     * @return 月份的最后一天
     */
    public static Date getDayOfMonthLast(Date dateIn) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取某一天的起始时间.
     *
     * @param date
     * @return
     */
    public static Date getTimeOfDayStart(Date dateIn) {
        mCalendar.setTime(dateIn);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        return mCalendar.getTime();
    }

    /**
     * 获取某一天的结束时间.
     *
     * @param date
     * @return
     */
    public static Date getTimeOfDayEnd(Date dateIn) {
        mCalendar.setTime(dateIn);
        mCalendar.set(Calendar.HOUR_OF_DAY, 23);
        mCalendar.set(Calendar.MINUTE, 59);
        mCalendar.set(Calendar.SECOND, 59);
        mCalendar.set(Calendar.MILLISECOND, 999);
        return mCalendar.getTime();
    }

    /**
     * 两个日期之间的差值：年
     *
     * @param dateLeft
     * @param dateRight
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static long getDateDifferenceYears(Date dateLeft, Date dateRight) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dateLeft);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dateRight);

        long returnYears = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
        return returnYears;
    }

    /**
     * 两个日期之间的差值：月
     *
     * @param dateLeft
     * @param dateRight
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static long getDateDifferenceMonths(Date dateLeft, Date dateRight) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dateLeft);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dateRight);

        long returnYears = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
        long returnMonths = calendar1.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH);
        returnMonths = returnYears * 12 + returnMonths;
        return returnMonths;
    }

    /**
     * 两个日期之间的差值：天
     *
     * @param dateLeft
     * @param dateRight
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static long getDateDifferenceDays(Date dateLeft, Date dateRight) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dateLeft);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dateRight);
        long returnDays = (calendar1.getTimeInMillis() - calendar2.getTimeInMillis()) / DAY_OF_MILLISECOND;
        return returnDays;
    }

    /**
     * 两个日期之间的差值：小时
     *
     * @param dateLeft
     * @param dateRight
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static long getDateDifferenceHours(Date dateLeft, Date dateRight) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dateLeft);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dateRight);

        long returnHours = (calendar1.getTimeInMillis() - calendar2.getTimeInMillis()) / HOUR_OF_MILLISECOND;
        return returnHours;
    }

    /**
     * 两个日期之间的差值：分钟
     *
     * @param dateLeft
     * @param dateRight
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static long getDateDifferenceMinutes(Date dateLeft, Date dateRight) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dateLeft);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dateRight);

        long returnMinutes = (calendar1.getTimeInMillis() - calendar2.getTimeInMillis()) / MINUTE_OF_MILLISECOND;
        return returnMinutes;
    }

    /**
     * 两个日期之间的差值：秒
     *
     * @param dateLeft
     * @param dateRight
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static long getDateDifferenceSeconds(Date dateLeft, Date dateRight) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dateLeft);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dateRight);

        long returnSeconds = (calendar1.getTimeInMillis() - calendar2.getTimeInMillis()) / 1000;
        return returnSeconds;
    }

    public static long getDateDifferenceSeconds(long dateLeft, long dateRight) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(dateLeft);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(dateRight);

        long returnSeconds = (calendar1.getTimeInMillis() - calendar2.getTimeInMillis()) / 1000;
        return returnSeconds;
    }

    /**
     * 获取日期的：年 <br>
     * 例如：2014
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getDateYearInt(Date dateIn) {
        Calendar calendarIn = Calendar.getInstance();
        calendarIn.setTime(dateIn);
        return calendarIn.get(Calendar.YEAR);
    }

    /**
     * 获取日期的：年 <br>
     * 例如："2014"
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateYearString(Date dateIn) {
        SimpleDateFormat sdf = new SimpleDateFormat(format_yyyy);
        return sdf.format(dateIn);
    }

    /**
     * 获取日期的：月 <br>
     * 例如：9 (Calendar.MONTH 返回的月需要加上1)
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getDateMonthInt(Date dateIn) {
        Calendar calendarIn = Calendar.getInstance();
        calendarIn.setTime(dateIn);
        return calendarIn.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的：月 <br>
     * 例如："09"
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateMonthString(Date dateIn) {
        SimpleDateFormat sdf = new SimpleDateFormat(format_MM);
        return sdf.format(dateIn);
    }

    /**
     * 获取日期的：日 <br>
     * 例如：31 or 1
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getDateDayInt(Date dateIn) {
        Calendar calendarIn = Calendar.getInstance();
        calendarIn.setTime(dateIn);
        return calendarIn.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期的：日 <br>
     * 例如："31" or "01"
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateDayString(Date dateIn) {
        SimpleDateFormat sdf = new SimpleDateFormat(format_dd);
        return sdf.format(dateIn);
    }

    /**
     * 获取日期的：小时 <br>
     * 例如：9
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getDateHourInt(Date dateIn) {
        Calendar calendarIn = Calendar.getInstance();
        calendarIn.setTime(dateIn);
        return calendarIn.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取日期的：小时 <br>
     * 例如："09"
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateHourString(Date dateIn) {
        SimpleDateFormat sdf = new SimpleDateFormat(format_HH);
        return sdf.format(dateIn);
    }

    /**
     * 获取日期的：分钟 <br>
     * 例如：9
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getDateMinuteInt(Date dateIn) {
        Calendar calendarIn = Calendar.getInstance();
        calendarIn.setTime(dateIn);
        return calendarIn.get(Calendar.MINUTE);
    }

    /**
     * 获取日期的：分钟 <br>
     * 例如："09"
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateMinuteString(Date dateIn) {
        SimpleDateFormat sdf = new SimpleDateFormat(format_mm);
        return sdf.format(dateIn);
    }

    /**
     * 获取日期的：秒 <br>
     * 例如：9
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getDateSecondInt(Date dateIn) {
        Calendar calendarIn = Calendar.getInstance();
        calendarIn.setTime(dateIn);
        return calendarIn.get(Calendar.SECOND);
    }

    /**
     * 获取日期的：秒 <br>
     * 例如："09"
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateSecondString(Date dateIn) {
        SimpleDateFormat sdf = new SimpleDateFormat(format_ss);
        return sdf.format(dateIn);
    }

    // TODO: 各种字符串转化日期格式的处理
    /*************************************** 各种字符串转化日期格式的处理 ***************************************/
    /**
     * 将字符串(标准格式)======>日期时间
     *
     * @param stringValue "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static Date getStringToDate_YYYY_MM_DD_HH_MM_SS_EN(String stringValue) {
        String format = format_yyyy_MM_dd_HH_mm_ss_24_EN;
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            format = format_yyyy_MM_dd_HH_mm_ss_12_EN;
        }
        SimpleDateFormat StringToDate = new SimpleDateFormat(format);
        Date returnDate = new Date();
        try {
            returnDate = StringToDate.parse(stringValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    /**
     * 将字符串(中文格式)======>日期时间
     *
     * @param stringValue "yyyy年MM月dd日 HH时mm分ss秒"
     * @return
     */
    public static Date getStringToDate_YYYY_MM_DD_HH_MM_SS_CN(String stringValue) {
        String format = format_yyyy_MM_dd_HH_mm_ss_24_CN;
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            format = format_yyyy_MM_dd_HH_mm_ss_12_CN;
        }
        SimpleDateFormat StringToDate = new SimpleDateFormat(format);
        Date returnDate = new Date();
        try {
            returnDate = StringToDate.parse(stringValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    /**
     * 将字符串(标准格式)======>日期时间
     *
     * @param stringValue "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static Date getStringToDate_YYYY_MM_DD_HH_MM_EN(String stringValue) {
        String format = format_yyyy_MM_dd_HH_mm_24_EN;
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            format = format_yyyy_MM_dd_HH_mm_12_EN;
        }
        SimpleDateFormat StringToDate = new SimpleDateFormat(format);
        Date returnDate = new Date();
        try {
            returnDate = StringToDate.parse(stringValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    /**
     * 将字符串(中文格式)======>日期时间
     *
     * @param stringValue "yyyy年MM月dd日 HH时mm分"
     * @return
     */
    public static Date getStringToDate_YYYY_MM_DD_HH_MM_CN(String stringValue) {
        String format = format_yyyy_MM_dd_HH_mm_24_CN;
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            format = format_yyyy_MM_dd_HH_mm_12_CN;
        }
        SimpleDateFormat StringToDate = new SimpleDateFormat(format);
        Date returnDate = new Date();
        try {
            returnDate = StringToDate.parse(stringValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    /**
     * 将字符串(标准格式)======>日期时间
     *
     * @param stringValue "yyyy-MM-dd"
     * @return
     */
    public static Date getStringToDate_YYYY_MM_DD_EN(String stringValue) {
        SimpleDateFormat StringToDate = new SimpleDateFormat(format_yyyy_MM_dd_EN);
        Date returnDate = new Date();
        try {
            returnDate = StringToDate.parse(stringValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    /**
     * 将字符串(中文格式)======>日期时间
     *
     * @param stringValue "yyyy年MM月dd日"
     * @return
     */
    public static Date getStringToDate_YYYY_MM_DD_CN(String stringValue) {
        SimpleDateFormat StringToDate = new SimpleDateFormat(format_yyyy_MM_dd_EN);
        Date returnDate = new Date();
        try {
            returnDate = StringToDate.parse(stringValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    // TODO:日期转化各种字符串格式的处理
    /*************************************** 日期转化各种字符串格式的处理 ***************************************/
    /**
     * 将日期转换为英文字符串（2014-01-01 12:30:30）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_YYYY_MM_DD_HH_MM_SS_EN(Date dateIn) {
        String format = format_yyyy_MM_dd_HH_mm_ss_24_EN;
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            format = format_yyyy_MM_dd_HH_mm_ss_12_EN;
        }
        SimpleDateFormat DateToString = new SimpleDateFormat(format);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为中文字符串（2014年01月01日 12时30分30秒）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_YYYY_MM_DD_HH_MM_SS_CN(Date dateIn) {
        String format = format_yyyy_MM_dd_HH_mm_ss_24_CN;
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            format = format_yyyy_MM_dd_HH_mm_ss_12_CN;
        }
        SimpleDateFormat DateToString = new SimpleDateFormat(format);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为英文字符串（2014-01-01 12:30）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_YYYY_MM_DD_HH_MM_EN(Date dateIn) {
        String format = format_yyyy_MM_dd_HH_mm_24_EN;
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            format = format_yyyy_MM_dd_HH_mm_12_EN;
        }
        SimpleDateFormat DateToString = new SimpleDateFormat(format);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为中文字符串（2014年01月01日 12时30分）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_YYYY_MM_DD_HH_MM_CN(Date dateIn) {
        String format = format_yyyy_MM_dd_HH_mm_24_CN;
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            format = format_yyyy_MM_dd_HH_mm_12_CN;
        }
        SimpleDateFormat DateToString = new SimpleDateFormat(format);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为英文字符串（2014-01-01）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_YYYY_MM_DD_EN(Date dateIn) {
        SimpleDateFormat DateToString = new SimpleDateFormat(format_yyyy_MM_dd_EN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为中文字符串（2014年01月01日）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_YYYY_MM_DD_CN(Date dateIn) {
        SimpleDateFormat DateToString = new SimpleDateFormat(format_yyyy_MM_dd_CN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为英文字符串（01-01）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_MM_DD_EN(Date dateIn) {
        SimpleDateFormat DateToString = new SimpleDateFormat(format_MM_dd_EN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为中文字符串（01月01日）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_MM_DD_CN(Date dateIn) {
        SimpleDateFormat DateToString = new SimpleDateFormat(format_MM_dd_CN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为英文字符串（2014）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_YYYY_EN(Date dateIn) {
        SimpleDateFormat DateToString = new SimpleDateFormat(format_YYYY_EN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为中文字符串（2014年）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_YYYY_CN(Date dateIn) {
        SimpleDateFormat DateToString = new SimpleDateFormat(format_YYYY_CN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为英文字符串（01）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_MM_EN(Date dateIn) {
        SimpleDateFormat DateToString = new SimpleDateFormat(format_MM_EN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为中文字符串（01月）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_MM_CN(Date dateIn) {
        SimpleDateFormat DateToString = new SimpleDateFormat(format_MM_CN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为英文字符串（01）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_DD_EN(Date dateIn) {
        SimpleDateFormat DateToString = new SimpleDateFormat(format_dd_EN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为中文字符串（01日）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_DD_CN(Date dateIn) {
        SimpleDateFormat DateToString = new SimpleDateFormat(format_dd_CN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为英文字符串（12:30:30） <br>
     * 其中处理了12/24制式对应的 AM/PM 和 上午／下午的问题
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_HH_MM_SS_EN(Date dateIn) {
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            SimpleDateFormat DateToString = new SimpleDateFormat(format_HH_mm_ss_12_EN);
            return getTIME_AM_PM(dateIn) + " " + DateToString.format(dateIn);
        } else {
            SimpleDateFormat DateToString = new SimpleDateFormat(format_HH_mm_ss_24_EN);
            return DateToString.format(dateIn);
        }
    }

    /**
     * 将日期转换为中文字符串（12时30分30秒） <br>
     * 其中处理了12/24制式对应的 AM/PM 和 上午／下午的问题
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_HH_MM_SS_CN(Date dateIn) {
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            SimpleDateFormat DateToString = new SimpleDateFormat(format_HH_mm_ss_12_CN);
            return getTIME_AM_PM(dateIn) + " " + DateToString.format(dateIn);
        } else {
            SimpleDateFormat DateToString = new SimpleDateFormat(format_HH_mm_ss_24_CN);
            return DateToString.format(dateIn);
        }
    }

    public static String getDateToString_HH_MM_SS_EN(long timeStamp) {
        Date dateIn = new Date(timeStamp);
        SimpleDateFormat DateToString = new SimpleDateFormat(format_HH_mm_ss_24_EN);
        return DateToString.format(dateIn);
    }

    /**
     * 将日期转换为英文字符串（12:30）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_HH_MM_EN(Date dateIn) {
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            SimpleDateFormat DateToString = new SimpleDateFormat(format_HH_mm_12_EN);
            return getTIME_AM_PM(dateIn) + " " + DateToString.format(dateIn);
        } else {
            SimpleDateFormat DateToString = new SimpleDateFormat(format_HH_mm_24_EN);
            return DateToString.format(dateIn);
        }
    }

    /**
     * 将日期转换为中文字符串（12时30分）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToString_HH_MM_CN(Date dateIn) {
        if (getTIME_12_24(MyApplication.getInstance()) == 12) {
            SimpleDateFormat DateToString = new SimpleDateFormat(format_HH_mm_12_CN);
            return getTIME_AM_PM(dateIn) + " " + DateToString.format(dateIn);
        } else {
            SimpleDateFormat DateToString = new SimpleDateFormat(format_HH_mm_24_CN);
            return DateToString.format(dateIn);
        }
    }

    // TODO:时间戳和日期转换方式处理
    /*************************************** 时间戳和日期转换方式处理 ***************************************/
    /**
     * 将成日期转化时间戳
     *
     * @param strTimeStamp
     * @return long
     * @author hwp
     * @since v0.0.1
     */
    public static long getDateToTimeStamp(Date dateIn) {
        mCalendar.setTime(dateIn);
        return mCalendar.getTimeInMillis();
    }

    /**
     * 将成日期转化时间戳
     *
     * @param strTimeStamp
     * @return String
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToTimeStampString(Date dateIn) {
        return String.valueOf(getDateToTimeStamp(dateIn));
    }

    /**
     * 将时间戳转化成日期
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static Date getTimeStampToDate(long lngTimeStamp) {
        if (String.valueOf(lngTimeStamp).length() == 10) {
            mCalendar.setTimeInMillis(lngTimeStamp * 1000);
        } else {
            mCalendar.setTimeInMillis(lngTimeStamp);
        }
        return mCalendar.getTime();
    }

    /**
     * 将时间戳转化成日期
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static Date getTimeStampToDate(String strTimeStamp) {
        return getTimeStampToDate(Long.parseLong(strTimeStamp));
    }

    // TODO:时间戳转化各种字符串格式的处理
    /*************************************** 时间戳转化各种字符串格式的处理 ***************************************/
    /**
     * 将日期转换为英文字符串（2014-01-01 12:30:30）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToStringYYYY_MM_DD_HH_MM_SS_EN(String strTimeStamp) {
        return getDateToString_YYYY_MM_DD_HH_MM_SS_EN(getTimeStampToDate(strTimeStamp));
    }

    public static String getTimeStampToStringYYYY_MM_DD_HH_MM_SS_EN(long lngTimeStamp) {
        return getDateToString_YYYY_MM_DD_HH_MM_SS_EN(getTimeStampToDate(String.valueOf(lngTimeStamp)));
    }

    /**
     * 将日期转换为中文字符串（2014年01月01日 12时30分30秒）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToStringYYYY_MM_DD_HH_MM_SS_CN(String strTimeStamp) {
        return getDateToString_YYYY_MM_DD_HH_MM_SS_CN(getTimeStampToDate(strTimeStamp));
    }

    public static String getTimeStampToStringYYYY_MM_DD_HH_MM_SS_CN(long lngTimeStamp) {
        return getDateToString_YYYY_MM_DD_HH_MM_SS_CN(getTimeStampToDate(String.valueOf(lngTimeStamp)));
    }

    /**
     * 将日期转换为英文字符串（2014-01-01 12:30）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToStringYYYY_MM_DD_HH_MM_EN(String strTimeStamp) {
        return getDateToString_YYYY_MM_DD_HH_MM_EN(getTimeStampToDate(strTimeStamp));
    }

    public static String getTimeStampToStringYYYY_MM_DD_HH_MM_EN(long lngTimeStamp) {
        return getDateToString_YYYY_MM_DD_HH_MM_EN(getTimeStampToDate(String.valueOf(lngTimeStamp)));
    }

    /**
     * 将日期转换为中文字符串（2014年01月01日 12时30分）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToStringYYYY_MM_DD_HH_MM_CN(String strTimeStamp) {
        return getDateToString_YYYY_MM_DD_HH_MM_CN(getTimeStampToDate(strTimeStamp));
    }

    public static String getTimeStampToStringYYYY_MM_DD_HH_MM_CN(long lngTimeStamp) {
        return getDateToString_YYYY_MM_DD_HH_MM_CN(getTimeStampToDate(String.valueOf(lngTimeStamp)));
    }

    /**
     * 将日期转换为英文字符串（2014-01-01）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToYYYY_MM_DD_EN(String strTimeStamp) {
        return getDateToString_YYYY_MM_DD_EN(getTimeStampToDate(strTimeStamp));
    }

    public static String getTimeStampToYYYY_MM_DD_EN(long lngTimeStamp) {
        return getDateToString_YYYY_MM_DD_EN(getTimeStampToDate(String.valueOf(lngTimeStamp)));
    }

    /**
     * 将日期转换为中文字符串（2014年01月01日）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToYYYY_MM_DD_CN(String strTimeStamp) {
        return getDateToString_YYYY_MM_DD_CN(getTimeStampToDate(strTimeStamp));
    }

    public static String getTimeStampToYYYY_MM_DD_CN(long lngTimeStamp) {
        return getDateToString_YYYY_MM_DD_CN(getTimeStampToDate(String.valueOf(lngTimeStamp)));
    }

    /**
     * 将日期转换为英文字符串（01-01）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToString_MM_DD_EN(String strTimeStamp) {
        return getDateToString_MM_DD_EN(getTimeStampToDate(strTimeStamp));
    }

    public static String getTimeStampToString_MM_DD_EN(long lngTimeStamp) {
        return getDateToString_MM_DD_EN(getTimeStampToDate(String.valueOf(lngTimeStamp)));
    }

    /**
     * 将日期转换为中文字符串（01月01日）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToString_MM_DD_CN(String strTimeStamp) {
        return getDateToString_MM_DD_CN(getTimeStampToDate(strTimeStamp));
    }

    public static String getTimeStampToString_MM_DD_CN(long lngTimeStamp) {
        return getDateToString_MM_DD_CN(getTimeStampToDate(String.valueOf(lngTimeStamp)));
    }

    /**
     * 将日期转换为英文字符串（12:30:30）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToString_HH_MM_SS_EN(String strTimeStamp) {
        return getDateToString_HH_MM_SS_EN(getTimeStampToDate(strTimeStamp));
    }

    public static String getTimeStampToString_HH_MM_SS_EN(long lngTimeStamp) {
        return getDateToString_HH_MM_SS_EN(getTimeStampToDate(String.valueOf(lngTimeStamp)));
    }

    /**
     * 将日期转换为中文字符串（12时30分30秒）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToString_HH_MM_SS_CN(String strTimeStamp) {
        return getDateToString_HH_MM_SS_CN(getTimeStampToDate(strTimeStamp));
    }

    /**
     * 将日期转换为英文字符串（12:30）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToString_HH_MM_EN(String strTimeStamp) {
        return getDateToString_HH_MM_EN(getTimeStampToDate(strTimeStamp));
    }

    /**
     * 将日期转换为中文字符串（12时30分）
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToString_HH_MM_CN(String strTimeStamp) {
        return getDateToString_HH_MM_CN(getTimeStampToDate(strTimeStamp));
    }

    // TODO:特定的时间字符串显示格式处理
    /*************************************** 特定的时间字符串显示格式处理 ***************************************/
    /**
     * 将给定日期和当前时间的差值转为英文字符串显示(5d ago或 5d after)
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateDifferenceToStringEN(Date dateIn) {
        String strOut = "1d";
        String strFrontOrBack = " ago";

        Calendar c1 = Calendar.getInstance();
        c1.setTime(getDateNow());
        Calendar c2 = Calendar.getInstance();
        c2.setTime(dateIn);
        int lngDuration = (c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR)) * 24 * 60 * 60;
        lngDuration += (c1.get(Calendar.HOUR_OF_DAY) - c2.get(Calendar.HOUR_OF_DAY)) * 60 * 60;
        lngDuration += (c1.get(Calendar.MINUTE) - c2.get(Calendar.MINUTE)) * 60;
        lngDuration += (c1.get(Calendar.SECOND) - c2.get(Calendar.SECOND)) * 1;
        if (lngDuration >= 0) {
            strFrontOrBack = " ago";
        } else {
            strFrontOrBack = " after";
        }
        lngDuration = Math.abs(lngDuration);
        int YY = 0;
        int MM = 0;
        int ww = 0;
        int dd = lngDuration / 24 * 60 * 60;
        if (dd > 7) {
            ww = dd / 7;
            if (ww > 4)
                MM = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
            if (MM > 12)
                YY = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        }
        int hh = lngDuration / 60 * 60;
        int mm = lngDuration / 60;
        int ss = lngDuration % 60;

        if (YY > 0) {
            strOut = Math.abs(YY) + "Y";
        } else {
            if (MM > 0) {
                strOut = Math.abs(MM) + "M";
            } else {
                if (ww > 0) {
                    strOut = Math.abs(ww) + "w";
                } else {
                    if (dd > 0) {
                        strOut = Math.abs(dd) + "d";
                    } else {
                        if (hh > 0) {
                            strOut = Math.abs(hh) + "h";
                        } else {
                            if (mm > 0) {
                                strOut = Math.abs(mm) + "m";
                            } else {
                                strOut = Math.abs(ss) + "s";
                            }
                        }
                    }
                }
            }
        }
        return strOut + strFrontOrBack;
    }

    /**
     * 将给定时间戳和当前时间的差值转为英文字符串显示(5d ago或 5d after)
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampDifferenceToStringEN(String strTimeStamp) {
        return getDateDifferenceToStringEN(getTimeStampToDate(strTimeStamp));
    }

    /**
     * 将给定日期和当前时间的差值转为字符串(5天前 或 5天后)
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateDifferenceToStringCN(Date dateIn) {
        String strOut = "0天";
        String strFrontOrBack = "前";
        Calendar c1 = Calendar.getInstance();
        c1.setTime(getDateNow());
        Calendar c2 = Calendar.getInstance();
        c2.setTime(dateIn);
        int lngDuration = (c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR)) * 24 * 60 * 60;
        lngDuration += (c1.get(Calendar.HOUR_OF_DAY) - c2.get(Calendar.HOUR_OF_DAY)) * 60 * 60;
        lngDuration += (c1.get(Calendar.MINUTE) - c2.get(Calendar.MINUTE)) * 60;
        lngDuration += (c1.get(Calendar.SECOND) - c2.get(Calendar.SECOND)) * 1;
        if (lngDuration >= 0) {
            strFrontOrBack = "前";
        } else {
            strFrontOrBack = "后";
        }
        lngDuration = Math.abs(lngDuration);
        int YY = 0;
        int MM = 0;
        int ww = 0;
        int dd = lngDuration / 24 * 60 * 60;
        if (dd > 7) {
            ww = dd / 7;
            if (ww > 4)
                MM = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
            if (MM > 12)
                YY = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        }
        int hh = lngDuration / 60 * 60;
        int mm = lngDuration / 60;
        int ss = lngDuration % 60;

        if (YY > 0) {
            strOut = Math.abs(YY) + "年";
        } else {
            if (MM > 0) {
                strOut = Math.abs(MM) + "月";
            } else {
                if (ww > 0) {
                    strOut = Math.abs(ww) + "周";
                } else {
                    if (dd > 0) {
                        strOut = Math.abs(dd) + "天";
                    } else {
                        if (hh > 0) {
                            strOut = Math.abs(hh) + "时";
                        } else {
                            if (mm > 0) {
                                strOut = Math.abs(mm) + "分";
                            } else {
                                strOut = Math.abs(ss) + "秒";
                            }
                        }
                    }
                }
            }
        }
        return strOut + strFrontOrBack;
    }

    /**
     * 将给定时间戳和当前时间的差值转为中文字符串(5天前 或 5天后)
     *
     * @param strTimeStamp
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampDifferenceToStringCN(String strTimeStamp) {
        return getDateDifferenceToStringCN(getTimeStampToDate(strTimeStamp));
    }

    /**
     * 将给定日期和当前时间的差值转为英文剩余时间显示(0d0h0m0s ago 或 0d0h0m0s after)
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToStringRemainEN(Date dateIn) {

        String strOut = "0d0h0m0s";
        String strFrontOrBack = " ago";
        Long lngDuration = getDateDifferenceSeconds(getDateNow(), dateIn);
        if (lngDuration >= 0) {
            strFrontOrBack = " ago";
        } else {
            strFrontOrBack = " after";
        }
        lngDuration = Math.abs(lngDuration);
        int nDay = (int) (lngDuration / (24 * 60 * 60));
        lngDuration = lngDuration - nDay * (24 * 60 * 60);
        int nHours = (int) (lngDuration / (60 * 60));
        lngDuration = lngDuration - nHours * (60 * 60);
        int nMinutes = (int) (lngDuration / 60);
        lngDuration = lngDuration - nMinutes * 60;
        int nSeconds = (int) (lngDuration % 60);
        strOut = "";
        if (nDay > 0) {
            strOut += nDay + "d";
        }
        if (nHours > 0) {
            strOut += nHours + "h";
        }
        if (nMinutes > 0) {
            strOut += nMinutes + "m";
        }
        if (nSeconds > 0) {
            strOut += nSeconds + "s";
        }
        return strOut + strFrontOrBack;
    }

    /**
     * 将给定时间戳和当前时间的差值转为英文剩余时间显示(0d0h0m0s ago 或 0d0h0m0s after)
     *
     * @param strTimeStampNow
     * @param strTimeStampInput
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeStampToStringRemainEN(String strTimeStamp) {
        return getDateToStringRemainEN(getTimeStampToDate(strTimeStamp));
    }

    /**
     * 将给定日期和当前时间的差值转为中文剩余时间显示(0天0时0分前 或 0天0时0分后)
     *
     * @param dateIn
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getDateToStringRemainCN(Date dateIn) {

        String strOut = "0天0时0分";
        String strFrontOrBack = "前";
        Long lngDuration = getDateDifferenceSeconds(getDateNow(), dateIn);
        if (lngDuration >= 0) {
            strFrontOrBack = "前";
        } else {
            strFrontOrBack = "后";
        }
        lngDuration = Math.abs(lngDuration);
        int nDay = (int) (lngDuration / (24 * 60 * 60));
        lngDuration = lngDuration - nDay * (24 * 60 * 60);
        int nHours = (int) (lngDuration / (60 * 60));
        lngDuration = lngDuration - nHours * (60 * 60);
        int nMinutes = (int) (lngDuration / 60);
        lngDuration = lngDuration - nMinutes * 60;
        int nSeconds = (int) (lngDuration % 60);
        strOut = "";
        if (nDay > 0) {
            strOut += nDay + "天";
        }
        if (nHours > 0) {
            strOut += nHours + "时";
        }
        if (nMinutes > 0) {
            strOut += nMinutes + "分";
        }
        if (nSeconds > 0) {
            strOut += nSeconds + "秒";
        }
        return strOut + strFrontOrBack;
    }
}
