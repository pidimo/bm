package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.cmd.common.SendEmailCmd;
import com.piramide.elwis.cmd.supportmanager.QuestionCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.template.TemplateFactory;
import com.piramide.elwis.web.common.template.TemplateResources;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.velocity.exception.VelocityException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Nov 7, 2005
 * Time: 11:28:25 AM
 * To change this template use File | Settings | File Templates.
 */

public class QuestionCreateAction extends com.piramide.elwis.web.common.action.DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("         QuestionCreateACTION  ... execute ...");

        if (request.getParameter("dto(cancel)") != null || isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        ActionErrors errors = new ActionErrors();
        ActionForward forward = mapping.findForward("Success");
        DefaultForm defaultForm = (DefaultForm) form;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);


        forward = super.execute(mapping, defaultForm, request, response);

        QuestionCmd questionCmd = new QuestionCmd();
        questionCmd.putParam("userId", user.getValue("userId"));
        questionCmd.putParam(defaultForm.getDtoMap());
        BusinessDelegate.i.execute(questionCmd, request);
        errors = MessagesUtil.i.convertToActionErrors(mapping, request, questionCmd.getResultDTO());
        saveErrors(request, errors);
        defaultForm.getDtoMap().putAll(questionCmd.getResultDTO());
        /*      for notification send   */
        if (SupportConstants.TRUE_VALUE.equals(questionCmd.getResultDTO().get("sendNotification"))) {
            sendNotification(request, defaultForm, user);
        }
        forward = mapping.findForward("Fail");

        return forward;
    }

    private void sendNotification(HttpServletRequest request, DefaultForm form, User user) {
        Map parameters = new HashMap();
        String message;

        if (form.getDtoMap().containsKey("TEMPLATE_PARAMETERS")) {
            Map templateParameters = (Map) form.getDtoMap().remove("TEMPLATE_PARAMETERS");

            parameters.put("question", templateParameters);
            try {
                SendEmailCmd cmd = new SendEmailCmd();
                Map mailParameters = (Map) form.getDtoMap().remove("MAIL_PARAMETERS");
                message = TemplateFactory.getTemplateManager().mergeTemplate("templates/email/QuestionNotification.vm", parameters, new TemplateResources(request));
                String subject = JSPHelper.getMessage(request, "Article.question");
                cmd.putParam(mailParameters);
                cmd.putParam("message", message);
                cmd.putParam("timeZone", user.getValue("dateTimeZone"));
                cmd.putParam("subject", "[".concat(subject).concat("]").concat(" ").concat((String) templateParameters.get("subject")));
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
