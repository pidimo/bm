package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: CompanyModule.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 11:14:36 AM Fernando Montaño Exp $
 */


public interface CompanyModule extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getModuleId();

    void setModuleId(Integer moduleId);

    Integer getMainTableRecordsLimit();

    void setMainTableRecordsLimit(Integer mainTableRecordsLimit);

    SystemModule getSystemModule();

    void setSystemModule(SystemModule systemModule);

    Boolean getActive();

    void setActive(Boolean active);
}
