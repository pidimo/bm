package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: AccessRights.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 11:41:00 AM Fernando Montaño Exp $
 */


public interface AccessRights extends EJBLocalObject {
    Integer getFunctionId();

    void setFunctionId(Integer functionId);

    Integer getRoleId();

    void setRoleId(Integer roleId);

    Byte getPermission();

    void setPermission(Byte permission);

    SystemFunction getSystemFunction();

    void setSystemFunction(SystemFunction systemFunction);

    Boolean getActive();

    void setActive(Boolean active);

    Integer getModuleId();

    void setModuleId(Integer moduleId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
