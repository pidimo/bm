package com.piramide.elwis.utils.campaign;

import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Util to manage the init of send mail in background, this verify the delay before init the send.
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public class CampaignBackgroundProcessUtil {
    private Log log = LogFactory.getLog(CampaignBackgroundProcessUtil.class);

    private Map<Integer, SendInBackgroundInfo> sendProgressMap;
    private Long lastStartMillis;
    private int processCount;

    public static CampaignBackgroundProcessUtil i = new CampaignBackgroundProcessUtil();

    private CampaignBackgroundProcessUtil() {
        sendProgressMap = new HashMap<Integer, SendInBackgroundInfo>();
        processCount = 0;
        lastStartMillis = null;
    }

    public synchronized void addInProgressItem(Integer sentLogContactId) {
        sendProgressMap.put(sentLogContactId, new SendInBackgroundInfo(sentLogContactId));
        lastStartMillis = System.currentTimeMillis();
    }

    public synchronized void removeInProgressItem(Integer sentLogContactId) {
        sendProgressMap.remove(sentLogContactId);
    }

    public void verifyLastStartProcessTime() {
        if (lastStartMillis != null) {
            long currentMillis = System.currentTimeMillis();
            long seconds = (currentMillis - lastStartMillis) / 1000;

            long verifyTimeInSec = 7200; //2 hrs
            if (!sendProgressMap.isEmpty() && seconds > verifyTimeInSec) {
                log.info("The background process is taking too long...  in process:" + this.toString());
            }
        }
    }

    public boolean isStartNextProcess() {
        boolean result = false;

        if (processCount <= 0) {
            verifyLastStartProcessTime();

            Integer limit = getSendInParallelLimit();
            processCount = limit - sendProgressMap.size();
        }

        result = (processCount > 0);

        processCount--;
        return result;
    }

    private Integer getSendInParallelLimit() {

        Integer limit = null;
        try {
            limit = Integer.valueOf(ConfigurationFactory.getConfigurationManager().getValue("elwis.campaignMail.background.sendInParallel.limit"));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Property 'elwis.campaignMail.background.sendInParallel.limit' is not valid..", e);
        }

        if (limit < 1) {
            throw new RuntimeException("Property 'elwis.campaignMail.background.sendInParallel.limit' must be greater than 0");
        }

        return limit;
    }

    public String toString() {
        return "CampaignBackgroundProcessUtil" + " sendProgressMap:" + sendProgressMap + " lastStartMillis:" + lastStartMillis;
    }
}
