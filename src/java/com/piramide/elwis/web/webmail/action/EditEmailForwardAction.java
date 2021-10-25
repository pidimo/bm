package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import net.java.dev.strutsejb.web.DefaultForm;
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
public class EditEmailForwardAction extends WebmailDefaultAction {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);

        DefaultForm defaultForm = (DefaultForm) form;
        User user = RequestUtils.getUser(request);
        Integer userMailId = (Integer) user.getValue(Constants.USERID);

        MailFormHelper emailHelper = new MailFormHelper();
        emailHelper.readUserConfiguration(userMailId, request, defaultForm);
        emailHelper.updateHtmlBodyForDraftEmail(defaultForm, request, response);

        if (null != defaultForm.getDto("mailAccountId")) {
            Integer mailAccountId = (Integer) defaultForm.getDto("mailAccountId");
            emailHelper.setDefaultSignature(mailAccountId, userMailId, defaultForm, request);

            emailHelper.setMailAccountDefaultSetting(mailAccountId, request, defaultForm);
        }

        return forward;
    }
}
