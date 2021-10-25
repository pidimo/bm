package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.cmd.campaignmanager.ReadAllCampaignTemplateFreeTextCmd;
import com.piramide.elwis.cmd.campaignmanager.util.HtmlTemplateToVelocity;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.HTMLEntityDecoder;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.EmailValidator;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.el.Functions;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import com.piramide.elwis.web.webmail.util.AttachFormHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: EmailSendForm.java 10519 2015-02-27 19:40:12Z miguel $
 */
public class EmailSendForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate EmailSendForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        //attach validation
        String listIdAttach = attachValidateAndGetIds(errors, request);
        setDto("listIdAttach", listIdAttach);

        //telecom types in template validation
        if (errors.isEmpty()) {
            errors = validateTelecomTypesInHtmlTemplates(mapping, request);
        }

        //process link attachs
        List<ArrayByteWrapper> linkAttachWrapperList = validateLinkAttachments(request, errors);
        if (!linkAttachWrapperList.isEmpty()) {
            setDto("newAttachments", linkAttachWrapperList);
        }

        //validate background notification email
        setDto("sendInBackground", "false");
        if (isSendInBackground()) {
            validateBackgroundNotificationEmail(request, errors);
            setDto("sendInBackground", "true");
        }

        boolean isCreateCommunication = false;
        if ("true".equals(getDto("createComm"))) {
            setDto("createComm", Boolean.TRUE);
            isCreateCommunication = true;
        } else {
            setDto("createComm", Boolean.FALSE);
        }

        //validate generation create communications, always the end
        if (isCreateCommunication) {
            //email account validation
            User user = RequestUtils.getUser(request);
            Integer userId = new Integer(user.getValue("userId").toString());
            MailFormHelper emailHelper = new MailFormHelper();
            Integer emailUserChecker = emailHelper.isValidEmailUser(userId, request);
            if (-1 == emailUserChecker) {
                errors.add("account", new ActionError("Campaign.activity.emailGenerate.accountNotFound"));
            }

            if (errors.isEmpty() && GenericValidator.isBlankOrNull(getDto("createAgain").toString())) {

                Integer templateId = new Integer(getDto("templateId").toString());
                if ("true".equals(getDto("isCampaignLight"))) {
                    errors = com.piramide.elwis.web.campaignmanager.el.Functions.generationComunicationsAlreadyCreated(templateId, request);
                } else {
                    errors = com.piramide.elwis.web.campaignmanager.el.Functions.generationComunicationsAlreadyCreated(new Integer(getDto("activityId").toString()), templateId, request);
                }

                if (!errors.isEmpty()) {
                    setDto("createAgain", "true");
                }
            } else {
                setDto("createAgain", "");
            }
        }
        return errors;
    }

    /**
     * validate email send attach, return attach ids as String
     *
     * @param errors
     * @param request
     * @return ids of attach as String
     */
    private String attachValidateAndGetIds(ActionErrors errors, HttpServletRequest request) {
        String ids = "";
        //only if is mail type
        if (!GenericValidator.isBlankOrNull((String) getDto("attachValues"))) {
            String data = getDto("attachValues").toString();

            boolean firstError = true;
            List result = new ArrayList();
            String[] arrayTupla = data.split(CampaignConstants.KEY_SEPARATOR);
            for (int i = 0; i < arrayTupla.length; i++) {
                String s = arrayTupla[i];
                if (!"".equals(s.trim())) {
                    String[] tupla = s.split(CampaignConstants.KEY_SEPARATOR_VALUE);
                    ActionErrors attErrors = new ActionErrors();

                    if (!"retry".equals(getDto("opSend"))) {
                        attErrors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_ATTACH, "attachid",
                                tupla[1], attErrors, new ActionError("Campaign.attach.someNotfound"));
                    }

                    if (attErrors.isEmpty()) {
                        result.add(new LabelValueBean(tupla[0], tupla[1]));

                        //compose ids
                        if (ids.length() > 0) {
                            ids = ids + "," + tupla[1];
                        } else {
                            ids = tupla[1];
                        }
                    } else if (firstError) {
                        errors.add(attErrors);
                        firstError = false;
                    }
                }
            }
            request.setAttribute("previousAttachList", result);
        }
        return ids;
    }

    /**
     * Validate variables of telecom types of the template html, if this not exist in the company, add error
     *
     * @param mapping
     * @param request
     * @return errors
     */
    private ActionErrors validateTelecomTypesInHtmlTemplates(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        //validate template text
        ReadAllCampaignTemplateFreeTextCmd templateFreeTextCmd = new ReadAllCampaignTemplateFreeTextCmd();
        templateFreeTextCmd.putParam("templateId", getDto("templateId"));

        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(templateFreeTextCmd, request);
        } catch (AppLevelException e) {
            log.warn("Error in read templates...", e);
            errors.add("serverError", new ActionError("msg.ServerError"));
            return errors;
        }

        if (resultDTO.isFailure()) {
            errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            return errors;
        } else {
            List templateTextList = (List) resultDTO.get("templateTextList");
            List<LabelValueBean> telecomTypeList = Functions.getTelecomTypes(request);

            for (Iterator iterator = templateTextList.iterator(); iterator.hasNext();) {
                Map templateTextMap = (Map) iterator.next();
                ArrayByteWrapper byteWrapper = (ArrayByteWrapper) templateTextMap.get("byteWrapper");

                Map<Integer, List<String>> templateTelecomTypeIdsMap = HtmlTemplateToVelocity.findTelecomTypeIdsVariableInTemplate(new StringBuilder(new String(byteWrapper.getFileData())));

                for (Integer telecomTypeId : templateTelecomTypeIdsMap.keySet()) {
                    if (!existTelecomTypeIdInCompany(telecomTypeId, telecomTypeList)) {
                        String templateName = templateTextMap.get("templateName").toString();
                        String languageName = templateTextMap.get("languageName").toString();

                        for (String fieldLabel : templateTelecomTypeIdsMap.get(telecomTypeId)) {
                            fieldLabel = HTMLEntityDecoder.decode(fieldLabel); //decode html entities in variable label
                            errors.add("fieldError", new ActionError("Campaign.activity.emailSend.template.fieldError", fieldLabel, templateName, languageName));
                        }
                    }
                }
            }
        }

        return errors;
    }

    /**
     * verify if telecomTypeId of html template exist in company telecom types
     *
     * @param telecomTypeId   telecomtype id of html template field
     * @param telecomTypeList company telecom types as LabelValueBean, value is telecomtype id
     * @return true or false
     */
    private boolean existTelecomTypeIdInCompany(Integer telecomTypeId, List<LabelValueBean> telecomTypeList) {
        for (LabelValueBean labelValueBean : telecomTypeList) {
            if (telecomTypeId.toString().equals(labelValueBean.getValue())) {
                return true;
            }
        }
        return false;
    }

    private List<ArrayByteWrapper> validateLinkAttachments(HttpServletRequest request, ActionErrors errors) {

        List<ArrayByteWrapper> linkAttachsWrapperList = new ArrayList<ArrayByteWrapper>();

        AttachFormHelper attachFormHelper = new AttachFormHelper();
        ActionError actionError = attachFormHelper.validateAttachments(this, request);
        if (null != actionError) {
            errors.add("attachs", actionError);
            errors.add("AttachmentsNewError", new ActionError("Webmail.Attach.attachAnew"));
        } else {
            linkAttachsWrapperList = attachFormHelper.buildNewAttachmentStructure();
        }
        return linkAttachsWrapperList;
    }

    private void validateBackgroundNotificationEmail(HttpServletRequest request, ActionErrors errors) {
        Object notificationMail = getDto("notificationMail");
        if (notificationMail == null || GenericValidator.isBlankOrNull(notificationMail.toString())) {
            errors.add("required", new ActionError("errors.required", JSPHelper.getMessage(request, "Campaign.generate.background.notificationMail")));
        } else {
            if (!EmailValidator.i.isValid(notificationMail)) {
                errors.add("error", new ActionError("errors.email", notificationMail));
            }
        }
    }

    private boolean isSendInBackground() {
        Integer limitSendSize = new Integer(ConfigurationFactory.getValue("elwis.campaignMail.background.send.size.limit"));
        Integer limitRecipients = new Integer(ConfigurationFactory.getValue("elwis.campaignMail.background.send.recipient.limit"));
        Integer sendSize = 0;
        Integer recipients = 0;

        if (getDto("sendEmailSize") != null && !GenericValidator.isBlankOrNull(getDto("sendEmailSize").toString())) {
            sendSize = new Integer(getDto("sendEmailSize").toString());
        }

        if (getDto("totalSendRecipients") != null && !GenericValidator.isBlankOrNull(getDto("totalSendRecipients").toString())) {
            recipients = new Integer(getDto("totalSendRecipients").toString());
        }

        return (sendSize > limitSendSize || recipients > limitRecipients);
    }

}
