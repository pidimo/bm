package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.schedulermanager.Task;
import com.piramide.elwis.dto.schedulermanager.TaskDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun Team
 * Reads the task description, if not exists then return an empty string
 *
 * @author alvaro
 * @version $Id: TaskDescriptionReadCmd 20-jun-2008 13:03:15
 */
public class TaskDescriptionReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing read task description command" + paramDTO);
        String description = "";
        Task task = (Task) ExtendedCRUDDirector.i.read(new TaskDTO(paramDTO), resultDTO, false);
        if (task != null && task.getSchedulerFreeText() != null) {
            description = new String(task.getSchedulerFreeText().getValue());
        }
        resultDTO.put("taskDescription", description);
    }

    public boolean isStateful() {
        return false;
    }
}
