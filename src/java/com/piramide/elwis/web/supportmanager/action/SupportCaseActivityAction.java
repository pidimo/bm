package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.cmd.common.SendEmailCmd;
import com.piramide.elwis.cmd.supportmanager.SupportCaseActivityCmd;
import com.piramide.elwis.cmd.supportmanager.SupportCaseActivityReadCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.template.TemplateFactory;
import com.piramide.elwis.web.common.template.TemplateResources;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.action.MainCommunicationAction;
import com.piramide.elwis.web.contactmanager.form.CommunicationFieldValidatorUtil;
import static com.piramide.elwis.web.contactmanager.form.CommunicationFieldValidatorUtil.FormButtonProperties.Send;
import com.piramide.elwis.web.supportmanager.form.SupportCaseActivityForm;
import com.piramide.elwis.web.webmail.delegate.EmailServiceDelegate;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;
import org.apache.velocity.exception.VelocityException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: SupportCaseActivityAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class SupportCaseActivityAction extends MainCommunicationAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        String caseId = "";
        if (request.getParameter("caseId") != null && !"".equals(request.getParameter("caseId"))) {
            caseId = request.getParameter("caseId");
        } else {
            caseId = request.getParameter("dto(caseId)");
        }

        User user = RequestUtils.getUser(request);
        ((DefaultForm) form).setDto("caseId", caseId);
        ((DefaultForm) form).setDto("companyId", user.getValue("companyId"));
        ((DefaultForm) form).setDto("userType", user.getValue("userType").toString());

        SupportCaseActivityForm activityForm = (SupportCaseActivityForm) form;
        Integer userAssigned = null;
        Integer toUser = null;
        ActionErrors e = new ActionErrors();
        ActionForward forward;

        if (!"true".equals(activityForm.getDto("isExternal")) && "update".equals(activityForm.getDto("op"))) {
            e = activityForm.validate(mapping, request);
        }

        if (!e.isEmpty()) {
            saveErrors(request, e);
            activityForm.getDtoMap().put("redirectValidation", "true");
            request.setAttribute("rebuild", "true");
            return mapping.findForward("Fail_validation");
        }
        if (activityForm.getDtoMap().containsKey("userAssigned")) {
            userAssigned = new Integer(activityForm.getDto("userAssigned").toString());
        }
        if (!activityForm.getDtoMap().containsKey("auto_assigned") && activityForm.getDtoMap().containsKey("toUserId")) {
            toUser = new Integer(activityForm.getDto("toUserId").toString());
        }
        boolean newActivity = "n_a".equals(request.getParameter("a_o"));

        if (userAssigned != null && toUser != null && userAssigned.intValue() != toUser.intValue() && !newActivity) {
            ActionErrors errors = new ActionErrors();
            errors.add("toUserId", new ActionError("SupportCase.msg.changeUserWithNextActivity"));
            activityForm.getDtoMap().put("toUserId", userAssigned);
            if (!errors.isEmpty()) {
                request.setAttribute("rebuild", "true");
            }
            saveErrors(request, errors);

            if ("5".equals(activityForm.getDto("type")) &&
                    activityForm.getDto("mailId") != null &&
                    !"".equals(activityForm.getDto("mailId"))) {
                SupportCaseActivityReadCmd readCmd = new SupportCaseActivityReadCmd();
                readCmd.putParam(activityForm.getDtoMap());
                BusinessDelegate.i.execute(readCmd, request);
                MessagesUtil.i.convertToActionErrors(mapping, request, readCmd.getResultDTO());
                activityForm.getDtoMap().putAll(readCmd.getResultDTO());
                activityForm.getDtoMap().put("redirectMail", "true");
                saveErrors(request, errors);
                if (readCmd.getResultDTO().isFailure()) {
                    return mapping.findForward("Cancel");
                }
            }
            ActionForward a = mapping.getInputForward();
            return a;
        }

        activityForm.setDto("toUserId", toUser);
        activityForm.setDto("USER_SESSIONID", user.getValue("userId"));
        activityForm.setDto("userAssigned", userAssigned);
        if (newActivity) {
            activityForm.setDto("new_activity", "true");
        }

        if ("s".equals(request.getParameter("comm")) && !"".equals(activityForm.getDto("type")) && activityForm.getDto("type") != null) { // Logica para communication!!!
            String commOp = "update";
            if ("d_c".equals(request.getParameter("a_o"))) {
                commOp = "delete";
            }

            activityForm.setDto("commOp", commOp);
        }
        forward = super.execute(mapping, form, request, response);
        if ("Success".equals(forward.getName())) {
            if (activityForm.getDtoMap().containsKey("IS_CLOSED")) {
                addError("SupportCaseActivity.msg.closeActivitySucessful", null, request);
                forward = mapping.findForward("CaseMainSearch");
            } else if (activityForm.getDtoMap().containsKey("DELETE_COMM") || newActivity) {
                if (activityForm.getDtoMap().containsKey("res_toUser") &&
                        !((Integer) activityForm.getDtoMap().get("res_toUser")).equals(user.getValue("userId"))) {

                    String userName = new SupportCaseActivityCmd().readUserName((Integer) activityForm.getDtoMap().get("res_toUser"));
                    sendNotification(request, activityForm, user);
                    addError("SupportCaseActivity.msg.asignedSuccesful", userName, request);
                    forward = mapping.findForward("CaseMainSearch");
                } else {
                    forward = new ActionForwardParameters()
                            .add("dto(activityId)", activityForm.getDtoMap().get("activityId").toString())
                            .forward(new ActionForward(forward.getPath()));
                }
            }
        }

        if (activityForm.getDtoMap().containsKey("activityId")) {
            Object activityId = activityForm.getDtoMap().get("activityId");
            if (activityId != null) {
                ActionForward forward1 = new ActionForwardParameters()
                        .add("dto(activityId)", activityId.toString())
                        .forward(new ActionForward(forward.getPath()));
                forward1.setContextRelative(forward.getContextRelative());
                forward1.setRedirect(forward.getRedirect());
                forward = forward1;
            }
        }

        return forward;
    }

    protected ActionForward checkMainReferences(HttpServletRequest request,
                                                ActionMapping mapping) {
        String caseId = "";
        if (request.getParameter("caseId") != null && !"".equals(request.getParameter("caseId"))) {
            caseId = request.getParameter("caseId");
        } else {
            caseId = request.getParameter("dto(caseId)");
        }

        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(SupportConstants.TABLE_SUPPORT_CASE, "caseId",
                caseId, errors, new ActionError("SupportCase.error.notFound"));
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("CaseMainSearch");
        }
        return null;
    }

    protected EJBCommand getGenerateDocumentCmd() {
        return new SupportCaseActivityCmd();
    }

    private void addError(String msgKey, String param, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        ActionError error = null;
        if (param != null) {
            error = new ActionError(msgKey, param);
        } else {
            error = new ActionError(msgKey);
        }
        errors.add("msg", error);
        saveErrors(request, errors);
    }

    private void sendNotification(HttpServletRequest request, SupportCaseActivityForm form, User user) {
        Map parameters = new HashMap();
        String message;
        if (form.getDtoMap().containsKey("TEMPLATE_PAMAMETERS")) {
            Map templateParameters = (Map) form.getDtoMap().remove("TEMPLATE_PAMAMETERS");
            parameters.put("case", templateParameters);
            try {
                SendEmailCmd cmd = new SendEmailCmd();
                Map mailParameters = (Map) form.getDtoMap().remove("MAIL_PAMAMETERS");
                message = TemplateFactory.getTemplateManager().mergeTemplate("templates/email/CaseAssigned.vm", parameters, new TemplateResources(request));
                log.debug("Message:" + message);
                cmd.putParam(mailParameters);
                cmd.putParam("message", message);
                cmd.putParam("timeZone", user.getValue("dateTimeZone"));
                String subject = JSPHelper.getMessage(request, "SupportCaseActivity.msg.assignmentNotification");
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

    @Override
    protected void hasCreatedCommunications(DefaultForm form,
                                            HttpServletRequest request) {
        List<Integer> communicationIdentifiers =
                (List<Integer>) form.getDto("communicationIdentifiers");

        if (null == communicationIdentifiers) {
            return;
        }

        if (!communicationIdentifiers.isEmpty()) {
            return;
        }
        ActionErrors errors = new ActionErrors();
        errors.add("NoActivityCreatedForSupportCase",
                new ActionError("Communication.msg.NoActivityCreatedForSupportCase"));

        saveErrors(request.getSession(), errors);
    }


    @Override
    protected void deliveryEmail(Integer userMailId,
                                 ActionForward forward,
                                 String op,
                                 HttpServletRequest request) {
        if (!CommunicationFieldValidatorUtil.isButtonPressed(Send.getKey(), request)) {
            return;
        }

        if (!"Success".equals(forward.getName())) {
            return;
        }

        EmailServiceDelegate.i.sentEmails(userMailId);
    }
}

