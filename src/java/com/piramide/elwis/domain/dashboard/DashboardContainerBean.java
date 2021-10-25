package com.piramide.elwis.domain.dashboard;

import com.piramide.elwis.utils.DashboardConstants;
import com.piramide.elwis.utils.PKGenerator;

import javax.ejb.*;
import java.util.Collection;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:44:56 PM
 */

public abstract class DashboardContainerBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(Integer userId, Integer companyId) throws CreateException {
        setDashboardContainerId(PKGenerator.i.nextKey(DashboardConstants.TABLE_DASHBOARDCONTAINER));
        setUserId(userId);
        setCompanyId(companyId);
        return null;
    }

    public void ejbPostCreate(Integer userId, Integer companyId) throws CreateException {
    }

    public DashboardContainerBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getDashboardContainerId();

    public abstract void setDashboardContainerId(Integer dashboardContainerId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Collection getAdminComponents();

    public abstract void setAdminComponents(Collection adminComponents);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
