package com.piramide.elwis.web.financemanager.util;

import com.piramide.elwis.cmd.admin.CompanyReadLightCmd;
import com.piramide.elwis.cmd.campaignmanager.DownloadDocumentCmd;
import com.piramide.elwis.cmd.contactmanager.TelecomReadCmd;
import com.piramide.elwis.cmd.financemanager.InvoiceReadCmd;
import com.piramide.elwis.cmd.webmailmanager.ComposeMailCmd;
import com.piramide.elwis.cmd.webmailmanager.ImportHtmlTemplateCmd;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.delegate.EmailServiceDelegate;
import com.piramide.elwis.web.webmail.el.Functions;
import com.piramide.elwis.web.webmail.util.EmailRecipientHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.ResponseUtils;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Util to create draft email from invoice, with invoice document as attach
 * @author Miguel A. Rojas Cardenas
 * @version 4.7
 */
public class InvoiceSendViaEmailUtil {
    private Log log = LogFactory.getLog(this.getClass());
    private ActionErrors errors;

    public InvoiceSendViaEmailUtil() {
        errors = new ActionErrors();
    }

    public Integer createDraftEmail(Integer userMailId, Integer companyId, Integer invoiceId, Integer telecomId, HttpServletRequest request) {
        Integer mailId = null;
        errors = new ActionErrors();

        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        Integer mailAccountId = Functions.getDefaultMailAccountId(request);

        if (mailAccountId != null) {

            Integer documentId = null;
            String invoiceNumber = null;
            Integer addressId = null;
            Integer contactPersonId = null;

            InvoiceReadCmd invoiceReadCmd = new InvoiceReadCmd();
            invoiceReadCmd.putParam("invoiceId", invoiceId);
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceReadCmd, null);
                if (!resultDTO.isFailure()) {
                    documentId = (Integer) resultDTO.get("documentId");
                    addressId = (Integer) resultDTO.get("addressId");
                    contactPersonId = (Integer) resultDTO.get("contactPersonId");
                    invoiceNumber = (String) resultDTO.get("number");
                }
            } catch (AppLevelException e) {
                log.debug("Error in read Invoice with..." + invoiceId, e);
            }

            ArrayByteWrapper invoiceDocument = readDocument(documentId);
            if (invoiceDocument != null) {
                invoiceDocument.setFileName(composeFileName(invoiceNumber, request));
                List<ArrayByteWrapper> attachments = new ArrayList<ArrayByteWrapper>();
                attachments.add(invoiceDocument);

                //to recipients
                Map<String, List<Map>> recipients = new HashMap<String, List<Map>>();
                if (telecomId != null) {
                    recipients = composeRecipients(telecomId);
                }

                ComposeMailCmd composeMailCmd = new ComposeMailCmd();
                composeMailCmd.putParam("isDraftEmail", "true");
                composeMailCmd.putParam("isDraftTemp", Boolean.TRUE);
                composeMailCmd.putParam("companyId", companyId);
                composeMailCmd.putParam("userMailId", userMailId);
                composeMailCmd.putParam("body", composeBody(addressId, contactPersonId, request));
                composeMailCmd.putParam("useHtmlEditor", "true");
                composeMailCmd.putParam("userTimeZone", dateTimeZone);
                composeMailCmd.putParam("mailAccountId", mailAccountId);
                composeMailCmd.putParam("mailPriority", Integer.valueOf(WebMailConstants.MAIL_PRIORITY_DEFAULT));
                composeMailCmd.putParam("mailSubject", composeSubject(addressId, contactPersonId, invoiceNumber, request));

                composeMailCmd.putParam("recipients", recipients); //Map<String,List<Map>>
                composeMailCmd.putParam("oldAttachments", new ArrayList()); //List<AttachDTO>
                composeMailCmd.putParam("newAttachments", attachments); //List<ArrayByteWrapper>

                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(composeMailCmd, null);
                    if (!resultDTO.isFailure()) {
                        mailId = (Integer) resultDTO.get("mailId");
                    }
                } catch (AppLevelException e) {
                    log.debug("Error in create draft email...", e);
                }
            } else {
                errors.add("error", new ActionError("Invoice.sendViaEmail.error.emptyDocument"));
            }
        } else {
            errors.add("error", new ActionError("Invoice.sendViaEmail.error.mailAccount"));
        }

        return mailId;
    }

    /**
     * Create emails and send for every invoice
     * @param invoiceIdList invoice ids
     * @param mailAccountId mail account
     * @param telecomTypeId
     * @param request
     * @return summary map
     */
    public Map sendEmailsForInvoices(List<Integer> invoiceIdList, Integer mailAccountId, Integer telecomTypeId, HttpServletRequest request) {
        Map summaryMap = new HashMap();
        int total = 0;
        int success = 0;
        int fail = 0;

        ActionErrors sendErrors = new ActionErrors();

        if (invoiceIdList != null) {
            total = invoiceIdList.size();
            for (Integer invoiceId : invoiceIdList) {
                ActionErrors invoiceErrors = createOutboxEmailForInvoice(invoiceId, mailAccountId, telecomTypeId, request);
                if (invoiceErrors.isEmpty()) {
                    success++;
                } else {
                    fail++;
                    sendErrors.add(invoiceErrors);
                }
            }
        }

        //process errors
        List<String> errorMessages = new ArrayList<String>();
        if (!sendErrors.isEmpty()) {

            Iterator iterator = sendErrors.get();
            while (iterator.hasNext()) {
                ActionError actionError = (ActionError) iterator.next();
                String message = JSPHelper.getMessage(request, actionError.getKey(), actionError.getValues());
                errorMessages.add(message);
            }
        }

        if (success > 0) {
            User user = RequestUtils.getUser(request);
            Integer userMailId = (Integer) user.getValue("userId");

            //send all email created in outbox
            EmailServiceDelegate.i.sentEmails(userMailId);
        }

        summaryMap.put("total", total);
        summaryMap.put("success", success);
        summaryMap.put("fail", fail);
        summaryMap.put("errorMessageList", errorMessages);

        return summaryMap;
    }

    public ActionErrors createOutboxEmailForInvoice(Integer invoiceId, Integer mailAccountId, Integer telecomTypeId, HttpServletRequest request) {
        Integer mailId = null;

        ActionErrors invoiceErrors = new ActionErrors();

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        Integer userMailId = (Integer) user.getValue("userId");

        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        if (mailAccountId != null) {

            Integer documentId = null;
            String invoiceNumber = null;
            Integer addressId = null;
            Integer contactPersonId = null;

            InvoiceReadCmd invoiceReadCmd = new InvoiceReadCmd();
            invoiceReadCmd.putParam("invoiceId", invoiceId);
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceReadCmd, null);
                if (!resultDTO.isFailure()) {
                    documentId = (Integer) resultDTO.get("documentId");
                    addressId = (Integer) resultDTO.get("addressId");
                    contactPersonId = (Integer) resultDTO.get("contactPersonId");
                    invoiceNumber = (String) resultDTO.get("number");
                }
            } catch (AppLevelException e) {
                log.debug("Error in read Invoice with..." + invoiceId, e);
            }

            ArrayByteWrapper invoiceDocument = readDocument(documentId);
            if (invoiceDocument != null) {

                TelecomDTO telecomDTO = com.piramide.elwis.web.contactmanager.el.Functions.findDefaultTelecomByTelecomType(addressId, contactPersonId, telecomTypeId);
                if (telecomDTO == null) {
                    invoiceErrors.add("withoutEmail", new ActionError("Invoice.sendViaEmail.error.withoutEmail", invoiceNumber));
                    return invoiceErrors;
                }

                invoiceDocument.setFileName(composeFileName(invoiceNumber, request));
                List<ArrayByteWrapper> attachments = new ArrayList<ArrayByteWrapper>();
                attachments.add(invoiceDocument);

                //to recipients
                Map<String, List<Map>> recipients = composeRecipients(telecomDTO);

                ComposeMailCmd composeMailCmd = new ComposeMailCmd();
                composeMailCmd.putParam("saveSendItem", "true");
                composeMailCmd.putParam("createOutCommunication", "true");
                composeMailCmd.putParam("companyId", companyId);
                composeMailCmd.putParam("userMailId", userMailId);
                composeMailCmd.putParam("body", composeBody(addressId, contactPersonId, request));
                composeMailCmd.putParam("useHtmlEditor", "true");
                composeMailCmd.putParam("userTimeZone", dateTimeZone);
                composeMailCmd.putParam("mailAccountId", mailAccountId);
                composeMailCmd.putParam("mailPriority", Integer.valueOf(WebMailConstants.MAIL_PRIORITY_DEFAULT));
                composeMailCmd.putParam("mailSubject", composeSubject(addressId, contactPersonId, invoiceNumber, request));

                composeMailCmd.putParam("recipients", recipients); //Map<String,List<Map>>
                composeMailCmd.putParam("oldAttachments", new ArrayList()); //List<AttachDTO>
                composeMailCmd.putParam("newAttachments", attachments); //List<ArrayByteWrapper>

                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(composeMailCmd, null);
                    if (!resultDTO.isFailure()) {
                        mailId = (Integer) resultDTO.get("mailId");
                    }

                    if (mailId == null) {
                        invoiceErrors.add("error", new ActionError("Invoice.sendViaEmail.error", invoiceNumber));
                    }
                } catch (AppLevelException e) {
                    log.debug("Error in create draft email...", e);
                }
            } else {
                invoiceErrors.add("error", new ActionError("Invoice.sendViaEmail.error.withoutDocument"));
            }
        } else {
            invoiceErrors.add("error", new ActionError("Invoice.sendViaEmail.error.mailAccount"));
        }

        return invoiceErrors;
    }


    /**
     * Read freetext document
     *
     * @param freeTextId
     * @return ArrayByteWrapper
     */
    private ArrayByteWrapper readDocument(Integer freeTextId) {
        ArrayByteWrapper textByteWrapper = null;

        if (freeTextId != null) {
            DownloadDocumentCmd downloadCmd = new DownloadDocumentCmd();
            downloadCmd.putParam("freeTextId", freeTextId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(downloadCmd, null);
                if (!resultDTO.isFailure()) {
                    textByteWrapper = (ArrayByteWrapper) resultDTO.get("freeText");
                }
            } catch (AppLevelException e) {
                log.debug("Error in read document...", e);
            }
        }
        return textByteWrapper;
    }

    private String composeFileName(String invoiceNumber, HttpServletRequest request) {
        String localizedFileName = JSPHelper.getMessage(request, "Invoice.file.name");
        StringBuffer fileName = new StringBuffer();
        fileName.append(localizedFileName).append("_").append(invoiceNumber).append(".pdf");
        return fileName.toString();
    }

    private String composeSubject(Integer addressId, Integer contactPersonId, String invoiceNumber, HttpServletRequest request) {
        Integer recipientAddressId = addressId;
        if (contactPersonId != null) {
            recipientAddressId = contactPersonId;
        }

        Map addressMap = com.piramide.elwis.web.contactmanager.el.Functions.getAddressMap(recipientAddressId);
        String languageIso = (String) addressMap.get("languageIso");

        String localizedName = null;
        if (languageIso != null) {
            Locale locale = new Locale(languageIso);
            localizedName = JSPHelper.getMessage(locale, "Invoice.sendViaEmail.subjectPrefix");
        }

        if (localizedName == null) {
            localizedName = JSPHelper.getMessage(request, "Invoice.sendViaEmail.subjectPrefix");
        }

        StringBuffer name = new StringBuffer();
        name.append(localizedName).append(" ").append(invoiceNumber);
        return name.toString();
    }

    private Map<String, List<Map>> composeRecipients(Integer telecomId) {
        TelecomDTO telecomDTO = findTelecom(telecomId);
        return composeRecipients(telecomDTO);
    }

    private Map<String, List<Map>> composeRecipients(TelecomDTO telecomDTO) {
        Map<String, List<Map>> recipients = new HashMap<String, List<Map>>();

        if (telecomDTO != null) {

            Integer recipientAddressId = telecomDTO.getAddressId();
            Integer organizationId = null;
            if (telecomDTO.getContactPersonId() != null) {
                recipientAddressId = telecomDTO.getContactPersonId();
                organizationId = telecomDTO.getAddressId();
            }

            String addressName = com.piramide.elwis.web.contactmanager.el.Functions.getAddressName(recipientAddressId);

            List<Integer> addressIdList = new ArrayList<Integer>();
            addressIdList.add(recipientAddressId);

            List<Integer> contactPersonOfList = new ArrayList<Integer>();
            if (organizationId != null) {
                contactPersonOfList.add(organizationId);
            }

            //build recipient structure to send the cmd
            EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
            Map recipientMap = emailRecipientHelper.buildMapRecipient(telecomDTO.getData(), addressName, addressIdList, contactPersonOfList);

            List<Map> emailList = new ArrayList<Map>();
            emailList.add(recipientMap);

            recipients.put("to", emailList);
        }

        return recipients;
    }

    private TelecomDTO findTelecom(Integer telecomId) {
        TelecomDTO telecomDTO = null;
        TelecomReadCmd telecomReadCmd = new TelecomReadCmd();
        telecomReadCmd.putParam("telecomId", telecomId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(telecomReadCmd, null);
            if (!resultDTO.isFailure() && resultDTO.containsKey("telecomDTO")) {
                telecomDTO = (TelecomDTO) resultDTO.get("telecomDTO");
            }
        } catch (AppLevelException e) {
            log.debug("Error in read telecom...", e);
        }
        return telecomDTO;
    }

    private String composeBody(Integer addressId, Integer contactPersonId, HttpServletRequest request) {
        String body = "";

        User user = RequestUtils.getUser(request);
        Integer companyId = Integer.valueOf(user.getValue(Constants.COMPANYID).toString());

        //get the default configured template for sen invoice via email
        CompanyReadLightCmd companyReadLightCmd = new CompanyReadLightCmd();
        companyReadLightCmd.putParam("companyId", companyId);

        Integer templateId = null;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(companyReadLightCmd, null);

            if (resultDTO.containsKey("invoiceMailTemplateId") && resultDTO.get("invoiceMailTemplateId") != null) {
                templateId = (Integer) resultDTO.get("invoiceMailTemplateId");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...");
        }

        if (templateId != null) {
            body = loadTemplate(templateId, addressId, contactPersonId, request);
        }

        return body;
    }

    private String loadTemplate(Integer templateId, Integer addressId, Integer contactPersonId, HttpServletRequest request) {
        String templateData = null;

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

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(importCmd, null);
            if (!resultDTO.isFailure() && resultDTO.get("importedHtml") != null) {
                templateData = resultDTO.get("importedHtml").toString();
            }
        } catch (AppLevelException e) {
            log.error("-> Execute cmd FAIL", e);
        }

        return templateData;
    }

    public ActionErrors getErrors() {
        return errors;
    }
}
