package com.piramide.elwis.domain.dashboard;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:40:37 PM
 */

public interface AdminComponentHome extends EJBLocalHome {
    public AdminComponent create(Integer containerId, Integer componentId) throws CreateException;

    AdminComponent findByPrimaryKey(AdminComponentPK key) throws FinderException;

    public Collection findByContainerId(Integer containerId) throws FinderException;

    public Collection findByColumnY(Integer containerId, Integer columnY) throws FinderException;
}
