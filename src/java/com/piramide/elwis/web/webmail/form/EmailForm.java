package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.el.Functions;
import com.piramide.elwis.web.webmail.util.AttachFormHelper;
import com.piramide.elwis.web.webmail.util.EmailRecipientHelper;
import com.piramide.elwis.web.webmail.util.TokenFieldEmailRecipientHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class EmailForm extends DefaultForm {

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate EmailForm......." + getDtoMap());

        if (!isSaveButtonPressed(request) && !isSaveAsDraftButtonPressed(request)) {
            rewriteHiddenElements();

            TokenFieldEmailRecipientHelper tokenFieldHelper = new TokenFieldEmailRecipientHelper(this);
            tokenFieldHelper.rewriteTokenFieldRecipients(request);
            return new ActionErrors();
        }

        ActionErrors errors = super.validate(mapping, request);

        if ("true".equals(getDto("isTokenFieldRecipients"))) {
            validateTokenFieldRecipients(errors, request);
        } else {
            validatePlainRecipients(errors);
        }

        ActionError createCommunicationValidation = validateCreateCommunicationOption(request);
        if (null != createCommunicationValidation) {
            errors.add("createCommunicationValidation", createCommunicationValidation);
        }

        ActionError attachmentsValidation = validateAttachments(request, errors);
        if (null != attachmentsValidation) {
            errors.add("attachmentsValidation", attachmentsValidation);
            errors.add("AttachmentsNewError", new ActionError("Webmail.Attach.attachAnew"));
        }

        //validate temporal image store, only if html editor is enabled
        if ("true".equals(getDto("useHtmlEditor"))) {
            ActionError imageStoreError = Functions.emailBodyImageStoreForeignKeyValidation(getDto("body").toString());
            if (imageStoreError != null) {
                errors.add("imageError", imageStoreError);
            }
        }

        if (!errors.isEmpty()) {
            rewriteHiddenElements();
        }

        if (errors.isEmpty()) {
            User user = RequestUtils.getUser(request);
            DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
            setDto("userTimeZone", dateTimeZone);
        }
        return errors;
    }

    private void validatePlainRecipients(ActionErrors errors) {
        List<ActionError> emailFormatErrors = new ArrayList<ActionError>();
        emailFormatErrors.addAll(validateEmailFormat((String) getDto("to")));
        emailFormatErrors.addAll(validateEmailFormat((String) getDto("cc")));
        emailFormatErrors.addAll(validateEmailFormat((String) getDto("bcc")));
        if (!emailFormatErrors.isEmpty()) {
            addError(emailFormatErrors, errors);
        }

        if (emailFormatErrors.isEmpty()) {
            List<ActionError> emailErrors = new ArrayList<ActionError>();
            emailErrors.addAll(validateEmails((String) getDto("to")));
            emailErrors.addAll(validateEmails((String) getDto("cc")));
            emailErrors.addAll(validateEmails((String) getDto("bcc")));
            if (!emailErrors.isEmpty()) {
                addError(emailErrors, errors);
            }
        }

        List<ActionError> duplicatedEmailError = validateDuplicatedEmails();
        if (!duplicatedEmailError.isEmpty()) {
            addError(duplicatedEmailError, errors);
        }

        if (errors.isEmpty()) {
            buildRecipienStructure();
        }
    }

    private void validateTokenFieldRecipients(ActionErrors errors, HttpServletRequest request) {
        TokenFieldEmailRecipientHelper tokenFieldHelper = new TokenFieldEmailRecipientHelper(this);

        List<ActionError> emailErrors = tokenFieldHelper.validateEmails();
        if (!emailErrors.isEmpty()) {
            addError(emailErrors, errors);
        }

        List<ActionError> duplicatedErrors = tokenFieldHelper.validateDuplicatedEmails();
        if (!duplicatedErrors.isEmpty()) {
            addError(duplicatedErrors, errors);
        }

        if (errors.isEmpty()) {
            tokenFieldHelper.buildRecipientStructure(this);
        }

        tokenFieldHelper.rewriteTokenFieldRecipients(request);
    }

    protected void buildRecipienStructure() {
        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();

        Map recipients = new HashMap();
        String to = (String) getDto("to");
        String cc = (String) getDto("cc");
        String bcc = (String) getDto("bcc");

        List<Map> toRecipient = emailRecipientHelper.buildRecipienStructure(this, to);

        List<Map> ccRecipient = emailRecipientHelper.buildRecipienStructure(this, cc);

        List<Map> bccRecipient = emailRecipientHelper.buildRecipienStructure(this, bcc);

        if (!toRecipient.isEmpty()) {
            recipients.put("to", toRecipient);
        }
        if (!ccRecipient.isEmpty()) {
            recipients.put("cc", ccRecipient);
        }
        if (!bccRecipient.isEmpty()) {
            recipients.put("bcc", bccRecipient);
        }

        setDto("recipients", recipients);
    }

    private void rewriteHiddenElements() {
        AttachFormHelper attachFormHelper = new AttachFormHelper();
        List<AttachDTO> attachments = attachFormHelper.rebuildAttachmentList(this);
        setDto("attachments", attachments);

        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
        setDto("dynamicHiddens", emailRecipientHelper.getDynamicHiddens(this));
    }

    private ActionError validateAttachments(HttpServletRequest request, ActionErrors errors) {
        AttachFormHelper attachFormHelper = new AttachFormHelper();
        ActionError actionError = attachFormHelper.validateAttachments(this, request);
        if (null != actionError) {
            setDto("attachmentCounter", "");
            setDto("messageCounter", "");
            return actionError;
        }


        if (!errors.isEmpty()) {
            setDto("attachmentCounter", "");
            setDto("messageCounter", "");
            return actionError;
        }


        List<ArrayByteWrapper> newAttachments = attachFormHelper.buildNewAttachmentStructure();
        List<AttachDTO> oldAttachments = attachFormHelper.buildOldAttachmentStructure(this);
        setDto("newAttachments", newAttachments);
        setDto("oldAttachments", oldAttachments);

        return null;
    }

    private ActionError validateCreateCommunicationOption(HttpServletRequest request) {
        String createOutCommunication = (String) getDto("createOutCommunication");
        if (GenericValidator.isBlankOrNull(createOutCommunication)) {
            return null;
        }

        if ("true".equals(createOutCommunication) && isSaveButtonPressed(request)) {
            String saveSendItem = (String) getDto("saveSendItem");
            if (GenericValidator.isBlankOrNull(saveSendItem)) {
                return new ActionError("Webmail.mailCommunications.outCommunications",
                        JSPHelper.getMessage(request, "Webmail.common.saveInSentItems"));
            }
        }

        return null;
    }

    private List<ActionError> validateEmails(String recipient) {
        if (GenericValidator.isBlankOrNull(recipient)) {
            return new ArrayList<ActionError>();
        }

        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
        return emailRecipientHelper.validateEmails(recipient);
    }

    private List<ActionError> validateEmailFormat(String recipient) {
        if (GenericValidator.isBlankOrNull(recipient)) {
            return new ArrayList<ActionError>();
        }

        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
        return emailRecipientHelper.validateEmailFormat(recipient);
    }

    private List<ActionError> validateDuplicatedEmails() {
        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
        return emailRecipientHelper.
                validateDuplicatedEmails((String) getDto("to"), (String) getDto("cc"), (String) getDto("bcc"));
    }

    private ActionErrors addError(List<ActionError> errorList, ActionErrors errors) {
        for (int i = 0; i < errorList.size(); i++) {
            errors.add("Error" + i, errorList.get(i));
        }

        return errors;
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("save");
    }

    private boolean isSaveAsDraftButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("saveAsDraft");
    }
}
