package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SystemFunction.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 10:50:17 AM Fernando Montaño Exp $
 */


public interface SystemFunction extends EJBLocalObject {
    Integer getFunctionId();

    void setFunctionId(Integer functionId);

    String getFunctionCode();

    void setFunctionCode(String functionCode);

    String getDescription();

    void setDescription(String description);

    Integer getModuleId();

    void setModuleId(Integer moduleId);

    String getNameKey();

    void setNameKey(String nameKey);

    Byte getPermissionsAllowed();

    void setPermissionsAllowed(Byte permissionsAllowed);

    Collection getDependencies();

    void setDependencies(Collection dependencies);

    SystemModule getSystemModule();

    void setSystemModule(SystemModule systemModule);

    Collection getAccessRights();

    void setAccessRights(Collection accessRights);
}
