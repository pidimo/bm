package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.utils.CommunicationTypes;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.el.Functions;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class MainCommunicationForm extends DefaultForm {
    private Log log = LogFactory.getLog(MainCommunicationForm.class);

    private CommunicationValidatorUtil util = new ContactCommunicationFormUtil();

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        String changeTelecomType = (String) getDto("changeCommunicationType");
        String communicationType = (String) getDto("type");
        if ("true".equals(changeTelecomType)) {
            ActionErrors errors = new ActionErrors();
            settingUpSkipErrors(communicationType, request, errors);
            if (null != util && !errors.isEmpty()) {
                util.settingUpOldValues(request, this);
            }

            if (CommunicationTypes.LETTER.equals(communicationType)) {
                //set default additional address
                Functions.setDefaultCommunicationAdditionalAddress(this, request);
            }

            if (CommunicationTypes.EMAIL.equals(communicationType) && isFromActivityCampaign()) {
                setDefaultRecipientEmail();
            }

            return errors;
        } else {
            boolean isSaveButtonPressed = CommunicationFieldValidatorUtil.isButtonPressed(
                    CommunicationFieldValidatorUtil.FormButtonProperties.Save.getKey(), request);
            boolean isGenerateButtonPressed = CommunicationFieldValidatorUtil.isButtonPressed(
                    CommunicationFieldValidatorUtil.FormButtonProperties.Generate.getKey(), request);

            boolean isSendButtonPressed = CommunicationFieldValidatorUtil.isButtonPressed(
                    CommunicationFieldValidatorUtil.FormButtonProperties.Send.getKey(), request);

            if (!isGenerateButtonPressed && !isSaveButtonPressed && !isSendButtonPressed) {
                ActionErrors errors = new ActionErrors();
                settingUpSkipErrors(communicationType, request, errors);
                if (null != util && !errors.isEmpty()) {
                    util.settingUpOldValues(request, this);
                }

                //set default additional address
                Functions.setDefaultCommunicationAdditionalAddress(this, request);

                return errors;
            }

            ActionErrors errors = super.validate(mapping, request);

            if (null != util) {
                util.aditionalValidations(request, errors, this);
            }

            if (null != util && !errors.isEmpty()) {
                util.settingUpOldValues(request, this);
            }

            return errors;
        }
    }

    private void settingUpSkipErrors(String communicationType, HttpServletRequest request, ActionErrors errors) {
        request.setAttribute("skipErrors", "true");
        errors.add("returnToForm", new ActionError("Address.error.cityNotFound"));
        if (com.piramide.elwis.utils.CommunicationTypes.EMAIL.equals(communicationType)) {
            User user = RequestUtils.getUser(request);
            Integer userId = (Integer) user.getValue(Constants.USERID);

            String changeTelecomType = (String) getDto("changeCommunicationType");
            if ("create".equals(getDto("op")) || "true".equals(changeTelecomType)) {
                MailFormHelper emailHelper = new MailFormHelper();
                Integer emailUserChecker = emailHelper.isValidEmailUser(userId, request);
                if (-1 == emailUserChecker) {
                    errors.clear();
                    request.removeAttribute("skipErrors");
                }
            }
        }
    }

    protected void setValidatorUtil(CommunicationValidatorUtil util) {
        this.util = util;
    }

    public CommunicationValidatorUtil getUtil() {
        return util;
    }

    private void setDefaultRecipientEmail() {
        Integer addressId = null;
        Integer contactPersonId = null;
        if (getDto("addressId") != null && !GenericValidator.isBlankOrNull(getDto("addressId").toString())) {
            addressId = new Integer(getDto("addressId").toString());
        }

        if (getDto("contactPersonId") != null && !GenericValidator.isBlankOrNull(getDto("contactPersonId").toString())) {
            contactPersonId = new Integer(getDto("contactPersonId").toString());
        }

        if (addressId != null) {
            TelecomDTO telecomDTO = Functions.findDefaultEmailTelecom(addressId, contactPersonId);
            if (telecomDTO != null) {
                setDto("to", telecomDTO.getData());
            }
        }
    }

    private boolean isFromActivityCampaign() {
        return getDto("activityId") != null && !GenericValidator.isBlankOrNull(getDto("activityId").toString());
    }

}
