package com.piramide.elwis.utils.webmail;

import com.jatun.commons.email.connection.Account;
import com.jatun.commons.email.connection.ConnectionFactory;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.pop.Mime4JEmailBuilder;
import com.jatun.commons.email.connection.pop.POPConnection;
//import com.jatun.commons.email.connection.pop.POPFactory;
import com.piramide.elwis.cmd.webmailmanager.POPFactory;
import com.jatun.commons.email.model.*;
import com.jatun.commons.email.template.EmailForwardBuilder;
import com.jatun.commons.email.template.EmailReplyBuilder;
import com.jatun.commons.email.template.EmailResumeBuilder;
import com.jatun.commons.email.template.ResumeConfiguration;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.webmailmanager.MailAccount;
import com.piramide.elwis.domain.webmailmanager.MailHome;
import com.piramide.elwis.domain.webmailmanager.UidlTrack;
import com.piramide.elwis.domain.webmailmanager.UidlTrackHome;
import com.piramide.elwis.service.webmail.DownloadLogFactory;
import com.piramide.elwis.service.webmail.SaveEmailService;
import com.piramide.elwis.service.webmail.SaveEmailServiceHome;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.utils.logging.LogInfoFactory;
import com.piramide.elwis.utils.webmail.jms.WebmailAccountMessage;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.mail.internet.MailDateFormat;
import javax.transaction.UserTransaction;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class DownloadEmailUtil {
    private Log log = LogFactory.getLog(DownloadEmailUtil.class);

    private UserTransaction userTransaction;
    private WebmailAccountMessage accountMessage;

    public DownloadEmailUtil(WebmailAccountMessage accountMessage,
                             UserTransaction userTransaction) {
        this.accountMessage = accountMessage;
        this.userTransaction = userTransaction;

    }


    public void downloadEmails() throws ConnectionException {
        POPConnection connection = null;
        try {
            //log.debug("Starting the download of mails for mail account Id:" + accountMessage.getMailAccountId());
            DownloadLogFactory.i.log(this.getClass(), " Starting the download of mails for mail account Id:" + accountMessage.getMailAccountId() + "  millisKey= " + accountMessage.getMailAccountId() + "A" + DownloadLogFactory.dateTimeLog(accountMessage.getMillisKey()));

            connection = getPOPConnection();
            List uidlList = getUidlList(accountMessage.getMailAccountId());

            //todo: when ELWIS-3025 is resolved, remove this flag
            if (LogInfoFactory.i.isActiveLog()) {
                uidlList = new ArrayList();
            }

            List<LightlyHeader> lightlyHeaderList = connection.getNewEmailLightlyHeaders(uidlList);

            storeEmails(lightlyHeaderList, connection);

            //delete email from POP server after x days
            deleteEmailFromPOPAfterXDays(accountMessage.getMailAccountId(), connection);

            //show logs if exist eml with parse error in cache
            WebmailCacheTimerUtil.i.logEmlMessagesWithParseError();

            //log.debug("Ending the download of mails for mail account Id:" + accountMessage.getMailAccountId());
            DownloadLogFactory.i.log(this.getClass(), " Ending the download of mails for mail account Id:" + accountMessage.getMailAccountId() + "  millisKey= " + accountMessage.getMailAccountId() + "A" + DownloadLogFactory.dateTimeLog(accountMessage.getMillisKey()));
        } finally {
            if (null != connection) {
                connection.close();
            }

        }
    }

    private void storeEmails(List<LightlyHeader> lightlyHeaderList, POPConnection connection) throws ConnectionException {
        MailAccount mailAccount = WebmailAccountUtil.i.getMailAccount(accountMessage.getMailAccountId());
        if (null == mailAccount) {
            throw new ConnectionException("Read MailAccount FAIL");
        }

        Boolean deleteEmailFromServer = true;
        if (null != mailAccount.getKeepEmailOnServer()) {
            deleteEmailFromServer = !mailAccount.getKeepEmailOnServer();
        }

        List<Header> bigEmails = new ArrayList<Header>();
        List<Email> emailsToReplyOrForward = new ArrayList<Email>();
        List<String> uidlToDeleteList = new ArrayList<String>();

        ByteArrayOutputStream byteArrayOutputStream;

        for (int i = 0; i < lightlyHeaderList.size(); i++) {
            LightlyHeader lightlyHeader = lightlyHeaderList.get(i);

            try {

                //this because the inbox folder can be closed by some unknown reason
                if (!connection.isFolderOpen()) {
                    connection.reopenFolder();
                }

                if (lightlyHeader.getSize() > accountMessage.getApplicationMaxSize()) {
                    Header bigHeader = connection.getEmailHeader(lightlyHeader.getUidl());
                    if (bigHeader != null) {
                        bigEmails.add(bigHeader);
                    }
                    continue;
                }

                //try download email eml
                byteArrayOutputStream = new ByteArrayOutputStream();
                connection.getEmailByUidl(lightlyHeader.getUidl(), false, byteArrayOutputStream);

                Email email = buildParsedEmail(byteArrayOutputStream, lightlyHeader);

                if (email != null) {
                    try {
                        //initialize the transaction
                        userTransaction.begin();
                        if (existsMessageInDataBase(email.getHeader(), mailAccount)) {
                            //log.debug("-> " + email.getHeader().getMessageId() + " already stored in application ");
                            DownloadLogFactory.i.log(this.getClass(), "-> " + email.getHeader().getMessageId() + " already stored in application mailAccountId=" + mailAccount.getMailAccountId());
                            userTransaction.commit();
                            continue;
                        }
                        storeEmail(email, accountMessage, mailAccount, byteArrayOutputStream);

                        userTransaction.commit();
                    } catch (Exception e) {
                        log.error("Error in save the downloaded email of mailAccountId:" + accountMessage.getMailAccountId() + ", Message UIDL:" + lightlyHeader.getUidl(), e);
                        userTransaction.rollback();
                        continue;
                    }

                    if (accountMessage.getAutoForward() || accountMessage.getAutoReply()) {
                        emailsToReplyOrForward.add(email);
                    }

                } else {
                    //because cannot parse all email, try save only with your headers, without body.
                    Header header = buildParsedHeader(byteArrayOutputStream, lightlyHeader);
                    if (header == null) {
                        continue;
                    }

                    try {
                        //initialize the transaction
                        userTransaction.begin();
                        if (existsMessageInDataBase(header, mailAccount)) {
                            userTransaction.commit();
                            continue;
                        }
                        storeEmailWithoutBody(header, accountMessage, mailAccount, byteArrayOutputStream);

                        userTransaction.commit();
                    } catch (Exception e) {
                        log.error("Error in save the downloaded email without body, mailAccountId:" + accountMessage.getMailAccountId() + ", Message UIDL:" + lightlyHeader.getUidl(), e);
                        userTransaction.rollback();
                        continue;
                    }
                }

                if (deleteEmailFromServer) {
                    uidlToDeleteList.add(lightlyHeader.getUidl());
                }

            } catch (Exception e) {
                log.error("There was an exception trying to download and save an email from POP server. accountId:" + accountMessage.getMailAccountId() +", Message UIDL:"+ lightlyHeader.getUidl(), e);

                if (!connection.isFolderOpen()) {
                    try {
                        connection.reopenFolder();
                    } catch (Exception e1) {
                        log.debug("Error in reopen folder... ", e1);
                    }
                }
            }
        }

        //this because the inbox folder can be closed by some unknown reason
        if (!connection.isFolderOpen()) {
            connection.reopenFolder();
        }

        if (deleteEmailFromServer) {
            connection.deleteEmails(uidlToDeleteList);
        }

        deliveryForwardOrReplyMessages(emailsToReplyOrForward, mailAccount);

        sentBigMessagesToSender(bigEmails, connection);
    }

    private void storeEmail(Email email,
                            WebmailAccountMessage accountMessage,
                            MailAccount mailAccount, ByteArrayOutputStream emailSource) {

        SaveEmailService saveEmailService = getSaveEmailService();
        if (null == saveEmailService) {
            log.error("Cannot Store email headers because SaveEmailService is unavailable!!!");
            throw new RuntimeException();
        }

        //save all email
        saveEmailService.storeEmail(email, accountMessage, mailAccount, emailSource);
    }

    private void storeEmailWithoutBody(Header header, WebmailAccountMessage accountMessage, MailAccount mailAccount, ByteArrayOutputStream emailSource) {

        SaveEmailService saveEmailService = getSaveEmailService();
        if (null == saveEmailService) {
            log.error("Cannot Store email without body because SaveEmailService is unavailable!!!");
            throw new RuntimeException();
        }
        saveEmailService.storeEmailWithoutBody(header, accountMessage, mailAccount, emailSource);
    }

    private void deliveryForwardOrReplyMessages(List<Email> sourceEmails,
                                                MailAccount mailAccount) {
        try {
            if (accountMessage.getAutoForward()) {
                sentForwardEmails(sourceEmails, mailAccount);
            }
        } catch (Exception e) {
            log.warn("-> Cannot Execute  sentForwardEmails() Method ");
        }

        try {
            if (accountMessage.getAutoReply()) {
                sentReplyEmails(sourceEmails, mailAccount);
            }
        } catch (Exception e) {
            log.warn("-> Cannot Execute  sentReplyEmails() Method ");
        }

    }

    private void sentForwardEmails(List<Email> sourceEmails, MailAccount mailAccount) {
        User user = WebmailAccountUtil.i.getElwisUser(accountMessage.getUserMailId());
        if (null == user) {
            log.debug("-> User was deleted and cannot sent Forward email");
            return;
        }

        String accountEmail = mailAccount.getEmail();

        ResourceManager resourceManager = new ResourceManager(user.getFavoriteLanguage());
        ResumeConfiguration resumeConfiguration = getResumeConfiguration(resourceManager);
        String bodyMessage = resourceManager.buildForwardMessageBody(accountEmail);
        TimeZone timeZone = WebmailAccountUtil.i.getTimeZone(user);

        Header forwardHeader;
        EmailForwardBuilder emailForwardBuilder;

        for (Email sourceEmail : sourceEmails) {
            forwardHeader = WebmailAccountUtil.i.getDefaultHeader();
            //because this is automatic forward, set from as sender from
            if (sourceEmail.getHeader().getFrom() != null) {
                forwardHeader.setFrom(sourceEmail.getHeader().getFrom());
            }

            forwardHeader.setTimeZone(timeZone);
            forwardHeader.addToAddress(new Address(accountMessage.getForwardEmail(), null));
            forwardHeader.setSubject(resourceManager.buildForwardMessageSubject(sourceEmail.getHeader().getSubject()));

            emailForwardBuilder = new EmailForwardBuilder(sourceEmail, forwardHeader, bodyMessage, resumeConfiguration);
            try {
                WebmailAccountUtil.i.deliveryEmailToDefaultSMTPServer(emailForwardBuilder.getEmail(), user.getFavoriteLanguage());
            } catch (ConnectionException e) {
                log.warn("Sent automatic Forward message Using default Server FAIL");
            }
        }
    }

    private void sentReplyEmails(List<Email> sourceEmails,
                                 MailAccount mailAccount) {
        User user = WebmailAccountUtil.i.getElwisUser(mailAccount.getUserMailId());
        if (null == user) {
            log.debug("-> User was deleted and cannot sent Forward email");
            return;
        }

        ResourceManager resourceManager = new ResourceManager(user.getFavoriteLanguage());
        ResumeConfiguration resumeConfiguration = getResumeConfiguration(resourceManager);
        String replyMessage = resourceManager.buildReplyMessageBody();
        TimeZone timeZone = WebmailAccountUtil.i.getTimeZone(user);

        EmailReplyBuilder emailReplyBuilder;
        Header replyHeader;

        for (Email sourceEmail : sourceEmails) {

            if (ConfigurationFactory.getConfigurationManager().getValue("elwis.system.fromEmailSender").
                    equals(sourceEmail.getHeader().getFrom().getEmail())) {
                continue;
            }

            if (mailAccount.getEmail().equals(sourceEmail.getHeader().getFrom().getEmail())) {
                continue;
            }

            //check if this source email is already an automatic reply
            if (isSourceEmailFromAutomaticReply(sourceEmail.getHeader().getSubject(), resourceManager)) {
                continue;
            }

            replyHeader = WebmailAccountUtil.i.getDefaultHeader();

            //add from as account email
            replyHeader.setFrom(new Address(mailAccount.getEmail(), null));
            String sourceSubject = sourceEmail.getHeader().getSubject();
            if (null == sourceSubject) {
                sourceSubject = " ";
            }

            replyHeader.setSubject(resourceManager.buildReplyMessageSubject(accountMessage.getAutoReplySubject(),
                    sourceSubject));

            replyHeader.addToAddress(sourceEmail.getHeader().getFrom());
            replyHeader.setTimeZone(timeZone);

            String bodyMessage;
            if (sourceEmail.getBody().isHtml()) {
                bodyMessage = accountMessage.getAutoReplyMessageHtml() + "<br>---<br>" + replyMessage;
            } else {
                bodyMessage = accountMessage.getAutoReplyMessage() + "\n---\n" + replyMessage;
            }

            emailReplyBuilder = new EmailReplyBuilder(sourceEmail, replyHeader, bodyMessage, resumeConfiguration);

            try {
                WebmailAccountUtil.i.deliveryEmailToDefaultSMTPServer(emailReplyBuilder.getEmail(), user.getFavoriteLanguage());
            } catch (ConnectionException e) {
                log.warn("Sent automatic Reply message Using default Server FAIL");
            }
        }
    }

    private boolean isSourceEmailFromAutomaticReply(String sourceSubject, ResourceManager resourceManager) {
        boolean isAlreadyReply = false;
        Map<String, String> map = resourceManager.getReplySubjectResourceConstants();

        String middleConstant = (String) map.get("containConstant");
        String endConstant = (String) map.get("endConstant");

        if (sourceSubject != null && middleConstant != null && endConstant != null) {
            isAlreadyReply = sourceSubject.contains(middleConstant) && sourceSubject.endsWith(endConstant);
        }

        return isAlreadyReply;
    }

    private String buildAsPreFormattedText(String text) {
        return "<pre>" + ((text != null) ? text : "") + "</pre>";
    }

    private Email buildParsedEmail(ByteArrayOutputStream byteArrayOutputStream, LightlyHeader lightlyHeader) {
        Mime4JEmailBuilder emailBuilder = new Mime4JEmailBuilder();
        Email email;
        try {
            email = emailBuilder.buildEmail(byteArrayOutputStream.toByteArray(), WebmailAccountUtil.i.getTimeZone(accountMessage.getUserMailId()));
            if (email != null) {
                //add the uidl in the parsed email
                email.getHeader().setUidl(lightlyHeader.getUidl());
                email.getHeader().setSize(lightlyHeader.getSize());
            }
        } catch (Exception e) {
            log.debug("Error in parse eml, uidl:" + lightlyHeader.getUidl() + ", accountId:" + accountMessage.getMailAccountId() + " Exception:" + e);

            email = null;

            if (!WebmailCacheManager.existParseErrorEmlFile(lightlyHeader.getUidl())) {
                log.error("There was an error trying to parse the eml source. Message UIDL:" + lightlyHeader.getUidl() + ", accountId:" + accountMessage.getMailAccountId(), e);
                if (byteArrayOutputStream != null) {
                    WebmailCacheManager.saveParseErrorEmlMessage(byteArrayOutputStream.toByteArray(), lightlyHeader.getUidl());
                } else {
                    log.error("Can't save eml in parseerror directory... source is NULL");
                }
            }
        }
        return email;
    }

    private Header buildParsedHeader(ByteArrayOutputStream byteArrayOutputStream, LightlyHeader lightlyHeader) {
        Mime4JEmailBuilder emailBuilder = new Mime4JEmailBuilder();

        Header header = null;
        try {
            header = emailBuilder.buildOnlyMainHeader(byteArrayOutputStream.toByteArray(), WebmailAccountUtil.i.getTimeZone(accountMessage.getUserMailId()));
            if (header != null) {
                //add the uidl in the parsed email header
                header.setUidl(lightlyHeader.getUidl());
                header.setSize(lightlyHeader.getSize());
            }
        } catch (Exception e) {
            log.debug("Error in parse eml only main header uidl:" + lightlyHeader.getUidl() + ", accountId:" + accountMessage.getMailAccountId() + " Exception:" + e);

            if (!WebmailCacheManager.existParseErrorEmlFile(lightlyHeader.getUidl())) {
                log.error("Error trying to parse and build email header from eml source. Message UIDL:" + lightlyHeader.getUidl() + ", accountId:" + accountMessage.getMailAccountId(), e);
                if (byteArrayOutputStream != null) {
                    WebmailCacheManager.saveParseErrorEmlMessage(byteArrayOutputStream.toByteArray(), lightlyHeader.getUidl());
                }
            }
        }
        return header;
    }

    private void sentBigMessagesToSender(List<Header> headers,
                                         POPConnection connection) {

        List<String> messagesToDelete = new ArrayList<String>();
        for (Header header : headers) {
            messagesToDelete.add(header.getUidl());
            sentBigMessageToSender(header);
        }

        connection.deleteEmails(messagesToDelete);
    }

    private void sentBigMessageToSender(Header sourceHeader) {

        User user = WebmailAccountUtil.i.getElwisUser(accountMessage.getUserMailId());
        if (null == user) {
            log.debug("-> User was deleted and cannot sent Forward email");
            return;
        }

        TimeZone timeZone = WebmailAccountUtil.i.getTimeZone(user);
        ResourceManager resourceManager = new ResourceManager(user.getFavoriteLanguage());
        ResumeConfiguration resumeConfiguration = getResumeConfiguration(resourceManager);

        Header replyHeader = WebmailAccountUtil.i.getDefaultHeader();
        replyHeader.addToAddress(sourceHeader.getFrom());
        replyHeader.setSubject(resourceManager.buildReplyFailureMessageSubject());
        replyHeader.setTimeZone(timeZone);

        String formatedDate = (new MailDateFormat()).format(sourceHeader.getSentDate());
        long maxSize = accountMessage.getApplicationMaxSize() / (1024 * 1024);
        String formatedMaxApplicationSize = String.valueOf(maxSize) + " MB";
        String messageBody = resourceManager.buildReplyFailureMessageBody(formatedDate,
                sourceHeader.getSubject(),
                formatedMaxApplicationSize);

        Body sourceBody = new Body(resourceManager.buildReplyFailureMessageOmmited(), Body.Type.TEXT);

        EmailResumeBuilder emailResumeBuilder = new EmailResumeBuilder(resumeConfiguration);
        String resumeMessage = emailResumeBuilder.getResume(sourceBody, sourceHeader);

        Body replyBody = new Body(messageBody + "\n" +
                resumeMessage + "\n" +
                resourceManager.buildReplyFailureMessageOmmited(), Body.Type.TEXT);

        Email email = new Email();
        email.setHeader(replyHeader);
        email.addPart(replyBody);

        try {
            WebmailAccountUtil.i.deliveryEmailToDefaultSMTPServer(email, user.getFavoriteLanguage());
        } catch (ConnectionException e) {
            log.warn("-> Sent reply for BigMessage FAIL ");
        }
    }

    private SaveEmailService getSaveEmailService() {
        SaveEmailServiceHome saveEmailServiceHome =
                (SaveEmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_SAVE_EMAIL_SERVICE);
        try {
            return saveEmailServiceHome.create();
        } catch (CreateException e) {
            return null;
        }
    }

    private Boolean existsMessageInDataBase(Header header, MailAccount mailAccount) {
        Boolean existWithUidl = existsUidlInDataBase(header.getUidl(), mailAccount.getMailAccountId(), mailAccount.getCompanyId());

        if (existWithUidl) {
            LogInfoFactory.i.log(this.getClass(), "  Check by uidl, mail to download already stored in application: mailAccountId=" + mailAccount.getMailAccountId() + "  uidl=" + header.getUidl() + "  messageId=" + header.getMessageId() + "  subject=" + header.getSubject());
        }

        return existWithUidl;
    }

    private Boolean existsMessageIdInDataBase(String messageId, String mailAccount, Integer companyId) {
        if (null == messageId) {
            return false;
        }

        MailHome mailHome =
                (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        try {
            return null != mailHome.findByMessageID(messageId, mailAccount, companyId);
        } catch (FinderException e) {
            return false;
        }
    }

    private Boolean existsUidlInDataBase(String uidl, Integer mailAccountId, Integer companyId) {
        UidlTrackHome uidlTrackHome =
                (UidlTrackHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_UIDLTRACK);

        try {
            return null != uidlTrackHome.findByUidl(uidl, mailAccountId, companyId);
        } catch (FinderException e) {
            return false;
        }
    }

    private void deleteEmailFromPOPAfterXDays(Integer mailAccountId, POPConnection connection) throws ConnectionException {
        if (mailAccountId != null) {
            UidlTrackHome uidlTrackHome = (UidlTrackHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_UIDLTRACK);

            List<String> uidlToDeleteList = new ArrayList<String>();

            try {
                Collection collection = uidlTrackHome.findByMailAccountIdDeleteFromPOPServerAt(mailAccountId, System.currentTimeMillis());
                for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
                    UidlTrack uidlTrack = (UidlTrack) iterator.next();

                    if (uidlTrack.getUidl() != null && uidlTrack.getDeleteFromPopAtTime() != null) {
                        uidlToDeleteList.add(uidlTrack.getUidl());
                        uidlTrack.setDeleteFromPopAtTime(null);
                    }
                }
            } catch (FinderException e) {
                log.debug("Error in find uidlTrack...", e);
            }

            if (!uidlToDeleteList.isEmpty()) {
                log.debug("removing emails from POP server after x days: " + uidlToDeleteList);
                connection.deleteEmails(uidlToDeleteList);
            }
        }
    }

    private POPConnection getPOPConnection() throws ConnectionException {
        MailAccount mailAccount = WebmailAccountUtil.i.getMailAccount(accountMessage.getMailAccountId());
        if (null == mailAccount) {
            throw new ConnectionException("Read MailAccount FAIL");
        }

        Account popAccount = WebmailAccountUtil.i.getPOPAccount(mailAccount);

        ConnectionFactory popConnectionFactory = new POPFactory();
        return (POPConnection) popConnectionFactory.getConnection(popAccount);
    }

    private List getUidlList(Integer mailAccountId) {
        List result = new ArrayList();
        try {
            //userTransaction.begin();  //read no require transaction
            UidlTrackHome uidlTrackHome =
                    (UidlTrackHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_UIDLTRACK);

            result = (List) uidlTrackHome.selectGetUidls(mailAccountId, accountMessage.getCompanyId());
            //userTransaction.commit();
        } catch (Exception e) {
            // no rollback because execute selectMethod, no database changes
        }
        return result;
    }

    private ResumeConfiguration getResumeConfiguration(ResourceManager resourceManager) {
        return new ResumeConfiguration(
                resourceManager.buildOriginalMessageTitle(),
                resourceManager.buildFromTitle(),
                resourceManager.buildToTitle(),
                resourceManager.buildCcTitle(),
                resourceManager.buildSubjectTitle(),
                resourceManager.buildDateTitle()
        );
    }
}
