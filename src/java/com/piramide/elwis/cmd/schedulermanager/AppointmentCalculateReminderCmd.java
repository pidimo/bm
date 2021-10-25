package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.domain.schedulermanager.RecurException;
import com.piramide.elwis.domain.schedulermanager.Reminder;
import com.piramide.elwis.dto.schedulermanager.RecurDateTime;
import com.piramide.elwis.dto.schedulermanager.RecurrenceManager;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.DateUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Calculate appointment dates in millis and define the next reminder time in create or update of
 * an appointment detail of an appointment recurrence info.
 * This class needs as first parameter a reference of Appointment Entity Bean.
 *
 * @author Fernando Montaño
 * @version $Id: AppointmentCalculateReminderCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class AppointmentCalculateReminderCmd extends EJBCommand {
    private Log log = LogFactory.getLog(AppointmentCalculateReminderCmd.class);

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        log.debug("Calculate appointment/reminder date times for appointment= " + paramDTO.get("appointment"));

        try {
            Appointment appointment = (Appointment) paramDTO.get("appointment");
            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            if (appointment != null) {
                User ownerUser = userHome.findByPrimaryKey(appointment.getUserId());
                DateTimeZone timeZone = DateTimeZone.getDefault();
                if (ownerUser.getTimeZone() != null) {
                    timeZone = DateTimeZone.forID(ownerUser.getTimeZone());
                }

                appointment.setStartDateTime(new Long(getCompleteDateTime(appointment.getStartDate(),
                        appointment.getStartTime(), timeZone).getMillis()));

                appointment.setEndDateTime(new Long(getCompleteDateTime(appointment.getEndDate(),
                        appointment.getEndTime(), timeZone).getMillis()));

                Reminder reminder = appointment.getReminder();
                //set the default reminder to appointment startdate
                if (reminder != null) {

                    reminder.setNextTime(new Long(RecurDateTime.decreaseDateTimeWithReminder(appointment.getStartDateTime().longValue(),
                            reminder.getTimeBefore().intValue(),
                            reminder.getReminderType().toString(), timeZone).getMillis()));
                    reminder.setData("1"); //only applicable for week type, the first day of week.

                    /**
                     * ONLY FOR RECURRENT APPOINTMENTS
                     * checking if the current nextdate in the reminder is greater than current date, if not, calculate
                     * the first greater date after now and update the nexttime date.
                     * This is to mantain a consistency in the reminder service, because is it not possible to
                     * send a reminder mail if the nexttime date is old than now.
                     */
                    DateTime systemTime = new DateTime();
                    systemTime = systemTime.secondOfMinute().setCopy(0);
                    systemTime = systemTime.millisOfSecond().setCopy(0);
                    //if the nexttime is lesser than current time then calculate a valid next datetime.
                    log.debug("Current Reminder time = " + new DateTime(reminder.getNextTime()) + " System time = " + systemTime);
                    if (reminder.getNextTime().longValue() < systemTime.getMillis()) {
                        if (reminder.getAppointment().getIsRecurrence().booleanValue() &&
                                reminder.getAppointment().getRecurrence() != null &&
                                reminder.getAppointment().getRecurrence().getRuleType() != null) { //has a recurrence
                            String recurrenceType = reminder.getAppointment().getRecurrence().getRuleType().toString();
                            DateTime appointmentStartDateTime =
                                    new DateTime(reminder.getAppointment().getStartDateTime().longValue(), timeZone);
                            List exceptions = new ArrayList(); //get the configured exceptions
                            for (Iterator iterator = reminder.getAppointment().getRecurrence().getExceptions().iterator(); iterator.hasNext();) {
                                RecurException exception = (RecurException) iterator.next();
                                exceptions.add(exception.getDateValue());//put the integer date representation
                            }
                            RecurrenceManager recurManager = new RecurrenceManager(exceptions,
                                    appointmentStartDateTime, reminder.getAppointment().getRecurrence().
                                            getRecurEvery().intValue(),
                                    reminder.getAppointment().getRecurrence().getRangeType().toString(),
                                    reminder.getAppointment().getRecurrence().getRangeValue(), recurrenceType,
                                    reminder.getAppointment().getRecurrence().getRuleValue(),
                                    reminder.getAppointment().getRecurrence().getRuleValueType() != null ?
                                            reminder.getAppointment().getRecurrence().getRuleValueType().toString() : null,
                                    timeZone);
                            DateTime currentTime = new DateTime(systemTime, timeZone);
                            RecurDateTime nextTime = null;
                            DateTime startTime = currentTime;
                            while ((nextTime = recurManager.getNextDateTimeFor(startTime)) != null) {
                                DateTime resultAsReminder = RecurDateTime.decreaseDateTimeWithReminder(nextTime.getMillis(),
                                        reminder.getTimeBefore().intValue(),
                                        reminder.getReminderType().toString(), timeZone);
                                if (resultAsReminder.getMillis() > currentTime.getMillis()) {
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
                        } else { //the reminder not belongs to appointment recurrent
                            //it means the reminder is no longer util, so it should be invalidated (nexttime = 0)
                            reminder.setNextTime(new Long(0));
                        }

                    }
                    if (reminder.getNextTime().longValue() != 0) {
                        log.debug("Next First Reminder Date = " + new DateTime(reminder.getNextTime(), timeZone));
                    } else {
                        log.debug("Next First Reminder Date = 0 (there is no a next reminder date");
                    }

                    //appointment.setReminder(reminder);
                } else {
                    log.debug("Reminder has not been configured, so there is no a next reminder date");
                }
            }
        } catch (Exception e) {
            log.error("Error calculating the reminder date", e);
            resultDTO.setResultAsFailure();
            throw new AppLevelException(e);
        }
    }

    public boolean isStateful() {
        return false;
    }

    private DateTime getCompleteDateTime(Integer date, String time, DateTimeZone zone) {
        String[] hourMinute = time.split(":");
        int yearMonthDay[] = DateUtils.getYearMonthDay(date);
        return new DateTime(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2],
                Integer.parseInt(hourMinute[0]), Integer.parseInt(hourMinute[1]), 0, 0, zone);
    }
}
