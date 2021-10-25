package com.piramide.elwis.web.bmapp.action.webmail;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.bmapp.util.MappingUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.action.ComposeEmailAction;
import com.piramide.elwis.web.webmail.form.EmailForm;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.6
 */
public class ComposeEmailRESTAction extends ComposeEmailAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  ComposeEmailRESTAction..." + request.getParameterMap());

        EmailForm emailForm = (EmailForm) form;
        setDefaultProperties(emailForm, request);

        ActionErrors errors = emailForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveRESTErrors(emailForm, errors, request);
            return mapping.getInputForward();
        }

        return super.execute(mapping, emailForm, request, response);
    }

    private void setDefaultProperties(DefaultForm defaultForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer sessionUserId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        //define the userMailId
        defaultForm.setDto("userMailId", sessionUserId);

        MailFormHelper emailHelper = new MailFormHelper();
        emailHelper.setDefaultEmailAccount(sessionUserId, (Integer) user.getValue(Constants.COMPANYID), request, defaultForm);

        if (null != defaultForm.getDto("mailAccountId")) {
            Integer mailAccountId = (Integer) defaultForm.getDto("mailAccountId");
            emailHelper.setDefaultSignature(mailAccountId, sessionUserId, defaultForm, request);

            emailHelper.setMailAccountDefaultSetting(mailAccountId, request, defaultForm);
        }
        emailHelper.readUserConfiguration(sessionUserId, request, defaultForm);

        if (WebMailConstants.BODY_TYPE_HTML.equals(defaultForm.getDto("bodyType"))) {
            defaultForm.setDto("useHtmlEditor", "true");
        } else {
            defaultForm.setDto("useHtmlEditor", "false");
        }

        defaultForm.setDto("mailPriority", WebMailConstants.MAIL_PRIORITY_DEFAULT);

        Map dto = new HashMap();
        dto.putAll(defaultForm.getDtoMap());
        MappingUtil.mappingPropertyAsString("createOutCommunication", dto, defaultForm);
        MappingUtil.mappingPropertyAsString("saveSendItem", dto, defaultForm);
    }

    private void saveRESTErrors(DefaultForm defaultForm, ActionErrors errors, HttpServletRequest request) {
        request.setAttribute("dto", defaultForm.getDtoMap());
        saveErrors(request, errors);
    }

}
