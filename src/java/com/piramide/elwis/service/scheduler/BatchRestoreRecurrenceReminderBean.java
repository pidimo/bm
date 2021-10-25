package com.piramide.elwis.service.scheduler;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.schedulermanager.RecurException;
import com.piramide.elwis.domain.schedulermanager.Reminder;
import com.piramide.elwis.domain.schedulermanager.ReminderHome;
import com.piramide.elwis.dto.schedulermanager.RecurDateTime;
import com.piramide.elwis.dto.schedulermanager.RecurrenceManager;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.*;

/**
 * Class which performs a batch operation trying to restore the reminders for all
 * appointments which have recurrence configured.
 * This process is only done when the applications is started (after stopped). Because it could have
 * various appointments which reminders should have been performed when the application was stopped.
 * To resolve this problem we peform a bacth controls to restore the respective reminder time in the
 * recurrence appointments.
 *
 * @author Fernando Montaño
 * @version 2.1.12
 */


public class BatchRestoreRecurrenceReminderBean implements MessageDrivenBean, MessageListener {
    private Log log = LogFactory.getLog(BatchRestoreRecurrenceReminderBean.class);
    private MessageDrivenContext messageContext = null;

    public BatchRestoreRecurrenceReminderBean() {
    }

    /**
     * Process the recevied MDB message
     *
     * @param message the MDB message
     */
    public void onMessage(Message message) {
        try {
            ReminderHome reminderHome = (ReminderHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_REMINDER);
            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            Date currentDate = new Date();
            Collection reminders = reminderHome.findAllLesserThanDate(new Long(currentDate.getTime() - 180000));
            log.info("checking appointments with reminders: " + reminders.size());
            for (Iterator iterator = reminders.iterator(); iterator.hasNext();) {
                Reminder reminder = (Reminder) iterator.next();
                log.debug("Reminder = " + reminder);
                processNextReminderDate(reminder, userHome);
            }
        } catch (Exception e) {
            log.error("Error processing batch of reminder with recurrences", e);
            messageContext.setRollbackOnly(); //invalid the transaction
        }
    }

    /**
     * Process of calculation of the next date for the reminder, if such date is greater than current time it's ok
     * and the reminder must be updated to such a date, otherwise the reminder cannot be updated because it doesn't have
     * a next date to be reminded in the recurrence
     *
     * @param reminder the reminder bean
     * @param userHome the home interface for the user
     * @throws Exception whether an exception occur
     */
    private void processNextReminderDate(Reminder reminder, UserHome userHome) throws Exception {

        if (reminder.getAppointment().getIsRecurrence().booleanValue() &&
                reminder.getAppointment().getRecurrence() != null &&
                reminder.getAppointment().getRecurrence().getRuleType() != null) { //has recurrence
            //get the owner user
            User ownerUser = userHome.findByPrimaryKey(reminder.getAppointment().getUserId());
            DateTimeZone ownerDateTimeZone = ownerUser.getTimeZone() != null ?
                    DateTimeZone.forID(ownerUser.getTimeZone()) : DateTimeZone.getDefault();

            String recurrenceType = reminder.getAppointment().getRecurrence().getRuleType().toString();
            DateTime appointmentStartDateTime = new DateTime(reminder.getAppointment().getStartDateTime().longValue(),
                    ownerDateTimeZone);
            RecurrenceManager recurManager = new RecurrenceManager(
                    getRecurrenceExceptions(reminder.getAppointment().getRecurrence().getExceptions()),
                    appointmentStartDateTime, reminder.getAppointment().getRecurrence().getRecurEvery().intValue(),
                    reminder.getAppointment().getRecurrence().getRangeType().toString(),
                    reminder.getAppointment().getRecurrence().getRangeValue(), recurrenceType,
                    reminder.getAppointment().getRecurrence().getRuleValue(),
                    reminder.getAppointment().getRecurrence().getRuleValueType() != null ?
                            reminder.getAppointment().getRecurrence().getRuleValueType().toString() : null,
                    ownerDateTimeZone);
            DateTime currentTime = new DateTime(ownerDateTimeZone);
            currentTime = currentTime.secondOfMinute().setCopy(0);
            currentTime = currentTime.millisOfSecond().setCopy(0);
            DateTime startTime = currentTime;
            RecurDateTime nextTime = null;
            while ((nextTime = recurManager.getNextDateTimeFor(startTime)) != null) {
                DateTime resultAsReminder = RecurDateTime.decreaseDateTimeWithReminder(nextTime.getMillis(),
                        reminder.getTimeBefore().intValue(),
                        reminder.getReminderType().toString(), ownerDateTimeZone);
                if (resultAsReminder.getMillis() >= currentTime.getMillis()) {
                    reminder.setNextTime(new Long(resultAsReminder.getMillis()));
                    reminder.setData(nextTime.getData()); //only applicable for week type, the first day of week.
                    break;
                } else {
                    startTime = nextTime.getDateTime();
                }
            }
            if (nextTime == null) //it means the reminder is no longer util, so it should be invalidated (nexttime = 0)
            {
                reminder.setNextTime(new Long(0));
            }
        } else { //a reminder without recurrence was sent, so this reminder now is no valid reminder date in this case, invalid nextdate
            reminder.setNextTime(new Long(0));
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

    public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) throws EJBException {
        this.messageContext = messageDrivenContext;
    }

    public void ejbCreate() {
    }

    public void ejbRemove() throws EJBException {
    }
}
