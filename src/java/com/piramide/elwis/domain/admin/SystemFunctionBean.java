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
 * @version $Id: SystemFunctionBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 10:49:56 AM Fernando Montaño Exp $
 */


public abstract class SystemFunctionBean implements EntityBean {
    public SystemFunctionBean() {
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

    public abstract Integer getFunctionId();

    public abstract void setFunctionId(Integer functionId);

    public abstract String getFunctionCode();

    public abstract void setFunctionCode(String functionCode);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract Integer getModuleId();

    public abstract void setModuleId(Integer moduleId);

    public abstract String getNameKey();

    public abstract void setNameKey(String nameKey);

    public abstract Byte getPermissionsAllowed();

    public abstract void setPermissionsAllowed(Byte permissionsAllowed);

    public abstract Collection getDependencies();

    public abstract void setDependencies(Collection dependencies);

    public abstract SystemModule getSystemModule();

    public abstract void setSystemModule(SystemModule systemModule);

    public abstract Collection getAccessRights();

    public abstract void setAccessRights(Collection accessRights);
}
