package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ComposeForwardAction extends DefaultForwardAction {
    private Integer userMailId;

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        userMailId = getUserMailId(request);
        if (null == userMailId) {
            //disable webmail configuration tabs
            request.setAttribute("hideMailConfigurationTabs", true);
            return mapping.findForward("NoUserMail");
        }

        //check userMail accessRights
        boolean hasExecutePermission = Functions.hasAccessRight(request, "MAIL", "EXECUTE");
        if (!hasExecutePermission) {
            //disable webmail configuration tabs
            request.setAttribute("hideMailConfigurationTabs", true);

            String msgs[] = {JSPHelper.getMessage(request, "EXECUTE"),
                    JSPHelper.getMessage(request, "Webmail.common.mail"),
                    JSPHelper.getMessage(request, "Common.webmail")};

            ActionErrors errors = new ActionErrors();
            errors.add("especial error", new ActionError("error.invalid_access2", msgs));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        return null;
    }

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        defaultForm.setDto(UserMailDTO.KEY_USERMAILID, userMailId);

        MailFormHelper emailHelper = new MailFormHelper();

        emailHelper.setDefaultEmailAccount(userMailId,
                (Integer) user.getValue(Constants.COMPANYID), request, defaultForm);

        if (null != defaultForm.getDto("mailAccountId")) {
            Integer mailAccountId = (Integer) defaultForm.getDto("mailAccountId");
            emailHelper.setDefaultSignature(mailAccountId, userMailId, defaultForm, request);

            emailHelper.setMailAccountDefaultSetting(mailAccountId, request, defaultForm);
        }
        emailHelper.readUserConfiguration(userMailId, request, defaultForm);
    }

    private Integer getUserMailId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        MailFormHelper mailFormHelper = new MailFormHelper();
        return mailFormHelper.getUserMailId(userId, request);
    }
}
