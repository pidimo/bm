package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.domain.schedulermanager.ScheduledUser;
import com.piramide.elwis.domain.schedulermanager.ScheduledUserHome;
import com.piramide.elwis.dto.schedulermanager.AppointmentDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 12, 2005
 * Time: 11:29:29 AM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing read appointment command" + paramDTO.get("appointmentId"));
        log.debug("Operation = " + getOp());
        Appointment appointment = (Appointment) ExtendedCRUDDirector.i.read(new AppointmentDTO(paramDTO), resultDTO, false);
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        ScheduledUserHome scheduledUserHome = (ScheduledUserHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULEDUSER);
        User u = null;

        if (paramDTO.getAsBool("withReferences")) {
            IntegrityReferentialChecker.i.check(new AppointmentDTO(paramDTO), resultDTO);
            if (resultDTO.isFailure()) {
                resultDTO.put("referenced", Boolean.valueOf(true));
            }
        }
        if (appointment != null && !resultDTO.isFailure()) {
            try { //viewerUserId... antes ahora se ve de acuerdo al timeZone del user de la session
                u = userHome.findByPrimaryKey(new Integer(paramDTO.get("viewerUserId").toString()));
            } catch (FinderException e) {
                log.debug("userViewer notFound ...");
            }

            String timeZone = u.getTimeZone();
            DateTimeZone zone = DateTimeZone.forID(timeZone);

            if (timeZone == null) {
                zone = DateTimeZone.getDefault();
            }
            Long startDate = appointment.getStartDateTime();
            Long endDate = appointment.getEndDateTime();
            DateTime startDateTime = new DateTime(startDate, zone);
            DateTime endDateTime = new DateTime(endDate, zone);

            resultDTO.put("isRecurrence", appointment.getIsRecurrence());
            resultDTO.put("SchedulerUsersNumber", new Integer(appointment.getScheludedUsers().size()));
            resultDTO.put("startDate", (DateUtils.dateToInteger(startDateTime)));
            resultDTO.put("startHour", new Integer(startDateTime.getHourOfDay()));
            resultDTO.put("startMin", new Integer(startDateTime.getMinuteOfHour()));
            resultDTO.put("endDate", (DateUtils.dateToInteger(endDateTime)));
            resultDTO.put("endHour", new Integer(endDateTime.getHourOfDay()));
            resultDTO.put("endMin", new Integer(endDateTime.getMinuteOfHour()));

            if (((Integer) resultDTO.get("startMin")).intValue() < 10) {
                resultDTO.put("startMin", "0" + resultDTO.get("startMin").toString());
            }
            if (((Integer) resultDTO.get("endMin")).intValue() < 10) {
                resultDTO.put("endMin", "0" + resultDTO.get("endMin").toString());
            }
            if (appointment.getReminder() != null) {
                resultDTO.put("reminder", new Boolean(true));
                resultDTO.put("reminderType", appointment.getReminder().getReminderType());
                if (!appointment.getReminder().getReminderType().equals(new Integer(1))) {
                    resultDTO.put("timeBefore_2", appointment.getReminder().getTimeBefore());
                } else {
                    if (appointment.getReminder().getTimeBefore().intValue() == 5) {
                        resultDTO.put("timeBefore_1", "0" + appointment.getReminder().getTimeBefore().toString());
                    } else {
                        resultDTO.put("timeBefore_1", appointment.getReminder().getTimeBefore());
                    }
                }
                /*resultDTO.put("telecomTypeId", appointment.getReminder().getTelecomTypeId());*/
            } else {
                resultDTO.put("reminder", new Boolean(false));
            }
            if (appointment.getSchedulerFreeText() != null) {
                resultDTO.put("descriptionText", new String(appointment.getSchedulerFreeText().getValue()));
                resultDTO.put("title", appointment.getTitle());
            }
            ScheduledUser scheduledUser = null;
            try {
                scheduledUser = scheduledUserHome.findByUserIdAndAppId(new Integer(paramDTO.get("userSessionId").toString()), appointment.getAppointmentId());
            } catch (FinderException e) {
            }

            if (scheduledUser != null) {
                resultDTO.put("participa", new Boolean(true));
            } else {
                resultDTO.put("participa", new Boolean(false));
            }

            if (appointment.getAddressId() != null) {
                AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
                try {
                    Address address = addressHome.findByPrimaryKey(appointment.getAddressId());
                    StringBuffer addressName = new StringBuffer();
                    addressName.append(address.getName1());

                    if (address.getName2() != null && !"".equals(address.getName2())) {
                        if ("1".equals(address.getAddressType())) {
                            addressName.append(", ");
                        } else {
                            addressName.append(" ");
                        }
                        addressName.append(address.getName2());
                    }
                    resultDTO.put("contact", addressName);
                } catch (FinderException e) {
                    log.debug("userName notFound ...");
                }
            }
            //para llenar los datos de recurrencia
            if (appointment.getIsRecurrence().booleanValue()) {
                RecurrenceReadCmd recurrenceCmd = new RecurrenceReadCmd();
                recurrenceCmd.putParam("appointmentId", appointment.getAppointmentId());
                recurrenceCmd.executeInStateless(ctx);

                resultDTO.putAll(recurrenceCmd.getResultDTO());
                if (recurrenceCmd.getResultDTO().get("ruleType").equals(new Integer(1))) {
                    resultDTO.put("recurEveryDay", recurrenceCmd.getResultDTO().get("recurEvery"));
                } else if (recurrenceCmd.getResultDTO().get("ruleType").equals(new Integer(2))) {
                    resultDTO.put("recurEveryWeek", recurrenceCmd.getResultDTO().get("recurEvery"));
                } else if (recurrenceCmd.getResultDTO().get("ruleType").equals(new Integer(3))) {
                    resultDTO.put("recurEveryMonth", recurrenceCmd.getResultDTO().get("recurEvery"));
                } else if (recurrenceCmd.getResultDTO().get("ruleType").equals(new Integer(4))) {
                    resultDTO.put("recurEveryYear", recurrenceCmd.getResultDTO().get("recurEvery"));
                }
            }
            resultDTO.put("appointmentId", appointment.getAppointmentId());
        }
    }

    public boolean isStateful() {
        return false;
    }
}


