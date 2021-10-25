package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Favorite Entity home interface
 *
 * @author Fernando Montaño
 * @version $Id: FavoriteHome.java 10417 2014-04-09 02:17:26Z miguel $
 */
public interface FavoriteHome extends EJBLocalHome {
    public Favorite create(ComponentDTO dto) throws CreateException;

    Favorite findByPrimaryKey(FavoritePK key) throws FinderException;

    public Collection findByCompanyUser(Integer userId, Integer companyId) throws FinderException;

    public Collection findByAddressId(Integer addressId) throws FinderException;
}
