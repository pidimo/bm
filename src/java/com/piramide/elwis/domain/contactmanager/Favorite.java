package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * Favorite entity local interface.
 *
 * @author Fernando Montaño
 * @version $Id: Favorite.java 9121 2009-04-17 00:28:59Z fernando $
 */
public interface Favorite extends EJBLocalObject {
    Integer getUserId();

    void setUserId(Integer userId);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Address getAddress();

    void setAddress(Address address);
}
