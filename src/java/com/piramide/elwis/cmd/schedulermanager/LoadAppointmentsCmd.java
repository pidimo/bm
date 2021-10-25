package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.catalogmanager.Priority;
import com.piramide.elwis.domain.catalogmanager.PriorityHome;
import com.piramide.elwis.domain.schedulermanager.*;
import com.piramide.elwis.dto.schedulermanager.AppointmentCompountView;
import com.piramide.elwis.dto.schedulermanager.AppointmentView;
import com.piramide.elwis.dto.schedulermanager.RecurrenceManager;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.*;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author alejandro
 * @version $Id: LoadAppointmentsCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class LoadAppointmentsCmd extends EJBCommand {
    private Log log = LogFactory.getLog(LoadAppointmentsCmd.class);
    private DateTime startRangeDate;
    private DateTime endRangeDate;
    private Map appointmentsByRange;
    private DateTimeZone zone;
    private PriorityHome priorityHome;
    private AppointmentTypeHome appointmentTypeHome;
    private Integer userId;
    private Map appointmentList;
    //For yearly view
    private int[][] appointments;
    private boolean isYearly;

    private boolean isYearView;

    public LoadAppointmentsCmd(boolean isYearView) {
        this.isYearView = isYearView;
        if (!isYearView) {
            appointmentsByRange = new HashMap();
            appointmentList = new HashMap();
        }
    }

    public void initialize(boolean isYearly) {
        this.isYearly = isYearly;
        appointments = isYearly ? new int[31][12] : new int[31][1];
    }

    public int[][] getAppointments() {
        return appointments;
    }

    public void setAppointments(int[][] appointments) {
        this.appointments = appointments;
    }


    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing LoadAppointmentsCmd......" + paramDTO);

        startRangeDate = (DateTime) paramDTO.get("startRangeDate");
        endRangeDate = (DateTime) paramDTO.get("endRangeDate");
        userId = (Integer) paramDTO.get("userId");
        zone = (DateTimeZone) paramDTO.get("timeZone");

        Integer appointmentTypeId = (paramDTO.get("appointmentTypeId") != null) ? new Integer(paramDTO.get("appointmentTypeId").toString()) : null;
        String viewAppointment = (String) paramDTO.get("viewAppointment");
        boolean viewAllAppointments = (SchedulerConstants.PROCESS_ALL_APP.equals(viewAppointment));
        log.debug("viewAllAppointments:" + viewAllAppointments);
        Boolean onlyPrivate = Boolean.valueOf(SchedulerConstants.PROCESS_ONLYPRIVATE_APP.equals(viewAppointment));

        AppointmentHome appointmentHome = (AppointmentHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_APPOINTMENT);
        PriorityHome priorityHome = (PriorityHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PRIORITY);
        AppointmentTypeHome appointmentTypeHome = (AppointmentTypeHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_APPOINTMENTTYPE);
        log.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.debug("Load appointmentsWithoutRecurrence in Range:" + startRangeDate + " TO:" + endRangeDate + " FROM:" + userId);
        log.debug("Load appointmentsWithoutRecurrence in Range:" + startRangeDate.getMillis() + " TO:" + endRangeDate.getMillis() + " FROM:" + userId);
        try {
            Collection appointmentsWithoutRecurrence;
            if (viewAllAppointments) {
                appointmentsWithoutRecurrence = (appointmentTypeId != null)
                        ? appointmentHome.findByAppointmentTypeWithoutRecurrence(new Long(startRangeDate.getMillis()), new Long(endRangeDate.getMillis()), userId, appointmentTypeId)
                        : appointmentHome.findAllAppointmentWithoutRecurrence(new Long(startRangeDate.getMillis()), new Long(endRangeDate.getMillis()), userId);
            } else {
                appointmentsWithoutRecurrence = (appointmentTypeId != null)
                        ? appointmentHome.findByAppointmentTypePublicOrPrivateWithoutRecurrence(new Long(startRangeDate.getMillis()), new Long(endRangeDate.getMillis()), userId, onlyPrivate, appointmentTypeId)
                        : appointmentHome.findAllPublicOrPrivateAppointmentWithoutRecurrence(new Long(startRangeDate.getMillis()), new Long(endRangeDate.getMillis()), userId, onlyPrivate);
            }
            log.debug("AppointmentsWithoutRecurrence:" + appointmentsWithoutRecurrence);
            for (Iterator iterator = appointmentsWithoutRecurrence.iterator(); iterator.hasNext();) {
                Appointment appointment = (Appointment) iterator.next();
                log.debug("===Verify if enter in range view:" + appointment + " - Name:" + appointment.getTitle() + "====");
                //log.debug(appointment.getStartDate() + "-" + appointment.getEndDate() + "-" + appointment.getStartTime() + "-" + appointment.getEndTime() + "-" + appointment.getIsPrivate() + "-" + appointment.getReminder());
                AppointmentView appointmentView = createAppointment(userId, appointment, appointmentTypeHome, priorityHome, zone);
                DateTime startDate = new DateTime(appointment.getStartDateTime().longValue(), zone);
                DateTime endDate = new DateTime(appointment.getEndDateTime().longValue(), zone);

                if (!isYearView) {
                    appointmentList.put(appointmentView.getId(), appointmentView);
                    addAppointmentAsResult(startDate, endDate, appointmentView);
                    log.debug("NOT  IS YEARLY!!!!");
                } else {
                    markValidIfCan(startDate, endDate);
                }
            }

        } catch (FinderException e) {
            e.printStackTrace();
        }

        log.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.debug("Load appointments with recurrence in Range, From:" + startRangeDate + " To:" + endRangeDate + " For:" + userId);
        try {//Calcula todos los appointmentes con recurrencia
            Collection appointmentsWithRecurrence;
            if (viewAllAppointments) {
                appointmentsWithRecurrence = (appointmentTypeId != null)
                        ? appointmentHome.findByAppointmentTypeWithRecurrence(DateUtils.dateToInteger(startRangeDate), userId, appointmentTypeId)
                        : appointmentHome.findAllAppointmentWithRecurrence(DateUtils.dateToInteger(startRangeDate), userId);
            } else {
                appointmentsWithRecurrence = (appointmentTypeId != null)
                        ? appointmentHome.findByAppointmentTypePublicOrPrivateWithRecurrence(DateUtils.dateToInteger(startRangeDate), userId, onlyPrivate, appointmentTypeId)
                        : appointmentHome.findAllPublicOrPrivateAppointmentWithRecurrence(DateUtils.dateToInteger(startRangeDate), userId, onlyPrivate);
            }

            long startRangeMillis = startRangeDate.getMillis();
            long endRangeMillis = endRangeDate.getMillis();

            //log.debug("appointmentsWithRecurrence:" + appointmentsWithRecurrence);

            for (Iterator iterator = appointmentsWithRecurrence.iterator(); iterator.hasNext();) {

                Appointment appointment = (Appointment) iterator.next();
                log.debug("===Verify recurrence if enter in range view:" + appointment + " - Name:" + appointment.getTitle() + "====");
                Recurrence recurrence = appointment.getRecurrence();
                Collection exceptionsApp = appointment.getRecurrence().getExceptions();
                List exceptions = new ArrayList(exceptionsApp.size());
                for (Iterator iterator1 = exceptionsApp.iterator(); iterator1.hasNext();) {
                    RecurException exception = (RecurException) iterator1.next();
                    exceptions.add(exception.getDateValue());
                }
                long appStartDateMillis = appointment.getStartDateTime().longValue();
                long appEndDateMillis = appointment.getEndDateTime().longValue();

                DateTime appStartDate = new DateTime(appStartDateMillis, zone);
                DateTime appEndDate = new DateTime(appEndDateMillis, zone);
                //Verify the recurrence appointment it's  bettwen range days to see, if in the range, is add as result
                // The next condition is same that EJB-QL for calculate recurrence appointments
                if ((appStartDateMillis >= startRangeMillis && appEndDateMillis <= endRangeMillis) ||
                        (appStartDateMillis <= startRangeMillis && appEndDateMillis >= endRangeMillis) ||
                        (appStartDateMillis >= startRangeMillis && appStartDateMillis <= endRangeMillis && appEndDateMillis >= endRangeMillis) ||
                        (appStartDateMillis <= startRangeMillis && appEndDateMillis >= startRangeMillis && appEndDateMillis <= endRangeMillis)) {
                    //log.debug("Valid in range:" + appointment);
                    AppointmentView appointmentView = createAppointment(userId, appointment, appointmentTypeHome, priorityHome, zone);

                    if (!isYearView) {
                        appointmentList.put(appointmentView.getId(), appointmentView);
                        addAppointmentAsResult(appStartDate, appEndDate, appointmentView);
                    } else {
                        markValidIfCan(appStartDate, appEndDate);
                    }
                }

                //Verify appointment has recurrence configured
                if (appointment.getIsRecurrence().booleanValue() && appointment.getRecurrence().getRuleType() != null) {
                    Duration duration = new Duration(appStartDate, appEndDate);
                    /**
                     * The duration is decreased in the start range date, this solves the problem
                     * of the appts. with days of duration and recurrence also.
                     */
                    DateTime finalStartRangeDate = startRangeDate.minus(duration);


                    log.debug("startRangeDate:" + startRangeDate + " - finalStartRangeDate" + finalStartRangeDate);
                    RecurrenceManager recurrenceManager = new RecurrenceManager(exceptions, appStartDate,
                            recurrence.getRecurEvery().intValue(), (recurrence.getRangeType() != null ? recurrence.getRangeType().toString() : null),
                            recurrence.getRangeValue(), recurrence.getRuleType() != null ? recurrence.getRuleType().toString() : null, recurrence.getRuleValue(),
                            recurrence.getRuleValueType() != null ? recurrence.getRuleValueType().toString() : null, zone);

                    LinkedList validAppointmentDates = (LinkedList) recurrenceManager.getRecurrencesBetween(finalStartRangeDate, endRangeDate);
                    log.debug("validAppointmentDates:" + validAppointmentDates);
                    if (!validAppointmentDates.isEmpty()) {
                        //Iterar todos los dias validos de la recurrencia de un appointment
                        int i = 1;
                        for (Iterator validDates = validAppointmentDates.iterator(); validDates.hasNext();) {
                            DateTime newStartDate = (DateTime) validDates.next();
                            AppointmentView appointmentView = createAppointment(userId, appointment, appointmentTypeHome, priorityHome, zone);
                            appointmentView.setVirtualId(i++);
                            DateTime newEndDate = newStartDate.plus(duration);
                            appointmentView.setStartDateTime(newStartDate);
                            appointmentView.setEndDateTime(newEndDate);
                            Interval viewRange = new Interval(startRangeDate, endRangeDate);//the view range
                            Interval newApptRange = new Interval(newStartDate, newEndDate); //the appointment interval, in the recurrence
                            /**
                             *  Having a range of view, only the dates of the appointments which intersects
                             * with the range of view are shown, the others are skipped.
                             * This conditional is done because when a duration is decreased to the appointment start
                             * date, sometimes a date which does not belong to the range of view is considered as
                             * valid in the recurrence, but it does not valid for the range of view in this case.
                             */
                            if (!(newApptRange.isBefore(viewRange) || newApptRange.isAfter(viewRange))) {
                                if (!isYearView) {
                                    appointmentList.put(appointmentView.getId(), appointmentView);
                                    addAppointmentAsResult(newStartDate, newEndDate, appointmentView);
                                } else {
                                    markValidIfCan(newStartDate, newEndDate);
                                }
                            }
                        }
                    }
                }
            }
        } catch (FinderException e) {
            e.printStackTrace();
        }
        //resultDTO.put("result", Collections.singletonMap(appointmentsByRange, appointmentList));
    }

    /**
     * Add appointmet as result, if the appointment duration its greater than one day, these appointment will be replicate
     * for all days of appointment duration.
     *
     * @param startDate
     * @param endDate
     * @param appointment
     */
    private void addAppointmentAsResult(DateTime startDate, DateTime endDate, AppointmentView appointment) {
        //log.debug("Compare sd:" + startDate + " - sr:" + startRangeDate + " -- ed:" + endDate + " - er:" + endRangeDate);
        log.debug(" ... addAppointmentAsResult function  execute ..........." + appointmentsByRange);
        if (startDate.isBefore(startRangeDate)) {
            startDate = new DateTime(startRangeDate);
        }
        if (endDate.isAfter(endRangeDate)) {
            endDate = new DateTime(endRangeDate);
        }
        //Verificar si dura mas de un dia el appointments..!!!
        do {
            log.debug("startDate:" + startDate + " -- endDate:" + endDate);
            Integer appDate = DateUtils.dateToInteger(startDate);
            if (!appointmentsByRange.containsKey(appDate)) {
                appointmentsByRange.put(appDate, new ArrayList());
            }

            List appointmentsInDay = (List) appointmentsByRange.get(appDate);
            appointmentsInDay.add(Collections.singletonMap(appointment.getId(), appointment.getStartTime()));
            //appointmentsByRange.put(appDate, appointmentsInDay);
            startDate = startDate.plus(Period.days(1)).toDateMidnight().toDateTime(zone);
            //startDate = startDate.plus(Period.days(1));
        } while (endDate.compareTo(startDate) == 1);

    }


    public Map getResult() {
        return (Map) resultDTO.get("result");
    }

    private AppointmentView createAppointment(Integer userId, Appointment appointment, AppointmentTypeHome appointmentTypeHome, PriorityHome priorityHome, DateTimeZone zone) {
        String priority = null;

        log.debug(" ... createAppointment function execute ...");
        try {
            if (appointment.getPriorityId() != null) {
                Priority prioritybean = priorityHome.findByPrimaryKey(appointment.getPriorityId());
                priority = prioritybean.getPriorityName();
            }
        } catch (FinderException e) {
            log.debug("BD are inestable, because the keys value relation no exist");
        }

        String color = null;
        String name = null;
        try {
            if (appointment.getAppointmentTypeId() != null) {
                AppointmentType appointmentType = appointmentTypeHome.findByPrimaryKey(appointment.getAppointmentTypeId());
                color = appointmentType.getColor();
                name = appointmentType.getName();
            }
        } catch (FinderException e) {
            log.fatal("BD are inestable, because the keys value relation no exist");
        }
        DateTime startDate = new DateTime(appointment.getStartDateTime().longValue(), zone);
        DateTime endDate = new DateTime(appointment.getEndDateTime().longValue(), zone);
        boolean isOwner = appointment.getUserId().equals(userId);
        AppointmentView appointmentView = new AppointmentCompountView(startDate, endDate, isOwner,
                /*Is recurrent*/        appointment.getIsRecurrence().booleanValue() && appointment.getRecurrence().getRuleType() != null,
                !appointment.getIsPrivate().booleanValue(),
                appointment.getIsAllDay().booleanValue(), priority,
                appointment.getReminder() != null);
        appointmentView.initialize(appointment.getAppointmentId(), appointment.getTitle(), color, name);
        appointmentView.setLocation(appointment.getLocation());
        if (appointment.getReminder() != null) {
            appointmentView.setReminderType(appointment.getReminder().getReminderType().intValue());
            appointmentView.setTimeBefore(appointment.getReminder().getTimeBefore().toString());
        }
        return appointmentView;
    }

    private void markValidIfCan(DateTime startDate, DateTime endDate) {

        log.debug(" ... markValidIfCan function execute ...");

        log.debug("---->startDate:" + startDate + " - ---> endDate:" + endDate);
        if (startDate.isBefore(startRangeDate)) {
            startDate = new DateTime(startRangeDate);
        }
        if (endDate.isAfter(endRangeDate)) {
            endDate = new DateTime(endRangeDate);
        }
        do {
            int day = startDate.getDayOfMonth() - 1;
            int month = isYearly ? startDate.getMonthOfYear() - 1 : 0;
            if (appointments[day][month] == 0) {
                appointments[day][month]++;
            }
            startDate = startDate.plus(Period.days(1)).toDateMidnight().toDateTime(zone);
        } while (endDate.compareTo(startDate) == 1);

    }

    public boolean isStateful() {
        return false;
    }

    public Map getAppointmentsByRange() {
        return appointmentsByRange;
    }

    public Map getAppointmentList() {
        return appointmentList;
    }
}

