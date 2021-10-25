package com.piramide.elwis.domain.schedulermanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: ScheduledUserHome.java 10394 2013-10-22 21:48:25Z miguel ${NAME}.java, v 2.0 22-04-2005 02:33:32 PM Fernando Montaño Exp $
 */


public interface ScheduledUserHome extends EJBLocalHome {

    Collection findAll() throws FinderException;

    ScheduledUser findByPrimaryKey(Integer key) throws FinderException;

    ScheduledUser findByUserIdAndAppId(Integer userId, Integer appointmentId) throws FinderException;

    Collection findByAppointmentId(Integer appointmentId) throws FinderException;

    Collection findByTaskId(Integer taskId) throws FinderException;

    Collection findByUserGroupIdAndAppId(Integer userGroupId, Integer appointmentId) throws FinderException;

    ScheduledUser findByUserIdAndTaskId(Integer userId, Integer taskId) throws FinderException;

    Collection findByUserGroupIdAndTaskId(Integer userGroupId, Integer taskId) throws FinderException;

    Collection findByUserGroupId(Integer userGroupId, Integer companyId) throws FinderException;

    ScheduledUser create(ComponentDTO dto) throws CreateException;

}
