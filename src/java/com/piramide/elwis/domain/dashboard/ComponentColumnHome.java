package com.piramide.elwis.domain.dashboard;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:50:56 PM
 */

public interface ComponentColumnHome extends EJBLocalHome {
    public ComponentColumn create(Integer xmlColumnId, Integer position, String order) throws CreateException;

    ComponentColumn findByPrimaryKey(Integer key) throws FinderException;

    ComponentColumn findByComponentIdAndXMLColumnId(Integer componentId, Integer xmlColumnId) throws FinderException;

    Collection findByComponentId(Integer componentId) throws FinderException;

}
