package com.piramide.elwis.domain.dashboard;

import com.piramide.elwis.utils.DashboardConstants;
import com.piramide.elwis.utils.PKGenerator;

import javax.ejb.*;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:48:13 PM
 */

public abstract class FilterBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(String name, String val, Boolean isRange) throws CreateException {
        this.setFilterId(PKGenerator.i.nextKey(DashboardConstants.TABLE_FILTER));
        this.setName(name);
        this.setVal(val);
        this.setIsRange(isRange);
        return null;
    }

    public void ejbPostCreate(String name, String val, Boolean isRange) throws CreateException {
    }

    public FilterBean() {
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

    public abstract Integer getFilterId();

    public abstract void setFilterId(Integer filterId);

    public abstract String getVal();

    public abstract void setVal(String val);

    public abstract Boolean getIsRange();

    public abstract void setIsRange(Boolean isRange);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
