package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Represent Address EntityBean home interface
 *
 * @author Ernesto
 * @version $Id: AddressHome.java 2190 2004-08-10 22:03:11Z fernando $
 */

public interface AddressHome extends EJBLocalHome {
    com.piramide.elwis.domain.contactmanager.Address findByPrimaryKey(Integer key) throws FinderException;

    Address create(ComponentDTO dto) throws CreateException;

    Collection findByFavorites(Integer companyId, Integer userId) throws FinderException;

    Address findByPersonNames(String name1, String name2, Integer companyId) throws FinderException;

    Collection selectAddressesByNames(String name1, String name2, String name3, String addressType, Integer companyId,
                                      Integer userId) throws FinderException;

}
