package com.piramide.elwis.domain.dashboard;

import com.piramide.elwis.utils.DashboardConstants;
import com.piramide.elwis.utils.PKGenerator;

import javax.ejb.*;
import java.util.Collection;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:46:47 PM
 */

public abstract class ComponentBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate() throws CreateException {
        setComponentId(PKGenerator.i.nextKey(DashboardConstants.TABLE_COMPONENT));
        return null;
    }

    public void ejbPostCreate() throws CreateException {
    }

    public ComponentBean() {
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

    public abstract Integer getComponentId();

    public abstract void setComponentId(Integer componentId);

    public abstract Integer getXmlComponentId();

    public abstract void setXmlComponentId(Integer xmlComponentId);

    public abstract Collection getColumns();

    public abstract void setColumns(Collection columns);

    public abstract Collection getFilters();

    public abstract void setFilters(Collection filters);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
