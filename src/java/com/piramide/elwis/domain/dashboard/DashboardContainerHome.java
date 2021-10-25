package com.piramide.elwis.domain.dashboard;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:44:57 PM
 */

public interface DashboardContainerHome extends EJBLocalHome {
    public DashboardContainer create(Integer userId, Integer companyId) throws CreateException;

    DashboardContainer findByPrimaryKey(Integer key) throws FinderException;

    DashboardContainer findByUserIdCompanyId(Integer userId, Integer companyId) throws FinderException;
}
