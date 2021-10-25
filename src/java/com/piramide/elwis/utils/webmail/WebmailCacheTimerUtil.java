package com.piramide.elwis.utils.webmail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

/**
 * Timer control util to manage webmail cache eml parse error files
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class WebmailCacheTimerUtil {
    private Log log = LogFactory.getLog(this.getClass());

    private int hoursTimer = 1;
    private DateTime nextVerifyDateTime;

    /**
     * Singleton instance.
     */
    public static final WebmailCacheTimerUtil i = new WebmailCacheTimerUtil();

    public WebmailCacheTimerUtil() {
    }

    public synchronized void logEmlMessagesWithParseError() {
        if (isVerifyTime()) {
            int filesWithParseError = WebmailCacheManager.getNumberOfParseErrorEmlFiles();
            if (filesWithParseError > 0) {
                log.error("There are " + filesWithParseError + " eml message files with parse error in webmail cache directory: " + WebmailCacheManager.pathParseError(true));
            }
        }
    }

    private boolean isVerifyTime() {
        DateTime currentDateTime = new DateTime(System.currentTimeMillis());
        if (nextVerifyDateTime == null || currentDateTime.isAfter(nextVerifyDateTime)) {
            nextVerifyDateTime = currentDateTime.plusHours(hoursTimer);
            return true;
        }
        return false;
    }

}
