package com.piramide.elwis.service.scheduler;

import com.piramide.elwis.cmd.utils.Email;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.schedulermanager.RecurException;
import com.piramide.elwis.domain.schedulermanager.Reminder;
import com.piramide.elwis.domain.schedulermanager.ReminderHome;
import com.piramide.elwis.domain.schedulermanager.ScheduledUser;
import com.piramide.elwis.dto.schedulermanager.RecurDateTime;
import com.piramide.elwis.dto.schedulermanager.RecurrenceManager;
import com.piramide.elwis.dto.schedulermanager.ReminderMessage;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import javax.ejb.*;
import java.util.*;

/**
 * Scheduler service methods
 *
 * @author Fernando Montaño
 * @version $Id: SchedulerServiceBean.java 9703 2009-09-12 15:46:08Z fernando $
 */


public class SchedulerServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(SchedulerServiceBean.class);
    private SessionContext ctx;

    public SchedulerServiceBean() {
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        this.ctx = sessionContext;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    /**
     * Perform the reminder in a specific time of scheduling service.
     *
     * @param timeOfCall            the time of call
     * @param intervalBetweenChecks the interval configured between reminders checking.
     */
    public void performReminder(Date timeOfCall, long intervalBetweenChecks) {
        try {
            ReminderHome reminderHome = (ReminderHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_REMINDER);
            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
            /** The range is -3+ minutes**/
            long initialTime = timeOfCall.getTime() - intervalBetweenChecks;
            long endingTime = timeOfCall.getTime() + intervalBetweenChecks;


            String reminderMailFrom = ConfigurationFactory.getConfigurationManager().getValue("elwis.system.fromEmailSender");

            try {
                Collection remindersToDo = reminderHome.findAllBetweenDate(new Long(initialTime), new Long(endingTime));
                for (Iterator iterator = remindersToDo.iterator(); iterator.hasNext();) {
                    Reminder reminder = (Reminder) iterator.next();
                    try {
                        sendReminderToParticipants(reminder, userHome, addressHome, reminderMailFrom);
                        processNextReminderDate(reminder, userHome);
                    } catch (Exception ex) {
                        log.error("Error sending reminder or calculating next reminder date", ex);
                    }

                }
            } catch (FinderException fe) {
                log.debug("Nothing to remind...");
            }


        } catch (Exception e) {
            log.error("Error processing reminders called at " + timeOfCall, e);
            ctx.setRollbackOnly();
        }
    }

    /**
     * Send the reminder to participants of the appointment.
     *
     * @param reminder The Reminder Cron object
     */
    private void sendReminderToParticipants(Reminder reminder, UserHome userHome, AddressHome addressHome,
                                            String reminderMailFrom) throws Exception {

        Collection scheduledUsers = reminder.getAppointment().getScheludedUsers();
        User user = null;
        DateTime userStartDateTime = null;
        DateTime userEndDateTime = null;
        Email email = null;
        DateTimeZone timeZone = null;
        // DateTime reminderDateTime = new DateTime(reminder.getNextTime().longValue());
        ReminderMessage reminderMsg = null;

        //get the owner user
        User ownerUser = userHome.findByPrimaryKey(reminder.getAppointment().getUserId());

        String contactName = null;
        String contactPersonName = null;
        //defining the contact name and contact person name if available.
        if (reminder.getAppointment().getAddressId() != null) {
            Address contact = null;
            try {
                contact = addressHome.findByPrimaryKey(reminder.getAppointment().getAddressId());
                contactName = contact.getName();
            } catch (FinderException e) {
                //nothing to do.
            }
            if (reminder.getAppointment().getContactPersonId() != null) {
                Address contactPerson = null;
                try {
                    contactPerson = addressHome.findByPrimaryKey(reminder.getAppointment().getContactPersonId());
                    contactPersonName = contactPerson.getName();
                } catch (FinderException e) {
                    //nothing to do.
                }
            }
        }
        boolean isAllDay = false;
        if (reminder.getAppointment().getIsAllDay() != null) {
            isAllDay = reminder.getAppointment().getIsAllDay().booleanValue();
        }

        DateTimeZone ownerDateTimeZone = ownerUser.getTimeZone() != null ?
                DateTimeZone.forID(ownerUser.getTimeZone()) : DateTimeZone.getDefault();

        DateTime nextAppointmentDate = RecurDateTime.increaseDateTimeWithReminder(reminder.getNextTime().longValue(),
                reminder.getTimeBefore().intValue(), reminder.getReminderType().toString(), ownerDateTimeZone);
        //calculating the duration of the original appointment in order to increment to get the end date for the appointment.
        Duration appointmentDuration = new Duration(reminder.getAppointment().getStartDateTime(),
                reminder.getAppointment().getEndDateTime());


        for (Iterator iterator = scheduledUsers.iterator(); iterator.hasNext();) {
            ScheduledUser scheduledUser = (ScheduledUser) iterator.next();
            try {
                user = userHome.findByPrimaryKey(scheduledUser.getUserId()); //user recipient
                timeZone = user.getTimeZone() != null ? DateTimeZone.forID(user.getTimeZone()) : DateTimeZone.getDefault();
                userStartDateTime = nextAppointmentDate.toDateTime(timeZone);
                userEndDateTime = userStartDateTime.plus(appointmentDuration);

                reminderMsg = new ReminderMessage(userStartDateTime, userEndDateTime, reminder.getAppointment().getTitle(),
                        reminder.getAppointment().getLocation(), user.getFavoriteLanguage(), contactName,
                        contactPersonName, isAllDay);
                for (Iterator iterator1 = user.getAppointmentNotificationEmails().iterator(); iterator1.hasNext();) {
                    email = new Email((String) iterator1.next(), reminderMailFrom, reminderMsg.getSubject(),
                            reminderMsg.getMessage(), "text/plain",
                            user.getTimeZone() != null ? timeZone.toTimeZone() : TimeZone.getDefault());
                    email.setUserId(user.getUserId());
                    JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_SENDSIMPLEMAILMDB, email, false);
                }
            } catch (FinderException fe) {
                log.debug("The user was removed, then it's not needed to send him an reminder email");
            }
        }
    }

    /**
     * Process the next reminder date for an appointment depending of the recurrence type. Only for recurrent
     * appointment
     *
     * @param reminder Reminder Bean
     */
    public void processNextReminderDate(Reminder reminder, UserHome userHome) throws Exception {

        if (reminder.getAppointment().getIsRecurrence().booleanValue() &&
                reminder.getAppointment().getRecurrence() != null &&
                reminder.getAppointment().getRecurrence().getRuleType() != null) { //has recurrence
            //get the owner user
            User ownerUser = userHome.findByPrimaryKey(reminder.getAppointment().getUserId());
            DateTimeZone ownerDateTimeZone = ownerUser.getTimeZone() != null ?
                    DateTimeZone.forID(ownerUser.getTimeZone()) : DateTimeZone.getDefault();

            String recurrenceType = reminder.getAppointment().getRecurrence().getRuleType().toString();
            DateTime appointmentStartDateTime =
                    new DateTime(reminder.getAppointment().getStartDateTime().longValue(), ownerDateTimeZone);
            DateTime seriesReminderDateTime = RecurDateTime.increaseDateTimeWithReminder(reminder.getNextTime().longValue(),
                    reminder.getTimeBefore().intValue(), reminder.getReminderType().toString(), ownerDateTimeZone);

            RecurDateTime nextSeriesReminderDateTime = null;
            RecurrenceManager recurManager =
                    new RecurrenceManager(getRecurrenceExceptions(reminder.getAppointment().getRecurrence().getExceptions()),
                            appointmentStartDateTime, reminder.getAppointment().getRecurrence().getRecurEvery().intValue(),
                            reminder.getAppointment().getRecurrence().getRangeType().toString(),
                            reminder.getAppointment().getRecurrence().getRangeValue(), recurrenceType,
                            reminder.getAppointment().getRecurrence().getRuleValue(),
                            reminder.getAppointment().getRecurrence().getRuleValueType() != null ?
                                    reminder.getAppointment().getRecurrence().getRuleValueType().toString() : null,
                            ownerDateTimeZone);

            //for daily recurrence type
            if (SchedulerConstants.RECURRENCE_DAILY.equals(recurrenceType)) {
                nextSeriesReminderDateTime = recurManager.getNextDailyDateTime(seriesReminderDateTime);
                //then update the reminderreminder with the next date time, otherwise leave as same, it means it
                // has not more recurrences
                if (nextSeriesReminderDateTime != null) {
                    reminder.setNextTime(new Long(RecurDateTime.decreaseDateTimeWithReminder(nextSeriesReminderDateTime.getMillis(),
                            reminder.getTimeBefore().intValue(),
                            reminder.getReminderType().toString(), ownerDateTimeZone).getMillis()));
                    reminder.setData(String.valueOf(nextSeriesReminderDateTime.getDateTime().getDayOfWeek()));
                } else {
                    reminder.setNextTime(new Long(0));//it means the reminder is no longer util, so it should be invalidated (nexttime = 0)
                }
            } else if (SchedulerConstants.RECURRENCE_WEEKLY.equals(recurrenceType)) { //for weekly recurrence type
                nextSeriesReminderDateTime = recurManager.getNextWeeklyDateTime(seriesReminderDateTime,
                        seriesReminderDateTime, new Integer(reminder.getData()).intValue());
                if (nextSeriesReminderDateTime != null) {
                    reminder.setNextTime(new Long(RecurDateTime.decreaseDateTimeWithReminder(nextSeriesReminderDateTime.getMillis(),
                            reminder.getTimeBefore().intValue(),
                            reminder.getReminderType().toString(), ownerDateTimeZone).getMillis()));
                    reminder.setData(nextSeriesReminderDateTime.getData());
                } else {
                    reminder.setNextTime(new Long(0));//it means the reminder is no longer util, so it should be invalidated (nexttime = 0)
                }
            } else if (SchedulerConstants.RECURRENCE_MONTHLY.equals(recurrenceType)) { //for monthly recurrence
                nextSeriesReminderDateTime = recurManager.getNextMonthlyDateTime(seriesReminderDateTime,
                        seriesReminderDateTime);
                //then update the reminderreminder with the next date time, otherwise leave as same, it means it
                //  has not more recurrences
                if (nextSeriesReminderDateTime != null) {
                    reminder.setNextTime(new Long(RecurDateTime.decreaseDateTimeWithReminder(nextSeriesReminderDateTime.getMillis(),
                            reminder.getTimeBefore().intValue(),
                            reminder.getReminderType().toString(), ownerDateTimeZone).getMillis()));
                    reminder.setData(String.valueOf(nextSeriesReminderDateTime.getDateTime().getMonthOfYear()));
                } else {
                    reminder.setNextTime(new Long(0));//it means the reminder is no longer util, so it should be invalidated (nexttime = 0)
                }
            } else { //it is yearly recurrence
                nextSeriesReminderDateTime = recurManager.getNextYearlyDateTime(seriesReminderDateTime);
                //then update the reminderreminder with the next date time, otherwise leave as same, it means it has
                // not more recurrences
                if (nextSeriesReminderDateTime != null) {
                    reminder.setNextTime(new Long(RecurDateTime.decreaseDateTimeWithReminder(nextSeriesReminderDateTime.getMillis(),
                            reminder.getTimeBefore().intValue(),
                            reminder.getReminderType().toString(), ownerDateTimeZone).getMillis()));
                    reminder.setData(String.valueOf(nextSeriesReminderDateTime.getDateTime().getYear()));
                } else {
                    reminder.setNextTime(new Long(0));//it means the reminder is no longer util, so it should be invalidated (nexttime = 0)
                }
            }
        } else { //a reminder without recurrence was sent, so this reminder now is no valid reminder date in this case, invalid nextdate
            reminder.setNextTime(new Long(0));//it means the reminder is no longer util, so it should be invalidated (nexttime = 0)
        }

    }

    /**
     * Process a recurrence exceptions
     *
     * @param recurExceptions the Collection on RecurException beans
     * @return a list of simple Integers os exceptions
     */
    private List getRecurrenceExceptions(Collection recurExceptions) {
        List result = new ArrayList();
        for (Iterator iterator = recurExceptions.iterator(); iterator.hasNext();) {
            RecurException exception = (RecurException) iterator.next();
            result.add(exception.getDateValue());//put the integer date representation
        }
        return result;
    }


}


