package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.delegate.EmailServiceDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class UploadAndDownloadEmailAction extends WebmailDefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionForward forward = super.execute(mapping, form, request, response);

        if (forward != null && "NoUserMail".equals(forward.getName())) {
            return mapping.findForward("NoUserMail");
        }

        DefaultForm defaultForm = (DefaultForm) form;

        User user = RequestUtils.getUser(request);
        Integer userMailId = Integer.valueOf(defaultForm.getDto("userMailId").toString());
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        EmailServiceDelegate.i.sentEmailsSync(userMailId, companyId);

        EmailServiceDelegate.i.downloadEmails(userMailId);

        return forward;
    }
}
