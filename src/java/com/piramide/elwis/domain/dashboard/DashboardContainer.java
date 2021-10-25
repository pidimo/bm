package com.piramide.elwis.domain.dashboard;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:44:57 PM
 */

public interface DashboardContainer extends EJBLocalObject {
    Integer getDashboardContainerId();

    void setDashboardContainerId(Integer dashboardContainerId);

    Integer getUserId();

    void setUserId(Integer userId);

    Collection getAdminComponents();

    void setAdminComponents(Collection adminComponents);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
