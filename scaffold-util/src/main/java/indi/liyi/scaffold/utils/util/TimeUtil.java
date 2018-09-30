package indi.liyi.scaffold.utils.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtil {
    private static final String TAG = "Scaffold-" + TimeUtil.class.getClass().getSimpleName();
    private static final String DEF_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Convert the timestamp to a time string
     *
     * @param millis The timestamp
     * @return a time string
     */
    public static String millisToString(long millis) {
        return millisToString(millis, DEF_FORMAT);
    }

    /**
     * Convert the timestamp to a time string
     *
     * @param millis The timestamp
     * @param format The time format
     * @return a time string
     */
    public static String millisToString(long millis, @Nullable String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(millis));
    }

    /**
     * Convert the time string to a timestamp
     *
     * @param timeStr The time string
     * @return a timestamp
     */
    public static long stringToMillis(String timeStr) {
        return TextUtils.isEmpty(timeStr) ? 0 : stringToMillis(timeStr, DEF_FORMAT);
    }

    /**
     * Convert the time string to a timestamp
     *
     * @param timeStr The time string
     * @param format  The time format
     * @return a timestamp
     */
    public static long stringToMillis(String timeStr, @Nullable String format) {
        if (TextUtils.isEmpty(timeStr)) return 0;
        long millis = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(timeStr);
            millis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "StringToMillis Error ======> timeStr: " + timeStr);
        }
        return millis;
    }

    /**
     * Convert the time string to the date.
     *
     * @param timeStr The time string
     * @return the date
     */
    public static Date stringToDate(String timeStr) {
        return TextUtils.isEmpty(timeStr) ? null : stringToDate(timeStr, DEF_FORMAT);
    }

    /**
     * Convert the time string to the date.
     *
     * @param timeStr The time string
     * @param format  The time format
     * @return the date
     */
    public static Date stringToDate(String timeStr, @Nullable String format) {
        if (TextUtils.isEmpty(timeStr)) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert the date to a time string.
     *
     * @param date The date
     * @return a time string
     */
    public static String dateToString(Date date) {
        return date == null ? null : dateToString(date, DEF_FORMAT);
    }

    /**
     * Convert the date to a time string.
     *
     * @param date   The date
     * @param format The time format
     * @return a time string
     */
    public static String dateToString(Date date, @Nullable String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Convert the date to a time string.
     *
     * @param date The date
     * @return a timestamp
     */
    public static long dateToMillis(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * Convert the timestamp to the date
     *
     * @param millis The timestamp
     * @return the date
     */
    public static Date millisToDate(long millis) {
        return new Date(millis);
    }

    /**
     * Convert the time string to days, hours, minutes, seconds
     *
     * @param timeStr The time string
     * @return int[0]: days <br> int[1]: hours <br> int[2]: minutes <br> int[3]: seconds
     */
    public static int[] stringToArray(String timeStr) {
        return stringToArray(timeStr, DEF_FORMAT);
    }

    /**
     * Convert the time string to days, hours, minutes, seconds
     *
     * @param timeStr The time string
     * @param format  The time format
     * @return int[0]: days <br> int[1]: hours <br> int[2]: minutes <br> int[3]: seconds
     */
    public static int[] stringToArray(String timeStr, @Nullable String format) {
        return TextUtils.isEmpty(timeStr) ? null : millisToArray(stringToMillis(timeStr, format));
    }

    /**
     * Convert the date to days, hours, minutes, seconds
     *
     * @param date The date
     * @return int[0]: days <br> int[1]: hours <br> int[2]: minutes <br> int[3]: seconds
     */
    public static int[] dateToArray(Date date) {
        return date == null ? null : millisToArray(date.getTime());
    }

    /**
     * Convert the timestamp to days, hours, minutes, seconds
     *
     * @param millis The timestamp
     * @return int[0]: days <br> int[1]: hours <br> int[2]: minutes <br> int[3]: seconds
     */
    public static int[] millisToArray(long millis) {
        long secondDiff = millis / 1000;
        int days = (int) (secondDiff / (60 * 60 * 24));
        int hours = (int) ((secondDiff - days * (60 * 60 * 24)) / (60 * 60));
        int minutes = (int) ((secondDiff - days * (60 * 60 * 24) - hours * (60 * 60)) / 60);
        int seconds = (int) ((secondDiff - days * (60 * 60 * 24) - hours * (60 * 60) - minutes * 60));
        return new int[]{days, hours, minutes, seconds};
    }

    /**
     * Calculate the time difference between two times
     *
     * @param startTime The start time
     * @param endTime   The end time
     * @return millisecond time difference
     */
    public static long caculateTimeDiff(@NonNull Object startTime, @NonNull Object endTime) {
        return caculateTimeDiff(startTime, endTime, DEF_FORMAT);
    }

    /**
     * Calculate the time difference between two times
     *
     * @param startTime The start time
     * @param endTime   The end time
     * @param foramt    The time format
     * @return millisecond time difference
     */
    public static long caculateTimeDiff(@NonNull Object startTime, @NonNull Object endTime, @Nullable String foramt) {
        long milliStart, milliEnd;
        if (startTime instanceof String) {
            milliStart = stringToMillis((String) startTime, foramt);
        } else if (startTime instanceof Long || startTime instanceof Integer) {
            milliStart = (long) startTime;
        } else if (startTime instanceof Date) {
            milliStart = ((Date) startTime).getTime();
        } else {
            LogUtil.e(TAG, "Error startTime in the caculateTimeDiff() method ======> startTime: " + startTime);
            throw new UnsupportedOperationException("startTime foramt error");
        }
        if (endTime instanceof String) {
            milliEnd = stringToMillis((String) endTime, foramt);
        } else if (endTime instanceof Long || startTime instanceof Integer) {
            milliEnd = (long) endTime;
        } else if (endTime instanceof Date) {
            milliEnd = ((Date) endTime).getTime();
        } else {
            LogUtil.e(TAG, "Error endTime in the caculateTimeDiff() method ======> endTime: " + endTime);
            throw new UnsupportedOperationException("endTime foramt error");
        }
        return (milliEnd - milliStart);
    }

    /**
     * Calculate the time difference between two times
     *
     * @param startTime The start time
     * @param endTime   The end time
     * @return int[0]: days <br> int[1]: hours <br> int[2]: minutes <br> int[3]: seconds
     */
    public static int[] caculateTimeDiffArray(@NonNull Object startTime, @NonNull Object endTime) {
        return caculateTimeDiffArray(startTime, endTime, DEF_FORMAT);
    }

    /**
     * Calculate the time difference between two times
     *
     * @param startTime The start time
     * @param endTime   The end time
     * @param format    The time format
     * @return int[0]: days <br> int[1]: hours <br> int[2]: minutes <br> int[3]: seconds
     */
    public static int[] caculateTimeDiffArray(@NonNull Object startTime, @NonNull Object endTime, @Nullable String format) {
        return millisToArray(caculateTimeDiff(startTime, endTime, format));
    }

    /**
     * Compare the size of two times
     *
     * @param t1
     * @param t2
     * @return {@code true}: t1 >= t2 <br> {@code false}: t1 < t2
     */
    public static boolean compareTime(@NonNull Object t1, @NonNull Object t2) {
        return caculateTimeDiff(t2, t1) >= 0;
    }
}
