package com.rakbow.kureakurusu.util.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Rakbow
 * @since 2023-04-23 22:30
 */
public class DateHelper {

    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
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
            return Timestamp.valueOf(ts.replaceAll("/", "-"));
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

}
