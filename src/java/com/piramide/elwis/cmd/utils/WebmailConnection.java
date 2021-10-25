package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.domain.webmailmanager.Connection;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: WebmailConnection.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 22-03-2005 01:49:33 PM ivan Exp $
 */
public class WebmailConnection implements Connection {
    private String smtpHost = "";
    private String smtpPort = "";

    private String popHost = "";
    private String popPort = "";

    private String email = "";
    private String password = "";
    private String account = "";

    private boolean useSSL = false;

    public WebmailConnection(String smtpHost, String smtpPort, String popHost, String popPort, String email, String password, String account) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.popHost = popHost;
        this.popPort = popPort;
        this.email = email;
        this.password = password;
        this.account = account;
    }

    public WebmailConnection() {
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getPopHost() {
        return popHost;
    }

    public void setPopHost(String popHost) {
        this.popHost = popHost;
    }

    public String getPopPort() {
        return popPort;
    }

    public void setPopPort(String popPort) {
        this.popPort = popPort;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String acount) {
        this.account = acount;
    }

    public String getUserAccount() {
        String userAccount = "";
        int index = getEmail().indexOf('@');
        userAccount = getEmail().substring(0, index);
        return userAccount;
    }

    public boolean isSMTPConfigurated() {
        if ("".equals(getSmtpHost()) || "".equals(getSmtpPort())) {
            return false;
        }
        return true;
    }

    public boolean isPOPConfigurated() {
        if ("".equals(getPopHost()) || "".equals(getPopPort())) {
            return false;
        }
        return true;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }


    public String toString() {
        return "smtpHost..: " + getSmtpHost() + "\n" +
                "smtpPort..: " + getSmtpPort() + "\n" +
                "popHost...: " + getPopHost() + "\n" +
                "popPort...: " + getPopPort() + "\n" +
                "email.....: " + getEmail() + "\n" +
                "account...: " + getAccount();
    }


}
