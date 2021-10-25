package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.schedulermanager.*;
import com.piramide.elwis.dto.schedulermanager.AppointmentDTO;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 12, 2005
 * Time: 11:29:15 AM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing delete AppointmentDeleteCMD");

        AppointmentDTO dto = new AppointmentDTO(paramDTO);
        Appointment appointment = null;
        IntegrityReferentialChecker.i.check(dto, resultDTO);
        if (resultDTO.isFailure()) { //is referenced
            resultDTO.setResultAsFailure();
            return;
        }

        appointment = (Appointment) EJBFactory.i.findEJB(dto);
        try {
            if (appointment.getRecurrence() != null) {
                log.debug("remove recurrence ...");
                Recurrence recurrence = appointment.getRecurrence();
                appointment.setRecurrence(null);
                recurrence.remove();
            }

            if (appointment.getReminder() != null) {
                log.debug("remove reminder ...");
                Reminder reminder = appointment.getReminder();
                appointment.setReminder(null);
                reminder.remove();
            }

            ScheduledUserHome home = (ScheduledUserHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULEDUSER);
            Collection users = new ArrayList(0);
            users = home.findByAppointmentId(appointment.getAppointmentId());

            if (users.size() > 0) {
                log.debug("remove ScheduledUser ...");
                for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                    ScheduledUser scheduledUser = (ScheduledUser) iterator.next();
                    scheduledUser.remove();
                }
            }

            if (appointment.getSchedulerFreeText() != null) {
                log.debug("remove schedulerFreeText ...");
                SchedulerFreeText freeText = appointment.getSchedulerFreeText();
                appointment.setSchedulerFreeText(null);
                freeText.remove();
            }
            log.debug("remove appointment ...");
            appointment.remove();
        } catch (FinderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (RemoveException e) {
            ctx.setRollbackOnly();//invalid the transaction
            dto.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
        }
    }

    public boolean isStateful() {
        return false;
    }
}
