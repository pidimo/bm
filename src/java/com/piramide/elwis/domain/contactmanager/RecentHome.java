package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Recent Entity Home interface
 *
 * @author Fernando Montaño
 * @version $Id: RecentHome.java 10417 2014-04-09 02:17:26Z miguel $
 */
public interface RecentHome extends EJBLocalHome {
    com.piramide.elwis.domain.contactmanager.Recent findByPrimaryKey(RecentPK key) throws FinderException;

    public Recent create(ComponentDTO dto) throws CreateException;

    public Collection findByCompanyUser(Integer userId, Integer companyId) throws FinderException;

    public Collection findByAddressId(Integer addressId) throws FinderException;
}
