package com.piramide.elwis.domain.dashboard;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:46:48 PM
 */

public interface ComponentHome extends EJBLocalHome {
    Component create() throws CreateException;

    Component findByPrimaryKey(Integer key) throws FinderException;
}
