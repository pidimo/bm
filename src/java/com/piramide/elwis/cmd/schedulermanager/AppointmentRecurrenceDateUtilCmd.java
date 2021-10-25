package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.domain.schedulermanager.RecurException;
import com.piramide.elwis.dto.schedulermanager.RecurDateTime;
import com.piramide.elwis.dto.schedulermanager.RecurrenceManager;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: yumi
 * Date: 31-ago-2007
 * Time: 16:05:19
 */

public class AppointmentRecurrenceDateUtilCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        log.debug(" ... AppointmentRecurrenceDateUtilCmd execute ...");
        Appointment appointment = (Appointment) paramDTO.get("appointment");
        List exceptions = new ArrayList();
        String ruleValueType = null;

        if (appointment.getRecurrence().getRuleValueType() != null) {
            ruleValueType = appointment.getRecurrence().getRuleValueType().toString();
        }
        for (Iterator iterator = appointment.getRecurrence().getExceptions().iterator(); iterator.hasNext();) {
            RecurException recurException = (RecurException) iterator.next();
            exceptions.add(recurException.getDateValue());
        }
        appointment.setStartDateTime(new Long(getCompleteDateTime(appointment.getStartDate(),
                appointment.getStartTime(), (DateTimeZone) paramDTO.get("userTimeZone")).getMillis()));
        appointment.setEndDateTime(new Long(getCompleteDateTime(appointment.getEndDate(),
                appointment.getEndTime(), (DateTimeZone) paramDTO.get("userTimeZone")).getMillis()));

        DateTime aDate = new DateTime(appointment.getStartDateTime(), (DateTimeZone) paramDTO.get("userTimeZone"));
        DateTime aDateMinus = aDate;
        if (new Integer(1).equals(appointment.getRecurrence().getRuleValueType())
                && ("1".equals(appointment.getRecurrence().getRuleValue()) && new Integer(1).equals(aDate.getDayOfMonth()))
                && (new Integer(3)).equals(appointment.getRecurrence().getRuleType())) {
            aDateMinus = aDateMinus.minusMonths(1);
        } else {
            aDateMinus = aDateMinus.minusDays(1);
        }

        RecurrenceManager recurrenceManager = new RecurrenceManager(
                exceptions,
                aDateMinus,
                appointment.getRecurrence().getRecurEvery().intValue(),
                appointment.getRecurrence().getRangeType().toString(),
                appointment.getRecurrence().getRangeValue(),
                appointment.getRecurrence().getRuleType().toString(),
                appointment.getRecurrence().getRuleValue(),
                ruleValueType,
                (DateTimeZone) paramDTO.get("userTimeZone"));

        RecurDateTime current = recurrenceManager.getNextDateTimeFor(aDateMinus);
        appointment.setStartDate(DateUtils.dateToInteger(current.getDateTime()));
        Duration duration = new Duration(appointment.getStartDateTime(), appointment.getEndDateTime());
        appointment.setStartDateTime(new Long(getCompleteDateTime(appointment.getStartDate(),
                appointment.getStartTime(), (DateTimeZone) paramDTO.get("userTimeZone")).getMillis()));
        DateTime newStartDate = new DateTime(appointment.getStartDateTime(), (DateTimeZone) paramDTO.get("userTimeZone"));
        DateTime newEndDate = newStartDate.plus(duration);
        appointment.setEndDate(DateUtils.dateToInteger(newEndDate));
        appointment.getRecurrence().getRuleType();
        if (!aDate.equals(newStartDate)) {
            resultDTO.put("showMessage", SchedulerConstants.TRUE_VALUE);
        }
        log.debug(" ... date    ..." + aDate);
        log.debug(" ... newDate ..." + newStartDate);
    }

    public boolean isStateful() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private DateTime getCompleteDateTime(Integer date, String time, DateTimeZone zone) {
        String[] hourMinute = time.split(":");
        int yearMonthDay[] = DateUtils.getYearMonthDay(date);
        return new DateTime(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2],
                Integer.parseInt(hourMinute[0]), Integer.parseInt(hourMinute[1]), 0, 0, zone);
    }
}
