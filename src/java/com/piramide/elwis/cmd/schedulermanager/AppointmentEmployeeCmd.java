package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.schedulermanager.SchedulerFreeText;
import com.piramide.elwis.domain.schedulermanager.SchedulerFreeTextHome;
import com.piramide.elwis.domain.schedulermanager.Task;
import com.piramide.elwis.dto.schedulermanager.TaskDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 14, 2005
 * Time: 6:54:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentEmployeeCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing create action Task  command");
        log.debug("Operation = " + getOp());

        Task task = null;
        StringBuffer startTime = new StringBuffer();
        StringBuffer expireTime = new StringBuffer();
        startTime.append(paramDTO.getAsString("startHour")).append(":").append(paramDTO.getAsString("startMin"));


        if (paramDTO.getAsBool("date")) {
            expireTime.append(paramDTO.getAsString("expireHour")).append(":").append(paramDTO.getAsString("expireMin"));
            paramDTO.put("expireTime", expireTime.toString());
        } else {
            paramDTO.put("expireTime", null);
            paramDTO.put("expireDate", null);
        }

        paramDTO.put("startTime", startTime.toString());
        TaskDTO taskDTO = new TaskDTO(paramDTO);

        task = (Task) ExtendedCRUDDirector.i.create(taskDTO, resultDTO, false);

        if (task != null) {
            SchedulerFreeTextHome freeTextHome = (SchedulerFreeTextHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULER_FREETEXT);
            try {
                SchedulerFreeText freeText = freeTextHome.create(paramDTO.getAsString("descriptionText").getBytes(), task.getCompanyId(),
                        new Integer(FreeTextTypes.FREETEXT_SCHEDULER));
                task.setFreeTextId(freeText.getFreeTextId());
            } catch (CreateException e) {
                e.printStackTrace();
            }
        } else {
            resultDTO.setForward("Fail");
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }
}

