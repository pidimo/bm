package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jul 12, 2005
 * Time: 6:00:55 PM
 * To change this template use File | Settings | File Templates.
 */

public class ParticipantDeleteAction extends TaskManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("... Participant delete action ...");
        DefaultForm participantForm = (DefaultForm) form;
        ActionForward forward = action.findForward("Fail");

        if (request.getParameter("size").equals("1") && (request.getParameter("userGroupId") == null || "".equals(request.getParameter("userGroupId")))) {
            ActionErrors errors = new ActionErrors();
            errors.add("empty", new ActionError("Task.Participant.noEmptyList"));
            saveErrors(request, errors);
            forward = action.findForward("Fail");
        } else {
            if (request.getParameter("userGroupId") != null && !"".equals(request.getParameter("userGroupId"))) {
                log.debug(" ... is group ...");

                return new ActionForwardParameters()
                        .add("size", request.getParameter("size"))
                        .add("userGroupId", request.getParameter("userGroupId"))
                        .add("index", request.getParameter("index"))
                        .add("taskId", request.getParameter("taskId"))
                        .add("scheduledUserId", request.getParameter("dto(scheduledUserId)"))
                        .add("participantName", request.getParameter("dto(participantName)"))
                        .add("taskGroupName", request.getParameter("dto(taskGroupName)"))
                        .add("title", request.getParameter("dto(title)"))
                        .forward(action.findForward("DeleteGroup"));
            }
            forward = super.execute(action, participantForm, request, response);
        }
        return forward;
    }
}