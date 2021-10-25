/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

public interface MailAccount extends EJBLocalObject {
    String getEmail();

    void setEmail(String email);

    Integer getCompanyId();

    void setCompanyId(Integer companyid);

    String getLogin();

    void setLogin(String login);

    Integer getMailAccountId();

    void setMailAccountId(Integer mailAccountId);

    String getPassword();

    void setPassword(String password);

    String getServerName();

    void setServerName(String serverName);

    String getServerPort();

    void setServerPort(String serverPort);

    Integer getUserMailId();

    void setUserMailId(Integer userMailId);

    Integer getUseSSLConnection();

    void setUseSSLConnection(Integer useSSLConection);

    Integer getAccountType();

    void setAccountType(Integer accountType);

    java.util.Collection getSignatures();

    void setSignatures(java.util.Collection signatures);

    Boolean getDefaultAccount();

    void setDefaultAccount(Boolean defaultAccount);

    String getPrefix();

    void setPrefix(String prefix);

    String getSmtpServer();

    void setSmtpServer(String smtpServer);

    String getSmtpPort();

    void setSmtpPort(String smtpPort);

    Boolean getSmtpAuthentication();

    void setSmtpAuthentication(Boolean smtpAuthentication);

    Integer getSmtpSSL();

    void setSmtpSSL(Integer smtpSSL);

    Long getLastDownloadTime();

    void setLastDownloadTime(Long lastDownloadTime);

    Boolean getUsePOPConfiguration();

    void setUsePOPConfiguration(Boolean usePOPConfiguration);

    String getSmtpLogin();

    void setSmtpLogin(String smtpLogin);

    String getSmtpPassword();

    void setSmtpPassword(String smtpPassword);

    Boolean getKeepEmailOnServer();

    void setKeepEmailOnServer(Boolean keepEmailOnServer);

    Boolean getCreateInCommunication();

    void setCreateInCommunication(Boolean createInCommunication);

    Boolean getCreateOutCommunication();

    void setCreateOutCommunication(Boolean createOutCommunication);

    Boolean getAutomaticForward();

    void setAutomaticForward(Boolean automaticForward);

    String getForwardEmail();

    void setForwardEmail(String forwardEmail);

    Boolean getAutomaticReply();

    void setAutomaticReply(Boolean automaticReply);

    String getAutomaticReplyMessageSubject();

    void setAutomaticReplyMessageSubject(String automaticReplyMessageSubject);

    String getAutomaticReplyMessage();

    void setAutomaticReplyMessage(String automaticReplyMessage);

    Integer getRemoveEmailOnServerAfterOf();

    void setRemoveEmailOnServerAfterOf(Integer removeEmailOnServerAfterOf);

    Integer getReplyMessageHtmlId();

    void setReplyMessageHtmlId(Integer replyMessageHtmlId);

    Integer getReplyMessageTextId();

    void setReplyMessageTextId(Integer replyMessageTextId);
}
