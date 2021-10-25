/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

import com.jatun.commons.email.connection.exeception.AuthenticationException;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.exeception.ProviderException;
import com.jatun.commons.email.connection.exeception.SendEmailFailedException;
import com.jatun.commons.email.model.Address;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.webmailmanager.EmailAccountErrorDTO;
import com.piramide.elwis.dto.webmailmanager.EmailAccountErrorDetailDTO;
import com.piramide.elwis.service.webmail.downloadlog.MyCustomEvent;
import com.piramide.elwis.service.webmail.downloadlog.MyCustomEventHandler;
import com.piramide.elwis.service.webmail.downloadlog.MyCustomListener;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.JMSUtil;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.utils.webmail.jms.EmailMessage;
import com.piramide.elwis.utils.webmail.jms.UserMessage;
import com.piramide.elwis.utils.webmail.jms.WebmailAccountMessage;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.jms.JMSException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;
import java.util.*;

public class UtilServiceBean implements SessionBean, MyCustomListener {
    private static final Log LOG = LogFactory.getLog(UtilServiceBean.class);

    public static final Long APPLICATION_BLOB_SIZE = new Long(ConfigurationFactory.getValue("elwis.db.blob.size"));

    private SessionContext ctx;

    public UtilServiceBean() {
    }

    public void sentEmailMessages(UserMessage userMessage) {
        UserMail userMail = getUserMail(userMessage.getUserMailId());
        if (null == userMail) {
            return;
        }

        List<EmailMessage> emails = getOutBoxEmails(userMail.getUserMailId(),
                userMail.getCompanyId(),
                userMessage.getConnected());

        for (EmailMessage emailMessage : emails) {
            queueJMSEmailMessageToSendEmail(emailMessage);
        }
    }

    public void downloadEmails(UserMessage userMessage) {
        UserMail userMail = getUserMail(userMessage.getUserMailId());
        if (null == userMail) {
            return;
        }

        List<WebmailAccountMessage> accounts = buildEmailAccountMessages(userMail, userMessage.getConnected());

        List<Integer> accountIdList = new ArrayList<Integer>();

        for (WebmailAccountMessage accountMessage : accounts) {
            accountMessage.setMillisKey(userMessage.getMillisKey());

            queueJMSWebmailAccountMessageToDownloadEmail(accountMessage);

            accountIdList.add(accountMessage.getMailAccountId());
        }

        DownloadLogFactory.i.log(this.getClass(), "Queue of accounts for download: userMailId=" + userMail.getUserMailId() + " mailAccountIds=" + accountIdList);
    }

    public void savePopErrors(Integer userMailId,
                              MailAccount mailAccount,
                              Integer companyId,
                              ConnectionException exception) throws Exception {

        WebMailConstants.EmailAccountErrorType errorType = WebMailConstants.EmailAccountErrorType.POP_SERVICE;
        if (exception instanceof AuthenticationException) {
            errorType = WebMailConstants.EmailAccountErrorType.POP_AUTHENTICATION;
        }
        if (exception instanceof ProviderException) {
            errorType = WebMailConstants.EmailAccountErrorType.POP_PROVIDER;
        }

        String causedBy = getErrorCause(exception);
        saveAccountErrors(userMailId, mailAccount, companyId, errorType, causedBy);
    }

    public void saveSmtpErrors(Integer userMailId,
                               MailAccount mailAccount,
                               Integer mailId,
                               Integer companyId,
                               ConnectionException exception) throws Exception {

        List<Map> accountErrorDetailList = new ArrayList<Map>();

        WebMailConstants.EmailAccountErrorType errorType = WebMailConstants.EmailAccountErrorType.SMTP_SERVICE;
        if (exception instanceof AuthenticationException) {
            errorType = WebMailConstants.EmailAccountErrorType.SMTP_AUTHENTICATION;
        }
        if (exception instanceof ProviderException) {
            errorType = WebMailConstants.EmailAccountErrorType.SMTP_PROVIDER;
        }
        if (exception instanceof SendEmailFailedException) {
            accountErrorDetailList = getSendAccountEmailErrorsDetail(mailId, (SendEmailFailedException) exception);
            errorType = WebMailConstants.EmailAccountErrorType.SMTP_SENDFAILED;
        }

        String causedBy = getErrorCause(exception);

        saveAccountErrors(userMailId, mailAccount, companyId, errorType, causedBy, accountErrorDetailList);
    }

    public List<WebmailAccountMessage> buildWebmailAccountMessages(Integer userMailId, Boolean isConnected) {
        UserMail userMail = getUserMail(userMailId);
        if (null == userMail) {
            return new ArrayList<WebmailAccountMessage>();
        }

        return buildEmailAccountMessages(userMail, isConnected);
    }

    public List<EmailMessage> getOutBoxEmails(Integer userMailId, Integer companyId, Boolean isConnected) {
        Folder outBoxFolder = getFolder(userMailId, companyId, WebMailConstants.FOLDER_OUTBOX);

        if (null == outBoxFolder) {
            return new ArrayList<EmailMessage>();
        }

        Integer outBoxFolderId = outBoxFolder.getFolderId();

        List<Integer> idList = getEmailsIdsToSend(outBoxFolderId, companyId);
        List<EmailMessage> result = new ArrayList<EmailMessage>();

        for (Integer id : idList) {
            EmailMessage message = new EmailMessage(id);
            message.setFolderId(outBoxFolderId);
            message.setUserMailId(userMailId);
            message.setConnected(isConnected);
            result.add(message);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Integer> getEmailsIdsToSend(Integer outBoxFolderId, Integer companyId) {
        MailHome mailHome = (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);

        List<Integer> result = new ArrayList<Integer>();
        try {
            result.addAll(mailHome.selectEmailIdsToSend(outBoxFolderId, companyId));
        } catch (FinderException e) {
            // No results found
        }

        return result;
    }

    private void saveAccountErrors(Integer userMailId,
                                   MailAccount account,
                                   Integer companyId,
                                   WebMailConstants.EmailAccountErrorType errorType,
                                   String causedBy) throws CreateException {

        saveAccountErrors(userMailId, account, companyId, errorType, causedBy, new ArrayList<Map>());
    }

    private void saveAccountErrors(Integer userMailId,
                                   MailAccount account,
                                   Integer companyId,
                                   WebMailConstants.EmailAccountErrorType errorType,
                                   String causedBy,
                                   List<Map> accountErrorDetailList) throws CreateException {

        EmailAccountErrorHome emailAccountErrorHome = (EmailAccountErrorHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILACCOUNTERROR);

        if (!errorAlreadyRegistered(userMailId, account, errorType.getConstant(), causedBy)) {

            EmailAccountErrorDTO dto = new EmailAccountErrorDTO();
            dto.put("errorType", errorType.getConstant());
            dto.put("companyId", companyId);
            dto.put("timeError", (new Date()).getTime());
            dto.put("userMailId", userMailId);
            dto.put("causedBy", causedBy);

            if (null != account) {
                dto.put("mailAccountId", account.getMailAccountId());
            }

            EmailAccountError emailAccountError = emailAccountErrorHome.create(dto);

            if (emailAccountError != null) {
                //create detail if exist
                EmailAccountErrorDetailHome emailAccountErrorDetailHome = (EmailAccountErrorDetailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILACCOUNTERRORDETAIL);
                for (Map detailMap : accountErrorDetailList) {

                    Integer mailIdentifier = (Integer) detailMap.get("mailIdentifier");

                    if (!existEmailAccountErrorDetail(emailAccountError.getEmailAccountErrorId(), mailIdentifier)) {

                        EmailAccountErrorDetailDTO detailDTO = new EmailAccountErrorDetailDTO();
                        detailDTO.put("emailAccountErrorId", emailAccountError.getEmailAccountErrorId());
                        detailDTO.put("companyId", companyId);
                        detailDTO.put("subject", detailMap.get("subject"));
                        detailDTO.put("emails", detailMap.get("emails"));
                        detailDTO.put("mailIdentifier", mailIdentifier);

                        emailAccountErrorDetailHome.create(detailDTO);
                    }
                }
            }
        }
    }

    private boolean errorAlreadyRegistered(Integer userMailId, MailAccount account, Integer errorType, String causedBy) {
        EmailAccountErrorHome emailAccountErrorHome = (EmailAccountErrorHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILACCOUNTERROR);
        try {
            Collection collection = emailAccountErrorHome.findByCause(userMailId, account.getMailAccountId(), errorType, account.getCompanyId(), causedBy);
            return collection.size() > 0;
        } catch (FinderException ignore) {
        }
        return false;
    }

    private String getErrorCause(ConnectionException e) {
        String cause = null;

        if (e != null) {
            Throwable throwable = e;

            //get the last exception message
            while (throwable.getCause() != null && !throwable.equals(throwable.getCause())) {

                cause = throwable.getCause().getMessage();

                //special message
                if (throwable.getCause() instanceof AddressException) {
                    AddressException addressException = (AddressException) throwable.getCause();
                    if (addressException.getRef() != null) {
                        cause = cause + "; " + addressException.getRef();
                        break;
                    }
                }

                //get the last exception
                throwable = throwable.getCause();
            }

            if (cause != null && cause.length() >= 250) {
                cause = cause.substring(0, 240) + "...";
            }
        }
        return cause;
    }

    private boolean existEmailAccountErrorDetail(Integer emailAccountErrorId, Integer mailIdentifier) {
        EmailAccountErrorDetailHome emailAccountErrorDetailHome = (EmailAccountErrorDetailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILACCOUNTERRORDETAIL);
        try {
            Collection collection = emailAccountErrorDetailHome.findByEmailAccountErrorIdMailIdentifier(emailAccountErrorId, mailIdentifier);
            if (collection.size() > 0) {
                return true;
            }
        } catch (FinderException ignore) {
        }
        return false;
    }

    private List<Map> getSendAccountEmailErrorsDetail(Integer mailId, SendEmailFailedException e) {
        List<Map> accountErrorDetailList = new ArrayList<Map>();
        Mail mail = getEmail(mailId);
        if (mail!= null) {
            String emails = "";

            List<Address> invalidEmailList = e.getInvalidEmail();
            for (int i = 0; i < invalidEmailList.size(); i++) {
                Address emailAddress = invalidEmailList.get(i);
                if (!emails.isEmpty()) {
                    emails += ", ";
                }
                emails += emailAddress.getEmail();
            }

            if (emails.length() >= 250) {
                emails = emails.substring(0, 240) + "...";
            }

            Map detailMap = new HashMap();
            detailMap.put("mailIdentifier", mail.getMailId());
            detailMap.put("subject", mail.getMailSubject());
            detailMap.put("emails", emails);
            accountErrorDetailList.add(detailMap);
        }
        return accountErrorDetailList;
    }

    private List<WebmailAccountMessage> buildEmailAccountMessages(UserMail userMail, Boolean isConnected) {
        Integer userMailId = userMail.getUserMailId();
        Integer companyId = userMail.getCompanyId();

        Integer inboxFolderId = getFolder(userMailId, companyId, WebMailConstants.FOLDER_INBOX).getFolderId();

        List dataBaseAccounts = getUserMailAccounts(userMailId, companyId);

        List<WebmailAccountMessage> result = new ArrayList<WebmailAccountMessage>();

        WebmailAccountMessage accountMessage;
        for (int i = 0; i < dataBaseAccounts.size(); i++) {
            MailAccount dataBaseAccount = (MailAccount) dataBaseAccounts.get(i);

            Boolean automaticForward = dataBaseAccount.getAutomaticForward();
            String forwardEmail = dataBaseAccount.getForwardEmail();

            Boolean automaticReply = dataBaseAccount.getAutomaticReply();
            String automaticReplySubject = dataBaseAccount.getAutomaticReplyMessageSubject();

            accountMessage = new WebmailAccountMessage(dataBaseAccount.getMailAccountId());
            accountMessage.setUserMailId(userMailId);
            accountMessage.setCompanyId(companyId);

            accountMessage.setLastDownloadTime(dataBaseAccount.getLastDownloadTime());

            //automatic forward configuration
            accountMessage.setAutoForward(automaticForward);
            accountMessage.setForwardEmail(forwardEmail);

            //automatic reply configuration
            accountMessage.setAutoReply(automaticReply);
            accountMessage.setAutoReplyMessage(readFreeTextValue(dataBaseAccount.getReplyMessageTextId()));
            accountMessage.setAutoReplyMessageHtml(readFreeTextValue(dataBaseAccount.getReplyMessageHtmlId()));
            accountMessage.setAutoReplySubject(automaticReplySubject);

            //now setting up elwis.db.blob.size
            accountMessage.setApplicationMaxSize(APPLICATION_BLOB_SIZE);

            //setting up inbox folder id
            accountMessage.setInboxFolderId(inboxFolderId);
            accountMessage.setConnected(isConnected);

            result.add(accountMessage);
        }

        return result;
    }

    private void queueJMSWebmailAccountMessageToDownloadEmail(WebmailAccountMessage message) {
        try {
            JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_QUEUE_DOWNLOAD_EMAIL, message, false);

            queueAccountDownloadLog(message);

        } catch (NamingException e) {
            LOG.error("Cannot queue the JMS WebmailAccountMessage to download emails.", e);
        } catch (JMSException e) {
            LOG.error("Cannot queue the JMS WebmailAccountMessage to download emails.", e);
        }
    }

    private void queueAccountDownloadLog(WebmailAccountMessage message) {
        MyCustomEvent myCustomEvent = new MyCustomEvent(this, "QUEUE_ACCOUNT", message.getMillisKey(), message.getMailAccountId());
        fire(myCustomEvent);
    }

    public void fire(MyCustomEvent event) {
        MyCustomEventHandler.fire(event);
    }

    private void queueJMSEmailMessageToSendEmail(EmailMessage message) {
        try {
            JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_QUEUE_SENT_EMAIL, message, false);
        } catch (NamingException e) {
            LOG.error("Cannot queue the JMS EmailMessage to send emails.", e);
        } catch (JMSException e) {
            LOG.error("Cannot queue the JMS EmailMessage to send emails.", e);
        }
    }

    private Folder getFolder(Integer userMailId, Integer companyId, String folderType) {
        FolderHome folderHome = (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);
        try {
            return folderHome.findByFolderType(userMailId, Integer.valueOf(folderType), companyId);
        } catch (FinderException e) {
            //Folder not found
        }

        return null;
    }

    private List getUserMailAccounts(Integer userMailId, Integer companyId) {
        MailAccountHome mailAccountHome =
                (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);
        try {
            return (List) mailAccountHome.findAccountsByUserMailAndCompany(userMailId, companyId);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    private UserMail getUserMail(Integer userMailId) {
        UserMailHome userMailHome = (UserMailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);
        try {
            return userMailHome.findByPrimaryKey(userMailId);
        } catch (FinderException e) {
            return null;
        }
    }

    private Mail getEmail(Integer mailId) {
        MailHome mailHome = (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        if (mailId != null) {
            try {
                return mailHome.findByPrimaryKey(mailId);
            } catch (FinderException ignore) {
            }
        }
        return null;
    }

    private FreeText findFreeText(Integer freeTextId) {
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        if (freeTextId != null) {
            try {
                return freeTextHome.findByPrimaryKey(freeTextId);
            } catch (FinderException ignore) {
            }
        }
        return null;
    }

    private String readFreeTextValue(Integer freeTextId) {
        String value = "";
        FreeText freeText = findFreeText(freeTextId);
        if (freeText != null) {
            value = new String(freeText.getValue());
        }
        return value;
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
