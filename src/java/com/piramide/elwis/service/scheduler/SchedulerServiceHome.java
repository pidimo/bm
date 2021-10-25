package com.piramide.elwis.service.scheduler;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * Scheduler Service Local Home interface
 *
 * @author Fernando Montaño
 * @version $Id: SchedulerServiceHome.java 9123 2009-04-17 00:32:52Z fernando $
 */


public interface SchedulerServiceHome extends EJBLocalHome {
    SchedulerService create() throws CreateException;
}
