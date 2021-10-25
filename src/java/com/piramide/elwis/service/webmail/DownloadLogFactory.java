package com.piramide.elwis.service.webmail;

import com.piramide.elwis.service.webmail.downloadlog.MyCustomEventHandler;
import com.piramide.elwis.service.webmail.downloadlog.MyMailAccountQueueObserver;
import com.piramide.elwis.service.webmail.downloadlog.MyUserMailQueueObserver;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2.3
 */
public class DownloadLogFactory {
    public static DownloadLogFactory i = new DownloadLogFactory();

    private MyUserMailQueueObserver myUserMailQueueObserver;
    private MyMailAccountQueueObserver myAccountQueueObserver;

    public DownloadLogFactory() {

        if (isActiveLog()) {
            myUserMailQueueObserver = new MyUserMailQueueObserver();
            myAccountQueueObserver = new MyMailAccountQueueObserver();

            MyCustomEventHandler.register(myUserMailQueueObserver, "QUEUE_USERMAIL");
            MyCustomEventHandler.register(myUserMailQueueObserver, "UNQUEUE_USERMAIL");

            MyCustomEventHandler.register(myAccountQueueObserver, "QUEUE_ACCOUNT");
            MyCustomEventHandler.register(myAccountQueueObserver, "UNQUEUE_ACCOUNT");
        }
    }

    public void log(Class clazz, Object text) {
        log(clazz, text, null);
    }

    public void log(Class clazz, Object text, Throwable e) {
        if (isActiveLog()) {
            Log log = LogFactory.getLog(clazz);
            if (e != null) {
                log.info(text, e);
            } else {
                log.info(text);
            }
        }
    }

    private boolean isActiveLog() {
        return "true".equals(ConfigurationFactory.getValue("elwis.bgEmailDownload.log.enable").trim());
    }


    public synchronized void mdbObserverPreviousLog(String message) {
        if (isActiveLog()) {

            boolean showLog = false;
            try {
                Map<Long, List<Integer>> tempMap1 = new LinkedHashMap<Long, List<Integer>>(myUserMailQueueObserver.getPreviousUnconsumedQueue());
                Map<Long, List<Integer>> tempMap2 = new LinkedHashMap<Long, List<Integer>>(myAccountQueueObserver.getPreviousUnconsumedQueue());

                if (!tempMap1.isEmpty() || !tempMap2.isEmpty()) {
                    log(this.getClass(), message);

                    for (Long key : tempMap1.keySet()) {
                        if (tempMap1.get(key).size() > 0) {
                            log(this.getClass(), " " + new DateTime(key) + " userMailId still in the queue:" + tempMap1.get(key));
                        }
                    }

                    for (Long key : tempMap2.keySet()) {
                        if (tempMap2.get(key).size() > 0) {
                            log(this.getClass(), " " + new DateTime(key) + " mailAccountId still in the queue:" + tempMap2.get(key));
                        }
                    }
                }
            } catch (Exception e) {
                log(this.getClass(), "Error in update previous queue log.. " + e);
            }
        }
    }

    public static String dateTimeLog(Long time) {
        return (time != null) ? (new DateTime(time)).toString() : "";
    }
}
