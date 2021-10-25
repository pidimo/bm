package com.piramide.elwis.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A DateUtils is a util class that provides services to date
 *
 * @author Tito
 * @version $Id: DateUtils.java 12551 2016-05-19 23:56:23Z miguel $
 */
public class DateUtils {

    private static Log log = LogFactory.getLog(DateUtils.class);

    /**
     * Convert java.Util.Date to Integer database format (yyyymmdd).
     *
     * @param date the date to format
     * @return the date in Integer format yyyymmdd.
     */
    public static Integer dateToInteger(Date date) {

        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int formatDate = year * 10000 + month * 100 + day;

        return new Integer(formatDate);

    }

    /**
     * Convert java.Util.Date to Integer database format (yyyymmdd).
     * In this case only mmdd. e.g. 02/23 will be returned as 223.
     *
     * @param date the date to format
     * @return the date in Integer format mmdd.
     */
    public static Integer dateToIntegerWithoutYear(Date date) {

        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int formatDate = month * 100 + day;

        return new Integer(formatDate);

    }

    /**
     * Convert java.Util.Date to Integer database format (yyyymmdd) and add the "inc" (days).
     *
     * @param date the date to format
     * @param inc  the num of days for increments the date
     * @return the date in Integer format yyyymmdd.
     */
    public static Integer dateToInteger(Date date, int inc) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.DATE, inc);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int formatDate = year * 10000 + month * 100 + day;

        return new Integer(formatDate);

    }

    public static Date getNextXMonth(Date initialDate, int x) {
        if (null == initialDate) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(initialDate.getTime());
        calendar.add(Calendar.MONTH, x);

        return calendar.getTime();
    }

    /**
     * Convert java.Util.DateTime to Integer database format (yyyymmdd).
     *
     * @param date the date to format
     * @return the date in Integer format yyyymmdd.
     */
    public static Integer dateToInteger(DateTime date) {
        if (date == null) {
            return null;
        }

        int year = date.getYear();
        int month = date.getMonthOfYear();
        int day = date.getDayOfMonth();

        int formatDate = year * 10000 + month * 100 + day;
        return new Integer(formatDate);

    }

    public static Integer getInteger(int year, int month, int day) {
        int formatDate = year * 10000 + month * 100 + day;
        return new Integer(formatDate);
    }

    /**
     * Convert a Integer value in format yyyymmdd to Date
     *
     * @param date the integer date value
     * @return the date converted
     */
    public static Date integerToDate(Integer date) {
        if (date == null || date.intValue() <= 0) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        int dateInt = date.intValue();
        int year = dateInt / 10000;
        int month = (dateInt / 100) - year * 100;
        int day = (dateInt % 100);
        //required to define 0,0,0 for hours, minutes and seconds
        calendar.set(year, month - 1, day, 0, 0, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * Convert a Integer value in format yyyymmdd to Date
     *
     * @param date     the integer date value
     * @param timezone timezone
     * @return the date converted
     */
    public static Date integerToDate(Integer date, DateTimeZone timezone) {
        Date resultDate = null;
        DateTime dateTime = integerToDateTime(date, timezone);
        if (dateTime != null) {
            resultDate = dateTime.toDate();
        }
        return resultDate;
    }

    /**
     * Convert a Integer value in format yyyymmdd to Date
     *
     * @param year  the integer date value
     * @param month the integer date value
     * @param day   the integer date value
     * @return the date converted
     */
    public static Date intToDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * Convert a Integer value in format yyyymmdd to Date
     *
     * @param date the integer date value
     * @return the date converted
     */
    public static DateTime integerToDateTime(Integer date) {
        return integerToDateTime(date, DateTimeZone.getDefault());
    }

    public static DateTime integerToDateTime(Integer date, DateTimeZone timezone) {
        return integerToDateTime(date, 0, 0, timezone);
    }

    public static String getFormattedDateTimeWithTimeZone(String date, DateTimeZone timeZone, String datePattern) {
        String result = "";
        try {
            Long finalDate = new Long(date);
            result = getFormattedDateTimeWithTimeZone(finalDate, timeZone, datePattern);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public static String getFormattedDateTimeWithTimeZone(Long finalDate, DateTimeZone timeZone, String datePattern) {
        String result = "";
        DateTime d = new DateTime(finalDate.longValue(), timeZone);
        DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);
        result = fmt.print(d);
        return result;
    }

    public static DateTime integerToDateTime(Integer date, int hour, int min) {
        return integerToDateTime(date, hour, min, DateTimeZone.getDefault());
    }

    public static DateTime integerToDateTime(Integer date, int hour, int min, DateTimeZone timeZone) {
        if (date == null || date.intValue() <= 0) {
            return null;
        }

        int dateInt = date.intValue();
        int year = dateInt / 10000;
        int month = (dateInt / 100) - year * 100;
        int day = (dateInt % 100);

        DateTime today;
        try {
            today = new DateTime(year, month, day, hour, min, 0, 0, timeZone);
        } catch (Exception e) {
            today = new DateTime();
        }
        return today;
    }

    public static int[] getYearMonthDay(Integer date) {
        int res[] = new int[3];
        DateTime d = integerToDateTime(date);

        res[0] = d.getYear();
        res[1] = d.getMonthOfYear();
        res[2] = d.getDayOfMonth();
        return res;
    }

    /**
     * Format a String to Date using a given pattern
     *
     * @param aDate   string to format
     * @param pattern pattern to use
     * @return a Date if can be formatted, null otherwise.
     */
    public static Date formatDate(String aDate, String pattern) {
        return DateUtils.formatDate(aDate, pattern, false);
    }

    /**
     * Format a string to Date using a given pattern
     *
     * @param aDate       string to format
     * @param pattern     pattern to use in format
     * @param withoutYear true, if you want to format only day and month without year, false otherwise
     * @return a Date if string can be formatted, null otherwise.
     */
    public static Date formatDate(String aDate, String pattern, boolean withoutYear) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
        dateFormatter.setLenient(false);
        ParsePosition position = new ParsePosition(0);
        Date newDate = dateFormatter.parse(aDate, position);
        if (newDate == null) {
            return null;
        }

        if (position.getIndex() != pattern.length()) //it means no all date was parsed
        {
            return null;
        } else if (position.getIndex() != aDate.length())//it means was typed more than allowed
        {
            return null;
        }

        Date date = new Date(newDate.getTime());

        if (withoutYear) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            calendar.set(Calendar.YEAR, 0);
            return new Date(calendar.getTimeInMillis());
        }
        return date;
    }


    public static String parseDate(Integer aDate, String pattern) {
        return parseDate(integerToDate(aDate), pattern);
    }

    public static String parseDate(Date aDate, String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(aDate);
    }

    public static String parseDate(Date aDate, String pattern, Locale locale) {
        DateFormat formatter = new SimpleDateFormat(pattern, locale);
        return formatter.format(aDate);
    }

    public static String getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        String result = "";
        if (hour < 10) {
            result += "0" + String.valueOf(hour);
        } else {
            result += String.valueOf(hour);
        }

        result += ":";
        if (minute < 10) {
            result += "0" + String.valueOf(minute);
        } else {
            result += String.valueOf(minute);
        }

        result += calendar.getTimeZone().getID();
        return result;
    }

    /**
     * Parse a DateTime as String format in the given date pattern.
     *
     * @param aDateTime   the DateTime to format
     * @param datePattern the format.
     * @return a String formatted.
     */
    public static String parseDate(DateTime aDateTime, String datePattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(datePattern);
        return formatter.print(aDateTime);
    }


    public static Map parseHour(String hour) {
        LinkedList tokenList = new LinkedList();
        tokenList.add("0");
        tokenList.add("0");
        tokenList.add("0");

        StringTokenizer tokenizer = new StringTokenizer(hour, ":");

        int counter = 0;
        while (tokenizer.hasMoreElements()) {
            tokenList.set(counter, tokenizer.nextElement().toString());
            counter++;
        }

        Integer h = Integer.valueOf("0");
        Integer m = Integer.valueOf("0");
        Integer s = Integer.valueOf("0");
        try {
            h = Integer.valueOf((String) tokenList.get(0));
            m = Integer.valueOf((String) tokenList.get(1));
            s = Integer.valueOf((String) tokenList.get(2));
        } catch (NumberFormatException ex) {
        }


        Map time = new HashMap(3);
        time.put("hour", h);
        time.put("minute", m);
        time.put("second", s);

        return time;
    }

    /**
     * Creates DateTime object
     *
     * @param date       date with Integer format
     * @param hour       hour with format "HH:mm:ss"
     * @param timeZoneID time zone Id
     * @return DateTime object
     */
    public static DateTime createDateTime(Integer date, String hour, String timeZoneID) {
        /*log.debug("Executing createDateTime method with\n" +
                "date     = " + date + "\n" +
                "hour     = " + hour + "\n" +
                "timeZone = " + timeZoneID);*/
        Map time = DateUtils.parseHour(hour != null ? hour : "");

        DateTimeZone dateTimeZone = null;
        if (timeZoneID != null) {
            dateTimeZone = DateTimeZone.forID(timeZoneID);
        } else {
            Calendar myCalendar = Calendar.getInstance();

            TimeZone zone = myCalendar.getTimeZone();
            dateTimeZone = DateTimeZone.forTimeZone(zone);
        }

        //convert the integer date in dateTime object
        DateTime dateTime = DateUtils.integerToDateTime(date);
        int dayOfMonth = dateTime.dayOfMonth().get();
        int monthOfYear = dateTime.monthOfYear().get();
        int year = dateTime.year().get();

        //constructs the new datetime
        DateTime myDateTime = new DateTime(dateTimeZone);
        myDateTime = myDateTime.year().setCopy(year);
        myDateTime = myDateTime.monthOfYear().setCopy(monthOfYear);
        myDateTime = myDateTime.dayOfMonth().setCopy(dayOfMonth);


        myDateTime = myDateTime.hourOfDay().setCopy(((Integer) time.get("hour")).intValue());
        myDateTime = myDateTime.minuteOfHour().setCopy(((Integer) time.get("minute")).intValue());
        myDateTime = myDateTime.secondOfMinute().setCopy(((Integer) time.get("second")).intValue());
        myDateTime = myDateTime.toDateTime(dateTimeZone);

        return myDateTime;
    }

    /**
     * Parse a DateTime as String format in the given date pattern.
     *
     * @param dateInMillis date in Long format
     * @param pattern      the format
     * @param timeZoneId   the zoneId for constructs the DateTime object
     * @return a String formatted.
     */
    public static String parseDate(Long dateInMillis, String pattern, String timeZoneId) {

        DateTimeZone zone = DateTimeZone.forID(timeZoneId);

        DateTime dateTime = new DateTime();
        dateTime = dateTime.withZone(zone);
        dateTime = dateTime.withMillis(dateInMillis.longValue());

        return DateUtils.parseDate(dateTime, pattern);
    }

    public static DateTime createDate(Long dateInMillis, String timeZoneId) {
        DateTimeZone zone = DateTimeZone.forID(timeZoneId);

        DateTime dateTime = new DateTime();
        dateTime = dateTime.withZone(zone);
        dateTime = dateTime.withMillis(dateInMillis.longValue());

        return dateTime;
    }

    public static Map<String, Date> getFirstAndEndDayOfMonth(Date actualDate) {
        Map<String, Date> result = new HashMap<String, Date>();
        Calendar actualCalendar = Calendar.getInstance();
        actualCalendar.setTime(actualDate);

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(actualCalendar.get(Calendar.YEAR),
                actualCalendar.get(Calendar.MONTH),
                actualCalendar.getMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0);

        result.put("firstDay", new Date(newCalendar.getTimeInMillis()));

        newCalendar.set(actualCalendar.get(Calendar.YEAR),
                actualCalendar.get(Calendar.MONTH),
                actualCalendar.getActualMaximum(Calendar.DAY_OF_MONTH), 11, 59, 59);

        result.put("endDay", new Date(newCalendar.getTimeInMillis()));

        return result;
    }

    public static Map<String, Date> getFirstAndEndDayOfYear(Date actualDate) {
        Map<String, Date> result = new HashMap<String, Date>();
        Calendar actualCalendar = Calendar.getInstance();
        actualCalendar.setTime(actualDate);

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(actualCalendar.get(Calendar.YEAR),
                Calendar.JANUARY,
                actualCalendar.getMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0);

        result.put("firstDay", new Date(newCalendar.getTimeInMillis()));

        newCalendar.set(actualCalendar.get(Calendar.YEAR),
                Calendar.DECEMBER,
                actualCalendar.getMaximum(Calendar.DAY_OF_MONTH), 11, 59, 59);

        result.put("endDay", new Date(newCalendar.getTimeInMillis()));

        return result;
    }

    public static Integer getCurrentHour(DateTimeZone dateTimeZone) {
        DateTime dateTime;
        if (null == dateTimeZone) {
            dateTime = createDate((new Date()).getTime(), DateTimeZone.getDefault().getID());
        } else {
            dateTime = createDate((new Date()).getTime(), dateTimeZone.getID());
        }

        return dateTime.getHourOfDay();
    }

    /**
     * Check if integer date has 8 digits
     * i.e: valid date is 20160519
     * @param integerDate date
     * @return true or false
     */
    public static boolean isIntegerDateFormat(Integer integerDate) {
        boolean isValid = false;

        if (integerDate != null) {
            //valid integer date 20160519 have 8 digits
            isValid = (integerDate.toString().length() == 8);
        }
        return isValid;
    }
}

