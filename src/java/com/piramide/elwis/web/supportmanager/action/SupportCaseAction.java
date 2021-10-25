package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.cmd.common.SendEmailCmd;
import com.piramide.elwis.cmd.supportmanager.AutoAssignateUserCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.common.template.TemplateFactory;
import com.piramide.elwis.web.common.template.TemplateResources;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.supportmanager.form.SupportCaseForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;
import org.apache.velocity.exception.VelocityException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: SupportCaseAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class SupportCaseAction extends SupportCaseBaseAction {

    protected boolean doValidate(HttpServletRequest request) {
        return !"create".equals(request.getParameter("dto(op)"));
    }

    public ActionForward myExecute(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        SupportCaseForm caseForm = (SupportCaseForm) form;
        log.debug("SupportCaseAction.....");
        log.debug("Form     :" + caseForm.getDtoMap());

        if (request.getParameter("dto(op)") != null && request.getParameter("dto(save)") == null) {
            if (caseForm.getDto("productId") != null) {
                AutoAssignateUserCmd cmd = new AutoAssignateUserCmd();
                caseForm.setDto("auto_assigned", "true");
                caseForm.setDto("isProduct", "true");
                // Object to_userId = caseForm.getDto("toUserId");
                if (caseForm.getDto("toUserId") != null && !"".equals(caseForm.getDto("toUserId"))) {
                    caseForm.setDto("toUserId", new Integer(caseForm.getDto("toUserId").toString()));
                } else {
                    caseForm.setDto("toUserId", new Integer(-100));
                }
                Integer toUserId = cmd.getPossibleUserForAssignate(caseForm.getDtoMap());
                if (toUserId != null) {
                    caseForm.setDto("toUserId", toUserId);
                }
            }
            return mapping.getInputForward();
        }

        if ("create".equals(request.getParameter("dto(op)")) || "update".equals(request.getParameter("dto(op)"))) {
            ActionErrors errors = caseForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.getInputForward();
            }
        }

        if ("create".equals(request.getParameter("dto(op)"))) {
            caseForm.setDto("fromUserId", user.getValue("userId"));

            if (isExternalUser) {
                caseForm.setDto("auto_assigned", "true");
                caseForm.setDto("addressId", user.getValue("userAddressId"));
            }/*else {caseForm.setDto("auto_assigned", "true");
                   caseForm.setDto("supportCreate", "true");}*/
        }

        log.debug("isExternalUser ..... :" + isExternalUser);
        if (!isExternalUser) {
            Integer userAssigned = null;
            Integer toUser = null;
            if (caseForm.getDtoMap().containsKey("toUserId")) {
                toUser = new Integer(caseForm.getDto("toUserId").toString());
            }
            if (caseForm.getDtoMap().containsKey("userAssigned")) {
                userAssigned = new Integer(caseForm.getDto("userAssigned").toString());
            }

            if ("update".equals(request.getParameter("dto(op)")) && !caseForm.getDtoMap().containsKey("isClosed")) {
                if (userAssigned != null && toUser != null && userAssigned.intValue() != toUser.intValue() &&
                        !caseForm.getDtoMap().containsKey("new_activity")) {
                    ActionErrors errors = new ActionErrors();
                    errors.add("toUserId", new ActionError("SupportCase.msg.changeUser"));
                    saveErrors(request, errors);
                    caseForm.setDto("new_activity", "true");
                    return mapping.getInputForward();
                } else if (userAssigned != null && toUser != null && userAssigned.intValue() == toUser.intValue()) {
                    caseForm.getDtoMap().remove("new_activity");
                }
            }

            if (caseForm.getDtoMap().containsKey("reopenDescription")) {
                caseForm.setDto("openUserId", user.getValue("userId"));
            }
            caseForm.setDto("toUserId", toUser);
            caseForm.setDto("userAssigned", userAssigned);
        }
        caseForm.setDto("USER_SESSIONID", user.getValue("userId"));
        ActionForward forward = superExecute(mapping, form, request, response);

        if ("Success".equals(forward.getName()) && request.getParameter("dto(op)") != null) {
            if (caseForm.getDtoMap().containsKey("IS_CLOSED") || "delete".equals(request.getParameter("dto(op)"))) {
                forward = mapping.findForward("CaseMainSearch");
            } else if (caseForm.getDtoMap().containsKey("toUserId")) {
                String nameForward = "Success";
                ActionForwardParameters forwardParameters = new ActionForwardParameters()
                        .add("caseId", caseForm.getDto("caseId").toString());
                if (caseForm.getDtoMap().get("toUserId").equals(user.getValue("userId")) && caseForm.getDtoMap().containsKey("activityId")) {
                    forwardParameters.add("dto(activityId)", caseForm.getDto("activityId").toString());
                    nameForward = "GoActivity";
                }
                forward = forwardParameters.forward(mapping.findForward(nameForward));
            }

            //caseForm.setDto("userTypeSelected",new Integer(isExternalUser?0:1));

            sendNotification(request, caseForm);
        }
        return forward;
    }

    private void sendNotification(HttpServletRequest request, SupportCaseForm form) {
        log.debug(" ... sendNotification function execute ....");
        Map parameters = new HashMap();
        String message;
        if (form.getDtoMap().containsKey("TEMPLATE_PAMAMETERS")) {
            Map templateParameters = (Map) form.getDtoMap().remove("TEMPLATE_PAMAMETERS");
            templateParameters.put("timeZone", user.getValue("dateTimeZone"));
            parameters.put("case", templateParameters);
            Integer userType = (Integer) form.getDto("userTypeSelected");
            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
            log.debug(" ... userType ... " + userType);
            if (!isExternalUser) {
                userType = 2;
            }
            if (userType != null) {
                String specialMsg = null;
                if (userType.intValue() == AutoAssignateUserCmd.ROOT) {
                    specialMsg = JSPHelper.getMessage(locale, "SupportCaseActivity.msg.assignedToRoot");
                } else if (userType.intValue() == AutoAssignateUserCmd.ANOTHER_SUPPORT_USER) {
                    specialMsg = JSPHelper.getMessage(locale, "SupportCaseActivity.msg.assignedToOtherSupportUser");
                }
                parameters.put("specialMessage", specialMsg);
            }
            try {
                SendEmailCmd cmd = new SendEmailCmd();
                Map mailParameters = (Map) form.getDtoMap().remove("MAIL_PAMAMETERS");

                message = TemplateFactory.getTemplateManager().mergeTemplate("templates/email/CaseAssigned.vm", parameters, new TemplateResources(request));
                //log.debug("Message:" + message);
                cmd.putParam(mailParameters);
                cmd.putParam("message", message);
                cmd.putParam("timeZone", user.getValue("dateTimeZone"));
                String subject = JSPHelper.getMessage(locale, "SupportCaseActivity.msg.assignmentNotification");
                String number = (String) templateParameters.get("number");
                String state = (String) templateParameters.get("state");
                String title = (String) templateParameters.get("title");
                cmd.putParam("subject", subject.replaceFirst("\\{CASE_NUMBER\\}", number).replaceFirst("\\{CASE_STATE\\}", state).replaceFirst("\\{CASE_TITLE\\}", title));
                cmd.putParam("userId", user.getValue(Constants.USERID));
                BusinessDelegate.i.execute(cmd, request);
            } catch (VelocityException e) {
                e.printStackTrace();
            } catch (AppLevelException e) {
                e.printStackTrace();
            }
        }
    }
}
