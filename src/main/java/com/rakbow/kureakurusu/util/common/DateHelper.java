package com.rakbow.kureakurusu.util.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rakbow.kureakurusu.data.common.Constant.*;

/**
 * @author Rakbow
 * @since 2023-04-23 22:30
 */
public class DateHelper {

    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String EMPTY_DURATION = "00:00:00";

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }
    public static String nowStr() {
        return timestampToString(new Timestamp(System.currentTimeMillis()));
    }

    //日期转为字符串(自定义格式)，例如：yyyy/MM/dd
    public static String dateToString(Date date) {
        if (date != null) {
            SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);
            return ft.format(date);
        } else {
            return null;
        }
    }

    //时间转为字符串(自定义格式)，例如：yyyy/MM/dd
    public static String timestampToString(Timestamp ts) {
        if (ts != null) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
            return localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        } else {
            return null;
        }
    }

    //时间转为字符串(自定义格式)，例如：yyyy/MM/dd
    public static Timestamp stringToTimestamp(String ts) {
        if (ts != null) {
            return Timestamp.valueOf(ts.replaceAll(SLASH, BAR));
        } else {
            return null;
        }
    }

    public static String getCurrentTime() {
        DateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        return sdf.format(DateHelper.now());
    }

    //字符串转为时间(自定义格式)，例如：yyyy/MM/dd
    public static Date stringToDate(String dateString, String dateFormat) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat(dateFormat);
        return ft.parse(dateString);
    }

    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);
        return ft.parse(dateString);
    }

    public static String getDuration(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
        } else {
            return String.format("%02d:%02d", minutes, remainingSeconds);
        }
    }

    public static int getDuration(String timeStr) {
        String time = timeStr.replace(TAB, "").trim();
        String[] parts = time.split(":");

        // 如果格式不符合预期，抛出异常
        if (parts.length < 2 || parts.length > 3) {
            throw new IllegalArgumentException("Invalid time format");
        }

        int minutes = Integer.parseInt(parts[parts.length - 2]);
        int seconds = Integer.parseInt(parts[parts.length - 1]);

        if (parts.length == 3) {
            int hours = Integer.parseInt(parts[0]);
            return hours * 3600 + minutes * 60 + seconds;
        } else {
            return minutes * 60 + seconds;
        }
    }

}
