/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.king.smart.car.mysmartcarapp.manager;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * TimeFormatUtils.java - 时间格式化工具类
 *
 * @author Kevin.Zhang
 *         <p/>
 *         2014-10-11 上午10:58:47
 */
public class TimeFormatUtils {

    /**
     * 调试TAG
     */
    private static final String TAG = TimeFormatUtils.class.getSimpleName();

    private static final String PARSE_FORMAT_DATE_FORMAT = "yyyy-MM-dd 00:00:00";
    private static final String PARSE_FORMAT_DATE_FORMAT2 = "yyyyMMdd_HHmmss";
    private static final String PARSE_FORMAT_TIME_FORMAT = "00:00:00";
    private static final String WALLET_DATE_FORMAT = "MMM d, yyyy H:mm a";
    private static final String GRUOP_CHAT_NOTICE_DATE_FORMAT = "MMM d, yyyy HH:mm";
    private static final String CHAT_DATE_FORMAT = "HH:mm";
    private static final String DATE_TIME_FORMAT = "MM-dd HH:mm";
    private static final String DATE_TIME_FORMAT2 = "yyyy/MM/dd";
    private static final String DATE_TIME_FORMAT3 = "yyyy/MM";
    private static final String DATE_TIME_FORMAT4 = "yyyy年MM月";
    private static final String DATE_TIME_FORMAT5 = "MM月dd日 HH:mm";

    private static final int ONE_DAY_MILLISECONDS = 24 * 60 * 60 * 1000;

    /**
     * 聊天会话列表Item显示的时间格式
     *
     * @return 时间格式 - 14:22
     */
    public static String getSessionFormatDate(long milliseconds) {
        Logger.d(TAG, "getSessionFormatDate milliseconds = " + milliseconds);

        SimpleDateFormat format = new SimpleDateFormat(CHAT_DATE_FORMAT, Locale.getDefault());
        String formatDate = format.format(new Date(milliseconds));
        Logger.d(TAG, "getSessionFormatDate formatDate = " + formatDate);

        return formatDate;
    }

    /**
     * 我的钱包时间显示格式
     *
     * @param milliseconds
     * @return 时间格式 - Aug 1, 2013 9:32 PM
     */
    public static String getWalletFormatDate(long milliseconds) {
        Logger.d(TAG, "getWalletFormatDate milliseconds = " + milliseconds);

        SimpleDateFormat format = new SimpleDateFormat(WALLET_DATE_FORMAT, Locale.getDefault());
        String formatDate = format.format(new Date(milliseconds));
        Logger.d(TAG, "getWalletFormatDate formatDate = " + formatDate);

        return formatDate;
    }

    /**
     * 群组提示消息显示格式
     *
     * @param milliseconds
     * @return 时间格式 - Aug 1, 2013
     */
    public static String getGroupChatNotficeTimeFormat(long milliseconds) {
        Logger.d(TAG, "getGroupChatNotficeTimeFormat milliseconds = " + milliseconds);

        SimpleDateFormat format = new SimpleDateFormat(GRUOP_CHAT_NOTICE_DATE_FORMAT, Locale.getDefault());
        String formatDate = format.format(new Date(milliseconds));
        Logger.d(TAG, "getGroupChatNotficeTimeFormat formatDate = " + formatDate);

        return formatDate;
    }

    /**
     * 获得特定格式的时间
     *
     * @return 时间格式 - 09-26 17:55
     */
    public static String getChatItemFormatDate(long milliseconds) {
        Logger.d(TAG, "getChatItemFormatDate milliseconds = " + milliseconds);

        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT2, Locale.getDefault());
        String formatDate = format.format(new Date(milliseconds));
        Logger.d(TAG, "getChatItemFormatDate formatDate = " + formatDate);

        return formatDate;
    }

    /**
     * 获得特定格式的时间
     *
     * @param date
     * @return 时间格式 - 09-26 17:55
     */
    public static String getChatItemFormatDate(Date date) {
        Logger.d(TAG, "getChatItemFormatDate date = " + date);

        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        String formatDate = format.format(date);
        Logger.d(TAG, "getChatItemFormatDate formatDate = " + formatDate);

        return formatDate;
    }

    /**
     * 今天直接显示时间，七天之内显示1-7d,其他显示月日
     *
     * @param milliseconds
     * @return
     */
    public static String getSessionNewFormatDate(long milliseconds) {
        Logger.d(TAG, "getSessionNewFormatDate milliseconds = " + milliseconds);

        long compareDayMillis = getFormatParseDateMillis(milliseconds);
        long todayMillis = getFormatParseDateMillis(System.currentTimeMillis());
        // 需要比较的时间减去今天的时间
        long intervalMilli = todayMillis - compareDayMillis;

        // 间隔的天数
        int intervalDay = (int) (intervalMilli / ONE_DAY_MILLISECONDS);
        int absIntervalDay = Math.abs(intervalDay);
        String formatDate = null;
        if (absIntervalDay <= 1) {
            formatDate = getSessionFormatDate(milliseconds);
        } else if (absIntervalDay <= 7) {
            formatDate = Math.abs(intervalDay) + "d";
        } else {
            formatDate = getChatItemFormatDate(milliseconds);
        }
        Logger.d(TAG, "getSessionNewFormatDate formatDate = " + formatDate);

        return formatDate;
    }

    /**
     * 今天直接显示时间,其他显示月日
     *
     * @param milliseconds
     * @return
     */
    public static String getSessionNewFormatDate2(long milliseconds) {
        Logger.d(TAG, "getSessionNewFormatDate milliseconds = " + milliseconds);

        long compareDayMillis = getFormatParseDateMillis(milliseconds);
        long todayMillis = getFormatParseDateMillis(System.currentTimeMillis());
        // 需要比较的时间减去今天的时间
        long intervalMilli = todayMillis - compareDayMillis;

        // 间隔的天数
        int intervalDay = (int) (intervalMilli / ONE_DAY_MILLISECONDS);
        int absIntervalDay = Math.abs(intervalDay);
        String formatDate = null;
        if (absIntervalDay <= 1) {
            formatDate = getSessionFormatDate(milliseconds);
        } else {
            formatDate = getChatItemFormatDate(milliseconds);
        }
        Logger.d(TAG, "getSessionNewFormatDate formatDate = " + formatDate);

        return formatDate;
    }

    private static long getFormatParseDateMillis(long milliseconds) {
        SimpleDateFormat comprayDay = new SimpleDateFormat(PARSE_FORMAT_DATE_FORMAT, Locale.ENGLISH);
        long formatDateMillis = -1;
        try {
            Date comprayDate = comprayDay.parse(getDayFormatDate(milliseconds));
            formatDateMillis = comprayDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDateMillis;
    }

    private static String getDayFormatDate(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat(PARSE_FORMAT_DATE_FORMAT, Locale.ENGLISH);
        String formatDate = format.format(new Date(milliseconds));
        return formatDate;
    }

    private static String getTimeFormatDate(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat(PARSE_FORMAT_TIME_FORMAT, Locale.ENGLISH);
        String formatDate = format.format(new Date(milliseconds));
        return formatDate;
    }

    public static String getRecordFormatDate(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat(PARSE_FORMAT_DATE_FORMAT2, Locale.ENGLISH);
        String formatDate = format.format(new Date(milliseconds));
        return formatDate;
    }

    public static String formatDurationHHMMss(long duration) {
        long second = duration / 1000;
        long min = second / 60;
        long s = second % 60;
        long h = 0;
        if (min > 60){
            min = min % 60;
            h = min / 60;
        }
        String sH = h > 9 ? ""+h : h == 0 ? "00" : "0" + h;
        String sMin = min > 9 ? "" + min : "0" + min;
        String sS = s > 9 ? "" + s : "0" + s;
        String fDuration = sH + ":" + sMin + ":" + sS;
        return fDuration;
    }

    public static String formatDurationH(long duration) {
        long second = duration / 1000;
        long min = second / 60;
        long s = second % 60;
        String sMin = min > 9 ? "" + min : "0" + min;
        String sS = s > 9 ? "" + s : "0" + s;
        String fDuration = "0:" + sMin + ":" + sS;
        return fDuration;
    }

    public static String formatDurationMMss(long duration) {
        long second = duration / 1000;
        long min = second / 60;
        long s = second % 60;
        String sMin = min > 9 ? "" + min : "0" + min;
        String sS = s > 9 ? "" + s : "0" + s;
        String fDuration = sMin + ":" + sS;
        return fDuration;
    }

    public static String getFormatDate4(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT4, Locale.getDefault());
        String formatDate = format.format(new Date(milliseconds));
        return formatDate;
    }

    public static String getFormatDate5(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT5, Locale.getDefault());
        String formatDate = format.format(new Date(milliseconds));
        return formatDate;
    }

    public static String formatDistance(double disctance) {
        long meter = (long) (disctance % 1000);
        long kilometer = (long) (disctance / 1000);
        String skilometer = kilometer > 9 ? "" + kilometer : "0" + kilometer;
        String smeter = meter > 9 ? "" + meter : "0" + meter;
        String fDistance = skilometer + "." + smeter;
        return fDistance;
    }

    static DecimalFormat decimalFormat = new DecimalFormat(".##");
    public static double retainOne(double d){
        double distance = Double.parseDouble(decimalFormat.format(d)) ;
        return distance;
    }
}
