package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: RoleHome.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 09:47:28 AM Fernando Montaño Exp $
 */


public interface RoleHome extends EJBLocalHome {
    Role findByPrimaryKey(Integer key) throws FinderException;

    Role create(ComponentDTO dto) throws CreateException;

    Collection findByCompanyKey(Integer companyId) throws FinderException;

    Role findDefault(Integer companyId) throws FinderException;

    Collection findByCompanyId(Integer companyId) throws FinderException;
}
