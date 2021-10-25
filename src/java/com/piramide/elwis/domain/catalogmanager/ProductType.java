/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductType.java 11758 2015-12-09 00:16:33Z miguel ${NAME}.java, v 2.0 16-ago-2004 16:51:27 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface ProductType extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getTypeId();

    void setTypeId(Integer typeId);

    String getTypeName();

    void setTypeName(String typeName);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getProductTypeType();

    void setProductTypeType(Integer productTypeType);
}
