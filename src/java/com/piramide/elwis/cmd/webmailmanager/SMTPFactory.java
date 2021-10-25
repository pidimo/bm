package com.piramide.elwis.cmd.webmailmanager;

import com.jatun.commons.email.configuration.ConfigurationManager;
import com.jatun.commons.email.connection.Account;
import com.jatun.commons.email.connection.Connection;
import com.jatun.commons.email.connection.ConnectionFactory;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.smtp.SMTPConnection;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Session;

public class SMTPFactory extends ConnectionFactory {

    public SMTPFactory() {
    }

    private Session getAccountSession(Account account) {
        Authenticator authenticator = this.getAuthenticator(account);
        Properties properties = new Properties();
        properties.put("mail.debug", String.valueOf(this.debug));
        properties.put("mail.smtp.user", account.getLogin());
        properties.put("mail.smtp.host", account.getHost());
        properties.put("mail.smtp.ssl.trust", account.getHost());
        properties.put("mail.smtp.port", account.getPort());
        properties.put("mail.smtp.socketFactory.port", account.getPort());
        properties.put("mail.smtp.auth", String.valueOf(account.getAuthentication()));
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.1 TLSv1.2");
        if (!"".equals(ConfigurationManager.i.getSMTPConnectiontimeout().trim())) {
            properties.put("mail.smtp.connectiontimeout", ConfigurationManager.i.getSMTPConnectiontimeout());
        }

        if (!"".equals(ConfigurationManager.i.getSMTPTimeout().trim())) {
            properties.put("mail.smtp.timeout", ConfigurationManager.i.getSMTPTimeout());
        }

        if (account.getTLS()) {
            properties.put("mail.smtp.starttls.enable", "true");
        }

        if (account.getSSL()) {
            properties.put("mail.smtp.ssl.enable", String.valueOf(account.getSSL()));
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        return Session.getInstance(properties, authenticator);
    }

    protected Connection create(Account account) throws ConnectionException {
        System.getProperties().put("mail.mime.charset", "UTF-8");
        return null != account ? new SMTPConnection(account, this.getAccountSession(account)) : new SMTPConnection();
    }

}
