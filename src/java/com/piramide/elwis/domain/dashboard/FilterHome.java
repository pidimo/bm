package com.piramide.elwis.domain.dashboard;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:48:14 PM
 */

public interface FilterHome extends EJBLocalHome {
    Filter findByPrimaryKey(Integer key) throws FinderException;

    public Filter create(String name, String val, Boolean isRange) throws CreateException;
}
