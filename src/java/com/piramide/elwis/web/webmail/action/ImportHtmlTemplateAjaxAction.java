package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.catalogmanager.ReadAllTemplateTextCmd;
import com.piramide.elwis.cmd.webmailmanager.ImportHtmlTemplateCmd;
import com.piramide.elwis.cmd.webmailmanager.ReadTelecomCmd;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.webmail.util.EmailRecipientHelper;
import com.piramide.elwis.web.webmail.util.TokenFieldEmailRecipientHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.apache.struts.util.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * AJAX action to import HTML template
 *
 * @author Miky
 * @version $Id: ImportHtmlTemplateAjaxAction.java 2009-05-14 06:51:22 PM $
 */
public class ImportHtmlTemplateAjaxAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ImportHtmlTemplateAjaxAction................" + request.getParameterMap());
        ActionForward forward = null;

        DefaultForm defaultForm = (DefaultForm) form;
        Integer templateId = new Integer(defaultForm.getDto("templateId").toString());

        if (isValidTemplate(templateId, request, response)) {
            if ("onlyTemplate".equals(defaultForm.getDto("op"))) {
                processOnlyTemplate(defaultForm, templateId, response, request);
            } else {
                processRecipients(defaultForm, templateId, response, request);
            }
        }

        return forward;
    }

    private boolean isValidTemplate(Integer templateId, HttpServletRequest request, HttpServletResponse response) {

        boolean existTemplate = existTemplate(templateId);
        if (!existTemplate) {
            String errorMessage = JSPHelper.getMessage(request, "template.NotFound");
            setErrorInResponse(errorMessage, response);
        }
        return existTemplate;
    }

    /**
     * Process all recipients selected to send the mail
     *
     * @param defaultForm
     * @param templateId
     * @param response
     * @param request
     * @throws Exception
     */
    private void processRecipients(DefaultForm defaultForm, Integer templateId, HttpServletResponse response, HttpServletRequest request) throws Exception {

        if (isValidFormRecipients(defaultForm)) {
            Map recipientDataMap = getValidRecipientData(defaultForm);
            if (!recipientDataMap.isEmpty()) {
                //load document with recipient data
                Integer addressId = new Integer(recipientDataMap.get("addressId").toString());
                Integer contactPersonId = recipientDataMap.containsKey("contactPersonId") ? new Integer(recipientDataMap.get("contactPersonId").toString()) : null;
                loadTemplate(templateId, addressId, contactPersonId, defaultForm, response, request);

            } else {
                //get by language if is necessary
                List<Map> templateTextList = new ArrayList<Map>();
                ReadAllTemplateTextCmd readAllTemplateTextCmd = new ReadAllTemplateTextCmd();
                readAllTemplateTextCmd.putParam("templateId", templateId);
                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(readAllTemplateTextCmd, null);
                    if (!resultDTO.isFailure()) {
                        templateTextList = (List) resultDTO.get("allTemplateTexts");
                    }
                } catch (AppLevelException e) {
                    log.error("-> Execute cmd FAIL", e);
                }

                if (templateTextList.size() == 1) {
                    Map templateTextMap = templateTextList.get(0);
                    Integer languageId = new Integer(templateTextMap.get("languageId").toString());
                    templateId = new Integer(templateTextMap.get("templateId").toString());
                    loadTemplate(templateId, languageId, defaultForm, response, request);

                } else {
                    setLanguageListInResponse(templateTextList, templateId, response);
                }
            }
        } else {
            setError400(response);
        }
    }

    /**
     * Porcess only one template, templateId and languageId be sent in form
     *
     * @param defaultForm
     * @param templateId
     * @param response
     * @param request
     * @throws Exception
     */
    private void processOnlyTemplate(DefaultForm defaultForm, Integer templateId, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Integer languageId = new Integer(defaultForm.getDto("languageId").toString());
        loadTemplate(templateId, languageId, defaultForm, response, request);
    }

    private void loadTemplate(Integer templateId, Integer addressId, Integer contactPersonId, DefaultForm defaultForm, HttpServletResponse response, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = new Integer(user.getValue("companyId").toString());
        Integer userAddressId = new Integer(user.getValue("userAddressId").toString());

        String msgUnknown = ResponseUtils.filter(JSPHelper.getMessage(request, "Mail.importTemplate.label.unknown"));

        ImportHtmlTemplateCmd importCmd = new ImportHtmlTemplateCmd();
        importCmd.putParam("companyId", companyId);
        importCmd.putParam("userAddressId", userAddressId);
        importCmd.putParam("templateId", templateId);
        importCmd.putParam("addressId", addressId);
        if (contactPersonId != null) {
            importCmd.putParam("contactPersonId", contactPersonId);
        }
        importCmd.putParam("msgUnknown", msgUnknown);
        importCmd.putParam("requestLocale", request.getLocale().toString());

        //this when is from communications
        if (!GenericValidator.isBlankOrNull((String) defaultForm.getDto("employeeId"))) {
            importCmd.putParam("employeeId", defaultForm.getDto("employeeId"));
        }

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(importCmd, null);
            if (resultDTO.isFailure()) {
                setError500(response);
            } else {
                setDataInResponse(response, "text/html", Functions.updateImageStoreDownloadUrlForImgTags(resultDTO.get("importedHtml").toString(), response, request));
            }
        } catch (AppLevelException e) {
            log.error("-> Execute cmd FAIL", e);
        } catch (IOException e) {
            log.debug("Error in set the error 404");
        }
    }

    private void loadTemplate(Integer templateId, Integer languageId, DefaultForm defaultForm, HttpServletResponse response, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = new Integer(user.getValue("companyId").toString());
        Integer userAddressId = new Integer(user.getValue("userAddressId").toString());

        String msgUnknown = ResponseUtils.filter(JSPHelper.getMessage(request, "Mail.importTemplate.label.unknown"));

        ImportHtmlTemplateCmd importCmd = new ImportHtmlTemplateCmd();
        importCmd.putParam("op", "onlyTemplate");
        importCmd.putParam("companyId", companyId);
        importCmd.putParam("userAddressId", userAddressId);
        importCmd.putParam("templateId", templateId);
        importCmd.putParam("languageId", languageId);
        importCmd.putParam("msgUnknown", msgUnknown);
        importCmd.putParam("requestLocale", request.getLocale().toString());

        //this when is from communications
        if (!GenericValidator.isBlankOrNull((String) defaultForm.getDto("employeeId"))) {
            importCmd.putParam("employeeId", defaultForm.getDto("employeeId"));
        }


        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(importCmd, null);
            if (resultDTO.isFailure()) {
                setError500(response);
            } else {
                setDataInResponse(response, "text/html", Functions.updateImageStoreDownloadUrlForImgTags(resultDTO.get("importedHtml").toString(), response, request));
            }
        } catch (AppLevelException e) {
            log.error("-> Execute cmd FAIL", e);
        } catch (Exception e) {
            log.debug("Error in set the error 404");
        }
    }

    private void setLanguageListInResponse(List<Map> templateTextList, Integer templateId, HttpServletResponse response) {
        StringBuffer xmlResponse = new StringBuffer();
        xmlResponse.append("<?xml version=\"1.0\" ?>\n");
        xmlResponse.append("<languageTemplates>");
        xmlResponse.append("<template id=\"").append(templateId).append("\">\n");
        for (Map map : templateTextList) {
            xmlResponse.append("<language id=\"").append(map.get("languageId")).append("\">\n")
                    .append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(map.get("languageName").toString()))
                    .append("</language>\n");
        }
        xmlResponse.append("</template>");
        xmlResponse.append("</languageTemplates>");

        setDataInResponse(response, "text/xml", xmlResponse.toString());
    }

    private void setErrorInResponse(String errorMessage, HttpServletResponse response) {
        StringBuffer xmlResponse = new StringBuffer();
        xmlResponse.append("<?xml version=\"1.0\" ?>\n");
        xmlResponse.append("<importErrors>");
        xmlResponse.append("<message>")
                .append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(errorMessage));
        xmlResponse.append("</message>");
        xmlResponse.append("</importErrors>");

        setDataInResponse(response, "text/xml", xmlResponse.toString());
    }

    private void setDataInResponse(HttpServletResponse response, String contentType, String data) {
        log.debug("REsponse Value:\n" + data);

        response.setContentType(contentType);
        try {
            PrintWriter write = response.getWriter();
            write.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setError400(HttpServletResponse response) throws IOException {
        log.debug("SET ERROR 400...");
        response.setContentType("text/html");
        response.sendError(400);
    }

    private void setError500(HttpServletResponse response) throws IOException {
        log.debug("SET ERROR 500...");
        response.setContentType("text/html");
        response.sendError(500);
    }

    private Map getValidRecipientData(DefaultForm defaultForm) {
        Map recipientDataMap = new HashMap();

        List<Map> allRecipients = buildRecipienStructure(defaultForm);
        log.debug("All recipients processed....." + allRecipients);

        for (Map map : allRecipients) {
            String email = (String) map.get("email");
            List addressIdList = (List) map.get("addressId");
            List contactPersonOfIdList = (List) map.get("contactPersonOf");

            if (!addressIdList.isEmpty()) {
                String addressId = addressIdList.get(0).toString();
                if (!contactPersonOfIdList.isEmpty()) {
                    for (Iterator iterator = contactPersonOfIdList.iterator(); iterator.hasNext();) {
                        String contactPersonOf = iterator.next().toString();
                        if (existContactPerson(contactPersonOf, addressId)) {
                            recipientDataMap.put("addressId", contactPersonOf);
                            recipientDataMap.put("contactPersonId", addressId);
                            return recipientDataMap;
                        }
                    }
                } else {
                    if (existAddress(addressId)) {
                        recipientDataMap.put("addressId", addressId);
                        return recipientDataMap;
                    }
                }

            } else {
                Integer companyId = new Integer(defaultForm.getDto("companyId").toString());
                recipientDataMap = getValidRecipientDataByEmail(email, companyId);
                if (!recipientDataMap.isEmpty()) {
                    return recipientDataMap;
                }
            }
        }
        return recipientDataMap;
    }

    /**
     * read an address or contact person by email telecom number
     *
     * @param email
     * @param companyId
     * @return Map
     */
    private Map getValidRecipientDataByEmail(String email, Integer companyId) {
        Map recipientDataMap = new HashMap();

        //all address are requested for who the email
        ReadTelecomCmd readTelecomCmd = new ReadTelecomCmd();
        readTelecomCmd.putParam("email", email);
        readTelecomCmd.putParam("companyId", companyId);
        readTelecomCmd.putParam("op", "readChiefList");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(readTelecomCmd, null);
            if (!resultDTO.isFailure()) {
                List<Map> onlyAddressIdsList = (List) resultDTO.get("myPersonalList");
                List<Map> withContactPersonIdsList = (List) resultDTO.get("myChiefList");

                if (onlyAddressIdsList.size() == 1 && withContactPersonIdsList.isEmpty()) {
                    Map addressMap = onlyAddressIdsList.get(0);
                    recipientDataMap.put("addressId", addressMap.get("addressId"));

                } else if (onlyAddressIdsList.isEmpty() && withContactPersonIdsList.size() == 1) {
                    Map withContactPersonMap = withContactPersonIdsList.get(0);
                    recipientDataMap.put("addressId", withContactPersonMap.get("addressId"));
                    recipientDataMap.put("contactPersonId", withContactPersonMap.get("contactPersonId"));
                }
            }
        } catch (AppLevelException e) {
            log.error("-> Execute cmd FAIL", e);
        }
        return recipientDataMap;
    }

    private boolean existTemplate(Integer templateId) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CatalogConstants.TABLE_TEMPLATE, "templateid", templateId, errors, new ActionError("template.NotFound"));
        return errors.isEmpty();
    }

    private boolean existAddress(String addressId) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(ContactConstants.TABLE_ADDRESS, "addressid", addressId, errors, new ActionError("Address.NotFound"));
        return errors.isEmpty();
    }

    private boolean existContactPerson(String addressId, String contactPersonId) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(ContactConstants.TABLE_CONTACTPERSON, "addressid", "contactpersonid",
                addressId, contactPersonId, errors, new ActionError("Address.NotFound"));
        return errors.isEmpty();
    }

    /**
     * Build structure of recipients from 'to', 'cc', 'bcc', and hiddens of form (this is same as email send form validation)
     *
     * @param defaultForm
     * @return List
     */
    private List<Map> buildRecipienStructure(DefaultForm defaultForm) {
        List<Map> allRecipients = new LinkedList<Map>();

        if ("true".equals(defaultForm.getDto("isTokenFieldRecipients"))) {
            allRecipients = buildStructureFromTokenFieldRecipient(defaultForm);
        } else {
            allRecipients = buildStructureFromPlainRecipient(defaultForm);
        }

        return allRecipients;
    }

    private List<Map> buildStructureFromTokenFieldRecipient(DefaultForm defaultForm) {

        List<Map> allRecipients = new LinkedList<Map>();

        TokenFieldEmailRecipientHelper tokenFieldHelper = new TokenFieldEmailRecipientHelper(defaultForm);

        allRecipients.addAll(tokenFieldHelper.buildRecipientStructureTO());
        allRecipients.addAll(tokenFieldHelper.buildRecipientStructureCC());
        allRecipients.addAll(tokenFieldHelper.buildRecipientStructureBCC());

        return allRecipients;
    }

    private List<Map> buildStructureFromPlainRecipient(DefaultForm defaultForm) {
        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();

        List<Map> allRecipients = new LinkedList<Map>();

        String to = (String) defaultForm.getDto("to");
        String cc = (String) defaultForm.getDto("cc");
        String bcc = (String) defaultForm.getDto("bcc");

        List<Map> toRecipient = emailRecipientHelper.buildRecipienStructure(defaultForm, to);

        List<Map> ccRecipient = emailRecipientHelper.buildRecipienStructure(defaultForm, cc);

        List<Map> bccRecipient = emailRecipientHelper.buildRecipienStructure(defaultForm, bcc);

        if (!toRecipient.isEmpty()) {
            allRecipients.addAll(toRecipient);
        }
        if (!ccRecipient.isEmpty()) {
            allRecipients.addAll(ccRecipient);
        }
        if (!bccRecipient.isEmpty()) {
            allRecipients.addAll(bccRecipient);
        }

        return allRecipients;
    }

    private boolean isValidFormRecipients(DefaultForm defaultForm) {
        boolean isValid = true;

        if ("true".equals(defaultForm.getDto("isTokenFieldRecipients"))) {
            isValid = isValidTokenFieldFormRecipients(defaultForm);
        } else {
            isValid = isValidFormPlainRecipients(defaultForm);
        }

        return isValid;
    }

    private boolean isValidTokenFieldFormRecipients(DefaultForm defaultForm) {
        boolean isValid = true;

        TokenFieldEmailRecipientHelper tokenFieldHelper = new TokenFieldEmailRecipientHelper(defaultForm);

        if (!tokenFieldHelper.existTokenFieldRecipients()) {
            isValid = false;
        }

        if (isValid) {
            List<ActionError> emailErrors = tokenFieldHelper.validateEmails();
            if (!emailErrors.isEmpty()) {
                isValid = false;
            }
        }

        return isValid;
    }


    private boolean isValidFormPlainRecipients(DefaultForm defaultForm) {
        boolean isValid = true;

        String to = (String) defaultForm.getDto("to");
        String cc = (String) defaultForm.getDto("cc");
        String bcc = (String) defaultForm.getDto("bcc");

        if (GenericValidator.isBlankOrNull(to) && GenericValidator.isBlankOrNull(cc) && GenericValidator.isBlankOrNull(bcc)) {
            isValid = false;
        }

        if (isValid) {
            List<ActionError> emailFormatErrors = new ArrayList<ActionError>();
            emailFormatErrors.addAll(validateEmailFormat(to));
            emailFormatErrors.addAll(validateEmailFormat(cc));
            emailFormatErrors.addAll(validateEmailFormat(bcc));
            if (!emailFormatErrors.isEmpty()) {
                isValid = false;
            }

            if (emailFormatErrors.isEmpty()) {
                List<ActionError> emailErrors = new ArrayList<ActionError>();
                emailErrors.addAll(validateEmails(to));
                emailErrors.addAll(validateEmails(cc));
                emailErrors.addAll(validateEmails(bcc));
                if (!emailErrors.isEmpty()) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }


    private List<ActionError> validateEmailFormat(String recipient) {
        if (GenericValidator.isBlankOrNull(recipient)) {
            return new ArrayList<ActionError>();
        }

        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
        return emailRecipientHelper.validateEmailFormat(recipient);
    }

    private List<ActionError> validateEmails(String recipient) {
        if (GenericValidator.isBlankOrNull(recipient)) {
            return new ArrayList<ActionError>();
        }

        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
        return emailRecipientHelper.validateEmails(recipient);
    }

}
