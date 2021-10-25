package com.piramide.elwis.domain.admin;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: FunctionDependencyBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 11:03:52 AM Fernando Montaño Exp $
 */


public abstract class FunctionDependencyBean implements EntityBean {
    public FunctionDependencyBean() {
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

    public abstract Integer getFunctionDependencyId();

    public abstract void setFunctionDependencyId(Integer functionDependencyId);

    public abstract Byte getOperationDependency();

    public abstract void setOperationDependency(Byte operationDependency);
}
