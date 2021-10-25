package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public interface UserAddressAccessHome extends EJBLocalHome {
    public UserAddressAccess create(ComponentDTO dto) throws CreateException;

    UserAddressAccess findByPrimaryKey(UserAddressAccessPK key) throws FinderException;

    public Collection findUserAddressAccessByAddress(Integer addressId) throws FinderException;

    public Collection findUserAddressAccessByUserGroup(Integer userGroupId) throws FinderException;
}
