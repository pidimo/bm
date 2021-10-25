package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * Recent Entity local interface
 *
 * @author Fernando Montaño
 * @version $Id: Recent.java 9121 2009-04-17 00:28:59Z fernando $
 */
public interface Recent extends EJBLocalObject {
    Integer getUserId();

    void setUserId(Integer userId);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getRecentId();

    void setRecentId(Integer recentId);

    void setAddress(Address add);

    Address getAddress();
}
