package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: FunctionDependency.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 11:04:10 AM Fernando Montaño Exp $
 */


public interface FunctionDependency extends EJBLocalObject {
    Integer getFunctionId();

    void setFunctionId(Integer functionId);

    Integer getFunctionDependencyId();

    void setFunctionDependencyId(Integer functionDependencyId);

    Byte getOperationDependency();

    void setOperationDependency(Byte operationDependency);
}
