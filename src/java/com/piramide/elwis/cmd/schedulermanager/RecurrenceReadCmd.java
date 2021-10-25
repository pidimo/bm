package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.domain.schedulermanager.AppointmentHome;
import com.piramide.elwis.domain.schedulermanager.RecurExceptionHome;
import com.piramide.elwis.domain.schedulermanager.Recurrence;
import com.piramide.elwis.dto.schedulermanager.RecurrenceDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Period;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 26, 2005
 * Time: 10:08:47 AM
 * To change this template use File | Settings | File Templates.
 */

public class RecurrenceReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read recurrence ........ command.." + paramDTO.get("appointmentId"));
        log.debug("Operation = " + getOp());

        AppointmentHome appointmentHome = (AppointmentHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_APPOINTMENT);
        Appointment appointment = null;

        try {
            appointment = appointmentHome.findByPrimaryKey(new Integer(paramDTO.get("appointmentId").toString()));
        } catch (FinderException e) {
        }

        if (appointment != null) {
            DateTime t1 = new DateTime(appointment.getStartDateTime());
            DateTime t2 = new DateTime(appointment.getEndDateTime());
            Period period = new Period(t1, t2);

            resultDTO.put("dd", new Integer(period.getDays()));
            resultDTO.put("hh", new Integer(period.getHours()));
            resultDTO.put("mm", new Integer(period.getMinutes()));

            RecurrenceDTO recurrenceDTO = new RecurrenceDTO();
            Collection exceptions = null;
            RecurExceptionHome home = (RecurExceptionHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_RECUREXCEPTION);
            recurrenceDTO.put("appointmentId", appointment.getAppointmentId());
            recurrenceDTO.put("companyId", appointment.getCompanyId());
            Recurrence recurrence = (Recurrence) ExtendedCRUDDirector.i.read(recurrenceDTO, resultDTO, false);

            /*resultDTO.put("startDate", appointment.getStartDate());
            resultDTO.put("endDate", appointment.getEndDate());*/
            resultDTO.put("startTime", appointment.getStartTime());
            resultDTO.put("endTime", appointment.getEndTime());
            try {
                exceptions = home.findByAppointmentId(recurrence.getAppointmentId());
                if (exceptions != null) {
                    resultDTO.put("exceptions", exceptions);
                } else {
                    resultDTO.put("exceptions", new ArrayList(0));
                }
            } catch (FinderException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }

}


