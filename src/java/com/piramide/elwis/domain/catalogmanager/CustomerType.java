package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the CustomerType Entity Bean
 *
 * @author Ivan
 * @version $Id: CustomerType.java 1933 2004-07-21 16:35:51Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface CustomerType extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCustomerTypeId();

    void setCustomerTypeId(Integer customerTypeId);

    String getCustomerTypeName();

    void setCustomerTypeName(String name);

    Integer getVersion();

    void setVersion(Integer version);
}
