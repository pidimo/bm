package com.piramide.elwis.utils.webmail.jms;

import java.io.Serializable;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class EmailMessage implements Serializable {
    private Integer userMailId;
    private Integer mailId;
    private Integer folderId;
    private Boolean isConnected;

    public EmailMessage(Integer mailId) {
        this.mailId = mailId;
    }

    public Integer getMailId() {
        return mailId;
    }

    public void setMailId(Integer mailId) {
        this.mailId = mailId;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public Integer getUserMailId() {
        return userMailId;
    }

    public void setUserMailId(Integer userMailId) {
        this.userMailId = userMailId;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }
}
