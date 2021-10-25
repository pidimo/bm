package com.piramide.elwis.domain.admin;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SystemModuleBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 10:36:41 AM Fernando Montaño Exp $
 */


public abstract class SystemModuleBean implements EntityBean {
    public SystemModuleBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
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

    public abstract Integer getModuleId();

    public abstract void setModuleId(Integer moduleId);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract String getNameKey();

    public abstract void setNameKey(String nameKey);

    public abstract String getPath();

    public abstract void setPath(String path);

    public abstract Collection getFunctions();

    public abstract void setFunctions(Collection functions);

    public abstract Collection getCompanyModules();

    public abstract void setCompanyModules(Collection companyModules);
}
