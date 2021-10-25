package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.*;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SchedulerAccessBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 22-04-2005 02:38:35 PM Fernando Montaño Exp $
 */


public abstract class SchedulerAccessBean implements EntityBean {
    private EntityContext entityContext;

    public SchedulerAccessPK ejbCreate(Integer ownerUserId, Integer userId, Integer companyId) throws CreateException {
        this.setOwnerUserId(ownerUserId);
        this.setUserId(userId);
        this.setCompanyId(companyId);
        return null;
    }

    public void ejbPostCreate(Integer ownerUserId, Integer userId, Integer companyId) throws CreateException {

    }

    public SchedulerAccessBean() {
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

    public abstract Integer getOwnerUserId();

    public abstract void setOwnerUserId(Integer ownerUserId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Byte getPermission();

    public abstract void setPermission(Byte permission);

    public abstract Byte getPrivatePermission();

    public abstract void setPrivatePermission(Byte privatePermission);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
