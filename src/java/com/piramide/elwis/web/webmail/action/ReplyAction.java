package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.dto.webmailmanager.MailRecipientDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class ReplyAction extends WebmailDefaultAction {


    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);

        if ("Fail".equals(forward.getName())) {
            return forward;
        }

        DefaultForm defaultForm = (DefaultForm) form;
        MailFormHelper mailFormHelper = new MailFormHelper();
        mailFormHelper.processEmail(request, response, defaultForm);

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        Integer mailAccountId = null;
        if (null == defaultForm.getDto("mailAccountId")) {
            mailFormHelper.setDefaultEmailAccount(userId, companyId, request, defaultForm);
            mailAccountId = (Integer) defaultForm.getDto("mailAccountId");
        } else {
            mailAccountId = (Integer) defaultForm.getDto("mailAccountId");
        }

        //set the default signature associated to the mailAccount
        if (null != mailAccountId) {
            mailFormHelper.setDefaultSignature(mailAccountId, userId, defaultForm, request);
        }

        processInvalidRecipients(request, mailFormHelper.getInvalidRecipients(defaultForm));

        //set in request to clean editor content
        request.setAttribute("enableCleanParagraphs", "true");

        return forward;
    }

    private void processInvalidRecipients(HttpServletRequest request, List<MailRecipientDTO> invalidRecipients) {
        String replyOperation = request.getParameter("replyOperation");
        if (!"REPLYALL".equals(replyOperation)) {
            return;
        }

        if (null == invalidRecipients) {
            return;
        }

        String message = "";
        for (int i = 0; i < invalidRecipients.size(); i++) {
            MailRecipientDTO emailRecipient = invalidRecipients.get(i);
            message += emailRecipient.getFormattedRecipient();

            if (i < invalidRecipients.size() - 1) {
                message += ", ";
            }
        }

        ActionErrors errors = new ActionErrors();
        errors.add("InvalidRecipientErrors", new ActionError("WebMailRecipient.invalidRecipients.error", message));
        saveErrors(request, errors);

    }
}
