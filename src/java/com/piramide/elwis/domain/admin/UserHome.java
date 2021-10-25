package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * User Bean home interface
 *
 * @author Fernando Montaño
 * @version $Id: UserHome.java 12552 2016-05-23 21:48:26Z miguel $
 */

public interface UserHome extends EJBLocalHome {

    public Collection findAll() throws FinderException;

    public User create(ComponentDTO dto) throws CreateException;

    User findByPrimaryKey(Integer key) throws FinderException;

    User findByUsername(String login, String loginCia) throws FinderException;

    User findByLogin(String login, Integer companyId) throws FinderException;

    User findByLoginWithOutMe(String login, Integer companyId, Integer userId) throws FinderException;

    User findUserWithinCompanyLogon(String userLogin, String password, String companyLogin) throws FinderException;

    User findUserLogonWithCompany(String userLogin, String companyLogin) throws FinderException;

    User findRootUserByCompany(Integer companyId) throws FinderException;

    Collection findUserByCompany(Integer companyId) throws FinderException;

    Collection findUserByCompanyAndActiveUsers(Integer companyId, Boolean active) throws FinderException;

    User findByAddressId(Integer companyId, Integer addressId) throws FinderException;

    User findByCompanyIdAddressIdAndUserType(Integer companyId, Integer addressId, Integer type) throws FinderException;

    Collection selectActiveApplicationUserIds() throws FinderException;

    public Integer selectCountCompanyUsersMobileAccessEnabledWithoutMe(Integer companyId, Integer currentUserId) throws FinderException;

    public Integer selectCountCompanyUsersMobileAccessEnabled(Integer companyId) throws FinderException;

    public Integer selectCountCompanyUsersWVAppAccessEnabledWithoutMe(Integer companyId, Integer currentUserId) throws FinderException;

    public Integer selectCountCompanyUsersWVAppAccessEnabled(Integer companyId) throws FinderException;

}
