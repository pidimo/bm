package com.piramide.elwis.service.webmail;

import com.jatun.commons.email.connection.Account;
import com.jatun.commons.email.connection.ConnectionFactory;
import com.jatun.commons.email.connection.exeception.AuthenticationException;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.exeception.ProviderException;
import com.jatun.commons.email.connection.exeception.SendEmailFailedException;
import com.jatun.commons.email.connection.smtp.SMTPConnection;
//import com.jatun.commons.email.connection.smtp.SMTPFactory;
import com.piramide.elwis.cmd.webmailmanager.SMTPFactory;
import com.jatun.commons.email.model.Email;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.WebmailAccountUtil;
import com.piramide.elwis.utils.webmail.jms.EmailMessage;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.*;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static net.java.dev.strutsejb.dto.EJBFactory.i;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class SentEmailServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(SentEmailServiceBean.class);

    private SessionContext ctx;

    public SentEmailServiceBean() {
    }

    public void sentEmailsSync(Integer userMailId, Integer companyId) {
        UtilService utilService = getUtilService();
        List<EmailMessage> emailMessages = utilService.getOutBoxEmails(userMailId, companyId, true);

        for (EmailMessage emailMessage : emailMessages) {
            sentEmail(emailMessage.getMailId(), emailMessage.getUserMailId(), emailMessage.getFolderId(), true);
        }
    }

    public void sentEmail(Integer mailId,
                          Integer userMailId,
                          Integer folderId,
                          Boolean isConnected) {
        try {
            deliveryMessage(mailId, userMailId, folderId, isConnected);
        } catch (SystemException e) {
            log.error("Cannot sent email for mailId=" + mailId + " folderId=" + folderId, e);
        }
    }

    private void deliveryMessage(Integer mailId,
                                 Integer userMailId,
                                 Integer folderId,
                                 Boolean isConnected) throws SystemException {
        UserTransaction userTransaction = ctx.getUserTransaction();

        Mail dataBaseEmail = getEmail(mailId);

        //email was deleted
        if (null == dataBaseEmail) {
            return;
        }

        //skip the email if it was moved to another folder or it was processed by another MDB
        if (!dataBaseEmail.getFolderId().equals(folderId) || dataBaseEmail.getProcessToSent()) {
            log.debug("-> Skip email mailId=" + dataBaseEmail.getMailId() + " Because it was processed...");
            return;
        }

        //update sentDate field in database email
        updateDataBaseEmailSentDate(dataBaseEmail, userTransaction, userMailId);

        Email email = WebmailAccountUtil.i.buildEmailMessage(dataBaseEmail, userMailId);
        MailAccount mailAccount = getMailAccount(userMailId, dataBaseEmail.getMailAccount());

        Account smtpAccount = WebmailAccountUtil.i.getSmtpAccount(mailAccount);

        ConnectionFactory smtpFactory = new SMTPFactory();
        SMTPConnection smtpConnection = null;
        ConnectionException connectionException = null;
        try {
            log.debug("-> Trying Sent Email mailId=" + mailId + " for userMailId=" + userMailId);
            smtpConnection = (SMTPConnection) smtpFactory.getConnection(smtpAccount);
            smtpConnection.send(email, WebmailAccountUtil.i.getServerSmtpSession());
            log.debug("-> Email mailId=" + mailId + " for userMailId=" + userMailId + " was sent successfully");
        } catch (AuthenticationException e) {
            connectionException = e;
        } catch (ProviderException e) {
            connectionException = e;
        } catch (SendEmailFailedException e) {
            connectionException = e;
        } catch (ConnectionException e) {
            connectionException = e;
        } catch (Exception e) {
            String exceptionMessage = "";
            if (null != e.getCause()) {
                exceptionMessage = e.getCause().toString();
            }
            String logMessage = "Unexpected exception has happened, SentEmailService was unable to send the email with mailId=" +
                    mailId + " to user with userMailId=" + userMailId + " cause : " + exceptionMessage;

            if (!"".equals(exceptionMessage.trim())) {
                log.error(logMessage);
            } else {
                log.error("Unexpected exception has happened, SentEmailService was unable to send the email with mailId=" +
                        mailId + " to user with userMailId=" + userMailId, e);
            }

            connectionException = new ConnectionException(logMessage, e);

        } finally {
            if (null != smtpConnection) {
                smtpConnection.close();
            }
        }

        try {
            userTransaction.begin();
            dataBaseEmail.setProcessToSent(false);

            if (connectionException != null) {
                String exceptionMessage = connectionException.toString();
                if (connectionException.getCause() != null) {
                    exceptionMessage = connectionException.getCause().toString();
                }
                if (isConnected) {

                    saveAccountErrors(userMailId, mailAccount, dataBaseEmail.getMailId(), dataBaseEmail.getCompanyId(), connectionException);

                    log.warn("Cannot send email for mailId=" + mailId + " and userMailId=" + userMailId + ". Cause: " + exceptionMessage);
                } else {
                    log.warn("Cannot send email for mailId=" + mailId + " and userMailId=" + userMailId + ". Cause: " + exceptionMessage);
                }
            } else {
                processDataBaseEmail(dataBaseEmail, userMailId);
            }
            userTransaction.commit();
        } catch (Exception e) {
            log.error("Error when updating email with mailId=" + mailId + " and for userMailId=" + userMailId, e);
            userTransaction.rollback();
        }
    }

    private void saveAccountErrors(Integer userMailId,
                                   MailAccount mailAccount,
                                   Integer mailId,
                                   Integer companyId,
                                   ConnectionException exception) throws Exception {
        log.debug("Save send email error...", exception);

        UtilService utilService = getUtilService();
        if (null == utilService) {
            return;
        }
        utilService.saveSmtpErrors(userMailId, mailAccount, mailId, companyId, exception);
    }

    private void updateDataBaseEmailSentDate(Mail dataBaseEmail, UserTransaction userTransaction, Integer userMailId) throws SystemException {
        TimeZone timeZone = WebmailAccountUtil.i.getTimeZone(userMailId);
        DateTimeZone dateTimeZone = DateTimeZone.forTimeZone(timeZone);
        DateTime dateTime = new DateTime(dateTimeZone);

        try {
            userTransaction.begin();
            dataBaseEmail.setSentDate(dateTime.getMillis());
            dataBaseEmail.setProcessToSent(true);
            userTransaction.commit();
        } catch (Exception e) {
            log.error("-> Update sentDate field for mailId=" + dataBaseEmail.getMailId() + " FAIL ", e);
            userTransaction.rollback();
        }
    }

    private void processDataBaseEmail(Mail dataBaseEmail, Integer userMailId) {
        Folder sentItemsFolder =
                getFolder(userMailId, dataBaseEmail.getCompanyId(), WebMailConstants.FOLDER_SENDITEMS);

        try {
            //for old emails stored in outbox
            if (null == dataBaseEmail.getSaveEmail()) {
                dataBaseEmail.setSaveEmail(true);
            }

            if (dataBaseEmail.getSaveEmail()) {
                dataBaseEmail.setFolderId(sentItemsFolder.getFolderId());
                dataBaseEmail.setIncomingOutgoing(WebMailConstants.OUT_VALUE);
                List emailRecipients = new ArrayList(dataBaseEmail.getMailRecipients());

                for (int i = 0; i < emailRecipients.size(); i++) {
                    MailRecipient recipient = (MailRecipient) emailRecipients.get(i);
                    if (WebMailConstants.TO_TYPE_BCC.equals(recipient.getType().toString())) {
                        removeRecipient(recipient);
                        recipient.remove();
                    }
                }
            } else {
                SaveEmailService saveEmailService = getSaveEmailService();
                saveEmailService.deleteEmail(dataBaseEmail);
            }
        } catch (RemoveException e) {
            log.debug("-> Remove Email Fail");
            dataBaseEmail.setFolderId(sentItemsFolder.getFolderId());
            dataBaseEmail.setIncomingOutgoing(WebMailConstants.OUT_VALUE);
        }
    }

    private MailAccount getMailAccount(Integer userMailId, String emailAccount) {
        MailAccountHome mailAccountHome =
                (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);
        List accounts = null;
        try {
            accounts = (List) mailAccountHome.findAccountsByEmailAndUser(emailAccount, userMailId);
        } catch (FinderException e) {
            //log message show before
        }

        if (null == accounts || accounts.isEmpty()) {
            log.debug("-> Any Account is associated to " + emailAccount + " for user userMailId=" + userMailId);
            return null;
        }

        return (MailAccount) accounts.get(0);
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

    private Folder getFolder(Integer userMailId, Integer companyId, String folderType) {
        FolderHome folderHome = (FolderHome) i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);

        try {
            return folderHome.findByFolderType(userMailId, Integer.valueOf(folderType), companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    private UtilService getUtilService() {
        UtilServiceHome utilServiceHome =
                (UtilServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_UTIL_SERVICE);

        try {
            return utilServiceHome.create();
        } catch (CreateException e) {
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

    private void removeRecipient(MailRecipient recipient) throws RemoveException {
        EmailRecipientAddressHome emailRecipientAddressHome =
                (EmailRecipientAddressHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILRECIPIENTADDRESS);
        try {
            List elements =
                    (List) emailRecipientAddressHome.findByMailRecipientId(recipient.getMailRecipientId(),
                            recipient.getCompanyId());
            for (int i = 0; i < elements.size(); i++) {
                EmailRecipientAddress emailRecipientAddress = (EmailRecipientAddress) elements.get(i);
                emailRecipientAddress.remove();
            }
        } catch (FinderException e) {
            // The recipient no registred EmailRecipientsAddress
        }
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
}
