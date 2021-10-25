package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.schedulermanager.ParticipantCreateCmd;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.schedulermanager.form.ParticipantForm;
import com.piramide.elwis.web.schedulermanager.util.AppointmentNotificationUtil;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 13-feb-2006
 * Time: 17:05:31
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentParticipantImportAction extends com.piramide.elwis.web.common.action.ListAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug(" --- AppointmentParticipantImportAction ------------- execute ----- ");

        ParticipantForm participantForm = (ParticipantForm) form;
        ActionForward forward = action.findForward("Fail");
        ActionErrors errors = new ActionErrors();
        ParticipantCreateCmd cmd = new ParticipantCreateCmd();

        if (!"".equals(request.getParameter("dto(cancel)")) && request.getParameter("dto(cancel)") != null) {
            log.debug(" . cancel execute . UserImport");
            return action.findForward("Cancel");
        }

        String groupId = null;
        if (request.getParameter("appointmentId") != null) {
            groupId = request.getParameter("appointmentId");
        }

        if ("true".equals(request.getParameter("Import_value")) || "group".equals(request.getParameter("dto(type)"))) {

            errors = ForeignkeyValidator.i.validate(SchedulerConstants.TABLE_APPOINTMENT, "appointmentId",
                    groupId, errors, new ActionError("customMsg.NotFound", request.getParameter("title")));

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return action.findForward("Fail");
            }

            if (request.getParameter("aditionals") != null || "group".equals(request.getParameter("dto(type)"))) {
                User user = RequestUtils.getUser(request);
                cmd.putParam(participantForm.getDto());
                cmd.putParam("userSessionId", user.getValue("userId"));
                BusinessDelegate.i.execute(cmd, request);

                //send notification emails
                if (cmd.getResultDTO().get("userNotificationList") != null) {
                    AppointmentNotificationUtil.sendNotificationEmails((List<Map>) cmd.getResultDTO().get("userNotificationList"), new Integer(groupId), request);
                }

                if (((Boolean) cmd.getResultDTO().get("failUser")).booleanValue()
                        || ((Boolean) cmd.getResultDTO().get("failGroup")).booleanValue()) {
                    errors = MessagesUtil.i.convertToActionErrors(action, request, cmd.getResultDTO());
                    saveErrors(request, errors);
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