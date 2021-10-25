package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the SupplierType Entity Bean
 *
 * @author Ivan
 * @version $Id: SupplierType.java 1922 2004-07-19 21:20:07Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface SupplierType extends EJBLocalObject {
    Integer getSupplierTypeId();

    void setSupplierTypeId(Integer suppliertypeId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getSupplierTypeName();

    void setSupplierTypeName(String name);

    Integer getVersion();

    void setVersion(Integer version);
}
