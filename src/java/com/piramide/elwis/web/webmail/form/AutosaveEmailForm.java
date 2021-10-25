package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.util.TokenFieldEmailRecipientHelper;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;

/**
 * Form to manage auto save email inputs
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class AutosaveEmailForm extends EmailForm {

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate AutosaveEmailForm........." + getDtoMap());

        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
        setDto("userTimeZone", dateTimeZone);

        //define userMailId if not exist in dto
        if (getDto("userMailId") == null || GenericValidator.isBlankOrNull(getDto("userMailId").toString())) {
            setDto("userMailId", getUserMailId(request));
        }

        buildRecipienStructure();

        return new ActionErrors();
    }

    @Override
    protected void buildRecipienStructure() {
        if ("true".equals(getDto("isTokenFieldRecipients"))) {
            TokenFieldEmailRecipientHelper tokenFieldHelper = new TokenFieldEmailRecipientHelper(this);
            tokenFieldHelper.buildRecipientStructure(this);
        } else {
            super.buildRecipienStructure();
        }
    }

    private Integer getUserMailId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        MailFormHelper mailFormHelper = new MailFormHelper();
        return mailFormHelper.getUserMailId(userId, request);
    }

    private boolean existEmailDataToSaveAsTemp() {
        boolean existData = false;

        if (!GenericValidator.isBlankOrNull((String) getDto("to"))) {
            return true;
        }
        if (!GenericValidator.isBlankOrNull((String) getDto("cc"))) {
            return true;
        }
        if (!GenericValidator.isBlankOrNull((String) getDto("bcc"))) {
            return true;
        }
        if (!GenericValidator.isBlankOrNull((String) getDto("mailSubject"))) {
            return true;
        }

        String bodyData = (String) getDto("body");
        if ("true".equals(getDto("useHtmlEditor"))) {
            //empty editor contain 107 characters, so to avoid empty values, verify with 110
            if (bodyData.length() > 110) {
                return true;
            }
        } else if (!GenericValidator.isBlankOrNull(bodyData)) {
            return true;
        }

        return false;
    }
}
