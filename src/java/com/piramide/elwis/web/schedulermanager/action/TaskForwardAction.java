package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.schedulermanager.TaskReadCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.schedulermanager.form.TaskForm;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mauren Carrasco
 */

public class TaskForwardAction extends com.piramide.elwis.web.schedulermanager.action.TaskManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        TaskForm taskForm = (TaskForm) form;
        ActionForward forward = action.findForward("Fail");
        if (isCancelled(request)) {
            log.debug("Is Cancel");
            return (action.findForward("Cancel"));
        }

        TaskReadCmd cmd = new TaskReadCmd();
        ActionErrors aErrors = new ActionErrors();
        cmd.putParam("taskId", request.getParameter("taskId"));
        cmd.putParam(taskForm.getDtoMap());
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        cmd.putParam("userId", user.getValue("userId"));

        BusinessDelegate.i.execute(cmd, request);
        aErrors = MessagesUtil.i.convertToActionErrors(action, request, cmd.getResultDTO());
        saveErrors(request, aErrors);

        if (!cmd.getResultDTO().isFailure()) {
            taskForm.getDtoMap().putAll(cmd.getResultDTO());
            if (cmd.getResultDTO().get("expireDate") == null) {
                if (!"delete".equals(request.getParameter("operation")) && !SchedulerConstants.TRUE_VALUE.equals(request.getParameter("isParticipant"))) {
                    request.setAttribute("jsLoad", "onLoad=\"deshabilita()\"");
                }
                taskForm.setDto("date", new Boolean(false));
            }
            if (cmd.getResultDTO().get("addressId") != null && cmd.getResultDTO().get("processId") != null) {
                taskForm.setDto("clear", SchedulerConstants.TRUE_VALUE);
            }
            taskForm.setDto("itParticipates", cmd.getResultDTO().get("itParticipates"));
            forward = action.findForward("Success");
        } else {
            forward = action.findForward("Fail");
        }
        return forward;
    }
}