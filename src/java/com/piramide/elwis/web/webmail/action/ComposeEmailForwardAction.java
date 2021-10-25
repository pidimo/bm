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
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ComposeEmailForwardAction extends WebmailDefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        DefaultForm composeForm = (DefaultForm) form;

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        MailFormHelper emailHelper = new MailFormHelper();

        emailHelper.setDefaultEmailAccount(userId, companyId, request, composeForm);
        if (null != composeForm.getDto("mailAccountId")) {
            String actualEmailAccount = composeForm.getDto("mailAccountId").toString();
            emailHelper.setDefaultSignature(Integer.valueOf(actualEmailAccount), userId, composeForm, request);

            emailHelper.setMailAccountDefaultSetting(actualEmailAccount, request, composeForm);
        }
        emailHelper.readUserConfiguration(userId, request, composeForm);
        return super.execute(mapping, composeForm, request, response);
    }
}
