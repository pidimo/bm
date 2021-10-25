package com.piramide.elwis.domain.webmailmanager;

/**
 * @author : ivan
 * @version : $Id Connection ${time}
 */
public interface Connection {

    public String getSmtpHost();

    public void setSmtpHost(String smtpHost);

    public String getSmtpPort();

    public void setSmtpPort(String smtpPort);

    public String getPopHost();

    public void setPopHost(String popHost);

    public String getPopPort();

    public void setPopPort(String popPort);

    public String getEmail();

    public void setEmail(String email);

    public String getPassword();

    public void setPassword(String password);

    public String getAccount();

    public void setAccount(String acount);

    public String getUserAccount();

    public boolean isSMTPConfigurated();

    public boolean isPOPConfigurated();

    public boolean isUseSSL();

    public void setUseSSL(boolean useSSL);
}
