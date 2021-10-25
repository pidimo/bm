package com.piramide.elwis.utils;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 7, 2005
 * Time: 9:42:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class SchedulerConstants {

    public static final String TABLE_APPOINTMENT = "appointment";
    public static final String TABLE_TASK = "task";
    public static final String TABLE_USERTASK = "usertask";
    public static final String TABLE_TASKTYPE = "tasktype";
    public static final String TABLE_REMINDER = "reminder";
    public static final String TABLE_SCHEDULEDUSER = "scheduleduser";
    public static final String TABLE_APPOINTMENTTYPE = "appointmenttype";
    public static final String TABLE_SCHEDULERFREETEXT = "freetext";
    public static final String TABLE_RECUREXCEPTION = "recurexception";
    public static final String TABLE_RECURRENCE = "recurrence";
    public static final String TABLE_SCHEDULERACCESS = "scheduleraccess";
    public static final String TABLE_HOLIDAY = "holiday";

    public static final String JNDI_APPOINTMENT = "Elwis.Appointment";
    public static final String JNDI_TASK = "Elwis.Task";
    public static final String JNDI_USERTASK = "Elwis.UserTask";
    public static final String JNDI_TASKTYPE = "Elwis.TaskType";
    public static final String JNDI_RECURRENCE = "Elwis.Recurrence";
    public static final String JNDI_RECUREXCEPTION = "Elwis.RecurException";
    public static final String JNDI_REMINDER = "Elwis.Reminder";
    public static final String JNDI_SCHEDULEDUSER = "Elwis.ScheduledUser";
    public static final String JNDI_APPOINTMENTTYPE = "Elwis.AppointmentType";
    public static final String JNDI_SCHEDULERACCESS = "Elwis.SchedulerAccess";
    public static final String JNDI_SCHEDULER_FREETEXT = "Elwis.SchedulerFreeText";
    public static final String JNDI_SCHEDULERSERVICE = "Elwis.SchedulerService";
    public static final String JNDI_HOLIDAY = "Elwis.Holiday";
    public static final String JNDI_BATCHRECURRENCESREMINDERMDB = "queue/Elwis.BatchRestoreRecurrenceReminder";

    public static final String NOTSTARTED = "2";
    public static final String INPROGRESS = "1";
    public static final String CONCLUDED = "3";
    public static final String DEFERRED = "4";
    public static final String CHECK = "5";
    /**
     * Reminder types constants. They can be:
     * Minutes = reminder x minutes before the appointment time
     * Hours = reminder x hours before the appointment time
     * Days = reminder x days before in the same time of appointment
     */
    public static final String REMINDER_TYPE_MINUTES = "1";
    public static final String REMINDER_TYPE_HOURS = "2";
    public static final String REMINDER_TYPE_DAYS = "3";


    /**
     * Separator for recurrence values *
     */
    public static final String RECURRENCE_VALUES_SEPARATOR = ";";


    /**
     * Recurrence range constants *
     */
    public static final String RECUR_RANGE_NO_ENDING = "1";
    public static final String RECUR_RANGE_AFTER_OCCURRENCE = "2";
    public static final String RECUR_RANGE_DATE = "3";


    /**
     * Recurrence types constants. They can be:
     * - Daily = recur daily
     * - Weekly = recur weekly
     * - Monthly = recur monthly
     * - Yearly = recur yearly
     */
    public static final String RECURRENCE_DAILY = "1";
    public static final String RECURRENCE_WEEKLY = "2";
    public static final String RECURRENCE_MONTHLY = "3";
    public static final String RECURRENCE_YEARLY = "4";

    public static final int RECURRENCE_DAILY_INT = 1;
    public static final int RECURRENCE_WEEKLY_INT = 2;
    public static final int RECURRENCE_MONTHLY_INT = 3;
    public static final int RECURRENCE_YEARLY_INT = 4;


    /**
     * Constant of types values within recurrence monthly type.
     */
    public static final String RECUR_MONTHLY_DAY_OF_MONTH = "1";
    public static final String RECUR_MONTHLY_DAY_OF_OCCURRENCE = "2";

    /**
     * Constant of types values within recurrence yearly type.
     */
    public static final String RECUR_YEARLY_ON_MONTH = "1";
    public static final String RECUR_YEARLY_THIS_DAY = "2";

    /**
     * Occurrence days for monthly recurrence type *
     */
    public static final int MONTHLY_OCCUR_1ST = 1;
    public static final int MONTHLY_OCCUR_2ST = 2;
    public static final int MONTHLY_OCCUR_3RD = 3;
    public static final int MONTHLY_OCCUR_4TH = 4;
    public static final int MONTHLY_OCCUR_5TH = 5;
    public static final int MONTHLY_OCCUR_LAST = 6;

    /*Day of week*/
    public static final String MONDAY = "1";
    public static final String TUESDAY = "2";
    public static final String WEDNESDAY = "3";
    public static final String THURSDAY = "4";
    public static final String FRIDAY = "5";
    public static final String SATURDAY = "6";
    public static final String SUNDAY = "7";


    /**
     * Scheduler configuration constants
     * dayFracmentation
     */
    public static final String DAY_FRAGMENTATION_15MIN = "1";
    public static final String DAY_FRAGMENTATION_30MIN = "2";
    public static final String DAY_FRAGMENTATION_45MIN = "3";
    public static final String DAY_FRAGMENTATION_1H = "4";

    /* return
    */
    public static final String RETURN_DAY = "1";
    public static final String RETURN_WEEK = "2";
    public static final String RETURN_MONTH = "3";
    public static final String RETURN_YEAR = "4";
    public static final String RETURN_SEARCHLIST = "5";
    public static final String RETURN_ADVANCED_SEARCHLIST = "6";
    public static final String RETURN_TAB_PARTICIPANTS = "7";

    /* misc
    */
    public static final String ZERO_VALUE = "0";
    public static final String EMPTY_VALUE = "";

    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";

    /*recurrence?   */
    public static final String ISRECURRENCE = "1";
    public static final String NOTRECURRENCE = "2";

    /*reminder?   */
    public static final String ISREMINDER = "1";
    public static final String NOTREMINDER = "2";

    /*  report types for module*/
    public static final String APPOINTMENT_REPORT_LIST = "1";
    public static final String PARTICIPANT_REPORT_LIST = "2";
    public static final String TASK_REPORT_LIST = "3";
    public static final String USERASSIGNED_REPORT_LIST = "4";

    /*notification?   */
    public static final String HAVE_NOTIFICATION = "1";
    public static final String NOT_HAVE_NOTIFICATION = "2";

    //user grant access to appointment view
    public static final String PROCESS_ONLYPUBLIC_APP = "1";
    public static final String PROCESS_ONLYPRIVATE_APP = "2";
    public static final String PROCESS_ALL_APP = "3";

    public static final String OVERVIEW_SHAREDCALENDARS = "0";

    //visible tasks in taskList
    public static final String SHOW_ALL = "-10";
    public static final String SHOW_NOT_CLOSED = "3";

    public static enum SingleAppointmentFilter {
        PENDING(1, "Appointment.filter.pending"),
        ALL(2, "Appointment.filter.all");

        private Integer code;
        private String resourceKey;

        SingleAppointmentFilter(Integer code, String resourceKey) {
            this.code = code;
            this.resourceKey = resourceKey;
        }

        public Integer getCode() {
            return code;
        }

        public String getResourceKey() {
            return resourceKey;
        }
    }
}
