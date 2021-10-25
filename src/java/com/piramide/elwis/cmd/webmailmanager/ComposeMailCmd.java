package com.piramide.elwis.cmd.webmailmanager;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.webmailmanager.util.HtmlCodeUtil;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.catalogmanager.FreeTextDTO;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.dto.webmailmanager.EmailRecipientAddressDTO;
import com.piramide.elwis.dto.webmailmanager.MailDTO;
import com.piramide.elwis.dto.webmailmanager.MailRecipientDTO;
import com.piramide.elwis.service.webmail.SaveEmailService;
import com.piramide.elwis.service.webmail.SaveEmailServiceHome;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import com.piramide.elwis.utils.webmail.WebmailAttachUtil;
import com.piramide.elwis.utils.webmail.filter.ProcessImgTagForTemporalImage;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.java.dev.strutsejb.dto.EJBFactory.i;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: ComposeMailCmd.java 12565 2016-07-22 16:58:26Z miguel $
 */

public class ComposeMailCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ComposeMailCmd...." + paramDTO);

        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
        Integer mailId = EJBCommandUtil.i.getValueAsInteger(this, "mailId");

        String mailState = (paramDTO.get("mailState") != null) ? paramDTO.get("mailState").toString() : null;
        if (null != mailState && !"".equals(mailState) && !isDraftEmail()) {
            updateState(mailId, mailState);
        }

        List toRecipient = extractMailsByType("to");
        List ccRecipient = extractMailsByType("cc");
        List bccRecipient = extractMailsByType("bcc");

        Integer userMailId = EJBCommandUtil.i.getValueAsInteger(this, "userMailId");
        UserMail userMail = getUserMail(userMailId);

        List<AttachDTO> oldAttachments = (List<AttachDTO>) paramDTO.get("oldAttachments");
        List<ArrayByteWrapper> newAttachments = (List<ArrayByteWrapper>) paramDTO.get("newAttachments");

        Mail email = saveEmail(toRecipient,
                ccRecipient,
                bccRecipient,
                userMailId,
                oldAttachments,
                newAttachments,
                companyId,
                ctx);

        //the message is saved in draft folder
        if (isDraftEmail()) {
            updateDraftEmail(mailId, email, userMail);
            resultDTO.put("mailId", email.getMailId());
            resultDTO.setForward("Draft");
            //remove the autosave temporal email if exist
            removeAutoSaveTemporalEmail(userMail);
            return;
        }

        //create email contacts
        if (saveEmailInSentFolder() && createOutCommunication()) {

            List addressSelectedUI = new ArrayList();
            if (null != paramDTO.get("addressList") && !"".equals(paramDTO.get("addressList"))) {
                addressSelectedUI = Arrays.asList(paramDTO.get("addressList").toString().split(","));
            }

            List allRecipientMapList = new ArrayList();
            allRecipientMapList.addAll(toRecipient);
            allRecipientMapList.addAll(ccRecipient);
            CommunicationManagerCmd cmd = new CommunicationManagerCmd();
            cmd.setOp(ExtendedCRUDDirector.OP_CREATE);
            cmd.putParam("email", email);
            cmd.putParam("userMailId", userMail.getUserMailId());
            cmd.putParam("recipientMapList", allRecipientMapList);
            cmd.putParam("addressSelectedUI", addressSelectedUI);
            cmd.executeInStateless(ctx);
            List<Integer> communicationIdentifiers =
                    (List<Integer>) cmd.getResultDTO().get("communicationIdentifiers");
            resultDTO.put("communicationIdentifiers", communicationIdentifiers);
        }

        //check recipients where sent the emails
        CommunicationManagerCmd cmd = new CommunicationManagerCmd();
        cmd.setOp("checkRecipientSent");
        cmd.putParam("mailId", email.getMailId());
        cmd.putParam("companyId", email.getCompanyId());
        cmd.putParam("saveSendItem", saveEmailInSentFolder());
        cmd.executeInStateless(ctx);
        resultDTO.put("mailListDispatched", cmd.getResultDTO().get("mailListDispatched"));
        resultDTO.put("saveSendItem", saveEmailInSentFolder().toString());
        resultDTO.put("mailId", email.getMailId());

        sendMailToFolder(email, WebMailConstants.FOLDER_OUTBOX, userMailId);

        //if previous email is temporal draft, should be removed
        removeTemporalDraftEmail(mailId, userMail);
    }

    private void updateDraftEmail(Integer draftEmailId, Mail actualEmail, UserMail userMail) {

        sendMailToFolder(actualEmail, WebMailConstants.FOLDER_DRAFTITEMS, userMail.getUserMailId());

        if (null == draftEmailId) {
            return;
        }

        //because an draft email is save anew as draft, remove the old draft
        Mail draftEmail = getEmail(draftEmailId);
        if (null == draftEmail || null != draftEmail.getIncomingOutgoing()) {
            return;
        }

        removeEmail(draftEmail, userMail);
    }

    private void removeTemporalDraftEmail(Integer draftEmailId, UserMail userMail) {
        if (draftEmailId != null) {
            Mail draftEmail = getEmail(draftEmailId);
            if (draftEmail != null && draftEmail.getIsDraftTemp() != null && draftEmail.getIsDraftTemp()) {
                removeEmail(draftEmail, userMail);
            }
        }

        //remove also the autosave temporal draft email if exist
        removeAutoSaveTemporalEmail(userMail);
    }

    private void removeAutoSaveTemporalEmail(UserMail userMail) {
        Integer tempMailId = EJBCommandUtil.i.getValueAsInteger(this, "tempMailId");
        if (tempMailId != null) {
            Mail tempEmail = getEmail(tempMailId);
            if (tempEmail != null && tempEmail.getIsDraftTemp() != null && tempEmail.getIsDraftTemp()) {
                //only temp email mark as "isDraftTemp" should be removed, if email has been saved as draft from button "save as draft", this not should be removed
                removeEmail(tempEmail, userMail);
            }
        }
    }

    protected Mail saveEmail(List toRecipient,
                           List ccRecipient,
                           List bccRecipient,
                           Integer userMailId,
                           List<AttachDTO> oldAttachments,
                           List<ArrayByteWrapper> newAttachments,
                           Integer companyId,
                           SessionContext ctx) {
        Folder sentFolder =
                getFolder(userMailId, companyId, Integer.valueOf(WebMailConstants.FOLDER_SENDITEMS));

        Body body = createBodyEmail(companyId);

        Mail email = createEmail(body, sentFolder.getFolderId());
        int toRecipientSize = createSendMailAddress(email, toRecipient, WebMailConstants.TO_TYPE_DEFAULT);
        int ccRecipientSize = createSendMailAddress(email, ccRecipient, WebMailConstants.TO_TYPE_CC);
        int bccRecipientSize = createSendMailAddress(email, bccRecipient, WebMailConstants.TO_TYPE_BCC);

        Integer attachmentSize = createAttachments(email, newAttachments, oldAttachments);

        //process temporal images
        Integer temporalImagesSize = attachTemporalImages(email, ctx);

        int subjectSize = 0;
        if (null != email.getMailSubject()) {
            subjectSize = email.getMailSubject().length();
        }

        int totalSize = toRecipientSize +
                ccRecipientSize + bccRecipientSize + attachmentSize + subjectSize + email.getMailSize() + temporalImagesSize;

        email.setMailSize(totalSize);
        email.setSaveEmail(saveEmailInSentFolder());
        email.setCreateContact(createOutCommunication());
        email.setIsNewEmail(false);
        email.setIsCompleteEmail(true);

        return email;
    }

    protected void sendMailToFolder(Mail mail, String folderType, Integer userMailId) {
        Folder folder = getFolder(userMailId, mail.getCompanyId(), Integer.valueOf(folderType));

        if (WebMailConstants.FOLDER_DRAFTITEMS.equals(folderType) ||
                WebMailConstants.FOLDER_OUTBOX.equals(folderType)) {
            mail.setIncomingOutgoing(null);
        }

        mail.setFolderId(folder.getFolderId());
    }

    private Integer createAttachments(Mail email,
                                      List<ArrayByteWrapper> newAttahments,
                                      List<AttachDTO> oldAttachments) {
        Integer attachmentSizeCounter = 0;

        boolean containOnlyImages = true;
        AttachHome attachHome = (AttachHome) i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);
        for (AttachDTO oldAttachDTO : oldAttachments) {
            Attach oldAttach = getAttach((Integer) oldAttachDTO.get("attachId"));
            if (null == oldAttach) {
                continue;
            }

            try {
                Attach newAttach = attachHome.create(oldAttach.getCompanyId(),
                        email.getMailId(),
                        oldAttach.getAttachName(),
                        WebmailAttachUtil.i.getAttachData(oldAttach));

                newAttach.setVisible(oldAttach.getVisible());
                attachmentSizeCounter += newAttach.getSize();

                if (null == oldAttach.getVisible()) {
                    newAttach.setVisible(false);
                    containOnlyImages = false;
                    continue;
                }

                if (!oldAttach.getVisible()) {
                    containOnlyImages = false;
                    continue;
                }

                if (WebMailConstants.BODY_TYPE_TEXT.equals(email.getBody().getBodyType().toString())) {
                    containOnlyImages = false;
                    continue;
                }

                Boolean hasUpdatedBody = updateBody(email, newAttach, oldAttach.getAttachId().toString());
                if (!hasUpdatedBody) {
                    try {
                        newAttach.remove();
                    } catch (RemoveException e) {
                        newAttach.setVisible(false);
                        containOnlyImages = false;
                    }
                }
            } catch (CreateException e) {
                log.error("-> Create new Attachment Fail", e);
            }
        }

        for (ArrayByteWrapper wrapper : newAttahments) {
            try {
                Attach newAttach = attachHome.create(email.getCompanyId(), email.getMailId(), wrapper.getFileName(), wrapper.getFileData());
                newAttach.setVisible(false);
                containOnlyImages = false;
                attachmentSizeCounter += newAttach.getSize();

            } catch (CreateException e) {
                log.error("-> Create new Attachment Fail", e);
            }
        }

        //update the temporal mail attachs, change the mailId
        Integer tempAttachsSize = updateMailTemporalAttachs(email.getMailId(), email.getCompanyId());
        if (tempAttachsSize > 0) {
            containOnlyImages = false;
            attachmentSizeCounter += tempAttachsSize;
        }

        email.setHaveAttachments(!containOnlyImages);
        return attachmentSizeCounter;
    }

    private Integer updateMailTemporalAttachs(Integer newMailId, Integer companyId) {
        Integer totalSize = 0;
        Integer tempMailId = EJBCommandUtil.i.getValueAsInteger(this, "tempMailId");

        if (tempMailId != null) {
            AttachHome attachHome = (AttachHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);
            try {
                List attachments = (List) attachHome.findByMailId(tempMailId, companyId);
                for (int i = 0; i < attachments.size(); i++) {
                    Attach attach = (Attach) attachments.get(i);

                    //assign only temporal attach
                    if (attach.getVisible() != null && !attach.getVisible()) {
                        //update to new mail
                        attach.setMailId(newMailId);
                        if (attach.getSize() != null) {
                            totalSize = totalSize + attach.getSize();
                        }
                    }
                }
            } catch (FinderException e) {
                log.debug("Not found attach for mail id " + tempMailId, e);
            }
        }
        return totalSize;
    }


    private Body createBodyEmail(Integer companyId) {
        Integer signatureId = EJBCommandUtil.i.getValueAsInteger(this, "signatureId");

        String body = paramDTO.getAsString("body");

        String bodyType;
        if (useHtmlEditor()) {
            bodyType = WebMailConstants.BODY_TYPE_HTML;
            body = processBody(body, signatureId, WebMailConstants.BODY_TYPE_HTML);
        } else {
            bodyType = WebMailConstants.BODY_TYPE_TEXT;
            body = processBody(body, signatureId, WebMailConstants.BODY_TYPE_TEXT);
        }

        BodyHome bodyHome = (BodyHome) i.getEJBLocalHome(WebMailConstants.JNDI_BODY);
        try {
            return bodyHome.create(body.getBytes(), Integer.valueOf(bodyType), companyId);
        } catch (CreateException e) {
            log.debug("-> Cannot Create Body Object", e);
        }

        return null;
    }

    private Mail createEmail(Body body, Integer folderId) {
        DateTimeZone dateTimeZone = (DateTimeZone) paramDTO.get("userTimeZone");
        DateTime dateTime = new DateTime(dateTimeZone);

        Integer signatureId = EJBCommandUtil.i.getValueAsInteger(this, "signatureId");
        Integer mailAccountId = EJBCommandUtil.i.getValueAsInteger(this, "mailAccountId");

        MailAccount mailAccount = getMailAccount(mailAccountId);

        MailDTO emailDTO = new MailDTO();
        EJBCommandUtil.i.setValueAsInteger(this, emailDTO, "companyId");
        emailDTO.put("bodyId", body.getBodyId());
        emailDTO.put("folderId", folderId);
        emailDTO.put("mailPriority", paramDTO.get("mailPriority"));
        emailDTO.put("mailState", MailStateUtil.addSendState());
        emailDTO.put("mailSubject", paramDTO.get("mailSubject"));
        emailDTO.put("mailFrom", mailAccount.getEmail());
        emailDTO.put("mailAccount", mailAccount.getEmail());
        emailDTO.put("mailSize", body.getBodyContent().length);
        emailDTO.put("mailPersonalFrom", mailAccount.getEmail());
        if (null != mailAccount.getPrefix() && !"".equals(mailAccount.getPrefix().trim())) {
            emailDTO.put("mailPersonalFrom", mailAccount.getPrefix());
        }
        emailDTO.put("sentDate", dateTime.getMillis());
        if (null != signatureId) {
            emailDTO.put("signatureId", signatureId);
        }
        emailDTO.put("incomingOutgoing", WebMailConstants.OUT_VALUE);
        emailDTO.put("hidden", false);
        emailDTO.put("processToSent", false);

        if (paramDTO.get("isDraftTemp") != null) {
            emailDTO.put("isDraftTemp", paramDTO.get("isDraftTemp"));
        }

        return (Mail) ExtendedCRUDDirector.i.create(emailDTO, resultDTO, false);
    }

    private boolean updateBody(Mail mail, Attach attach, String oldAttachId) {
        Boolean result;
        String actualbody = new String(mail.getBody().getBodyContent());

        Map updatedMap = HtmlCodeUtil.updateImageCid(actualbody, oldAttachId, attach.getAttachId().toString());
        result = (Boolean) updatedMap.get("hasUpdated");

        if (result) {
            String newBody = (String) updatedMap.get("newBody");
            mail.getBody().setBodyContent(newBody.getBytes());
        }
        log.debug("-> Has Updated oldId=" + oldAttachId + " by newId=" + attach.getAttachId() + " in Body? " + result);
        return result;
    }

    /**
     * Extracts to, cc, bcc list of emailAddress
     *
     * @param type can be equals to to, cc or bcc
     * @return structure [{name=aaaa, email=aaa@some.com, contactPersonOf=[14,30]},{name=bbb, email=bbb@some.com},...]
     */
    public List extractMailsByType(String type) {
        List result = new ArrayList();

        Map addressContainner = (Map) paramDTO.get("recipients");
        if (addressContainner.get(type) != null) {
            result = (List) addressContainner.get(type);
        }

        return result;
    }

    public boolean isStateful() {
        return false;
    }

    private void updateState(Integer emailId, String state) {
        if (null == emailId) {
            return;
        }

        Mail email = getEmail(emailId);
        if (null == email) {
            return;
        }

        if (state != null) {
            email.setMailState(new Byte(state));
        }
    }

    private Boolean isDraftEmail() {
        return null != paramDTO.get("isDraftEmail") &&
                "true".equals(paramDTO.get("isDraftEmail").toString());
    }

    protected Boolean saveEmailInSentFolder() {
        return null != paramDTO.get("saveSendItem") &&
                "true".equals(paramDTO.get("saveSendItem").toString());
    }

    protected Boolean createOutCommunication() {
        return null != paramDTO.get("createOutCommunication") &&
                "true".equals(paramDTO.get("createOutCommunication").toString());
    }

    private Boolean useHtmlEditor() {
        return null != paramDTO.get("useHtmlEditor") &&
                "true".equals(paramDTO.get("useHtmlEditor").toString());
    }

    protected Mail getEmail(Integer mailId) {
        MailHome mailHome = (MailHome) i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        try {
            return mailHome.findByPrimaryKey(mailId);
        } catch (FinderException e) {
            return null;
        }
    }

    protected UserMail getUserMail(Integer userMailId) {
        UserMailHome userMailHome =
                (UserMailHome) i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);
        try {
            return userMailHome.findByPrimaryKey(userMailId);
        } catch (FinderException e) {
            return null;
        }
    }

    private Folder getFolder(Integer userMailId, Integer companyId, Integer folderType) {
        FolderHome folderHome = (FolderHome) i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);

        try {
            return folderHome.findByFolderType(userMailId, folderType, companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    protected MailAccount getMailAccount(Integer mailAccountId) {
        MailAccountHome mailAccountHome =
                (MailAccountHome) i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);
        try {
            return mailAccountHome.findByPrimaryKey(mailAccountId);
        } catch (FinderException e) {
            return null;
        }
    }

    private Signature getSignature(Integer signatureId) {
        if (null == signatureId) {
            return null;
        }

        SignatureHome signatureHome = (SignatureHome) i.getEJBLocalHome(WebMailConstants.JNDI_SIGNATURE);

        try {
            return signatureHome.findByPrimaryKey(signatureId);
        } catch (FinderException e) {
            return null;
        }
    }

    private String readSignatureFreeText(Integer freeTextId) {
        FreeText freeText = (FreeText)
                i.callFinder(new FreeTextDTO(), "findByPrimaryKey", new Integer[]{freeTextId});

        return new String(freeText.getValue());
    }

    private String processBody(String body, Integer signatureId, String bodyType) {
        //no apply signature for draft emails
        if (isDraftEmail()) {
            return body;
        }

        String htmlSignature = "";
        String textSignature = "";
        if (null != signatureId) {
            Signature signature = getSignature(signatureId);
            if (null != signature) {
                htmlSignature = readSignatureFreeText(signature.getHtmlSignatureId());
                textSignature = readSignatureFreeText(signature.getTextSignatureId());
            }
        }

        if (WebMailConstants.BODY_TYPE_HTML.equals(bodyType)) {
            StringBuilder htmlBuilder = new StringBuilder().
                    append(body).
                    append("</br>").
                    append("</br>").append(htmlSignature);
            return HtmlCodeUtil.processAttributesForLink(htmlBuilder.toString());
        }

        if (WebMailConstants.BODY_TYPE_TEXT.equals(bodyType)) {
            return body + "\n\n\n" + textSignature;
        }

        return body;
    }

    protected int createSendMailAddress(Mail mail, List recipient, String type) {
        int size = 0;

        if (null == recipient) {
            return size;
        }

        for (Object object : recipient) {
            Map emailAddress = (Map) object;

            String email = ((String) emailAddress.get("email")).toLowerCase();
            String name = email;

            if (null != emailAddress.get("personalName") &&
                    !"".equals(emailAddress.get("personalName").toString().trim())) {
                name = emailAddress.get("personalName").toString().trim();
            }

            MailRecipientDTO mailRecipientDTO = new MailRecipientDTO();
            mailRecipientDTO.put("companyId", paramDTO.get("companyId"));
            mailRecipientDTO.put("personalName", name);
            mailRecipientDTO.put("mailId", mail.getMailId());
            mailRecipientDTO.put("type", new Integer(type));
            mailRecipientDTO.put("email", email);

            MailRecipient mailRecipient =
                    (MailRecipient) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, mailRecipientDTO, new ResultDTO());
            size += email.getBytes().length + name.getBytes().length;

            List addressIds = (List) emailAddress.get("addressId");
            List contactPersonOf = (List) emailAddress.get("contactPersonOf");

            createEmailRecipientAddresses(mailRecipient, addressIds, contactPersonOf);
        }

        return size;
    }

    private void createEmailRecipientAddresses(MailRecipient mailRecipient,
                                               List addressIds,
                                               List contactPersonIds) {
        if (null != addressIds) {
            for (Object object : addressIds) {
                Integer addressId = (Integer) object;
                EmailRecipientAddressDTO emailRecipientAddressDTO = new EmailRecipientAddressDTO();
                emailRecipientAddressDTO.put("addressId", addressId);
                emailRecipientAddressDTO.put("companyId", mailRecipient.getCompanyId());
                emailRecipientAddressDTO.put("mailRecipientId", mailRecipient.getMailRecipientId());

                ExtendedCRUDDirector.i.create(emailRecipientAddressDTO, resultDTO, false);
            }
        }

        if (null != contactPersonIds) {
            for (Object object : contactPersonIds) {
                Integer contactPersonId = (Integer) object;
                EmailRecipientAddressDTO emailRecipientAddressDTO = new EmailRecipientAddressDTO();
                emailRecipientAddressDTO.put("contactPersonId", contactPersonId);
                emailRecipientAddressDTO.put("companyId", mailRecipient.getCompanyId());
                emailRecipientAddressDTO.put("mailRecipientId", mailRecipient.getMailRecipientId());

                ExtendedCRUDDirector.i.create(emailRecipientAddressDTO, resultDTO, false);
            }
        }
    }

    private Attach getAttach(Integer attachId) {
        AttachHome attachHome = (AttachHome) i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);

        try {
            return attachHome.findByPrimaryKey(attachId);
        } catch (FinderException e) {
            return null;
        }
    }

    private void removeEmail(Mail email, UserMail userMail) {
        SaveEmailService saveEmailService = getSaveEmailService();
        try {
            saveEmailService.deleteEmail(email);
        } catch (RemoveException e) {
            log.debug("-> Cannot remove email ", e);
            sendMailToFolder(email, WebMailConstants.FOLDER_TRASH, userMail.getUserMailId());
        }
    }

    private SaveEmailService getSaveEmailService() {
        SaveEmailServiceHome saveEmailServiceHome =
                (SaveEmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_SAVE_EMAIL_SERVICE);
        try {
            return saveEmailServiceHome.create();
        } catch (CreateException e) {
            log.error("Create SaveEmailService FAIL ", e);
            return null;
        }
    }

    /**
     * Process mail temporal images, create attachs for this
     *
     * @param mail
     * @param ctx
     * @return size of attached
     */
    protected Integer attachTemporalImages(Mail mail, SessionContext ctx) {

        Integer attachTemporalImageSize = 0;
        String actualbody = new String(mail.getBody().getBodyContent());

        ProcessImgTagForTemporalImage filter = new ProcessImgTagForTemporalImage(ctx, mail.getMailId());
        HtmlEmailParser parser = new HtmlEmailDOMParser();
        parser.addFilter(filter);
        try {
            parser.parseHtml(actualbody);
            String newBody = parser.getHtml().toString();
            if (filter.getHasChanges()) {
                mail.getBody().setBodyContent(newBody.getBytes());
                attachTemporalImageSize = filter.getBytesAttachSize();
            }
        } catch (Exception e) {
            log.debug("-> Change temporal images src atttibute on IMG tags FAIL");
        }

        return attachTemporalImageSize;
    }

}
