package com.piramide.elwis.cmd.utils;

import javax.mail.PasswordAuthentication;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: WebmailPasswordAuthenticator.java 7936 2007-10-27 16:08:39Z fernando ${CLASS_NAME}.java,v 1.2 23-02-2005 04:28:10 PM ivan Exp $
 */
public class WebmailPasswordAuthenticator extends javax.mail.Authenticator {
    private String userName;
    private String password;

    public WebmailPasswordAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
