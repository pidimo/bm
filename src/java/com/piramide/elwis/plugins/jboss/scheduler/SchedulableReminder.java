package com.piramide.elwis.plugins.jboss.scheduler;

import com.piramide.elwis.service.scheduler.SchedulerService;
import com.piramide.elwis.service.scheduler.SchedulerServiceHome;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.varia.scheduler.Schedulable;

import java.util.Date;

/**
 * Schedulable reminder service
 *
 * @author Fernando Montaño
 * @version $Id: SchedulableReminder.java 9123 2009-04-17 00:32:52Z fernando $
 */

public class SchedulableReminder implements Schedulable {

    private Log log = LogFactory.getLog(SchedulableReminder.class);
    private long intervalBetweenChecks;

    public SchedulableReminder(long intervalBetweenChecks) {
        this.intervalBetweenChecks = intervalBetweenChecks;
    }

    public void perform(Date timeOfCall, long remainingRepetitions) {
        try {

            SchedulerServiceHome serviceHome = (SchedulerServiceHome)
                    EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULERSERVICE);
            SchedulerService service = serviceHome.create();
            service.performReminder(timeOfCall, intervalBetweenChecks);
            service.remove();
        } catch (Exception e) {
            log.error("Error scheduling the reminders", e);
        }

    }
}
