/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

import com.piramide.elwis.domain.webmailmanager.EmailSource;
import com.piramide.elwis.domain.webmailmanager.Folder;
import com.piramide.elwis.domain.webmailmanager.Mail;
import com.piramide.elwis.domain.webmailmanager.MailHome;
import com.piramide.elwis.utils.JMSUtil;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.WebmailUtil;
import com.piramide.elwis.utils.webmail.jms.DeleteEmailMessage;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteEmailServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(this.getClass());

    private SessionContext ctx;

    public DeleteEmailServiceBean() {
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        this.ctx = sessionContext;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void deleteSelectedEmails(List identifiers, Integer userMailId) {
        try {
            deleteEmails(identifiers, userMailId);
        } catch (SystemException e) {
            log.error("-> Delete Emails form Data Base userMailId =" + userMailId + " FAIL", e);
        }
    }

    public void emptyTrashFolder(Integer userMailId, Integer companyId) {
        try {
            deleteEmailsFromTrashFolder(userMailId, companyId);
        } catch (SystemException e) {
            log.error("-> Delete Emails form Data Base userMailId =" + userMailId + " FAIL", e);
        }
    }

    public void deleteEmailsByfolder(Integer folderId, Integer userMailId) {
        try {
            deleteEmailsFromFolder(folderId, userMailId);
        } catch (SystemException e) {
            log.error("-> Delete Emails form Data Base userMailId =" + userMailId + " FAIL", e);
        }
    }

    private void deleteEmailsFromFolder(Integer folderId, Integer userMailId) throws SystemException {
        UserTransaction transaction = ctx.getUserTransaction();

        try {
            transaction.begin();

            Folder folder = WebmailUtil.i.getFolder(folderId);

            if (null == folder) {
                return;
            }

            List<Mail> emailsToDeleteList = getEmailsToDeleteByFolder(folder.getFolderId(), folder.getCompanyId());

            Map incompleteEmails = filterIncompleteEmails(emailsToDeleteList);

            deleteDataBaseEmails(emailsToDeleteList);

            transaction.commit();

            deleteEmailMessagesFromPOPServer(incompleteEmails, userMailId);
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    private void deleteEmails(List identifiers, Integer userMailId) throws SystemException {
        UserTransaction transaction = ctx.getUserTransaction();

        try {
            transaction.begin();
            List<Mail> emailsToDeleteList = getEmailsToDelete(identifiers);

            Map incompleteEmails = filterIncompleteEmails(emailsToDeleteList);

            deleteDataBaseEmails(emailsToDeleteList);

            transaction.commit();

            deleteEmailMessagesFromPOPServer(incompleteEmails, userMailId);
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    private void deleteEmailsFromTrashFolder(Integer userMailId, Integer companyId) throws SystemException {

        UserTransaction transaction = ctx.getUserTransaction();

        try {
            transaction.begin();

            Folder trash = WebmailUtil.i.getSystemFolder(userMailId,
                    Integer.valueOf(WebMailConstants.FOLDER_TRASH),
                    companyId);

            if (null == trash) {
                return;
            }

            List<Mail> emailsToDeleteList = getEmailsToDeleteByFolder(trash.getFolderId(), trash.getCompanyId());

            Map incompleteEmails = filterIncompleteEmails(emailsToDeleteList);

            deleteDataBaseEmails(emailsToDeleteList);

            transaction.commit();

            deleteEmailMessagesFromPOPServer(incompleteEmails, userMailId);
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    private List<Mail> getEmailsToDeleteByFolder(Integer folderId, Integer companyId) {
        List<Mail> emailsToDeleteList = new ArrayList<Mail>();

        List emailsByFolderList = WebmailUtil.i.getEmails(folderId, companyId);
        if (emailsByFolderList.isEmpty()) {
            return emailsToDeleteList;
        }

        for (int i = 0; i < emailsByFolderList.size(); i++) {
            Mail email = (Mail) emailsByFolderList.get(i);

            if (!email.getIsCompleteEmail()) {
                emailsToDeleteList.add(email);
                continue;
            }

            //email contain some relacion with contacts, actions or supportActivities
            if (!canDeleteEmail(email)) {
                email.setHidden(true);
                continue;
            }

            emailsToDeleteList.add(email);
        }

        return emailsToDeleteList;
    }

    private List<Mail> getEmailsToDelete(List identifiers) {
        List<Mail> emailsToDeleteList = new ArrayList<Mail>();

        for (int i = 0; i < identifiers.size(); i++) {
            //null ids are skipped
            if (null == identifiers.get(i)) {
                continue;
            }

            //bad ids are skipped
            Integer emailId;
            try {
                emailId = new Integer(identifiers.get(i).toString());
            } catch (NumberFormatException e) {
                continue;
            }

            Mail email = getEmail(emailId);
            if (null == email) {
                continue;
            }

            if (!email.getIsCompleteEmail()) {
                emailsToDeleteList.add(email);
                continue;
            }

            //email contain some relacion with contacts, actions or supportActivities
            if (!canDeleteEmail(email)) {
                email.setHidden(true);
                continue;
            }

            emailsToDeleteList.add(email);
        }

        return emailsToDeleteList;
    }

    private Boolean canDeleteEmail(Mail email) {
        Map readResultDto = WebmailUtil.i.emailIsRelatedWithCommunication(email);
        Boolean isAction = (Boolean) readResultDto.get("isAction");
        Boolean haveMailContacts = (Boolean) readResultDto.get("haveMailContacts");
        Boolean isActivity = (Boolean) readResultDto.get("isSupportActivity");
        return !isAction && !haveMailContacts && !isActivity;
    }

    private void deleteEmailMessagesFromPOPServer(Map<String, List<String>> incompleteEmails, Integer userMailId) {
        DeleteEmailMessage deleteEmailMessage = new DeleteEmailMessage(userMailId, incompleteEmails);
        try {
            JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_QUEUE_DELETE_EMAIL, deleteEmailMessage, false);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void deleteDataBaseEmails(List emailsToDeleteList) {
        for (int i = 0; i < emailsToDeleteList.size(); i++) {
            Mail email = (Mail) emailsToDeleteList.get(i);
            try {
                deleteDataBaseEmail(email);
            } catch (RemoveException e) {
                log.error("Delete Email from DataBase Fail", e);
                continue;
            }
        }
    }

    private void deleteDataBaseEmail(Mail dataBaseEmail) throws RemoveException {
        log.debug("-> trying delete mailId=" + dataBaseEmail.getMailId());
        if (null != dataBaseEmail.getBodyId()) {
            dataBaseEmail.getBody().remove();
        }

        EmailSource emailSource = WebmailUtil.i.getEmailSource(dataBaseEmail.getMailId(), dataBaseEmail.getCompanyId());
        if (null != emailSource) {
            emailSource.remove();
        }

        dataBaseEmail.remove();
    }

    private Map<String, List<String>> filterIncompleteEmails(List<Mail> emails) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        for (int i = 0; i < emails.size(); i++) {
            Mail mail = emails.get(i);
            if (mail.getIsCompleteEmail()) {
                continue;
            }

            List<String> messageIds = result.get(mail.getMailAccount());
            if (null == messageIds) {
                messageIds = new ArrayList<String>();
                messageIds.add(mail.getMessageId());
                result.put(mail.getMailAccount(), messageIds);
            } else {
                messageIds.add(mail.getMessageId());
                result.put(mail.getMailAccount(), messageIds);
            }
        }

        return result;
    }

    private Mail getEmail(Integer mailId) {
        MailHome mailHome =
                (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        try {
            return mailHome.findByPrimaryKey(mailId);
        } catch (FinderException e) {
            return null;
        }
    }
}
