package com.piramide.elwis.utils.webmail;

import com.jatun.commons.email.connection.Account;
import com.jatun.commons.email.connection.ConnectionFactory;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.smtp.SMTPConnection;
//import com.jatun.commons.email.connection.smtp.SMTPFactory;
import com.piramide.elwis.cmd.webmailmanager.SMTPFactory;
import com.jatun.commons.email.model.Address;
import com.jatun.commons.email.model.Email;
import com.jatun.commons.email.model.Header;
import com.piramide.elwis.domain.admin.ApplicationMailSignature;
import com.piramide.elwis.domain.admin.ApplicationMailSignatureHome;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.EncryptUtil;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.mail.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class WebmailAccountUtil {
    private Log log = LogFactory.getLog(WebmailAccountUtil.class);

    public static WebmailAccountUtil i = new WebmailAccountUtil();

    private WebmailAccountUtil() {
    }

    public Account getPOPAccount(MailAccount mailAccount) {
        Account popAccount = new Account();
        popAccount.setLogin(mailAccount.getLogin());
        popAccount.setPassword(decodePassword(mailAccount.getUserMailId(), mailAccount.getPassword()));
        popAccount.setHost(mailAccount.getServerName());
        popAccount.setPort(mailAccount.getServerPort());
        popAccount.enableAuthentication();

        if(isSTARTTLSEnabled(mailAccount.getUseSSLConnection())) {
            popAccount.enableTLS();
        }

        if (isSSLEnabled(mailAccount.getUseSSLConnection())) {
            popAccount.enableSSL();
        }

        return popAccount;
    }

    public Account getSmtpAccount(MailAccount account) {
        if (null == account) {
            return null;
        }

        if (null == account.getSmtpServer() || null == account.getSmtpPort()) {
            return null;
        }

        String smtpUser = account.getLogin();
        String smtpPassword = decodePassword(account.getUserMailId(), account.getPassword());
        String smtpHost = account.getSmtpServer();
        String smtpPort = account.getSmtpPort();
        if (null != account.getUsePOPConfiguration() && !account.getUsePOPConfiguration()) {
            if (null != account.getSmtpLogin()) {
                smtpUser = account.getSmtpLogin();
            }
            if (null != account.getSmtpPassword()) {
                smtpPassword = decodePassword(account.getUserMailId(), account.getSmtpPassword());
            }
        }

        Account smtpAccount = new Account();
        smtpAccount.setLogin(smtpUser);
        smtpAccount.setPassword(smtpPassword);
        smtpAccount.setHost(smtpHost);
        smtpAccount.setPort(smtpPort);

        if (account.getSmtpAuthentication()) {
            smtpAccount.enableAuthentication();
        }

        if (isSTARTTLSEnabled(account.getSmtpSSL())) {
            smtpAccount.enableTLS();
        }
        if (isSSLEnabled(account.getSmtpSSL())) {
            smtpAccount.enableSSL();
        }

        return smtpAccount;
    }

    public Session getServerSmtpSession() {
        InitialContext initialContext;
        try {
            initialContext = new InitialContext();
            return (Session) initialContext.lookup(Constants.JNDI_MAILSESSION);
        } catch (NamingException e) {
            log.error("-> Read SMTP configuration from Server FAIL ", e);
        }
        return null;
    }

    public String decodePassword(Integer userMailId, String password) {
        String decodedPassword = password;

        try {
            decodedPassword = EncryptUtil.i.desCipher(password, userMailId.toString());
        } catch (Exception e) {
            log.debug("Cannot desCipher password for " + userMailId);
        }

        return decodedPassword;
    }

    public Header getDefaultHeader() {
        Header header = getBasicHeader();
        header.setFrom(
                new Address(ConfigurationFactory.getConfigurationManager().getValue("elwis.system.fromEmailSender"),
                        null));
        return header;
    }

    public Header getBasicHeader() {
        Header header = new Header();
        header.setXMailer("bm.bm-od.com");
        return header;
    }

    public Email buildEmailMessage(Mail dataBaseEmail, Integer userMailId) {

        enableEncodeDecodeConfForAttachName();

        Header header = getBasicHeader();
        header.setSentDate(new Date(dataBaseEmail.getSentDate()));
        header.setTimeZone(getTimeZone(userMailId));

        header.setSubject(dataBaseEmail.getMailSubject());
        header.setFrom(new Address(dataBaseEmail.getMailFrom(), dataBaseEmail.getMailPersonalFrom()));
        List dataBaseRecipients = getDataBaseRecipients(dataBaseEmail.getMailId(), dataBaseEmail.getCompanyId());
        for (int i = 0; i < dataBaseRecipients.size(); i++) {
            MailRecipient dataBaseRecipient = (MailRecipient) dataBaseRecipients.get(i);
            String type = dataBaseRecipient.getType().toString();
            String personalName = dataBaseRecipient.getPersonalName();
            if (dataBaseRecipient.getEmail().equals(personalName)) {
                personalName = null;
            }

            Address address = new Address(dataBaseRecipient.getEmail(), personalName);

            if (WebMailConstants.TO_TYPE_DEFAULT.equals(type)) {
                header.addToAddress(address);
            }
            if (WebMailConstants.TO_TYPE_CC.equals(type)) {
                header.addCcAddress(address);
            }
            if (WebMailConstants.TO_TYPE_BCC.equals(type)) {
                header.addBccAddress(address);
            }
        }

        if (WebMailConstants.MAIL_PRIORITY_HIGHT.equals(dataBaseEmail.getMailPriority().toString())) {
            header.setPriority((short) 1);
        }

        Body dataBaseBody = dataBaseEmail.getBody();
        com.jatun.commons.email.model.Body body = null;
        if (WebMailConstants.BODY_TYPE_HTML.equals(dataBaseBody.getBodyType().toString())) {
            body = new com.jatun.commons.email.model.Body(new String(dataBaseBody.getBodyContent()),
                    com.jatun.commons.email.model.Body.Type.HTML);
        }

        if (WebMailConstants.BODY_TYPE_TEXT.equals(dataBaseBody.getBodyType().toString())) {
            body = new com.jatun.commons.email.model.Body(new String(dataBaseBody.getBodyContent()),
                    com.jatun.commons.email.model.Body.Type.TEXT);
        }

        addApplicationSignature(body, userMailId);

        Email email = new Email();
        email.setHeader(header);
        email.addPart(body);

        List dataBaseAttachments = getDataBaseAttachments(dataBaseEmail.getMailId(), dataBaseEmail.getCompanyId());
        for (int i = 0; i < dataBaseAttachments.size(); i++) {
            Attach dataBaseAttachment = (Attach) dataBaseAttachments.get(i);
            com.jatun.commons.email.model.Attach attach =
                    new com.jatun.commons.email.model.Attach(dataBaseAttachment.getAttachName());
            com.jatun.commons.email.model.ByteArrayDataSource dataSource =
                    new com.jatun.commons.email.model.ByteArrayDataSource(WebmailAttachUtil.i.getAttachData(dataBaseAttachment), null, dataBaseAttachment.getAttachName());
            attach.setDataSource(dataSource);

            if (null != dataBaseAttachment.getVisible() && dataBaseAttachment.getVisible()) {
                attach.setCid(dataBaseAttachment.getAttachId().toString());
            }

            email.addPart(attach);
        }
        return email;
    }

    /**
     * This configuration is to java/mail, if the properties be defined as TRUE,
     * the name of the attach will be encoded(when send the email) and decoded (when read the eml source).
     * documentation: http://docs.oracle.com/javaee/6/api/javax/mail/internet/package-summary.html
     */
    private void enableEncodeDecodeConfForAttachName() {
        Properties props = System.getProperties();

        if (props.get("mail.mime.encodefilename") == null) {
            props.put("mail.mime.encodefilename", Boolean.TRUE);
        }
        if (props.get("mail.mime.decodefilename") == null) {
            props.put("mail.mime.decodefilename", Boolean.TRUE);
        }
    }

    public void addApplicationSignature(Email email, String favoriteLanguage) {
        addApplicationSignature(email.getBody(), favoriteLanguage);
    }

    private void addApplicationSignature(com.jatun.commons.email.model.Body body, Integer userId) {
        User user = getElwisUser(userId);
        addApplicationSignature(body, user.getFavoriteLanguage());
    }

    private void addApplicationSignature(com.jatun.commons.email.model.Body body, String favoriteLanguage) {
        Map<String, String> applicationSignatures = getApplicationSignature(favoriteLanguage);
        if (null != applicationSignatures) {
            if (body.isHtml()) {
                String htmlApplicationSignature = applicationSignatures.get("html");
                String actualContent = body.getContentAsHtml();
                body.setContent(actualContent + "<br>" + htmlApplicationSignature,
                        com.jatun.commons.email.model.Body.Type.HTML);
            } else {
                String textApplicationSignature = applicationSignatures.get("text");
                String actualContent = body.getContendAsPlainText();
                body.setContent(actualContent + "\n" + textApplicationSignature,
                        com.jatun.commons.email.model.Body.Type.TEXT);
            }
        }
    }

    private Map<String, String> getApplicationSignature(String favoriteLanguage) {
        if (null == favoriteLanguage) {
            return null;
        }

        return searchApplicationSignature(favoriteLanguage);
    }

    private Map<String, String> searchApplicationSignature(String language) {
        Map<String, String> result = null;

        ApplicationMailSignatureHome applicationMailSignatureHome =
                (ApplicationMailSignatureHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_APPLICATIONMAILSIGNATURE);
        ApplicationMailSignature signature;
        try {
            signature = applicationMailSignatureHome.findByPrimaryKey(language);
        } catch (FinderException e) {
            log.debug("-> Read ApplicationMailSignature for key=" + language + " FAIL");
            return result;
        }

        if (null != signature.getEnabled() && signature.getEnabled()) {
            result = new HashMap<String, String>();
            String textSignature = "";
            if (null != signature.getTextSignature()) {
                textSignature = new String(signature.getTextSignature());
            }

            String htmlSignature = "";
            if (null != signature.getHtmlSignature()) {
                htmlSignature = new String(signature.getHtmlSignature());
            }

            result.put("text", textSignature);
            result.put("html", htmlSignature);
        }

        return result;
    }

    public UserMail getUserMail(Integer userMailId) {
        UserMailHome userMailHome =
                (UserMailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);
        try {
            return userMailHome.findByPrimaryKey(userMailId);
        } catch (FinderException e) {
            return null;
        }
    }

    public MailAccount getMailAccount(String email, Integer userMailId) {
        MailAccountHome mailAccountHome =
                (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);
        try {
            List mailAccounts = (List) mailAccountHome.findAccountsByEmailAndUser(email, userMailId);
            if (null == mailAccounts || mailAccounts.isEmpty()) {
                return null;
            }

            return (MailAccount) mailAccounts.get(0);

        } catch (FinderException e) {
            return null;
        }
    }

    public MailAccount getMailAccount(Integer mailAccountId) {
        MailAccountHome mailAccountHome =
                (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);

        try {
            return mailAccountHome.findByPrimaryKey(mailAccountId);
        } catch (FinderException e) {
            return null;
        }
    }


    private List getDataBaseRecipients(Integer mailId, Integer companyId) {
        MailRecipientHome mailRecipientHome =
                (MailRecipientHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILRECIPIENT);

        try {
            return (List) mailRecipientHome.findByMailId(mailId, companyId);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    private List getDataBaseAttachments(Integer mailId, Integer companyId) {
        AttachHome attachHome =
                (AttachHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);
        try {
            return (List) attachHome.findByMailId(mailId, companyId);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    public TimeZone getTimeZone(Integer userMailId) {
        User elwisUser = getElwisUser(userMailId);
        return getTimeZone(elwisUser);
    }

    public TimeZone getTimeZone(User elwisUser) {
        if (null != elwisUser.getTimeZone()) {
            return DateTimeZone.forID(elwisUser.getTimeZone()).toTimeZone();
        }

        return TimeZone.getDefault();
    }

    public User getElwisUser(Integer userId) {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            return userHome.findByPrimaryKey(userId);
        } catch (FinderException e) {
            return null;
        }
    }

    public void deliveryEmailToDefaultSMTPServer(Email email) throws ConnectionException {
        ConnectionFactory smtpFactory = new SMTPFactory();
        SMTPConnection smtpConnection = null;
        try {
            smtpConnection = (SMTPConnection) smtpFactory.getConnection(null);
            smtpConnection.send(email, getServerSmtpSession());
        } finally {
            if (null != smtpConnection) {
                smtpConnection.close();
            }
        }
    }

    public void deliveryEmailToDefaultSMTPServer(Email email, Integer userId) throws ConnectionException {
        if (null != userId) {
            User user = getElwisUser(userId);
            deliveryEmailToDefaultSMTPServer(email, user.getFavoriteLanguage());
        } else {
            deliveryEmailToDefaultSMTPServer(email);
        }

    }

    public void deliveryEmailToDefaultSMTPServer(Email email, String favoriteLanguage) throws ConnectionException {
        addApplicationSignature(email, favoriteLanguage);
        deliveryEmailToDefaultSMTPServer(email);
    }

    public void deliveryEmailToDefaultSMTPServer(List<Email> emails) throws ConnectionException {
        ConnectionFactory smtpFactory = new SMTPFactory();
        SMTPConnection smtpConnection = null;
        try {
            smtpConnection = (SMTPConnection) smtpFactory.getConnection(null);
            for (Email email : emails) {
                smtpConnection.send(email, getServerSmtpSession());
            }
        } finally {
            if (null != smtpConnection) {
                smtpConnection.close();
            }
        }
    }

    public boolean isSSLEnabled(Integer secureConnectionType) {
        return SecureConnectionType.SSL.value().equals(secureConnectionType);
    }

    public boolean isSTARTTLSEnabled(Integer secureConnectionType) {
        return SecureConnectionType.STARTTLS.value().equals(secureConnectionType);
    }


}
