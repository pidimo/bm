package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 22, 2005
 * Time: 3:40:33 PM
 * To change this template use File | Settings | File Templates.
 */

public class NotificationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing Notification CMD ...command...");

        if (SchedulerConstants.TRUE_VALUE.equals(paramDTO.get("view"))) {
            ExtendedCRUDDirector.i.read(new UserDTO(paramDTO), resultDTO, false);
        } else {
            String appMail = null;
            String taskMail = null;
            String caseMail = null;
            String questionMail = null;
            UserDTO userDTO = new UserDTO(paramDTO);

            if (paramDTO.get("notificationAppointmentEmail") != null) {
                appMail = paramDTO.get("notificationAppointmentEmail").toString().trim();
            }
            if (paramDTO.get("notificationSchedulerTaskEmail") != null) {
                taskMail = paramDTO.get("notificationSchedulerTaskEmail").toString().trim();
            }
            if (paramDTO.get("notificationSupportCaseEmail") != null) {
                caseMail = paramDTO.get("notificationSupportCaseEmail").toString().trim();
            }
            if (paramDTO.get("notificationQuestionEmail") != null) {
                questionMail = paramDTO.get("notificationQuestionEmail").toString().trim();
            }

            if (appMail != null) {
                userDTO.put("notificationAppointmentEmail", appMail);
            }
            if (taskMail != null) {
                userDTO.put("notificationSchedulerTaskEmail", taskMail);
            }
            if (caseMail != null) {
                userDTO.put("notificationSupportCaseEmail", caseMail);
            }
            if (questionMail != null) {
                userDTO.put("notificationQuestionEmail", questionMail);
            }

            ExtendedCRUDDirector.i.update(userDTO, resultDTO, false, true, false, "Fail");

            if (!resultDTO.isFailure() && !"Fail".equals(resultDTO.getForward())) {
                resultDTO.addResultMessage("Common.changesOK");
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}

