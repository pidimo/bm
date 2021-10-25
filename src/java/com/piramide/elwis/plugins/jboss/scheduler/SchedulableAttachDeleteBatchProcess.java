package com.piramide.elwis.plugins.jboss.scheduler;

import com.piramide.elwis.service.webmail.EmailSourceService;
import com.piramide.elwis.service.webmail.EmailSourceServiceHome;
import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.varia.scheduler.Schedulable;
import org.joda.time.DateTime;
import org.joda.time.Period;

import javax.ejb.CreateException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Schedulable batch process to rebuild mails with emailsource and that contain attach with file blob
 * @author Miguel A. Rojas Cardenas
 * @version 4.8
 */
public class SchedulableAttachDeleteBatchProcess implements Schedulable {
    private static final Log log = LogFactory.getLog(SchedulableAttachDeleteBatchProcess.class);
    private long processedBytes;

    public void perform(Date date, long remainingRepetitions) {
        log.debug("Executing SchedulableAttachDeleteBatchProcess........." + date);
        processRebuildMailAttach(date);
    }

    private void processRebuildMailAttach(Date date) {
        int itemsToProcess = getItemsToProcess();
        try {
            if (isValidHourToExecute(date)) {
                log.debug("Start update process..." + itemsToProcess);

                if (itemsToProcess > 0) {
                    List<Integer> mailIdList = getMailIdsToProcess(itemsToProcess);
                    if (mailIdList.size() > 0) {
                        long startMillis = System.currentTimeMillis();

                        EmailSourceService emailSourceService = getEmailSourceService();
                        emailSourceService.rebuildEmailBodyAttachInBackgroundBatchProcess(mailIdList);
                        emailSourceService.remove();

                        long endMillis = System.currentTimeMillis();
                        log.info("Background rebuild email completed. mails processed=" + mailIdList.size()
                                + ", time in seconds=" + getSecondInterval(startMillis, endMillis)
                                + ", size in megabytes=" + getAsMegaBytes(processedBytes));
                    } else {
                        log.info("No more emails to rebuild in background.");
                    }
                }
            }
        } catch (Exception e) {
            log.error("A unexpected exception happen when trying rebuild emails in background.", e);
        }
    }

    /**
     * Read item to process from external file config
     * return negative to stop the process
     * @return int
     */
    private int getItemsToProcess() {
        String configItems = ConfigurationFactory.getValue("elwis.batchProcess.attachDelete.items");
        Integer itemsToProcess = null;
        if (configItems != null) {
            try {
                itemsToProcess = new Integer(configItems.trim());
            } catch (Exception e) {
                itemsToProcess = 50;
            }
        }

        if (itemsToProcess == null) {
            itemsToProcess = 50;
        }
        return itemsToProcess;
    }

    private boolean isValidHourToExecute(Date date) {
        //hours in 24hrs
/*
        int startHour = 2;
        int endHour = 5;

        DateTime dateTime = new DateTime(date);
        DateTime startDateTime = dateTime.withTime(startHour, 0, 0, 0);
        DateTime endDateTime = dateTime.withTime(endHour, 0, 0, 0);

        int currentHour = dateTime.getHourOfDay();

        //currentHour > endHour || currentHour < startHour;
        boolean isValidHour = dateTime.isAfter(endDateTime) || dateTime.isBefore(startDateTime);
        return isValidHour;
*/
        //todo: because no require stop the process
        return true;
    }

    private int getSecondInterval(long startMillis, long endMillis) {
        DateTime startTime = new DateTime(startMillis);
        DateTime endTime = new DateTime(endMillis);
        Period period = new Period(startTime, endTime);
        return (period.getMinutes() * 60) + period.getSeconds();
    }

    private List<Integer> getMailIdsToProcess(int maxItems) {
        List<Integer> mailIdList = new ArrayList<Integer>();

        long availableMemory = getAvailable25PercentMemory();
        long sumFileSize = 0;
        processedBytes = 0;

        List<Map> resultMapList =  QueryUtil.i.executeQuery(getSql());
        if (resultMapList != null) {
            for (int i = 0; i < resultMapList.size(); i++) {
                Map resultMap = resultMapList.get(i);
                Integer mailId = new Integer(resultMap.get("id").toString());
                Long size = new Long(resultMap.get("filesize").toString());

                sumFileSize = sumFileSize + size;
                if (i < maxItems && sumFileSize < availableMemory) {
                    mailIdList.add(mailId);
                    processedBytes = processedBytes + size;
                } else {
                    break;
                }
            }
        }
        return mailIdList;
    }

    /**
     * sql to filter all mail with emailsource and attachments with blob data
     * @return String
     */
    private String getSql() {
        String sql = "SELECT DISTINCT m.mailid as id, s.filesize as filesize FROM mail as m, emailsource as s, attach as a\n" +
                " WHERE m.mailid = s.mailid AND m.mailid = a.mailid AND a.file IS NOT NULL";

        return sql;
    }

    /**
     * JVM free 25% memory in bytes
     * @return
     */
    private long getAvailable25PercentMemory() {
        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.freeMemory();
        long available = (freeMemory * 25) / 100;
        return available;
    }

    private double getAsMegaBytes(long bytes) {
        double megabytes = (bytes / (1024 * 1024));
        megabytes = BigDecimalUtils.roundBigDecimal(BigDecimal.valueOf(megabytes)).doubleValue();
        return megabytes;
    }

    private EmailSourceService getEmailSourceService() throws CreateException {
        EmailSourceServiceHome emailSourceServiceHome =
                (EmailSourceServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAIL_SOURCE_SERVICE);
        return emailSourceServiceHome.create();
    }

}
