package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SchedulerAccessHome.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 22-04-2005 02:38:49 PM Fernando Montaño Exp $
 */


public interface SchedulerAccessHome extends EJBLocalHome {
    public SchedulerAccess create(Integer ownerUserId, Integer userId, Integer companyId) throws CreateException;

    SchedulerAccess findByPrimaryKey(SchedulerAccessPK key) throws FinderException;

    Collection findUsersViewCalendar(Integer userId) throws FinderException;
}
