package com.piramide.elwis.domain.reportmanager;

import javax.ejb.*;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public abstract class ColumnTotalizeBean implements EntityBean {
    EntityContext entityContext;

    public ColumnTotalizePK ejbCreate(ColumnTotalizePK pk, Integer companyId) throws CreateException {
        setColumnId(pk.columnId);
        setTotalizeId(pk.totalizeId);
        setCompanyId(companyId);
        return null;
    }

    public void ejbPostCreate(ColumnTotalizePK pk, Integer companyId) throws CreateException {
    }

    public ColumnTotalizeBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getColumnId();

    public abstract void setColumnId(Integer columnId);

    public abstract Integer getTotalizeId();

    public abstract void setTotalizeId(Integer totalizeId);

}
