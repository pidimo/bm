package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.dto.schedulermanager.TaskDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: TaskListAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class TaskListAction extends com.piramide.elwis.web.common.action.ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Task list action execution...*******");
        if ("Cancel".equals(request.getParameter("dto(cancel)"))) {
            log.debug("cancel  ....  TaskListAction");
            return mapping.findForward("Cancel");
        }
        //cheking if working address was not deleted by other user.
        ActionErrors errors = new ActionErrors();
        SearchForm listForm = (SearchForm) form;
        String taskId = null;

        if (request.getParameter("taskId") != null && !"".equals(request.getParameter("taskId"))) {
            taskId = request.getParameter("taskId");
        } else {
            taskId = request.getParameter("dto(taskId)");
        }
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setPrimKey(taskId);
        try {
            EJBFactory.i.findEJB(taskDTO); //task already exists
        } catch (EJBFactoryException e) {
            if (e.getCause() instanceof FinderException) {
                log.debug("The task was deleted by other user... show errors and " +
                        "return to taskList search page");
                errors = ForeignkeyValidator.i.validate(SchedulerConstants.TABLE_TASK, "taskId",
                        taskId, errors, new ActionError("msg.NotFound",
                                request.getParameter("dto(title)")));
                if (!errors.isEmpty()) {
                    saveErrors(request, errors);
                    return mapping.findForward("TaskList");
                }
            }
        }
        listForm.getParams().put("taskId", taskId);
        addFilter("taskId", taskId);
        return super.execute(mapping, listForm, request, response);
    }
}
