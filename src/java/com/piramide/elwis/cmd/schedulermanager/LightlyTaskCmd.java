package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.schedulermanager.Task;
import com.piramide.elwis.domain.schedulermanager.TaskHome;
import com.piramide.elwis.dto.schedulermanager.TaskDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: LightlyTaskCmd.java 10014 2010-12-14 19:51:57Z ivan $
 */
public class LightlyTaskCmd extends EJBCommand {

    public void executeInStateless(SessionContext ctx) {
        boolean useDefaultMethod = true;
        if ("getTask".equals(getOp())) {
            useDefaultMethod = false;
            Integer taskId = EJBCommandUtil.i.getValueAsInteger(this, "taskId");
            getTask(taskId);
        }

        if (useDefaultMethod) {
            defaultReadMethod();
        }
    }

    private void defaultReadMethod() {
        TaskDTO taskDTO = new TaskDTO(paramDTO);
        Task task;

        try {
            task = (Task) EJBFactory.i.findEJB(taskDTO);
            resultDTO.put("title", task.getTitle());
            resultDTO.put("userId", task.getUserId());
        } catch (EJBFactoryException ex) {
            taskDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setResultAsFailure();
        }
    }

    private void getTask(Integer taskId) {
        TaskHome taskHome =
                (TaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_TASK);
        TaskDTO taskDTO = null;
        try {
            Task task = taskHome.findByPrimaryKey(taskId);
            taskDTO = new TaskDTO();

            DTOFactory.i.copyToDTO(task, taskDTO);
        } catch (FinderException e) {
            //
        }

        resultDTO.put("getTask", taskDTO);
    }

    public boolean isStateful() {
        return false;
    }
}

