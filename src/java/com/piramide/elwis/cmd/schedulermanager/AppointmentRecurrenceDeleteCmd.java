package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.domain.schedulermanager.RecurException;
import com.piramide.elwis.dto.schedulermanager.AppointmentDTO;
import com.piramide.elwis.dto.schedulermanager.RecurExceptionDTO;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 7, 2005
 * Time: 5:49:07 PM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentRecurrenceDeleteCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("For delete current serie appointment... ");
        Appointment appointment = null;
        RecurException exception = null;
        RecurExceptionDTO dto = new RecurExceptionDTO();
        appointment = (Appointment) ExtendedCRUDDirector.i.read(new AppointmentDTO(paramDTO), resultDTO, false);

        if (appointment != null && !resultDTO.isFailure()) {
            dto.put("appointmentId", appointment.getAppointmentId());
            dto.put("dateValue", paramDTO.get("currentDate"));
            dto.put("companyId", appointment.getCompanyId());
            exception = (RecurException) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
            appointment.getRecurrence().getExceptions().add(exception);

            //update reminder if is required
            updateReminderNextTimeForRecurrentAppointment(appointment, ctx);
        } else {
            resultDTO.setForward("Fail");
            resultDTO.setResultAsFailure();
        }
    }

    private void updateReminderNextTimeForRecurrentAppointment(Appointment appointment, SessionContext ctx) {
        AppointmentCalculateReminderCmd calculateReminderCmd = new AppointmentCalculateReminderCmd();
        calculateReminderCmd.putParam("appointment", appointment);

        try {
            calculateReminderCmd.executeInStateless(ctx);
        } catch (AppLevelException e) {
            log.debug("Error in calculate reminder when delete app recurrent... ", e);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
