package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: TaskManagerAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class TaskManagerAction extends AbstractDefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("TaskManagerAction execution  ...");
        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }

        ActionErrors errors = new ActionErrors();
        String taskId = "";

        if (request.getParameter("taskId") != null && !"".equals(request.getParameter("taskId"))) {
            taskId = request.getParameter("taskId");
        } else {
            taskId = request.getParameter("dto(taskId)");
        }

        errors = ForeignkeyValidator.i.validate(SchedulerConstants.TABLE_TASK, "taskId",
                taskId, errors, new ActionError("msg.NotFound",
                        request.getParameter("dto(title)")));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("TaskList");
        }

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        ((DefaultForm) form).setDto("companyId", user.getValue(Constants.COMPANYID).toString());
        ((DefaultForm) form).setDto("taskId", taskId);
        return super.execute(mapping, form, request, response);
    }
}
