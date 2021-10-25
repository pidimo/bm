package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.webmailmanager.util.HtmlCodeUtil;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.domain.supportmanager.SupportContact;
import com.piramide.elwis.domain.supportmanager.SupportContactHome;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.dto.webmailmanager.FolderDTO;
import com.piramide.elwis.dto.webmailmanager.MailDTO;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import com.piramide.elwis.utils.webmail.WebmailAttachUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * This class reads a mail and his related components (attachs, to's, etc)
 *
 * @author Alvaro
 * @version $Id: ReadMailCmd.java 10320 2013-02-26 20:02:58Z miguel $
 */
public class ReadMailCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {

        String op = (paramDTO.get("op") != null) ? paramDTO.get("op").toString() : "readMail";

        if (op.equals("readMail") || op.equals("readMailForDelete")) {
            log.debug("Reading a mail");
            String mailId = paramDTO.get("mailId").toString();
            String userMailId = paramDTO.get("userMailId").toString();
            String companyId = paramDTO.get("companyId").toString();

            //Read the mail
            MailDTO mailDTO = new MailDTO();
            mailDTO.put("mailId", new Integer(mailId));
            ResultDTO result = new ResultDTO();
            Mail mail = (Mail) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, mailDTO, result);
            resultDTO.put("mailId", mail.getMailId());
            String mailSubject = mail.getMailSubject();
            mailSubject = mailSubject != null ? mailSubject : "";
            resultDTO.put("subject", mailSubject);
            resultDTO.put("mailPriority", mail.getMailPriority());
            HashMap mailFrom = new HashMap();
            mailFrom.put("name", getFromName(mail.getMailFrom(), mail.getMailPersonalFrom()));
            mailFrom.put("email", mail.getMailFrom());
            resultDTO.put("mailFrom", mailFrom);
            resultDTO.put("sentDate", mail.getSentDate());
            resultDTO.put("signature", mail.getSignatureId());
            resultDTO.put("mailAccount", mail.getMailAccount());
            resultDTO.put("senderFrom", mailFrom);

            //search from field in user mail accounts
            searchFromEmail(mail, new Integer(userMailId), ctx);

            //Read MailTo's
            readMailTo(mail);
            //Read the Body
            readMailBody(mail);
            //Read attachs
            readAttachs(mail);
            //Read mail Folder type
            readFolderType(mail.getFolderId());
            //Read the email of the user
            resultDTO.put("userEmail", mail.getMailAccount());
            //Verify if the from is a contact and return his addressId
            getContactId(mail.getMailFrom(), companyId);

            if (!op.equals("readMailForDelete")) {
                //Read the constants for MoveTo
                try {
                    MailTrayCmd mailTrayCmd = new MailTrayCmd();
                    DTO dto = new DTO();
                    dto.put("userMailId", userMailId);
                    dto.put("companyId", companyId);
                    dto.put("op", "readUserFolders");
                    mailTrayCmd.putParam(dto);
                    mailTrayCmd.executeInStateless(ctx);
                    resultDTO.put("folderConstants", mailTrayCmd.getResultDTO().get("folderConstants"));
                } catch (Exception ex) {
                }
            }

            //Mark as Read
            if (!op.toString().equals("readMailForDelete")) {
                mail.setMailState(MailStateUtil.addReadState(mail.getMailState()));
            }

            //Read if the mail have mailcontacts
            if (op.equals("readMailForDelete")) {
                isRelatedWithCommunication(mail);
                //haveMailContacts(mail, paramDTO.get("findByFolderType"));
            }
        } else if (op.equals("readRedirectAttachs")) {
            readAttachsFromRedirect((String[]) paramDTO.get("attachIds"));
        } else if (op.equals("readAttachsGroup")) {
            readAttachs((String[]) paramDTO.get("attachIds"));
        } else if (op.equals("readRecipientsEmails")) {
            getMailRecipientsEmails(paramDTO.get("mailId").toString());
        } else if (op.equals("readEditMode")) {
            getEditMode(paramDTO.get("userMailId").toString());
        } else if (op.equals("readMailBody")) {   //Reads only the body of the mail
            Mail mail = readMail(paramDTO.get("mailId").toString());
            readMailBody(mail);
            readVisibleAttachments(mail);
        } else if (op.equals("readMailForCommunications")) {
            String userMailId = paramDTO.get("userMailId").toString();
            String mailId = paramDTO.get("mailId").toString();
            Mail mail = readMail(mailId);
            //search from field in user mail accounts
            searchFromEmail(mail, new Integer(userMailId), ctx);
            readMailBody(mail);
            readAttachs(mail);
            readMailTo(mail);
            resultDTO.put("mailId", mailId);
        } else if (op.equals("readForPrint")) {//This way to use op is not fine...bad practice...
            String mailId = paramDTO.get("mailId").toString();
            Mail mail = readMail(mailId);
            readMailBody(mail);
            readAttachs(mail);
            readMailTo(mail);
            readMailForPrint(mail);//the order is important here!
            resultDTO.put("mailId", mailId);
        } else if (op.equals("haveMailContacts")) {
            isRelatedWithCommunication(readMail(paramDTO.get("mailId").toString()));
            //haveMailContacts(readMail(paramDTO.get("mailId").toString()), paramDTO.get("findByFolderType"));
        }
    }

    /**
     * This method search email account that is associated with the Mail
     * object in field MailFrom, besides establishing the identifier of the mail account
     * in <code>resultDTO</code> object.
     *
     * @param mail       mail object
     * @param userMailId user mail identifier
     * @param ctx        sessionContext to execute <code>MailAccountCmd</code>
     */
    private void searchFromEmail(Mail mail, Integer userMailId, SessionContext ctx) {
        MailAccountCmd accountCmd = new MailAccountCmd();
        accountCmd.setOp("searchAccountByEmail");
        accountCmd.putParam("email", mail.getMailFrom());
        accountCmd.putParam("userMailId", userMailId);
        accountCmd.putParam("companyId", mail.getCompanyId());
        accountCmd.executeInStateless(ctx);
        ResultDTO accountResult = accountCmd.getResultDTO();
        Integer mailAccountId = (Integer) accountResult.get("mailAccountId");
        if (null != mailAccountId) {
            resultDTO.put("mailAccountId", mailAccountId.toString());
        }
    }

    private Mail readMail(String mailId) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.put("mailId", new Integer(mailId));
        Mail mail = (Mail) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, mailDTO, resultDTO);
        resultDTO.put("emailForm", mail.getMailFrom());
        return (mail);
    }

    /**
     * Read information needed to print...
     */
    private void readMailForPrint(Mail mail) {
        String mailSubject = mail.getMailSubject();
        mailSubject = mailSubject != null ? mailSubject : "";
        resultDTO.put("subject", mailSubject);
        resultDTO.put("mailPriority", mail.getMailPriority());

        HashMap mailFrom = new HashMap();
        mailFrom.put("name", getFromName(mail.getMailFrom(), mail.getMailPersonalFrom()));
        mailFrom.put("email", mail.getMailFrom());

        resultDTO.put("from", getFormattedEmailAddress(mailFrom));
        resultDTO.put("to", getFormattedEmailAddresses((List) resultDTO.get("mailTos")));
        resultDTO.put("cc", getFormattedEmailAddresses((List) resultDTO.get("mailTosCC")));

        resultDTO.put("sentDate", mail.getSentDate());
    }

    /**
     * This should be in a helper class at domain level if it is required in other place
     *
     * @param nameEmailPair map for name an email
     * @return the formated string
     */
    private String getFormattedEmailAddress(HashMap nameEmailPair) {
        String res = "";
        Object name = nameEmailPair.get("name");
        Object email = nameEmailPair.get("email");
        if (name != null && name.toString().length() > 0 && (email != null && !name.equals(email))) {
            res = "\"" + name + "\" <" + email + ">";
        } else {
            res = email.toString();
        }
        return (res);
    }

    /**
     * This should be in a helper class at domain level if it is required in other place
     * String with the To addresses formatted
     *
     * @param data the list of maps
     * @return formatted string
     */
    private String getFormattedEmailAddresses(List data) {
        String res = "";
        for (int i = 0; i < data.size(); i++) {
            HashMap hm = new HashMap();
            hm = (HashMap) data.get(i);
            res += getFormattedEmailAddress(hm);
            if (i < data.size() - 1) {
                res += ", ";
            }
        }
        return (res);
    }

    /**
     * Reads the MailTo's of the Mail
     *
     * @param mail
     */
    public void readMailTo(Mail mail) {
        ArrayList mailtos = new ArrayList();
        ArrayList mailtosCC = new ArrayList();
        List mailtosBCC = new ArrayList();

        Collection tos = mail.getMailRecipients();
        Object[] mailRecipients = (Object[]) (tos.toArray());
        //Read MailTo
        if (mailRecipients != null) {
            for (int i = 0; i < mailRecipients.length; i++) {
                MailRecipient mailRecipient_i = (MailRecipient) mailRecipients[i];
                HashMap hm_i = new HashMap();
                hm_i.put("name", mailRecipient_i.getPersonalName());
                hm_i.put("email", mailRecipient_i.getEmail());
                if (mailRecipient_i.getType().toString().equals(WebMailConstants.TO_TYPE_DEFAULT) && !mailtos.contains(hm_i)) {
                    mailtos.add(hm_i);
                } else if (((MailRecipient) mailRecipients[i]).getType().toString().equals(WebMailConstants.TO_TYPE_CC) && !mailtosCC.contains(hm_i)) {
                    mailtosCC.add(hm_i);
                } else if (((MailRecipient) mailRecipients[i]).getType().toString().equals(WebMailConstants.TO_TYPE_BCC) && !mailtosCC.contains(hm_i)) {
                    mailtosBCC.add(hm_i);
                }
            }
        }
        resultDTO.put("mailTos", mailtos);
        resultDTO.put("mailTosCC", mailtosCC);
        resultDTO.put("mailTosBCC", mailtosBCC);
    }

    /**
     * Reads the mail body
     *
     * @param mail
     */
    public void readMailBody(Mail mail) {
        if (null == mail.getBody()) {
            return;
        }

        Body body = mail.getBody();
        resultDTO.put("body", new String(body.getBodyContent()));
        resultDTO.put("bodyType", body.getBodyType());
        if (body.getBodyType().toString().equals(WebMailConstants.BODY_TYPE_HTML)) {
            String htmlBody = HtmlCodeUtil.processAttributesForLink(new String(body.getBodyContent()));
            resultDTO.put("body", htmlBody);
            resultDTO.put("html", "true");//For a draft
        }
    }

    private void readVisibleAttachments(Mail mail) {

        AttachHome attachHome =
                (AttachHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);

        List attachments = null;
        try {
            attachments = (List) attachHome.findByMailId(mail.getMailId(), mail.getCompanyId());
        } catch (FinderException e) {
            //
        }

        Collection visibleAttachments = new ArrayList();

        if (null != attachments) {
            for (int i = 0; i < attachments.size(); i++) {
                Attach attachment = (Attach) attachments.get(i);
                if (null == attachment.getVisible() || !attachment.getVisible()) {
                    continue;
                }

                AttachDTO attachDTO = new AttachDTO();
                attachDTO.put("attachId", attachment.getAttachId());
                attachDTO.put("attachName", attachment.getAttachName());
                attachDTO.put("attachVisibility", attachment.getVisible().toString());
                visibleAttachments.add(attachDTO);
            }
        }

        if (visibleAttachments.size() > 0) {
            resultDTO.put("hasAttachs", "true");
        }

        resultDTO.put("attachs", visibleAttachments);
        resultDTO.put("visibleAttachs", visibleAttachments.size());
    }

    /**
     * Reads the attachs of the mail
     *
     * @param mail
     */
    public void readAttachs(Mail mail) {
        AttachHome attachHome =
                (AttachHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);

        List attachments = null;
        try {
            attachments = (List) attachHome.findByMailId(mail.getMailId(), mail.getCompanyId());
        } catch (FinderException e) {
            //
        }

        Collection attachDTOs = new ArrayList();
        int visibleAttachs = 0; //to store # of visible attachs
        if (null != attachments) {
            for (int i = 0; i < attachments.size(); i++) {
                Attach attach = (Attach) (attachments.get(i));
                Boolean attachVisibility = attach.getVisible();
                AttachDTO attachDTO = new AttachDTO();
                attachDTO.put("attachId", attach.getAttachId());
                attachDTO.put("attachName", attach.getAttachName());
                if (attachVisibility == null || !attachVisibility) {
                    visibleAttachs++;
                }
                attachDTO.put("attachVisibility", (attachVisibility != null ? attachVisibility.toString() : "false"));
                if (null == attach.getSize()) {
                    byte[] attachData = WebmailAttachUtil.i.getAttachData(attach);
                    Integer fileSize = attachData.length;
                    attach.setSize(fileSize);
                }
                attachDTO.put("attachSize", attach.getSize());
                attachDTOs.add(attachDTO);
            }
        }
        /*Collection attachs = mail.getAttachs();*/

        /*Iterator i = attachs.iterator();*/

        /* while (i.hasNext()) {
            Attach attach = (Attach) (i.next());
            Boolean attachVisibility = attach.getVisible();
            AttachDTO attachDTO = new AttachDTO();
            attachDTO.put("attachId", attach.getAttachId());
            attachDTO.put("attachName", attach.getAttachName());
            if (attachVisibility == null || !attachVisibility) {
                visibleAttachs++;
            }
            attachDTO.put("attachVisibility", (attachVisibility != null ? attachVisibility.toString() : "false"));
            if (null == attach.getSize()) {
                Integer fileSize = attach.getAttachFile().length;
                attach.setSize(fileSize);
            }
            attachDTO.put("attachSize", attach.getSize());
            attachDTOs.add(attachDTO);
        }*/
        if (attachDTOs.size() > 0) {
            resultDTO.put("hasAttachs", "true");//For a draft
        }
        resultDTO.put("attachs", attachDTOs);
        resultDTO.put("visibleAttachs", new Integer(visibleAttachs));
    }

    /**
     * Reads the information of an group of attachs(id, name, visibility, size)
     *
     * @param attachIds
     */
    public void readAttachs(String attachIds[]) {
        ArrayList attachDTOs = new ArrayList();
        if (attachIds != null) {
            for (int i = 0; i < attachIds.length; i++) {
                AttachDTO attachDTO = new AttachDTO();
                attachDTO.put("attachId", new Integer(attachIds[i]));
                Attach attach = (Attach) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, attachDTO, new ResultDTO());

                if (null == attach) {
                    resultDTO.setResultAsFailure();
                    break;
                }

                Object attachVisibility = attach.getVisible();
                attachDTO = new AttachDTO();
                attachDTO.put("attachId", attach.getAttachId());
                attachDTO.put("attachName", attach.getAttachName());
                attachDTO.put("attachVisibility", (attachVisibility != null ? attachVisibility.toString() : "false"));

                if (null == attach.getSize()) {
                    byte[] attachData = WebmailAttachUtil.i.getAttachData(attach);
                    Integer fileSize = attachData.length;
                    attach.setSize(fileSize);
                }
                attachDTO.put("attachSize", attach.getSize());
                attachDTOs.add(attachDTO);
            }
            if (attachDTOs.size() > 0) {
                resultDTO.put("hasAttachs", "true");
            }
            resultDTO.put("attachs", attachDTOs);
        }
    }

    /**
     * Read the folder type
     *
     * @param folderId
     */
    public void readFolderType(Integer folderId) {
        FolderDTO folderDTO = new FolderDTO();
        folderDTO.put("folderId", folderId);
        resultDTO.put("mailFolderType", ((Folder) (ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, folderDTO, new ResultDTO()))).getFolderType());
    }

    /**
     * Loads the attachs from a mail
     *
     * @param attachIds
     */
    public void readAttachsFromRedirect(String[] attachIds) {
        ArrayList arrayByteWrapperList = new ArrayList();
        ArrayList fileNamesList = new ArrayList();
        ArrayList res[] = null;
        if (attachIds != null) {
            for (int i = 0; i < attachIds.length; i++) {
                AttachDTO attachDTO = new AttachDTO();
                attachDTO.put("attachId", new Integer(attachIds[i]));
                Attach attach = (Attach) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, attachDTO, new ResultDTO());
                if (null == attach) {
                    resultDTO.setResultAsFailure();
                    break;
                }

                attach.getAttachId();
                ArrayByteWrapper wrapper = new ArrayByteWrapper();
                wrapper.setFileData(WebmailAttachUtil.i.getAttachData(attach));
                fileNamesList.add(attach.getAttachName());
                arrayByteWrapperList.add(wrapper);
            }
            resultDTO.put("arrayByteWrapperList", arrayByteWrapperList);
            resultDTO.put("fileNamesList", fileNamesList);
        }
    }

    /**
     * @param email
     * @param companyId
     */
    private void getContactId(String email, String companyId) {
        String contactType = "", contactId = "";
        if (email != null) {
            ArrayList telecoms = new ArrayList();
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            try {
                telecoms = new ArrayList(telecomHome.findTelecomsWithTelecomNumber(email, new Integer(companyId)));
            } catch (FinderException e) {
                telecoms = null;
            }
            if (telecoms != null && telecoms.size() == 1) {
                Telecom telecom = (Telecom) telecoms.get(0);
                Integer contactPersonId = telecom.getContactPersonId();
                Integer addressId = telecom.getAddressId();
                if (contactPersonId != null) {
                    contactId = contactPersonId.toString();
                    contactType = "1";
                } else if (addressId != null) {
                    contactId = addressId.toString();
                    Address address = (Address) telecom.getAddress();
                    if (address.getAddressType().equals(ContactConstants.ADDRESSTYPE_ORGANIZATION)) {
                        contactType = "0";
                    } else {
                        contactType = "1";
                    }
                }
            }
        }
        resultDTO.put("fromContactId", contactId);
        resultDTO.put("fromContactType", contactType);
    }

    //used by mailtray
    private void getMailRecipientsEmails(String mailId) {
        ArrayList res = new ArrayList();
        MailDTO mailDTO = new MailDTO();
        mailDTO.put("mailId", new Integer(mailId));
        Mail mail = (Mail) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, mailDTO, new ResultDTO());
        Iterator i = mail.getMailRecipients().iterator();
        while (i.hasNext()) {
            HashMap hm_i = new HashMap();
            MailRecipient mr = (MailRecipient) i.next();
            hm_i.put("email", mr.getEmail());
            hm_i.put("personalName", mr.getPersonalName());
            res.add(hm_i);
        }
        resultDTO.put("mailRecipientsData", res);
        isRelatedWithCommunication(mail);
    }

    private void getEditMode(String userMailId) {
        Object params[] = {new Integer(userMailId)};
        UserMail userMail = (UserMail) EJBFactory.i.callFinder(new UserMailDTO(), "findByPrimaryKey", params);
        Object editMode = userMail.getEditMode();
        if (editMode != null) {
            resultDTO.put("editMode", editMode);
        }

        Boolean replyMode = userMail.getReplyMode();
        if (replyMode != null) {
            resultDTO.put("replyMode", replyMode);
        }
    }

    private String getFromName(Object fromEmail, Object personalFrom) {
        String res = "";
        if (personalFrom != null && personalFrom.toString().length() > 0 && (fromEmail != null && !fromEmail.toString().equals(personalFrom.toString()))) {
            res = personalFrom.toString();
        }
        return (res);
    }


    private void isRelatedWithCommunication(Mail email) {
        resultDTO.put("haveMailContacts", false);
        resultDTO.put("isAction", false);
        resultDTO.put("isSupportActivity", false);
        resultDTO.put("allRecipientsRelated", true);

        Integer incomingOutGoing = email.getIncomingOutgoing();
        if (null == incomingOutGoing) {
            return;
        }

        List emailCommunications = getEmailCommunications(email.getMailId(), email.getCompanyId());

        // email no contains relationship with communications.
        if (emailCommunications.isEmpty()) {
            return;
        }

        resultDTO.put("haveMailContacts", true);

        //because in emails are related only with one communication.
        if (WebMailConstants.IN_VALUE == incomingOutGoing) {
            return;
        }

        //to show relation icon in user interface
        if (WebMailConstants.OUT_VALUE == incomingOutGoing &&
                emailCommunications.size() != email.getMailRecipients().size()) {
            resultDTO.put("allRecipientsRelated", false);
        }

        for (int i = 0; i < emailCommunications.size(); i++) {
            MailContact emailCommunication = (MailContact) emailCommunications.get(i);

            Contact contact = getContact(emailCommunication.getContactId());
            if (null != contact && contact.getIsAction()) {
                resultDTO.put("isAction", true);
                break;
            }

            SupportContact supportContact = getSupportContact(emailCommunication.getContactId(),
                    emailCommunication.getCompanyId());
            if (null != supportContact && null != supportContact.getActivityId()) {
                resultDTO.put("isSupportActivity", true);
                break;
            }
        }

    }

    private SupportContact getSupportContact(Integer contactId, Integer companyId) {
        SupportContactHome supportContactHome =
                (SupportContactHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CONTACT);
        try {
            return supportContactHome.findByContactId(contactId, companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    private Contact getContact(Integer contactId) {
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        try {
            return contactHome.findByPrimaryKey(contactId);
        } catch (FinderException e) {
            return null;
        }
    }

    private List getEmailCommunications(Integer mailId, Integer companyId) {
        MailContactHome mailContactHome =
                (MailContactHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILCONTACT);
        try {
            return (List) mailContactHome.findByMailId(mailId, companyId);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }
}
