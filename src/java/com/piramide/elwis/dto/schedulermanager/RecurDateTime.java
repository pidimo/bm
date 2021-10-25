package com.piramide.elwis.dto.schedulermanager;

import com.piramide.elwis.utils.SchedulerConstants;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

/**
 * Reminder datetime wrapper.
 *
 * @author Fernando Montaño
 * @version $Id: RecurDateTime.java 9122 2009-04-17 00:31:07Z fernando $
 */

public class RecurDateTime {
    DateTime dateTime;
    String data;

    public RecurDateTime(DateTime dateTime, String data) {
        this.dateTime = dateTime;
        this.data = data;
    }

    public RecurDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
        this.data = null;
    }

    public DateTime getDateTime() {
        return dateTime.toDateTime();
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static boolean isDateTimeAfterThan(DateTime toCompareDate, DateTime compareWithDate) {
        return toCompareDate.toDateMidnight().getMillis() > compareWithDate.toDateMidnight().getMillis();
        /*return DateUtils.dateToInteger(toCompareDate.toDate()).intValue() >
                DateUtils.dateToInteger(compareWithDate.toDate()).intValue();*/
    }

    public String toString() {
        return (dateTime != null) ? dateTime.toString() : "null";
    }

    public long getMillis() {
        return dateTime.getMillis();
    }

    /**
     * gets a reminder date as the representation in the recurrence serie.
     *
     * @param currentNextTime    current reminder time
     * @param reminderTimeBefore time before configured for the reminder
     * @param reminderType       the type of reminder
     * @param ownerDateTimeZone  The owner time zone.
     * @return the DateTime represented in the recurrence series.
     */
    public synchronized static DateTime increaseDateTimeWithReminder(long currentNextTime, int reminderTimeBefore,
                                                                     String reminderType, DateTimeZone ownerDateTimeZone) {
        DateTime currentReminderTime = new DateTime(currentNextTime, ownerDateTimeZone);
        if (SchedulerConstants.REMINDER_TYPE_MINUTES.equals(reminderType)) { //minutes
            return currentReminderTime.plus(Period.minutes(reminderTimeBefore));
        } else if (SchedulerConstants.REMINDER_TYPE_HOURS.equals(reminderType)) { // hours
            return currentReminderTime.plus(Period.hours(reminderTimeBefore));
        } else { //days
            return currentReminderTime.plus(Period.days(reminderTimeBefore));
        }
    }

    /**
     * gets a appointment series date as the representation in the reminder series
     *
     * @param appointmentSeriesTime current appointment time
     * @param reminderTimeBefore    time before configured for the reminder
     * @param reminderType          the type of reminder
     * @param ownerDateTimeZone     The owner time zone.
     * @return the DateTime represented in the recurrence series.
     */
    public synchronized static DateTime decreaseDateTimeWithReminder(long appointmentSeriesTime, int reminderTimeBefore,
                                                                     String reminderType, DateTimeZone ownerDateTimeZone) {
        DateTime currentReminderTime = new DateTime(appointmentSeriesTime, ownerDateTimeZone);
        if (SchedulerConstants.REMINDER_TYPE_MINUTES.equals(reminderType)) { //minutes
            return currentReminderTime.minus(Period.minutes(reminderTimeBefore));
        } else if (SchedulerConstants.REMINDER_TYPE_HOURS.equals(reminderType)) { // hours
            return currentReminderTime.minus(Period.hours(reminderTimeBefore));
        } else { //days
            return currentReminderTime.minus(Period.days(reminderTimeBefore));
        }
    }


}
