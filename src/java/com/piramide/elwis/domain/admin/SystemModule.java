package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SystemModule.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 10:36:57 AM Fernando Montaño Exp $
 */


public interface SystemModule extends EJBLocalObject {
    Integer getModuleId();

    void setModuleId(Integer moduleId);

    String getDescription();

    void setDescription(String description);

    String getNameKey();

    void setNameKey(String nameKey);

    String getPath();

    void setPath(String path);

    Collection getFunctions();

    void setFunctions(Collection functions);

    Collection getCompanyModules();

    void setCompanyModules(Collection companyModules);
}
