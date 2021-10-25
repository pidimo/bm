package com.piramide.elwis.web.schedulermanager.taglib;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Apr 26, 2005
 * Time: 2:58:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateUI {
    public static final int DAY_VIEW = 0;
    public static final int WEEK_VIEW = 1;
    public static final int MONTH_VIEW = 2;
    public static final int YEAR_VIEW = 3;
    private DateTime date;

    private int type;

    public DateUI(long millis, int type) {
        date = new DateTime(millis);
        this.type = type;
    }

    public long get(int i) {
        Period period = null;
        switch (type) {
            case DAY_VIEW:
                period = Period.days(i);
                break;
            case WEEK_VIEW:
                period = Period.weeks(i);
                break;
            case MONTH_VIEW:
                period = Period.months(i);
                break;
            case YEAR_VIEW:
                period = Period.years(i);
                break;
        }
        return date.plus(period).getMillis();
    }


    public long getPrevius() {
        return get(-1);
    }

    public long getNext() {
        return get(1);
    }


    public Date[] getWeekDates() {
        Date[] d = new Date[2];
        int min, max;
        min = (date.getDayOfWeek() - 1) * -1;
        max = 7 - date.getDayOfWeek();
        DateTime d1, d2;
        d1 = date.plus(Period.days(min));
        d2 = date.plus(Period.days(max));
        d[0] = d1.toDate();
        d[1] = d1.getMonthOfYear() != d2.getMonthOfYear() ? d2.toDate() : null;
        return d;
    }

    public int getYear() {
        return date.getWeekOfWeekyear();
    }

    public Date getDayOrMonthDate() {
        return date.toDate();
    }

    public static void main(String[] arg) {
        /*DateUI ui = new DateUI(new Date(new DateTime().plus(Period.weeks(-1)).getMillis()).getTime(), DateUI.MONTH_VIEW);
        System.out.println(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
        Date[] d = ui.getWeekDates();
        System.out.println(d[0]);
        System.out.println(d[1]);*/


    }

}
