package com.piramide.elwis.utils.campaign;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public class SendInBackgroundInfo {

    private Log log = LogFactory.getLog(this.getClass());

    private Long startMillis;
    private Integer sentLogContactId;

    public SendInBackgroundInfo(Integer sentLogContactId) {
        this.sentLogContactId = sentLogContactId;
        this.startMillis = System.currentTimeMillis();
    }

    public Long getStartMillis() {
        return startMillis;
    }

    public void setStartMillis(Long startMillis) {
        this.startMillis = startMillis;
    }

    public Integer getSentLogContactId() {
        return sentLogContactId;
    }

    public void setSentLogContactId(Integer sentLogContactId) {
        this.sentLogContactId = sentLogContactId;
    }

    @Override
    public String toString() {
        return "SendInBackgroundInfo" + " sentLogContactId:" + sentLogContactId + " startTime:" + (startMillis != null ? new DateTime(startMillis) : "");
    }
}
