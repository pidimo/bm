package com.piramide.elwis.domain.catalogmanager;

/**
 * @author Ivan
 * @version $Id: AddressBank.java 1066 2004-06-05 14:20:27Z mauren ${NAME}.java, v 2.0 28-abr-2004 11:58:50 Ivan Exp $
 */

import javax.ejb.EJBLocalObject;

public interface AddressBank extends EJBLocalObject {
    Integer getAddressBankId();

    void setAddressBankId(Integer addressId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getName();

    void setName(String name1);

    Boolean getActive();

    void setActive(Boolean active);

    Integer getRecordDate();

    void setRecordDate(Integer recordDate);

    String getAddressType();

    void setAddressType(String addressType);

    Integer getVersion();

    void setVersion(Integer version);
}
