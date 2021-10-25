package com.piramide.elwis.web.contactmanager.action;

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
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jul 19, 2005
 * Time: 11:02:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskForwardAction extends com.piramide.elwis.web.contactmanager.action.ContactManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug(".........   taskForwardAction    execute  .........");
        TaskForm taskForm = (TaskForm) form;
        request.setAttribute("noSch", SchedulerConstants.TRUE_VALUE);
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        ActionForward forward = action.findForward("Fail");
        if (isCancelled(request)) {
            log.debug("Is Cancel");
            return (action.findForward("Cancel"));
        }
        TaskReadCmd cmd = new TaskReadCmd();
        ActionErrors aErrors = new ActionErrors();
        cmd.putParam(taskForm.getDtoMap());
        if (request.getParameter("taskId") != null && !request.getParameter("taskId").equals("")) {
            cmd.putParam("taskId", request.getParameter("taskId"));
        } else {
            cmd.putParam("taskId", request.getAttribute("taskId"));
        }

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
            taskForm.setDto("itParticipates", cmd.getResultDTO().get("itParticipates"));
            forward = action.findForward("Success");
        } else {
            forward = action.findForward("Fail");
        }
        return forward;
    }
}
