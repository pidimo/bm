package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of AddressSource Entity Bean
 *
 * @author Ivan
 * @version $Id: AddressSource.java 1933 2004-07-21 16:35:51Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface AddressSource extends EJBLocalObject {
    Integer getAddressSourceId();

    void setAddressSourceId(Integer addressSourceId);

    String getAddressSourceName();

    void setAddressSourceName(String name);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

}
