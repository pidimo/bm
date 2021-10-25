package com.piramide.elwis.dto.schedulermanager;

import com.piramide.elwis.utils.Constants;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class to wrapper the reminder messages.
 *
 * @author Fernando Montaño
 * @version $Id: ReminderMessage.java 10302 2012-12-17 23:00:03Z miguel $
 */

public class ReminderMessage {


    private static final String SUBJECT_BEY = "Reminder.Msg.subject";
    private static final String LOCATION_KEY = "Reminder.Msg.location";
    private static final String TITLE_KEY = "Reminder.Msg.title";
    private static final String CONTACT_KEY = "Reminder.Msg.Contact";
    private static final String CONTACT_PERSON_KEY = "Reminder.Msg.ContactPerson";

    private static final String DATE_PATTERN_KEY = "datePattern";
    private static final String TIME_PATTERN_KEY = "timePattern";

    private static final String DATETIME_SAMEDAY_KEY = "Reminder.Msg.dateTime_sameDay";
    private static final String DATETIME_DIFFERENTDAY_KEY = "Reminder.Msg.dateTime_differentDay";
    private static final String ALLDAY_SAMEDAY_KEY = "Reminder.Msg.allDay_SameDay";
    private static final String ALLDAY_DIFFERENTDAY_KEY = "Reminder.Msg.allDay_DifferentDay";

    private boolean isAllDay;
    private DateTime appointmentStartTime;
    private DateTime appointmentEndTime;
    private String appointmentTitle;
    private String appointmentLocation;
    private String subject;
    private String contactName;
    private String contactPersonName;
    private ResourceBundle rb;


    public ReminderMessage(DateTime appointmentStartTime, DateTime appointmentEndTime,
                           String appointmentTitle, String appointmentLocation, String locale, String contactName,
                           String contactPersonName, boolean isAllDay) {


        this.appointmentStartTime = appointmentStartTime;
        this.appointmentEndTime = appointmentEndTime;
        this.appointmentTitle = appointmentTitle;
        this.appointmentLocation = appointmentLocation;
        this.contactName = contactName;
        this.contactPersonName = contactPersonName;
        this.isAllDay = isAllDay;

        if (locale != null) {
            rb = ResourceBundle.getBundle(Constants.REMINDER_RESOURCES, new Locale(locale));
        } else {
            rb = ResourceBundle.getBundle(Constants.REMINDER_RESOURCES);
        }

        subject = rb.getString(SUBJECT_BEY) + ": " + (appointmentTitle != null ? appointmentTitle : "");

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the Message to be sent by email.
     *
     * @return the message formatted
     */
    public String getMessage() {
        StringBuilder sb = new StringBuilder(calculateDateStrings()).append("\n");
        sb.append(formatString(TITLE_KEY, new Object[]{appointmentTitle})).append("\n");
        if (appointmentLocation != null) {
            sb.append(formatString(LOCATION_KEY, new Object[]{appointmentLocation})).append("\n");
        }
        if (contactName != null) {
            sb.append(formatString(CONTACT_KEY, new Object[]{contactName})).append("\n");
        }
        if (contactPersonName != null) {
            sb.append(formatString(CONTACT_PERSON_KEY, new Object[]{contactPersonName})).append("\n");
        }

        return new String(sb);
    }

    /**
     * Calculate the message for the dates according its type.
     *
     * @return the date fomatted.
     */
    private String calculateDateStrings() {
        String dateTimeMessage;
        if (isAllDay) {
            if (isSameDay(appointmentStartTime, appointmentEndTime)) {
                dateTimeMessage = formatString(ALLDAY_SAMEDAY_KEY,
                        new Object[]{formatDateTime(DATE_PATTERN_KEY, appointmentStartTime)});
            } else {
                dateTimeMessage = formatString(ALLDAY_DIFFERENTDAY_KEY,
                        new Object[]{formatDateTime(DATE_PATTERN_KEY, appointmentStartTime),
                                formatDateTime(DATE_PATTERN_KEY, appointmentEndTime)});
            }
        } else {
            if (isSameDay(appointmentStartTime, appointmentEndTime)) {
                dateTimeMessage = formatString(DATETIME_SAMEDAY_KEY,
                        new Object[]{formatDateTime(DATE_PATTERN_KEY, appointmentStartTime),
                                formatDateTime(TIME_PATTERN_KEY, appointmentStartTime),
                                formatDateTime(TIME_PATTERN_KEY, appointmentEndTime)});

            } else {
                dateTimeMessage = formatString(DATETIME_DIFFERENTDAY_KEY,
                        new Object[]{formatDateTime(DATE_PATTERN_KEY, appointmentStartTime),
                                formatDateTime(TIME_PATTERN_KEY, appointmentStartTime),
                                formatDateTime(DATE_PATTERN_KEY, appointmentEndTime),
                                formatDateTime(TIME_PATTERN_KEY, appointmentEndTime)});
            }
        }
        return dateTimeMessage;
    }

    /**
     * Calculate if two DateTime are equal only by MM/DD/YY
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return true if are equal, false if one is greather than another.
     */
    private boolean isSameDay(DateTime startTime, DateTime endTime) {
        YearMonthDay startDay = startTime.toYearMonthDay();
        YearMonthDay endDay = endTime.toYearMonthDay();
        if (startDay.isEqual(endDay)) {
            return true;
        }
        return false;

    }

    /**
     * Format an string with params
     *
     * @param key    the resource key
     * @param params the object params
     * @return formatted string
     */
    private String formatString(String key, Object[] params) {
        MessageFormat formatString = new MessageFormat(rb.getString(key));
        return formatString.format(params);
    }

    /**
     * Format a DateTime using a pattern obtained from a resource key given as parameter.
     *
     * @param dateFormatKey the date pattern key.
     * @param dateTime      the date time to be formatted.
     * @return the string reprsenting the formatted datetime.
     */
    private String formatDateTime(String dateFormatKey, DateTime dateTime) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(rb.getString(dateFormatKey));
        return fmt.print(dateTime);
    }

}
