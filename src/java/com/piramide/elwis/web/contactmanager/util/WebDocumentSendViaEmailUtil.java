package com.piramide.elwis.web.contactmanager.util;

import com.piramide.elwis.cmd.campaignmanager.DownloadDocumentCmd;
import com.piramide.elwis.cmd.contactmanager.LightlyContactReadCmd;
import com.piramide.elwis.cmd.contactmanager.TelecomReadCmd;
import com.piramide.elwis.cmd.webmailmanager.ComposeMailCmd;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.action.DownloadWebDocumentAction;
import com.piramide.elwis.web.webmail.el.Functions;
import com.piramide.elwis.web.webmail.util.EmailRecipientHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Util to create draft email to web document
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class WebDocumentSendViaEmailUtil {
    private Log log = LogFactory.getLog(this.getClass());
    private ActionErrors errors;

    public WebDocumentSendViaEmailUtil() {
        errors = new ActionErrors();
    }

    public Integer createDraftEmailForCommunication(Integer userMailId, Integer companyId, Integer contactId, Integer telecomId, HttpServletRequest request) {
        Integer mailId = null;
        errors = new ActionErrors();

        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        Integer mailAccountId = Functions.getDefaultMailAccountId(request);

        if (mailAccountId != null) {

            Integer freeTextId = null;
            Integer addressId = null;
            Integer contactPersonId = null;
            String note = null;

            LightlyContactReadCmd contactReadCmd = new LightlyContactReadCmd();
            contactReadCmd.putParam("contactId", contactId);
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(contactReadCmd, null);
                if (!resultDTO.isFailure()) {
                    freeTextId = (Integer) resultDTO.get("freeTextId");
                    addressId = (Integer) resultDTO.get("addressId");
                    contactPersonId = (Integer) resultDTO.get("contactPersonId");
                    note = (String) resultDTO.get("note");
                }
            } catch (AppLevelException e) {
                log.debug("Error in read communication with..." + contactId, e);
            }

            ArrayByteWrapper webDocumentWrapper = readDocument(freeTextId);
            if (webDocumentWrapper != null) {
                webDocumentWrapper.setFileName(DownloadWebDocumentAction.getCommunicationWebDocumentName(contactId, request));
                List<ArrayByteWrapper> attachments = new ArrayList<ArrayByteWrapper>();
                attachments.add(webDocumentWrapper);

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
                composeMailCmd.putParam("mailSubject", composeSubject(note, request));

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
                errors.add("error", new ActionError("Communication.webDoc.sendViaEmail.error.emptyDocument"));
            }
        } else {
            errors.add("error", new ActionError("Communication.webDoc.sendViaEmail.error.mailAccount"));
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

    private String composeSubject(String note, HttpServletRequest request) {
        return note;
    }

    private Map<String, List<Map>> composeRecipients(Integer telecomId) {
        Map<String, List<Map>> recipients = new HashMap<String, List<Map>>();

        TelecomDTO telecomDTO = findTelecom(telecomId);
        if (telecomDTO != null) {

            Integer recipientAddressId = telecomDTO.getAddressId();
            if (telecomDTO.getContactPersonId() != null) {
                recipientAddressId = telecomDTO.getContactPersonId();
            }

            String addressName = com.piramide.elwis.web.contactmanager.el.Functions.getAddressName(recipientAddressId);

            Map emailMap = new HashMap();
            emailMap.put(EmailRecipientHelper.RecipientKey.EMAIL.getKey(), telecomDTO.getData());
            emailMap.put(EmailRecipientHelper.RecipientKey.PERSONAL_NAME.getKey(), addressName);

            List<Map> emailList = new ArrayList<Map>();
            emailList.add(emailMap);

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
        return body;
    }

    public ActionErrors getErrors() {
        return errors;
    }
}
