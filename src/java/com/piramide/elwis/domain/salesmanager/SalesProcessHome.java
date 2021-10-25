package com.piramide.elwis.domain.salesmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Sales process local home interface
 *
 * @author Fernando Montaño
 * @version $Id: SalesProcessHome.java 12581 2016-08-26 22:42:33Z miguel $
 */


public interface SalesProcessHome extends EJBLocalHome {
    SalesProcess findByPrimaryKey(Integer key) throws FinderException;

    SalesProcess findByActivityIdAndAddressId(Integer activityId, Integer addressId) throws FinderException;

    SalesProcess create(ComponentDTO dto) throws CreateException;

    Collection findByAddressId(Integer addressId) throws FinderException;

    Collection findByEmployeeId(Integer employeeId) throws FinderException;

    Collection findByActivityId(Integer activityId) throws FinderException;
}
