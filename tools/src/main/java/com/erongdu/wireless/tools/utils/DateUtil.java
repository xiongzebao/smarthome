package com.erongdu.wireless.tools.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/7/28 13:49
 * <p>
 * Description: 常用的日期格式化方法
 */
@SuppressWarnings("unused")
public class DateUtil {
    public enum Format {
        /** 日期 + 时间类型格式，到秒 */
        SECOND("yyyy-MM-dd HH:mm:ss"),
        /** 日期 + 时间类型格式，到分 */
        MINUTE("yyyy-MM-dd HH:mm"),
        /** 日期类型格式，到日 */
        DATE("yyyy-MM-dd"),
        /** 日期类型格式，到月 */
        MONTH("yyyy-MM"),
        /** 日期类型格式，到月 */
        MONTH_CHINA("yyyy年MM月"),
        /** 时间类型的格式 */
        TIME("HH:mm:ss"),
        /** 日期类型格式，年月天 */
        YEAR_MONTH("yyMMdd");
        // 格式化格式
        private String value;

        Format(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // 注意SimpleDateFormat不是线程安全的
    private static SoftHashMap<String, ThreadLocal<SimpleDateFormat>> map = new SoftHashMap<>();

    /**
     * 日期格式化
     */
    public static String formatter(Format format, Object date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf;
            String           key = format.getValue();
            if (map.containsKey(key)) {
                sdf = map.get(key).get();
            } else {
                sdf = new SimpleDateFormat(key, Locale.getDefault());
                ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();
                threadLocal.set(sdf);
                map.put(key, threadLocal);
            }
            return sdf.format(new Date(ConverterUtil.getLong(date.toString())));
        }
    }

    /**
     * 日期格式化
     */
    public static String formatter(Format format, Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf;
            String           key = format.getValue();
            if (map.containsKey(key)) {
                sdf = map.get(key).get();
            } else {
                sdf = new SimpleDateFormat(key, Locale.getDefault());
                ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();
                threadLocal.set(sdf);
                map.put(key, threadLocal);
            }
            return sdf.format(date);
        }
    }

    /**
     * 日期格式化
     */
    public static String formatter(String format, Object date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.format(new Date(ConverterUtil.getLong(date.toString())));
        }
    }

    /**
     * 初略的剩余时间，年、个月、天、小时、分钟
     */
    public static String getTimeLeft(Object time) {
        if (time == null) {
            return "";
        } else {
            long diffValue = ConverterUtil.getLong(time.toString());

            long minute = 1000 * 60;
            long hour   = minute * 60;
            long day    = hour * 24;
            long month  = day * 30;
            long year   = month * 12;

            long _year  = diffValue / year;
            long _month = diffValue / month;
            long _day   = diffValue / day;
            long _hour  = diffValue / hour;
            long _min   = diffValue / minute;

            if (_year >= 1) {
                return (_year) + "年";
            } else if (_month >= 1) {
                return (_month) + "个月";
            } else if (_day >= 1) {
                return (_day) + "天";
            } else if (_hour >= 1) {
                return (_hour) + "小时";
            } else {
                return (_min) + "分钟";
            }
        }
    }

    /**
     * 倒计时格式化，时:分:秒
     */
    public static String getCountdownTime(Object time) {
        if (time == null) {
            return "";
        } else {
            long diffValue = ConverterUtil.getLong(time.toString());
            long day       = diffValue / (24 * 60 * 60 * 1000);
            long hour      = (diffValue / (60 * 60 * 1000) - day * 24);
            long min       = ((diffValue / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long sec       = (diffValue / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            return (hour > 9 ? hour : ("0" + hour)) + ":" + (min > 9 ? min : ("0" + min)) + ":" + (sec > 9 ? sec : ("0" + sec));
        }
    }
}
