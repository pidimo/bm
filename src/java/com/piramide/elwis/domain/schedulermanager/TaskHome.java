package com.piramide.elwis.domain.schedulermanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 6, 2005
 * Time: 3:30:50 PM
 * To change this template use File | Settings | File Templates.
 */

public interface TaskHome extends EJBLocalHome {

    Collection findAll() throws FinderException;

    Collection findByActivityId(Integer activityId) throws FinderException;

    Task findByPrimaryKey(Integer key) throws FinderException;

    Task create(ComponentDTO dto) throws CreateException;

    Collection findByAddress(Integer addressId) throws FinderException;

    Collection findByContactPerson(Integer addressId, Integer contactPersonId) throws FinderException;

}
