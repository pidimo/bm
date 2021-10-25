package com.piramide.elwis.utils.webmail;

import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.domain.supportmanager.SupportContact;
import com.piramide.elwis.domain.supportmanager.SupportContactHome;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class WebmailUtil {
    private Log log = LogFactory.getLog(WebmailUtil.class);

    public static WebmailUtil i = new WebmailUtil();

    private WebmailUtil() {
    }


    public Folder getSystemFolder(Integer userMailId, Integer folderType, Integer companyId) {
        FolderHome folderHome =
                (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);
        try {
            return folderHome.findByFolderType(userMailId, folderType, companyId);
        } catch (FinderException e) {
            log.debug("-> Read Webmail SystemFolder userMailId=" + userMailId + " Fail");
        }

        return null;
    }

    public Folder getFolder(Integer folderId) {
        FolderHome folderHome =
                (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);
        try {
            return folderHome.findByPrimaryKey(folderId);
        } catch (FinderException e) {
            log.debug("-> Read Webmail Folder folderId=" + folderId + " Fail");
        }

        return null;
    }

    public List getEmails(Integer folderId, Integer companyId) {
        MailHome mailHome = (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);

        List result = new ArrayList();
        try {
            result = (List) mailHome.findByFolderId(folderId, companyId);
        } catch (FinderException e) {
            log.debug("-> Read emails for folderId=" + folderId + " FAIL");
        }

        return result;
    }

    public EmailSource getEmailSource(Integer mailId, Integer companyId) {
        EmailSourceHome emailSourceHome =
                (EmailSourceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILSOURCE);
        try {
            return emailSourceHome.findByMailId(mailId, companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    /**
     * This method verifies if email have relations with contacts, actions or supportActivities
     *
     * @param email Mail Object
     * @return Map than contain the next keys:
     *         haveMailContacts     : true if email have relation with contact false in other case
     *         isAction             : true if email have relacion with action false in other case
     *         isSupportActivity    : true if email hace relation with supportActivity false in other case
     *         allRecipientsRelated : true if all recipients (to) of email have relations with contacts.
     */
    public Map emailIsRelatedWithCommunication(Mail email) {
        Map resultDTO = new HashMap();

        resultDTO.put("haveMailContacts", false);
        resultDTO.put("isAction", false);
        resultDTO.put("isSupportActivity", false);
        resultDTO.put("allRecipientsRelated", true);

        Integer incomingOutGoing = email.getIncomingOutgoing();
        if (null == incomingOutGoing) {
            return resultDTO;
        }

        List emailCommunications = getEmailCommunications(email.getMailId(), email.getCompanyId());

        // email no contains relationship with communications.
        if (emailCommunications.isEmpty()) {
            return resultDTO;
        }

        resultDTO.put("haveMailContacts", true);

        //because in emails are related only with one communication.
        if (WebMailConstants.IN_VALUE == incomingOutGoing) {
            return resultDTO;
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

        return resultDTO;
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
