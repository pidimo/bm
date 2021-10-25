package com.piramide.elwis.web.salesmanager.util;

import com.piramide.elwis.cmd.campaignmanager.DownloadDocumentCmd;
import com.piramide.elwis.cmd.contactmanager.TelecomReadCmd;
import com.piramide.elwis.cmd.salesmanager.ActionTypeCmd;
import com.piramide.elwis.cmd.webmailmanager.ComposeMailCmd;
import com.piramide.elwis.cmd.webmailmanager.ImportHtmlTemplateCmd;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.salesmanager.ActionDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Util to create draft email with action document as attach
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.5.3
 */
public class ActionSendViaEmailUtil {
    private Log log = LogFactory.getLog(this.getClass());
    private ActionErrors errors;

    public ActionSendViaEmailUtil() {
        errors = new ActionErrors();
    }

    public Integer createDraftEmail(Integer userMailId, Integer companyId, Integer freeTextId, Integer telecomId, Integer processId, Integer contactId, HttpServletRequest request) {
        Integer mailId = null;
        errors = new ActionErrors();

        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        Integer mailAccountId = Functions.getDefaultMailAccountId(request);

        if (mailAccountId != null) {

            Integer addressId = null;
            Integer contactPersonId = null;
            Integer actionTypeId = null;
            String subject = "";

            if (contactId != null && processId != null) {
                ActionDTO actionDTO = com.piramide.elwis.web.salesmanager.el.Functions.getActionDTO(contactId.toString(), processId.toString(), request);
                if (actionDTO != null) {
                    addressId = (Integer) actionDTO.get("addressId");
                    contactPersonId = (Integer) actionDTO.get("contactPersonId");
                    actionTypeId = (Integer) actionDTO.get("actionTypeId");
                    subject = (String) actionDTO.get("note");
                }
            }

            ArrayByteWrapper documentWrapper = readDocument(freeTextId);
            if (documentWrapper != null) {
                documentWrapper.setFileName(getDocumentName(freeTextId));
                List<ArrayByteWrapper> attachments = new ArrayList<ArrayByteWrapper>();
                attachments.add(documentWrapper);

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
                composeMailCmd.putParam("body", composeBody(actionTypeId, addressId, contactPersonId, request));
                composeMailCmd.putParam("useHtmlEditor", "true");
                composeMailCmd.putParam("userTimeZone", dateTimeZone);
                composeMailCmd.putParam("mailAccountId", mailAccountId);
                composeMailCmd.putParam("mailPriority", Integer.valueOf(WebMailConstants.MAIL_PRIORITY_DEFAULT));
                composeMailCmd.putParam("mailSubject", subject);

                composeMailCmd.putParam("recipients", recipients); //Map<String,List<Map>>
                composeMailCmd.putParam("oldAttachments", new ArrayList()); //List<AttachDTO>
                composeMailCmd.putParam("newAttachments", attachments); //List<ArrayByteWrapper>

                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(composeMailCmd, null);
                    if (!resultDTO.isFailure()) {
                        mailId = (Integer) resultDTO.get("mailId");
                    }
                } catch (AppLevelException e) {
                    log.debug("Error in create draft email for sales process action...", e);
                }
            } else {
                errors.add("error", new ActionError("SalesProcessAction.sendViaEmail.error.emptyDocument"));
            }
        } else {
            errors.add("error", new ActionError("Invoice.sendViaEmail.error.mailAccount"));
        }

        return mailId;
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


    /**
     * Get document name from freetext id
     * @param freeTextId id
     * @return String
     */
    private String getDocumentName(Integer freeTextId) {
        log.debug("Get document name from freetext...." + freeTextId);
        String fileName = null;
        if (freeTextId != null) {
            fileName = com.piramide.elwis.web.common.el.Functions.getFreTextDocumentName(freeTextId);
        }
        if (fileName == null) {
            fileName = "Letter.doc";
        }
        return fileName;
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

    private String composeBody(Integer actionTypeId, Integer addressId, Integer contactPersonId, HttpServletRequest request) {
        String body = "";

        Integer templateId = null;
        if (actionTypeId != null) {
            //get the html template for send via email
            ActionTypeCmd actionTypeCmd = new ActionTypeCmd();
            actionTypeCmd.putParam("actionTypeId", actionTypeId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(actionTypeCmd, null);

                if (resultDTO.get("templateId") != null) {
                    templateId = (Integer) resultDTO.get("templateId");
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd...", e);
            }
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
