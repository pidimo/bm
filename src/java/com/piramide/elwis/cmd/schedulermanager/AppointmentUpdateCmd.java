package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.domain.schedulermanager.Recurrence;
import com.piramide.elwis.domain.schedulermanager.SchedulerFreeText;
import com.piramide.elwis.domain.schedulermanager.SchedulerFreeTextHome;
import com.piramide.elwis.dto.schedulermanager.AppointmentDTO;
import com.piramide.elwis.dto.schedulermanager.RecurrenceDTO;
import com.piramide.elwis.dto.schedulermanager.ReminderDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;

/**
 * User: yumi
 * Date: Apr 12, 2005
 * Time: 11:29:48 AM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentUpdateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) throws AppLevelException {

        log.debug("Executing update appointment command");
        log.debug("Operation = " + getOp());
        Appointment appointment = null;
        SchedulerFreeText freeText = null;
        StringBuffer startTime = new StringBuffer();
        StringBuffer expireTime = new StringBuffer();
        AppointmentDTO dto = new AppointmentDTO(paramDTO);

        if ("on".equals(paramDTO.getAsString("isAllDay"))) {
            startTime.append("00:00");
            expireTime.append("23:59");
        } else {
            startTime.append(paramDTO.getAsString("startHour")).append(":").append(paramDTO.getAsString("startMin"));
            expireTime.append(paramDTO.getAsString("endHour")).append(":").append(paramDTO.getAsString("endMin"));
        }

        dto.put("startTime", startTime.toString());
        dto.put("endTime", expireTime.toString());
        dto.put("isPrivate", new Boolean("on".equals(paramDTO.getAsString("isPrivate"))));
        dto.put("isRecurrence", new Boolean("on".equals(paramDTO.getAsString("isRecurrence"))));
        dto.put("isAllDay", new Boolean("on".equals(paramDTO.getAsString("isAllDay"))));

        appointment = (Appointment) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");

        if (appointment != null && !resultDTO.isFailure()) {
            try {
                SchedulerFreeTextHome frHome = (SchedulerFreeTextHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULER_FREETEXT);
                if (appointment.getFreeTextId() != null) {
                    freeText = appointment.getSchedulerFreeText();
                    freeText.setValue(paramDTO.get("descriptionText").toString().getBytes());
                } else if (paramDTO.get("descriptionText") != null && !SchedulerConstants.EMPTY_VALUE.equals(paramDTO.get("descriptionText"))) {
                    freeText = frHome.create(paramDTO.get("descriptionText").toString().getBytes(),
                            new Integer(paramDTO.getAsInt("companyId")),
                            new Integer(FreeTextTypes.FREETEXT_SCHEDULER));
                    freeText.setValue(paramDTO.get("descriptionText").toString().getBytes());
                    appointment.setFreeTextId(freeText.getFreeTextId());
                }
                //crea reminder si antes no habia
                ReminderDTO reminderDTO = new ReminderDTO(paramDTO);
                if (paramDTO.getAsBool("reminder") && appointment.getReminder() == null) {
                    reminderDTO.put("appointmentId", appointment.getAppointmentId());
                    if (!paramDTO.get("reminderType").equals("1")) {
                        reminderDTO.put("timeBefore", paramDTO.get("timeBefore_2"));
                    } else {
                        reminderDTO.put("timeBefore", paramDTO.get("timeBefore_1"));
                    }
                    ExtendedCRUDDirector.i.create(reminderDTO, resultDTO, false);
                } else if (!paramDTO.getAsBool("reminder") && appointment.getReminder() != null) {
                    appointment.getReminder().remove();
                } else if (appointment.getReminder() != null && paramDTO.getAsBool("reminder")) {
                    appointment.getReminder().setReminderType(new Integer(paramDTO.get("reminderType").toString()));
                    if (!paramDTO.get("reminderType").equals("1")) {
                        appointment.getReminder().setTimeBefore(new Integer(paramDTO.get("timeBefore_2").toString()));
                    } else {
                        appointment.getReminder().setTimeBefore(new Integer(paramDTO.get("timeBefore_1").toString()));
                    }
                }

                if ("on".equals(paramDTO.getAsString("isRecurrence")) && appointment.getRecurrence() == null) {
                    RecurrenceDTO recurrenceDTO = new RecurrenceDTO();
                    recurrenceDTO.put("appointmentId", appointment.getAppointmentId());
                    recurrenceDTO.put("companyId", appointment.getCompanyId());
                    //si no existe lo crea
                    ExtendedCRUDDirector.i.create(recurrenceDTO, resultDTO, false);
                    RecurrenceUpdateCmd recurrenceCmd = new RecurrenceUpdateCmd();
                    recurrenceCmd.putParam("appointmentId", appointment.getAppointmentId());
                    recurrenceCmd.putParam(paramDTO);
                    recurrenceCmd.executeInStateless(ctx);
                    resultDTO.putAll(recurrenceCmd.getResultDTO());
                } else if (!"on".equals(paramDTO.getAsString("isRecurrence")) && appointment.getRecurrence() != null) {
                    Recurrence r = appointment.getRecurrence();
                    appointment.setIsRecurrence(new Boolean(false));
                    r.remove();
                } else if ("on".equals(paramDTO.getAsString("isRecurrence")) && appointment.getRecurrence() != null) {
                    RecurrenceUpdateCmd recurrenceCmd = new RecurrenceUpdateCmd();
                    recurrenceCmd.putParam("appointmentId", appointment.getAppointmentId());
                    recurrenceCmd.putParam(paramDTO);
                    recurrenceCmd.executeInStateless(ctx);
                    resultDTO.putAll(recurrenceCmd.getResultDTO());

                    if (!new Integer(4).equals(appointment.getRecurrence().getRuleType()) &&
                            !new Integer(1).equals(appointment.getRecurrence().getRuleType())) {
                        // recalculate startDate and endDate
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
                    resultDTO.setForward("fail");
                }

            } catch (RemoveException e) {
                ctx.setRollbackOnly();//invalid the transaction.
                dto.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
            } catch (CreateException e) {
                ctx.setRollbackOnly();//invalid the transaction.
                log.error("Error creating ", e);
                resultDTO.setForward("Fail");
            }
        } else {
            log.debug("It seems was updated by other user");
            resultDTO.setResultAsFailure();
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }
}