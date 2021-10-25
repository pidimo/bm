package com.piramide.elwis.web.schedulermanager.taglib;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Apr 26, 2005
 * Time: 6:43:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CalendarIU {
    protected static Log log = LogFactory.getLog(CalendarIU.class);

    //protected static Log log = LogFactory.getLog(CalendarIU.class);
    private DateTime month;
    private int currentMaxDaysOfMonth;
    private int startWeek;
    private int endWeek;

    private int requestMonth;
    private int requestYear;
    private int currentWeek;
    private int currentDay;
    private int currentYear;
    private int currentMonth;
    private int currentMaxWeeksOfYear;
    private int weeksAmount;

    private DateTime getFirstDateOfWeek(DateTime dateOfWeek) {
        int min = dateOfWeek.getDayOfWeek() - 1;
        return dateOfWeek.minus(Period.days(min));
    }

    private DateTime getLastDateOfWeek(DateTime dateOfWeek) {
        int min = 7 - dateOfWeek.getDayOfWeek();
        return dateOfWeek.plus(Period.days(min));
    }

    // TODO: Controlar que no pueda pedir el iterador si se creo como WEEK
    public CalendarIU(int month, int year) {
        if (month < 1) {
            month = 1;
        } else if (month > 12) {
            month = 12;
        }

        this.month = new DateTime(year, month, 1, 0, 0, 0, 0);
        initialize();
    }

    public DateTime getFirstDayOfMonth() {
        return month;
    }

    public DateTime getLastDayOfMonth() {
        return month.plus(Period.days(month.dayOfMonth().getMaximumValue() - 1));
    }

    public CalendarIU() {
    }

    public void setWeek(int week) {
        setWeek(week, -1);
    }

    public void setWeek(int week, int year) {
        DateTime today;
        if (year < 0) {
            today = new DateTime();
        } else {
            today = new DateTime(year, 6, 1, 0, 0, 0, 0);
        }


        if (week < 1) {
            week = 1;
        } else if (week > today.weekOfWeekyear().getMaximumValue()) {
            week = today.weekOfWeekyear().getMaximumValue();
        }

        int offsetWeeks = week - today.getWeekOfWeekyear();
        //log.debug("Off:" + offsetWeeks + " - TW:" + today.getWeekOfWeekyear() + " -TODAY:" + today);
        month = today.plus(Period.weeks(offsetWeeks));
        //log.debug("MONTH:" + month);
        initialize();
        currentWeek++;
    }


    private void initialize() {
        DateTime iniDayOfFirstWeekMonth = getFirstDateOfWeek(month);
//        log.debug("Start date.........:"+month);
//        log.debug("Fixed to process date.......:"+iniDayOfFirstWeekMonth);
        currentMaxDaysOfMonth = iniDayOfFirstWeekMonth.dayOfMonth().getMaximumValue();
        currentMaxWeeksOfYear = iniDayOfFirstWeekMonth.weekOfWeekyear().getMaximumValue();
        DateTime endDayOfMonth = new DateTime(month.getYear(), month.getMonthOfYear(), month.dayOfMonth().getMaximumValue(), 0, 0, 0, 0);
        startWeek = iniDayOfFirstWeekMonth.getWeekOfWeekyear();
        endWeek = endDayOfMonth.getWeekOfWeekyear();

        currentYear = iniDayOfFirstWeekMonth.getYear();
        currentMonth = iniDayOfFirstWeekMonth.getMonthOfYear();
        currentDay = iniDayOfFirstWeekMonth.getDayOfMonth() - 1;
        currentWeek = startWeek - 1;
        requestMonth = month.getMonthOfYear();
        requestYear = month.getYear();


        if (startWeek <= endWeek) {
            weeksAmount = endWeek - startWeek;
        } else if (endWeek == 1) {
            weeksAmount = 1 + currentMaxWeeksOfYear - startWeek;
        } else {
            weeksAmount = endWeek;
        }


//        log.debug("StartWeek:" + startWeek + " - " + "endWeek:" + endWeek);
        weeksAmount++;

        //log.debug("INIT:" + currentYear + "-" + currentMonth + "-" + currentDay + "|" + requestYear + "-" + requestMonth);
    }

    public Iterator getIteratorWeek() {
        return new IteratorWeek();
    }

    public Week getWeek() {
        return new Week();
    }

    public final class Day {
        private boolean isOutMonth;
        private int day;
        private int month;
        private int year;

        public Day(boolean isOutMonth, int day, int month, int year) {
            this.isOutMonth = isOutMonth;
            this.day = day;
            this.month = month;
            this.year = year;
        }

        public int getDay() {
            return day;
        }

        public int getMonth() {
            return month;
        }

        public int getYear() {
            return year;
        }

        public Integer toIntegerDate() {
            return new Integer(year * 10000 + month * 100 + day);
        }

        public boolean isOutMonth() {
            return isOutMonth;
        }

        public boolean equalTo(DateTime dt) {
            return dt.getDayOfMonth() == day && dt.getMonthOfYear() == month && dt.getYear() == year;
        }


        public String toString() {
            return "day=" + day +
                    ", month=" + month +
                    ", year=" + year;

        }
    }

    public final class Week {
        private int count;
        private int weekYear;

        private Week() {
            count = 0;
            weekYear = currentYear;
        }

        private Week(boolean isWeekInNextYear) {
            count = 0;
            weekYear = currentYear;
            if (isWeekInNextYear) {
                weekYear = currentYear + 1;
            }
        }

        public int getYear() {
            return currentYear;
        }

        public int getWeekYearToUI() {
            return weekYear;
        }

        public boolean hasNextDay() {
            return count + 1 <= 7;
        }

        public Day nextDay() {
            count++;
            currentDay++;
            if (currentDay > currentMaxDaysOfMonth) {
                currentDay = 1;
                currentMonth++;
                if (currentMonth > 12) {
                    currentMonth = 1;
                    currentYear++;
                }
                currentMaxDaysOfMonth = new DateTime(currentYear, currentMonth, 1, 0, 0, 0, 0).dayOfMonth().getMaximumValue();
            }

            return new Day(currentMonth != requestMonth, currentDay, currentMonth, currentYear);
        }

        public int getWeek() {
            return currentWeek;
        }

        public DateTime getStartDate() {
            return new DateTime(currentYear, currentMonth, currentDay + 1, 0, 0, 0, 0);
        }

        public DateTime getStartDate(DateTimeZone timeZone) {
            return new DateTime(currentYear, currentMonth, currentDay + 1, 0, 0, 1, 0, timeZone);
        }

        public DateTime getEndDate(DateTime startDate) {
            return startDate.plus(Period.days(6));
        }
    }

    private class IteratorWeek implements Iterator {
        private int week_count;

        public IteratorWeek() {
            week_count = 0;
        }

        public boolean hasNext() {
            return week_count + 1 <= weeksAmount;
        }

        public Object next() {
            boolean isInNextYear = false;
            currentWeek++;
            if (currentWeek > currentMaxWeeksOfYear) {
                currentWeek = 1;
                if (currentMonth == 12) {
                    isInNextYear = true;
                }
            } else if (currentWeek == 1 && currentYear < requestYear) {
                isInNextYear = true;
            }

            week_count++;

            if (isInNextYear) {
                return new Week(isInNextYear);
            }
            return new Week();
        }

        public void remove() {
        }
    }


    public String toString() {
        return "CalendarIU{" +
                "monthOfWeek=" + month +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", currentWeek=" + currentWeek +
                "}";
    }

    public void showWeek(CalendarIU.Week week) {
        System.out.print(StringUtils.center(Integer.toString(week.getWeek()) + "-" + week.getYear(), 7) + "|");
        while (week.hasNextDay()) {
            CalendarIU.Day day = week.nextDay();
            System.out.print(StringUtils.center((day.getDay() < 10 ? "0" : "") + Integer.toString(day.getDay()) + "-" + (day.getMonth() < 10 ? "0" : "") + day.getMonth() + "-" + day.getYear(), 5) + "|");
        }
    }
}
