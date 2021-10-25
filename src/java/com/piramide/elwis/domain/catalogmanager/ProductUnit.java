/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductUnit.java 2270 2004-08-16 21:53:54Z ivan ${NAME}.java, v 2.0 16-ago-2004 17:10:23 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface ProductUnit extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getUnitId();

    void setUnitId(Integer unitId);

    String getUnitName();

    void setUnitName(String unitName);

    Integer getVersion();

    void setVersion(Integer version);
}
