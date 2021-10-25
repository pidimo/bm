package com.piramide.elwis.domain.dashboard;

import javax.ejb.*;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:40:32 PM
 */

public abstract class AdminComponentBean implements EntityBean {

    public AdminComponentPK ejbCreate(Integer containerId, Integer componentId) throws CreateException {
        setContainerId(containerId);
        setComponentId(componentId);
        return null;
    }

    public void ejbPostCreate(Integer containerId, Integer componentId) throws CreateException {
    }

    EntityContext entityContext;

    public AdminComponentBean() {
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

    public abstract Integer getContainerId();

    public abstract void setContainerId(Integer containerId);

    public abstract Integer getComponentId();

    public abstract void setComponentId(Integer componentId);

    public abstract Integer getRowX();

    public abstract void setRowX(Integer rowX);

    public abstract Integer getColumnY();

    public abstract void setColumnY(Integer columnY);

    public abstract Integer getXmlComponentId();

    public abstract void setXmlComponentId(Integer xmlComponentId);

    public abstract Component getComponent();

    public abstract void setComponent(Component component);

    public abstract Integer getCompanyid();

    public abstract void setCompanyid(Integer companyid);
}
