package com.piramide.elwis.domain.dashboard;

import com.piramide.elwis.utils.DashboardConstants;
import com.piramide.elwis.utils.PKGenerator;

import javax.ejb.*;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:50:55 PM
 */

public abstract class ComponentColumnBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(Integer xmlColumnId, Integer position, String order) throws CreateException {
        setComponentColumnId(PKGenerator.i.nextKey(DashboardConstants.TABLE_COMPONENTCOLUMN));
        setXmlColumnId(xmlColumnId);
        setPosition(position);
        setOrd(order);
        return null;
    }

    public void ejbPostCreate(Integer xmlColumnId, Integer position, String order) throws CreateException {
    }

    public ComponentColumnBean() {
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

    public abstract Integer getComponentColumnId();

    public abstract void setComponentColumnId(Integer componentColumnId);

    public abstract Integer getXmlColumnId();

    public abstract void setXmlColumnId(Integer xmlColumnId);

    public abstract String getOrd();

    public abstract void setOrd(String ord);

    public abstract Integer getPosition();

    public abstract void setPosition(Integer position);

    public abstract Component getComponent();

    public abstract void setComponent(Component component);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
