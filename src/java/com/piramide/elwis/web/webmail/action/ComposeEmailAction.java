package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.web.webmail.delegate.EmailServiceDelegate;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ComposeEmailAction extends WebmailAction {

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;
        Integer userMailId = Integer.valueOf(defaultForm.getDto("userMailId").toString());

        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            if (isSearch(request)) {
                request.removeAttribute(Globals.CANCEL_KEY);
                return mapping.findForward("SearchEmail");
            }
            if (returnToDraft(defaultForm)) {
                return mapping.findForward("Draft");
            }
            return forward;
        }

        if (null != (forward = priorValidations(form, request, mapping))) {
            return forward;
        }

        forward = super.execute(mapping, form, request, response);

        deliveryEmail(userMailId, forward);

        return forward;
    }

    private void deliveryEmail(Integer userMailId, ActionForward forward) {
        if (!"Success".equals(forward.getName())) {
            return;
        }

        EmailServiceDelegate.i.sentEmails(userMailId);
    }

    protected ActionForward priorValidations(ActionForm form,
                                             HttpServletRequest request,
                                             ActionMapping mapping) {

        if (isSaveButtonPressed(request) || isSaveAsDraftButtonPressed(request)) {
            return null;
        }

        DefaultForm defaultForm = (DefaultForm) form;

        if (null != defaultForm.getDto("mailAccountId")
                && !"".equals(defaultForm.getDto("mailAccountId").toString().trim())) {
            MailFormHelper emailHelper = new MailFormHelper();
            Integer mailAccountId = Integer.valueOf(defaultForm.getDto("mailAccountId").toString());
            Integer userMailId = Integer.valueOf(defaultForm.getDto("userMailId").toString());
            emailHelper.setDefaultSignature(mailAccountId, userMailId, defaultForm, request);
        }

        return mapping.getInputForward();
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("save");
    }

    private boolean isSaveAsDraftButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("saveAsDraft");
    }

    private boolean isSearch(HttpServletRequest request) {
        return null != request.getParameter("mailSearch") &&
                "true".equals(request.getParameter("mailSearch").trim());
    }

    private boolean returnToDraft(DefaultForm defaultForm) {
        return null != defaultForm.getDto("draftId") && !"".equals(defaultForm.getDto("draftId"));
    }
}
