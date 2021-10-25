package com.piramide.elwis.utils.webmail.jms;

import java.io.Serializable;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class WebmailAccountMessage implements Serializable {
    private Integer userMailId;
    private Integer companyId;
    private Integer mailAccountId;
    private Integer inboxFolderId;
    private Long applicationMaxSize;

    private Boolean autoReply = false;
    private String autoReplyMessage;
    private String autoReplyMessageHtml;
    private String autoReplySubject;

    private Boolean autoForward = false;
    private String forwardEmail;

    private Long lastDownloadTime;

    private Boolean isConnected = false;

    private Long millisKey;

    public WebmailAccountMessage(Integer mailAccountId) {
        this.mailAccountId = mailAccountId;
    }

    public Integer getUserMailId() {
        return userMailId;
    }

    public void setUserMailId(Integer userMailId) {
        this.userMailId = userMailId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getMailAccountId() {
        return mailAccountId;
    }

    public Boolean getAutoReply() {
        return autoReply;
    }

    public void setAutoReply(Boolean autoReply) {
        this.autoReply = autoReply;
    }

    public Boolean getAutoForward() {
        return autoForward;
    }

    public void setAutoForward(Boolean autoForward) {
        this.autoForward = autoForward;
    }

    public Long getApplicationMaxSize() {
        return applicationMaxSize;
    }

    public void setApplicationMaxSize(Long applicationMaxSize) {
        this.applicationMaxSize = applicationMaxSize;
    }

    public String getAutoReplyMessage() {
        return autoReplyMessage;
    }

    public void setAutoReplyMessage(String autoReplyMessage) {
        this.autoReplyMessage = autoReplyMessage;
    }

    public String getAutoReplyMessageHtml() {
        return autoReplyMessageHtml;
    }

    public void setAutoReplyMessageHtml(String autoReplyMessageHtml) {
        this.autoReplyMessageHtml = autoReplyMessageHtml;
    }

    public String getAutoReplySubject() {
        return autoReplySubject;
    }

    public void setAutoReplySubject(String autoReplySubject) {
        this.autoReplySubject = autoReplySubject;
    }

    public Long getLastDownloadTime() {
        return lastDownloadTime;
    }

    public void setLastDownloadTime(Long lastDownloadTime) {
        this.lastDownloadTime = lastDownloadTime;
    }

    public String getForwardEmail() {
        return forwardEmail;
    }

    public void setForwardEmail(String forwardEmail) {
        this.forwardEmail = forwardEmail;
    }

    public Integer getInboxFolderId() {
        return inboxFolderId;
    }

    public void setInboxFolderId(Integer inboxFolderId) {
        this.inboxFolderId = inboxFolderId;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public Long getMillisKey() {
        return millisKey;
    }

    public void setMillisKey(Long millisKey) {
        this.millisKey = millisKey;
    }
}
