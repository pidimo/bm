package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public interface UserAddressAccess extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getUserGroupId();

    void setUserGroupId(Integer userGroupId);

    Address getAddress();

    void setAddress(Address address);
}
