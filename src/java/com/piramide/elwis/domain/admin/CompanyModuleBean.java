package com.piramide.elwis.domain.admin;

import javax.ejb.*;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: CompanyModuleBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 11:14:18 AM Fernando Montaño Exp $
 */


public abstract class CompanyModuleBean implements EntityBean {
    EntityContext entityContext;

    public CompanyModulePK ejbCreate(Integer companyId, Integer moduleId, Integer tableLimit) throws CreateException {

        setCompanyId(companyId);
        setModuleId(moduleId);
        setMainTableRecordsLimit(tableLimit);
        return null;
    }

    public void ejbPostCreate(Integer companyId, Integer moduleId, Integer tableLimit) throws CreateException {

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

    public abstract Integer getModuleId();

    public abstract void setModuleId(Integer moduleId);

    public abstract Integer getMainTableRecordsLimit();

    public abstract void setMainTableRecordsLimit(Integer mainTableRecordsLimit);

    public abstract SystemModule getSystemModule();

    public abstract void setSystemModule(SystemModule systemModule);

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);
}
