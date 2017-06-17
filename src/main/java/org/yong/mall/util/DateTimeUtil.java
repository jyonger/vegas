package org.yong.mall.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by Yong on 2017/6/12.
 */
public class DateTimeUtil {

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String toString(Date dateTime, String format) {
        if (dateTime == null) {
            return "";
        }
        DateTime jodaDate = new DateTime(dateTime);
        return jodaDate.toString(format);
    }

    public static Date toDate(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        DateTime newDate = formatter.parseDateTime(dateString);
        return newDate.toDate();
    }

    public static String toString(Date dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTime jodaDate = new DateTime(dateTime);
        return jodaDate.toString(STANDARD_FORMAT);
    }

    public static Date toDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime newDate = formatter.parseDateTime(dateString);
        return newDate.toDate();
    }
}
