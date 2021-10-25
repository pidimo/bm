package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.domain.schedulermanager.SchedulerFreeText;
import com.piramide.elwis.domain.schedulermanager.SchedulerFreeTextHome;
import com.piramide.elwis.dto.schedulermanager.AppointmentDTO;
import com.piramide.elwis.dto.schedulermanager.RecurrenceDTO;
import com.piramide.elwis.dto.schedulermanager.ReminderDTO;
import com.piramide.elwis.dto.schedulermanager.ScheduledUserDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

/**
 * User: yumi
 * Date: Apr 12, 2005
 * Time: 11:28:54 AM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) throws AppLevelException {

        log.debug(" ...  Executing create  Appointment command  ... " + paramDTO);
        log.debug("Operation = " + getOp());
        Appointment appointment = null;
        StringBuffer startTime = new StringBuffer();
        StringBuffer expireTime = new StringBuffer();

        if ("on".equals(paramDTO.getAsString("isAllDay"))) {
            startTime.append("00:00");
            expireTime.append("23:59");
        } else {
            startTime.append(paramDTO.getAsString("startHour")).append(":").append(paramDTO.getAsString("startMin"));
            expireTime.append(paramDTO.getAsString("endHour")).append(":").append(paramDTO.getAsString("endMin"));
        }

        AppointmentDTO appointmentDTO = new AppointmentDTO(paramDTO);
        appointmentDTO.put("endTime", expireTime.toString());
        appointmentDTO.put("startTime", startTime.toString());
        appointmentDTO.put("isPrivate", new Boolean("on".equals(paramDTO.getAsString("isPrivate"))));
        appointmentDTO.put("isRecurrence", new Boolean("on".equals(paramDTO.getAsString("isRecurrence"))));
        appointmentDTO.put("isAllDay", new Boolean("on".equals(paramDTO.getAsString("isAllDay"))));


        appointment = (Appointment) ExtendedCRUDDirector.i.create(appointmentDTO, resultDTO, false);

        if (appointment != null && !resultDTO.isFailure()) {

            if (paramDTO.get("descriptionText") != null && !SchedulerConstants.EMPTY_VALUE.equals(paramDTO.get("descriptionText"))) {
                SchedulerFreeTextHome freeTextHome = (SchedulerFreeTextHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULER_FREETEXT);
                try {
                    SchedulerFreeText freeText = freeTextHome.create(paramDTO.getAsString("descriptionText").getBytes(), appointment.getCompanyId(),
                            new Integer(FreeTextTypes.FREETEXT_SCHEDULER));
                    appointment.setFreeTextId(freeText.getFreeTextId());
                } catch (CreateException e) {
                    e.printStackTrace();
                }
            } else {
                appointment.setFreeTextId(null);
            }

            ScheduledUserDTO dto = new ScheduledUserDTO();
            dto.put("appointmentId", appointment.getAppointmentId());
            dto.put("userId", paramDTO.get("userId"));
            dto.put("userGroupId", null);
            dto.put("companyId", appointment.getCompanyId());
            ExtendedCRUDDirector.i.create(dto, resultDTO, false);

            if (paramDTO.getAsBool("reminder")) {
                ReminderDTO reminderDTO = new ReminderDTO(paramDTO);
                reminderDTO.put("appointmentId", appointment.getAppointmentId());
                if (!paramDTO.get("reminderType").equals("1")) {
                    reminderDTO.put("timeBefore", paramDTO.get("timeBefore_2"));
                } else {
                    reminderDTO.put("timeBefore", paramDTO.get("timeBefore_1"));
                }
                ExtendedCRUDDirector.i.create(reminderDTO, resultDTO, false);
            }

            if (appointment.getIsRecurrence().booleanValue()) {
                RecurrenceDTO recurrenceDTO = new RecurrenceDTO();
                recurrenceDTO.put("appointmentId", appointment.getAppointmentId());
                recurrenceDTO.put("companyId", appointment.getCompanyId());

                ExtendedCRUDDirector.i.create(recurrenceDTO, resultDTO, false);
                RecurrenceUpdateCmd recurrenceCmd = new RecurrenceUpdateCmd();
                recurrenceCmd.putParam("appointmentId", appointment.getAppointmentId());
                recurrenceCmd.putParam(paramDTO);
                recurrenceCmd.executeInStateless(ctx);

                // recalculate startDate and endDate
                if (!new Integer(4).equals(appointment.getRecurrence().getRuleType()) &&
                        !new Integer(1).equals(appointment.getRecurrence().getRuleType())) {
                    AppointmentRecurrenceDateUtilCmd utilCmd = new AppointmentRecurrenceDateUtilCmd();
                    utilCmd.putParam("appointment", appointment);
                    utilCmd.putParam("userTimeZone", paramDTO.get("userTimeZone"));
                    utilCmd.executeInStateless(ctx);
                    if (utilCmd.getResultDTO().isFailure()) {
                        resultDTO.setForward("Fail");
                    }
                    if (SchedulerConstants.TRUE_VALUE.equals(utilCmd.getResultDTO().get("showMessage"))) {
                        resultDTO.put("sDateChangeMessage", SchedulerConstants.TRUE_VALUE);
                    }
                }
            }

            AppointmentCalculateReminderCmd calculateReminderCmd = new AppointmentCalculateReminderCmd();
            calculateReminderCmd.putParam("appointment", appointment);
            calculateReminderCmd.executeInStateless(ctx);
            ResultDTO reminderCalculateResultDTO = calculateReminderCmd.getResultDTO();
            if (reminderCalculateResultDTO.isFailure()) {
                resultDTO.setForward("Fail");
            }
            resultDTO.put("appointmentId", appointment.getAppointmentId());
        } else {
            resultDTO.setResultAsFailure();
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }
}