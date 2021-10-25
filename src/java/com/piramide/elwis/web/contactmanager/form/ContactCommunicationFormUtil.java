package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CommunicationTypes;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.el.Functions;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.FileValidator;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import com.piramide.elwis.web.webmail.util.AttachFormHelper;
import com.piramide.elwis.web.webmail.util.EmailRecipientHelper;
import com.piramide.elwis.web.webmail.util.TokenFieldEmailRecipientHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ContactCommunicationFormUtil implements CommunicationValidatorUtil {
    private MailFormHelper emailHelper = new MailFormHelper();

    private Log log = LogFactory.getLog(ContactCommunicationFormUtil.class);

    public void aditionalValidations(HttpServletRequest request, ActionErrors errors, DefaultForm form) {

        String communicationType = (String) form.getDto("type");

        boolean isSaveButtonPressed = CommunicationFieldValidatorUtil.isButtonPressed(
                CommunicationFieldValidatorUtil.FormButtonProperties.Save.getKey(), request);

        if (CommunicationTypes.DOCUMENT.equals(communicationType) && isSaveButtonPressed) {
            documentCommunicationValidations(request, errors, form);
        }

        boolean isSendButtonPressed = CommunicationFieldValidatorUtil.isButtonPressed(
                CommunicationFieldValidatorUtil.FormButtonProperties.Send.getKey(), request);
        if (com.piramide.elwis.utils.CommunicationTypes.EMAIL.equals(communicationType) && isSendButtonPressed) {
            emailValidations(request, errors, form);
        }

        boolean isGenerateButtonPressed = CommunicationFieldValidatorUtil.isButtonPressed(
                CommunicationFieldValidatorUtil.FormButtonProperties.Generate.getKey(), request);

        if ((com.piramide.elwis.utils.CommunicationTypes.FAX.equals(communicationType) ||
                com.piramide.elwis.utils.CommunicationTypes.LETTER.equals(communicationType)) && isGenerateButtonPressed) {

            if (!Functions.isCampaignGenerationCommunication((String) form.getDto("contactId"))) {
                String templateId = (String) form.getDto("templateId");
                if (GenericValidator.isBlankOrNull(templateId)) {
                    errors.add("templateRequired", new ActionError("errors.required",
                            JSPHelper.getMessage(request, "Document.template")));
                }
            }
        }
    }

    public void settingUpOldValues(HttpServletRequest request, DefaultForm form) {
        log.debug("-> Setting up old values in MainCommunicationForm");

        String communicationType = (String) form.getDto("type");
        if (com.piramide.elwis.utils.CommunicationTypes.EMAIL.equals(communicationType)) {
            settingUpEmailValues(request, form);
        }

    }

    private void documentCommunicationValidations(HttpServletRequest request,
                                                  ActionErrors errors,
                                                  DefaultForm form) {
        User user = RequestUtils.getUser(request);
        String op = (String) form.getDto("op");

        FormFile documentFile = (FormFile) form.getDto("documentFile");

        if ("create".equals(op) || null == form.getDto("contactId")) {
            if ("".equals(documentFile.getFileName().trim())) {
                errors.add("RequiredError", new ActionError("errors.required",
                        JSPHelper.getMessage(request, "Communication.document")));
                return;
            }
        }

        if (!"".equals(documentFile.getFileName().trim())) {
            ActionError fileError = validateFile(documentFile, user);
            if (null != fileError) {
                errors.add("DocumentFileValidation", fileError);
                return;
            }

            try {
                ArrayByteWrapper wrapper = new ArrayByteWrapper(documentFile.getFileData());
                form.setDto("freeText", wrapper);
                form.setDto("documentFileName", documentFile.getFileName());
                form.getDtoMap().remove("documentFile");
            } catch (IOException e) {
                //
            }
        }
    }

    private ActionError validateFile(FormFile file, User user) {

        ActionError fileContentError = FileValidator.i.validateContent(file);
        if (null != fileContentError) {
            return fileContentError;
        }

        Integer maxAttachSize = (Integer) user.getValue("maxAttachSize");
        ActionError fileSizeError = FileValidator.i.validateFileSize(file, maxAttachSize);
        if (null != fileSizeError) {
            return fileSizeError;
        }

        return null;
    }


    private void emailValidations(HttpServletRequest request, ActionErrors errors, DefaultForm form) {
        String mailAccountId = (String) form.getDto("mailAccountId");
        if (GenericValidator.isBlankOrNull(mailAccountId)) {
            errors.add("EmailValidation", new ActionError("errors.required",
                    JSPHelper.getMessage(request, "Mail.from")));
        }

        String to = (String) form.getDto("to");
        if (GenericValidator.isBlankOrNull(to)) {
            errors.add("To_Required", new ActionError("errors.required",
                    JSPHelper.getMessage(request, "Mail.to")));
        }

        String subject = (String) form.getDto("mailSubject");
        if (GenericValidator.isBlankOrNull(subject)) {
            errors.add("subject_required", new ActionError("errors.required",
                    JSPHelper.getMessage(request, "Mail.subject")));
        }

        if ("true".equals(form.getDto("isTokenFieldRecipients"))) {
            validateTokenFieldRecipients(errors, form, request);
        } else {
            validatePlainRecipients(errors, form);
        }

        ActionError attachmentsValidation = validateAttachments(request, errors, form);
        if (null != attachmentsValidation) {
            errors.add("attachmentsValidation", attachmentsValidation);
            errors.add("AttachmentsNewError", new ActionError("Webmail.Attach.attachAnew"));
        }

        //validate temporal image store, only if html editor is enabled
        if ("true".equals(form.getDto("useHtmlEditor"))) {
            ActionError imageStoreError = com.piramide.elwis.web.webmail.el.Functions.emailBodyImageStoreForeignKeyValidation(form.getDto("body").toString());
            if (imageStoreError != null) {
                errors.add("imageError", imageStoreError);
            }
        }

        if (errors.isEmpty()) {
            User user = RequestUtils.getUser(request);
            DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
            form.setDto("userTimeZone", dateTimeZone);

            settingUpComposeEmailOptions(form, request);
        }
    }

    private void validatePlainRecipients(ActionErrors errors, DefaultForm form) {
        List<ActionError> emailFormatErrors = new ArrayList<ActionError>();
        emailFormatErrors.addAll(validateEmailFormat((String) form.getDto("to")));
        emailFormatErrors.addAll(validateEmailFormat((String) form.getDto("cc")));
        emailFormatErrors.addAll(validateEmailFormat((String) form.getDto("bcc")));
        if (!emailFormatErrors.isEmpty()) {
            addError(emailFormatErrors, errors);
        }

        if (emailFormatErrors.isEmpty()) {
            List<ActionError> emailErrors = new ArrayList<ActionError>();
            emailErrors.addAll(validateEmails((String) form.getDto("to")));
            emailErrors.addAll(validateEmails((String) form.getDto("cc")));
            emailErrors.addAll(validateEmails((String) form.getDto("bcc")));
            if (!emailErrors.isEmpty()) {
                addError(emailErrors, errors);
            }
        }

        List<ActionError> duplicatedEmailError = validateDuplicatedEmails(form);
        if (!duplicatedEmailError.isEmpty()) {
            addError(duplicatedEmailError, errors);
        }

        if (errors.isEmpty()) {
            buildRecipienStructure(form);
        }
    }

    private void validateTokenFieldRecipients(ActionErrors errors, DefaultForm form, HttpServletRequest request) {
        TokenFieldEmailRecipientHelper tokenFieldHelper = new TokenFieldEmailRecipientHelper(form);

        List<ActionError> emailErrors = tokenFieldHelper.validateEmails();
        if (!emailErrors.isEmpty()) {
            addError(emailErrors, errors);
        }

        List<ActionError> duplicatedErrors = tokenFieldHelper.validateDuplicatedEmails();
        if (!duplicatedErrors.isEmpty()) {
            addError(duplicatedErrors, errors);
        }

        if (errors.isEmpty()) {
            tokenFieldHelper.buildRecipientStructure(form);
        }

        tokenFieldHelper.rewriteTokenFieldRecipients(request);
    }

    private List<ActionError> validateDuplicatedEmails(DefaultForm form) {
        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
        return emailRecipientHelper.
                validateDuplicatedEmails((String) form.getDto("to"), (String) form.getDto("cc"), (String) form.getDto("bcc"));
    }

    private void settingUpComposeEmailOptions(DefaultForm form, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        form.setDto("userMailId", userId);
        form.setDto("companyId", companyId);
        form.setDto("personalName", getAddressName(new Integer(form.getDto("employeeId").toString()), request));
        form.setDto("note", form.getDto("mailSubject"));
    }

    private String getAddressName(Integer addressId, HttpServletRequest request) {
        String addressName = "";
        LightlyAddressCmd addressCmd = new LightlyAddressCmd();
        addressCmd.putParam("addressId", addressId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(addressCmd, request);
            addressName = resultDTO.get("addressName").toString();
        } catch (AppLevelException e) {
            log.debug("-> Execute " + LightlyAddressCmd.class.getName() + " FAIL", e);
        }
        return addressName;
    }

    private void settingUpEmailValues(HttpServletRequest request, DefaultForm form) {

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        rewriteHiddenElements(form);
        String mailAccountId = (String) form.getDto("mailAccountId");

        if (GenericValidator.isBlankOrNull(mailAccountId)) {
            emailHelper.setDefaultEmailAccount(userId, companyId, request, form);
            if (null != form.getDto("mailAccountId")) {
                mailAccountId = form.getDto("mailAccountId").toString();
            }
        }

        if (!GenericValidator.isBlankOrNull(mailAccountId)) {
            emailHelper.setDefaultSignature(Integer.valueOf(mailAccountId), userId, form, request);
        }

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

    private void rewriteHiddenElements(DefaultForm form) {
        AttachFormHelper attachFormHelper = new AttachFormHelper();
        List<AttachDTO> attachments = attachFormHelper.rebuildAttachmentList(form);
        form.setDto("attachments", attachments);

        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
        form.setDto("dynamicHiddens", emailRecipientHelper.getDynamicHiddens(form));
    }

    private ActionErrors addError(List<ActionError> errorList, ActionErrors errors) {
        for (int i = 0; i < errorList.size(); i++) {
            errors.add("Error" + i, errorList.get(i));
        }

        return errors;
    }

    private ActionError validateAttachments(HttpServletRequest request, ActionErrors errors, DefaultForm form) {
        AttachFormHelper attachFormHelper = new AttachFormHelper();
        ActionError actionError = attachFormHelper.validateAttachments(form, request);
        if (null != actionError) {
            form.setDto("attachmentCounter", "");
            form.setDto("messageCounter", "");
            return actionError;
        }


        if (!errors.isEmpty()) {
            form.setDto("attachmentCounter", "");
            form.setDto("messageCounter", "");
            return actionError;
        }


        List<ArrayByteWrapper> newAttachments = attachFormHelper.buildNewAttachmentStructure();
        List<AttachDTO> oldAttachments = attachFormHelper.buildOldAttachmentStructure(form);
        form.setDto("newAttachments", newAttachments);
        form.setDto("oldAttachments", oldAttachments);

        return null;
    }

    private void buildRecipienStructure(DefaultForm form) {
        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();

        Map recipients = new HashMap();
        String to = (String) form.getDto("to");
        String cc = (String) form.getDto("cc");
        String bcc = (String) form.getDto("bcc");

        List<Map> toRecipient = emailRecipientHelper.buildRecipienStructure(form, to);

        List<Map> ccRecipient = emailRecipientHelper.buildRecipienStructure(form, cc);

        List<Map> bccRecipient = emailRecipientHelper.buildRecipienStructure(form, bcc);

        if (!toRecipient.isEmpty()) {
            recipients.put("to", toRecipient);
        }
        if (!ccRecipient.isEmpty()) {
            recipients.put("cc", ccRecipient);
        }
        if (!bccRecipient.isEmpty()) {
            recipients.put("bcc", bccRecipient);
        }

        form.setDto("recipients", recipients);
    }
}
