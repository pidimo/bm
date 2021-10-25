package com.piramide.elwis.plugins.jboss.scheduler;

import com.piramide.elwis.service.sales.ContractEndReminderService;
import com.piramide.elwis.service.sales.ContractEndReminderServiceHome;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.varia.scheduler.Schedulable;

import java.util.Date;

/**
 * Jatun S.R.L.
 * Schedulable to send contract end reminder. This is called each 1 hour.
 *
 * @author Miky
 * @version $Id: SchedulableContractReminder.java  06-nov-2009 16:42:05$
 */
public class SchedulableContractEndReminder implements Schedulable {

    private Log log = LogFactory.getLog(SchedulableContractEndReminder.class);
    private long intervalBetweenChecks;

    public SchedulableContractEndReminder(long intervalBetweenChecks) {
        this.intervalBetweenChecks = intervalBetweenChecks;
    }

    public void perform(Date timeOfCall, long remainingRepetitions) {
        log.debug("Call schedulable contract end reminder.." + timeOfCall);
        try {
            ContractEndReminderServiceHome reminderServiceHome = (ContractEndReminderServiceHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_CONTRACTENDREMINDERSERVICE);
            ContractEndReminderService reminderService = reminderServiceHome.create();

            reminderService.performContractEndReminder(timeOfCall, intervalBetweenChecks);
            reminderService.remove();
        } catch (Exception e) {
            log.error("Error scheduling the contract end reminders", e);
        }

    }
}
