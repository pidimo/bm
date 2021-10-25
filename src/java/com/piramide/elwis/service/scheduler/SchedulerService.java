package com.piramide.elwis.service.scheduler;

import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.schedulermanager.Reminder;

import javax.ejb.EJBLocalObject;
import java.util.Date;


/**
 * JaTun S.R.L
 *
 * @author Fernando Montaño
 * @version $Id: SchedulerService.java 9123 2009-04-17 00:32:52Z fernando $
 */


public interface SchedulerService extends EJBLocalObject {

    public void performReminder(Date timeOfCall, long intervalBetweenChecks);

    public void processNextReminderDate(Reminder reminder, UserHome userHome) throws Exception;
}
