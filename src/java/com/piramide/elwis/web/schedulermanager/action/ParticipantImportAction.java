package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.schedulermanager.ParticipantTaskCreateCmd;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.schedulermanager.form.ParticipantForm;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Oct 31, 2005
 * Time: 3:32:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class ParticipantImportAction extends TaskListAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug(" -- ParticipantImportAction ------------- execute ----- ");

        ParticipantForm participantForm = (ParticipantForm) form;
        ActionForward forward = action.findForward("Fail");
        ActionErrors errors = new ActionErrors();
        ParticipantTaskCreateCmd cmd = new ParticipantTaskCreateCmd();

        if (!"".equals(request.getParameter("dto(cancel)")) && request.getParameter("dto(cancel)") != null) {
            log.debug(" . cancel execute . UserImport");
            return action.findForward("Cancel");
        }

        String groupId = null;
        if (request.getParameter("taskId") != null) {
            groupId = request.getParameter("taskId");
        }

        if ("true".equals(request.getParameter("Import_value")) || "group".equals(request.getParameter("dto(type)"))) {

            ForeignkeyValidator.i.validate(SchedulerConstants.TABLE_TASK, "taskId",
                    groupId, errors, new ActionError("customMsg.NotFound", request.getParameter("title")));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return action.findForward("Fail");
            }


            if (request.getParameter("aditionals") != null || "group".equals(request.getParameter("dto(type)"))) {
                cmd.putParam(participantForm.getDto());
                BusinessDelegate.i.execute(cmd, request);
                if (((Boolean) cmd.getResultDTO().get("failUser")).booleanValue()
                        || ((Boolean) cmd.getResultDTO().get("failGroup")).booleanValue()) {
                    errors = MessagesUtil.i.convertToActionErrors(action, request, cmd.getResultDTO());
                    saveErrors(request.getSession(), errors);
                    forward = action.findForward("Fail");
                } else {
                    forward = action.findForward("Success");
                }
            } else if (request.getParameter("aditionals") == null && !"group".equals(request.getParameter("dto(type)"))) {
                errors.add("empty", new ActionError("User.RelationUserGroup.delete"));
                saveErrors(request, errors);
                forward = action.findForward("importFail");
            }
        }
        return forward;
    }
}