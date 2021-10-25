package com.piramide.elwis.domain.common.config;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SystemConstantBean.java 9121 2009-04-17 00:28:59Z fernando $
 */


public abstract class SystemConstantBean implements javax.ejb.EntityBean {
    public SystemConstantBean() {
    }

    public void setEntityContext(javax.ejb.EntityContext entityContext) throws javax.ejb.EJBException {
    }

    public void unsetEntityContext() throws javax.ejb.EJBException {
    }

    public void ejbRemove() throws javax.ejb.RemoveException, javax.ejb.EJBException {
    }

    public void ejbActivate() throws javax.ejb.EJBException {
    }

    public void ejbPassivate() throws javax.ejb.EJBException {
    }

    public void ejbLoad() throws javax.ejb.EJBException {
    }

    public void ejbStore() throws javax.ejb.EJBException {
    }

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getResourceKey();

    public abstract void setResourceKey(String resourceKey);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract String getType();

    public abstract void setType(String type);

    public abstract String getValue();

    public abstract void setValue(String value);
}
