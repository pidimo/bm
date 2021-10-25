package com.piramide.elwis.utils.webmail.jms;

import java.io.Serializable;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class UserMessage implements Serializable {

    private Integer userMailId;

    private Boolean isConnected = false;

    private Boolean isValid = true;

    private Long millisKey;

    public UserMessage(Integer userMailId, Boolean isConnected) {
        this.userMailId = userMailId;
        this.isConnected = isConnected;
    }

    public Integer getUserMailId() {
        return userMailId;
    }

    public Boolean isValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
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

