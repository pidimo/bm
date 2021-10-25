package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.dto.webmailmanager.MailDTO;
import com.piramide.elwis.dto.webmailmanager.MailRecipientDTO;
import com.piramide.elwis.service.webmail.SaveEmailService;
import com.piramide.elwis.service.webmail.SaveEmailServiceHome;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import com.piramide.elwis.utils.webmail.WebmailAttachUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

import static net.java.dev.strutsejb.dto.EJBFactory.i;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class ReadEmailCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ReadEmailCmd.class);

    @Override
    public void executeInStateless(SessionContext context) {
        boolean isRead = true;

        if ("readBody".equals(getOp())) {
            isRead = false;
            Integer mailId = EJBCommandUtil.i.getValueAsInteger(this, "mailId");
            readBody(mailId, context);
        }

        if (isRead) {
            Integer mailId = EJBCommandUtil.i.getValueAsInteger(this, "mailId");
            Integer userMailId = EJBCommandUtil.i.getValueAsInteger(this, "userMailId");
            Integer readerUserMailId = EJBCommandUtil.i.getValueAsInteger(this, "readerUserMailId");

            Boolean filterInvalidRecipients = EJBCommandUtil.i.getValueAsBoolean(this, "filterInvalidRecipients");
            if (null == filterInvalidRecipients) {
                filterInvalidRecipients = false;
            }

            Boolean createCommunications = EJBCommandUtil.i.getValueAsBoolean(this, "createCommunications");
            if (null == createCommunications) {
                createCommunications = false;
            }

            Boolean changeToReadState = EJBCommandUtil.i.getValueAsBoolean(this, "changeToReadState");

            read(mailId,
                    userMailId,
                    readerUserMailId,
                    filterInvalidRecipients,
                    createCommunications,
                    changeToReadState,
                    context);
        }
    }

    protected void readBody(Integer mailId, SessionContext ctx) {
        Mail email = getEmail(mailId);

        if (null == email) {
            return;
        }

        resultDTO.put("body", getBody(email, ctx));
        resultDTO.put("attachments", getAttachments(email, ctx));
    }

    protected void read(Integer mailId,
                        Integer userMailId,
                        Integer readerUserMailId,
                        Boolean filterInvalidRecipients,
                        Boolean createCommunications,
                        Boolean changeToReadState,
                        SessionContext ctx) {

        Mail email = getEmail(mailId);
        if (null == email) {
            resultDTO.addResultMessage("Webmail.email.notFound");
            resultDTO.setForward("Fail");
            return;
        }

        if (null != userMailId && null == changeToReadState) {
            changeToReadState = email.getFolder().getUserMailId().equals(userMailId);
        }

        if (userMailId.equals(readerUserMailId)) {
            readerUserMailId = null;
        }


        MailAccount mailAccount = getMailAccount(email.getMailAccount(), userMailId);

        //the email contain only headers
        if (null == email.getIsCompleteEmail() || !email.getIsCompleteEmail()) {
            resultDTO.addResultMessage("Webmail.incompleteEmail.error");
            resultDTO.setForward("Fail");
            return;
        }


        MailDTO mailDTO = new MailDTO();
        DTOFactory.i.copyToDTO(email, mailDTO);
        resultDTO.putAll(mailDTO);

        List<MailRecipient> invalidEmailRecipients = procesEmailContacts(email);

        if (null != mailAccount) {
            resultDTO.put("mailAccountId", mailAccount.getMailAccountId());
        }

        resultDTO.put("to", getRecipientAsString(
                email, WebMailConstants.TO_TYPE_DEFAULT, filterInvalidRecipients, invalidEmailRecipients, false));
        resultDTO.put("cc", getRecipientAsString(
                email, WebMailConstants.TO_TYPE_CC, filterInvalidRecipients, invalidEmailRecipients, false));
        resultDTO.put("bcc", getRecipientAsString(
                email, WebMailConstants.TO_TYPE_BCC, filterInvalidRecipients, invalidEmailRecipients, false));

        resultDTO.put("toWithOutAccount", getRecipientAsString(
                email, readerUserMailId, WebMailConstants.TO_TYPE_DEFAULT, filterInvalidRecipients, invalidEmailRecipients));

        resultDTO.put("ccWithOutAccount", getRecipientAsString(
                email, readerUserMailId, WebMailConstants.TO_TYPE_CC, filterInvalidRecipients, invalidEmailRecipients));

        resultDTO.put("bodyType", WebMailConstants.BODY_TYPE_TEXT);

        resultDTO.put("body", getBody(email, ctx));

        resultDTO.put("from", getFromAsString(email.getMailPersonalFrom(), email.getMailFrom()));

        resultDTO.put("mailFrom", email.getMailFrom());

        resultDTO.put("attachments", getAttachments(email, ctx));

        resultDTO.put("folderType", email.getFolder().getFolderType());

        setFromAddress(email.getMailFrom(), email.getCompanyId());

        if (filterInvalidRecipients) {
            resultDTO.put("invalidRecipients", processInvalidRecipients(invalidEmailRecipients));
        }

        if (!WebMailConstants.FOLDER_OUTBOX.equals(email.getFolder().getFolderType().toString()) && changeToReadState) {
            email.setMailState(MailStateUtil.addReadState(email.getMailState()));
        }

        if (changeToReadState) {
            //because this email no require more notification as new email
            email.setIsNewEmail(false);
        }

        //if email is as draft, set the temporal email id to autosave in this
        if (WebMailConstants.FOLDER_DRAFTITEMS.equals(email.getFolder().getFolderType().toString())) {
            resultDTO.put("tempMailId", email.getMailId());
        }
    }

    private List<MailRecipientDTO> processInvalidRecipients(List<MailRecipient> invalidRecipients) {
        List<MailRecipientDTO> result = new ArrayList<MailRecipientDTO>();
        if (null != invalidRecipients) {
            for (MailRecipient emailRecipient : invalidRecipients) {
                MailRecipientDTO emailRecipientDTO = new MailRecipientDTO();
                DTOFactory.i.copyToDTO(emailRecipient, emailRecipientDTO);
                result.add(emailRecipientDTO);
            }
        }

        return result;
    }

    private List<MailRecipient> procesEmailContacts(Mail email) {
        Collection emailContacts = getEmailContacts(email.getMailId(), email.getCompanyId());
        if (null == emailContacts) {
            return new ArrayList<MailRecipient>();
        }

        List<Map> recipients = new ArrayList<Map>();
        List<Integer> addressIds = new ArrayList<Integer>();
        List<MailRecipient> invalidRecipients = new ArrayList<MailRecipient>();

        for (Object object : emailContacts) {
            MailContact emailContact = (MailContact) object;
            Contact contact = getContact(emailContact.getContactId());
            if (null == contact) {
                continue;
            }

            List<MailRecipient> relatedRecipients = getEmailRecipient(email, emailContact.getEmail());

            if (relatedRecipients.size() != 1) {
                invalidRecipients.addAll(relatedRecipients);
                continue;
            }

            MailRecipient emailRecipient = relatedRecipients.get(0);
            recipients = addContact(recipients,
                    addressIds,
                    emailRecipient.getPersonalName(),
                    emailRecipient.getEmail(),
                    contact.getAddressId(),
                    contact.getContactPersonId());
        }

        for (Object object : emailContacts) {
            MailContact emailContact = (MailContact) object;
            if (!emailContact.getEmail().equals(email.getMailFrom())) {
                continue;
            }

            Contact contact = getContact(emailContact.getContactId());
            if (null == contact) {
                continue;
            }

            if (ContactConstants.OUT_VALUE.equals(contact.getInOut().toString())) {
                continue;
            }

            recipients = addContact(recipients,
                    addressIds,
                    email.getMailPersonalFrom(),
                    email.getMailFrom(),
                    contact.getAddressId(),
                    contact.getContactPersonId());
            break;

        }
        resultDTO.put("contactAddressIds", addressIds);
        resultDTO.put("recipientsWithContact", recipients);
        return invalidRecipients;
    }

    private List<MailRecipient> getEmailRecipient(Mail email, String contactEmail) {
        Collection recipients = email.getMailRecipients();

        List<MailRecipient> result = new ArrayList<MailRecipient>();
        for (Object object : recipients) {
            MailRecipient recipient = (MailRecipient) object;
            if (contactEmail.equals(recipient.getEmail())) {
                result.add(recipient);
            }
        }

        return result;
    }

    private List<Map> addContact(List<Map> recipients,
                                 List<Integer> addressList,
                                 String personalName,
                                 String email,
                                 Integer addressId,
                                 Integer contactPersonId) {

        Map recipient = getRecipient(addressId, recipients);
        if (null == recipient) {
            recipient = createRecipient(personalName, email, addressId);
            recipients.add(recipient);
        }

        if (null == contactPersonId) {
            addressList.add(addressId);
            return recipients;
        }

        addContactPerson(recipient, contactPersonId);

        return recipients;
    }

    private void addContactPerson(Map recipient, Integer contactPersonId) {
        ((List<Integer>) recipient.get("contactPersonOf")).add(contactPersonId);
    }

    private Map createRecipient(String name, String email, Integer addressId) {
        List<Integer> addressList = new ArrayList<Integer>();
        if (null != addressId) {
            addressList.add(addressId);
        }

        Map recipient = new HashMap();
        recipient.put("email", email);
        recipient.put("personalName", name);
        recipient.put("contactPersonOf", new ArrayList<Integer>());
        recipient.put("addressId", addressList);
        return recipient;
    }

    private Map getRecipient(Integer addressId, List<Map> recipients) {
        for (Map recipient : recipients) {
            List<Integer> addressList = (List<Integer>) recipient.get("addressId");
            if (null == addressList) {
                continue;
            }

            if (addressList.isEmpty()) {
                continue;
            }

            Integer recipientAddressId = addressList.get(0);
            if (addressId.equals(recipientAddressId)) {
                return recipient;
            }
        }

        return null;
    }

    private String getFromAsString(String personal, String email) {
        MailRecipientDTO dto = new MailRecipientDTO();
        dto.put("personalName", personal);
        dto.put("email", email);
        return dto.getFormattedRecipient();
    }

    private void setFromAddress(String email, Integer companyId) {
        if (null == email) {
            return;
        }
        TelecomHome telecomHome = (TelecomHome) i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        try {
            Collection telecoms = telecomHome.findTelecomsWithTelecomNumber(email, companyId);
            //Just one coincidence
            if (telecoms.size() == 1) {
                Telecom telecom = (Telecom) telecoms.iterator().next();
                resultDTO.put("fromAddressId", telecom.getAddressId());
                resultDTO.put("fromAddressType", telecom.getAddress().getAddressType());
                if (telecom.getContactPersonId() != null) {
                    resultDTO.put("fromContactPersonId", telecom.getContactPersonId());
                }
            }
        } catch (FinderException e) {
            //nothing to do
        }

    }

    protected List<AttachDTO> getAttachments(Mail email, SessionContext ctx) {
        AttachHome attachHome =
                (AttachHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);

        List attachments = null;
        try {
            attachments = (List) attachHome.findByMailId(email.getMailId(), email.getCompanyId());
        } catch (FinderException e) {
            //
        }

        if (null == attachments) {
            return new ArrayList<AttachDTO>();
        }

        boolean containOnlyEmbeddedAttachments = true;
        List<AttachDTO> result = new ArrayList<AttachDTO>();

        for (int i = 0; i < attachments.size(); i++) {
            Attach attach = (Attach) attachments.get(i);

            Boolean visible = null != attach.getVisible() && attach.getVisible();
            AttachDTO attachDTO = new AttachDTO();
            attachDTO.put("attachId", attach.getAttachId());
            attachDTO.put("attachFile", attach.getAttachName());
            attachDTO.put("visible", visible);

            if (!visible) {
                resultDTO.put(attach.getAttachId().toString(), true);
                containOnlyEmbeddedAttachments = false;
            }

            if (null == attach.getSize()) {
                byte[] attachData = WebmailAttachUtil.i.getAttachData(attach);
                Integer size = attachData.length;
                attach.setSize(size);
            }

            attachDTO.put("size", attach.getSize());
            result.add(attachDTO);
        }

        if (containOnlyEmbeddedAttachments) {
            email.setHaveAttachments(false);
        } else {
            email.setHaveAttachments(true);
        }

        return result;
    }

    protected String getBody(Mail email, SessionContext ctx) {
        if (null == email.getBody()) {
            return "";
        }

        resultDTO.put("bodyType", email.getBody().getBodyType());
        String body = new String(email.getBody().getBodyContent());
        body = body.replaceAll("(&apos;)", "&#39;");
        return body;
    }

    private String getRecipientAsString(Mail email,
                                        String type,
                                        Boolean filterInvalidRecipients,
                                        List<MailRecipient> invalidRecipients,
                                        boolean filterEmailAccount) {


        List recipients = getRecipient(email, Integer.valueOf(type));
        if (recipients.isEmpty()) {
            return "";
        }

        String result = "";
        MailRecipientDTO mailRecipientDTO;
        for (int i = 0; i < recipients.size(); i++) {
            MailRecipient recipient = (MailRecipient) recipients.get(i);
            if (filterInvalidRecipients && invalidRecipients.contains(recipient)) {
                continue;
            }

            if (filterEmailAccount) {
                if (recipient.getEmail().equals(email.getMailAccount())) {
                    continue;
                }
            }

            mailRecipientDTO = new MailRecipientDTO();
            DTOFactory.i.copyToDTO(recipient, mailRecipientDTO);

            result += mailRecipientDTO.getFormattedRecipient();
            if (i < recipients.size() - 1) {
                result += ", ";
            }
        }

        return result;
    }

    /**
     * Read the email recipients associated to type parameter, after of this, filtered the duplicate
     * recipients and remove the recipients associated to email owner account or email reader account.
     *
     * @param email                   <code>Mail</code> object that contain the recipients to build the <code>String</code> object.
     * @param readerUserMailId        <code>Integer</code> object that is the user mail identifier of the user that read the mail.
     * @param type                    Type of recipient (CC recipient or TO recipient).
     * @param filterInvalidRecipients <code>Boolean</code> object to enable or disable the filtered of invalid recipients.
     * @param invalidRecipients       <code>List</code> of <code>MailRecipient</code> all there are invalid recipients
     * @return <code>String</code> object that contain all recipients.
     */
    private String getRecipientAsString(Mail email,
                                        Integer readerUserMailId,
                                        String type,
                                        Boolean filterInvalidRecipients,
                                        List<MailRecipient> invalidRecipients) {

        List<String> readerMailAccounts = new ArrayList<String>();
        if (null != readerUserMailId) {
            List accounts = getMailAccounts(readerUserMailId, email.getCompanyId());
            readerMailAccounts = buildListOfEmails(accounts);
        }


        Map<String, MailRecipient> cacheMap = new HashMap<String, MailRecipient>();

        List recipients = getRecipient(email, Integer.valueOf(type));
        if (recipients.isEmpty()) {
            return "";
        }

        for (int i = 0; i < recipients.size(); i++) {
            MailRecipient recipient = (MailRecipient) recipients.get(i);
            if (filterInvalidRecipients && invalidRecipients.contains(recipient)) {
                continue;
            }

            if (null != readerUserMailId) {
                if (readerMailAccounts.contains(recipient.getEmail())) {
                    continue;
                }
            } else {
                if (recipient.getEmail().equals(email.getMailAccount())) {
                    continue;
                }
            }

            MailRecipient cachedRecipient = cacheMap.get(recipient.getEmail());
            if (null == cachedRecipient) {
                cacheMap.put(recipient.getEmail(), recipient);
                continue;
            }

            if (null == cachedRecipient.getPersonalName() && null != recipient.getPersonalName()) {
                cacheMap.put(recipient.getEmail(), recipient);
            }
        }

        String result = "";
        MailRecipientDTO mailRecipientDTO;
        int i = 0;

        for (Object object : cacheMap.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            MailRecipient recipient = (MailRecipient) entry.getValue();
            mailRecipientDTO = new MailRecipientDTO();
            DTOFactory.i.copyToDTO(recipient, mailRecipientDTO);
            result += mailRecipientDTO.getFormattedRecipient();
            if (i < cacheMap.size() - 1) {
                result += ", ";
            }
            i++;
        }

        return result;
    }

    private List getRecipient(Mail email, Integer type) {
        MailRecipientHome mailRecipientHome =
                (MailRecipientHome) i.getEJBLocalHome(WebMailConstants.JNDI_MAILRECIPIENT);

        List recipients = new ArrayList();
        try {
            Collection ejbCollection = mailRecipientHome.findByMailIdAndToType(email.getMailId(), type, email.getCompanyId());
            recipients.addAll(ejbCollection);
        } catch (FinderException e) {
            log.debug("-> Email mailId=" + email.getMailId() + " not contain recipients of type" + type);
        }
        return recipients;
    }

    private MailAccount getMailAccount(String email, Integer userMailId) {
        MailAccountHome mailAccountHome =
                (MailAccountHome) i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);
        try {
            Collection accounts = mailAccountHome.findAccountsByEmailAndUser(email, userMailId);
            if (accounts.isEmpty()) {
                return null;
            }

            if (accounts.size() > 1) {
                return null;
            }

            return (MailAccount) accounts.toArray()[0];
        } catch (FinderException e) {
            return null;
        }
    }

    private List getMailAccounts(Integer userMailId, Integer companyId) {
        MailAccountHome mailAccountHome =
                (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);

        List result = new ArrayList();
        try {
            result = (List) mailAccountHome.findAccountsByUserMailAndCompany(userMailId, companyId);
        } catch (FinderException e) {
            // the user have not accounts
        }

        return result;
    }

    private List<String> buildListOfEmails(List accounts) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < accounts.size(); i++) {
            MailAccount mailAccount = (MailAccount) accounts.get(i);
            result.add(mailAccount.getEmail());
        }

        return result;
    }

    private Mail getEmail(Integer mailId) {
        MailHome mailHome =
                (MailHome) i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        try {
            return mailHome.findByPrimaryKey(mailId);
        } catch (FinderException e) {
            return null;
        }
    }

    private Collection getEmailContacts(Integer mailId, Integer companyId) {
        MailContactHome mailContactHome =
                (MailContactHome) i.getEJBLocalHome(WebMailConstants.JNDI_MAILCONTACT);
        try {
            return mailContactHome.findByMailId(mailId, companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    private Contact getContact(Integer contactId) {
        ContactHome contactHome =
                (ContactHome) i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        try {
            return contactHome.findByPrimaryKey(contactId);
        } catch (FinderException e) {
            return null;
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

    public boolean isStateful() {
        return false;
    }
}
