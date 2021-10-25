package com.piramide.elwis.cmd.webmailmanager;

import com.jatun.commons.email.configuration.ConfigurationManager;
import com.jatun.commons.email.connection.Account;
import com.jatun.commons.email.connection.Connection;
import com.jatun.commons.email.connection.ConnectionFactory;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.pop.POPConnection;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Session;

public class POPFactory extends ConnectionFactory {
	
    public POPFactory() {
    }

    private Session getAccountSession(Account account) {
        Authenticator authenticator = this.getAuthenticator(account);
        Properties properties = new Properties();
        properties.put("mail.debug", String.valueOf(this.debug));
        properties.put("mail.pop3.user", account.getLogin());
        properties.put("mail.pop3.host", account.getHost());
        properties.put("mail.pop3.port", account.getPort());
        properties.put("mail.pop3.socketFactory.port", account.getPort());
        properties.put("mail.pop.auth", String.valueOf(account.getAuthentication()));
        properties.put("mail.pop3.forgettopheaders", "true");
        properties.put("mail.pop3.connectiontimeout", ConfigurationManager.i.getPOP3Connectiontimeout());
        properties.put("mail.pop3.timeout", ConfigurationManager.i.getPOP3Timeout());
        properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.mime.base64.ignoreerrors", "true");
        properties.setProperty("mail.pop3.ssl.protocols", "TLSv1.1 TLSv1.2");
        
        if (account.getTLS()) {
            properties.put("mail.pop3.starttls.enable", "true");
        }

        if (account.getSSL()) {
            properties.put("mail.pop3.ssl.enable", String.valueOf(account.getSSL()));
            properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        return Session.getInstance(properties, authenticator);
    }

    protected Connection create(Account account) throws ConnectionException {
        return new POPConnection(account, this.getAccountSession(account));
    }

}
