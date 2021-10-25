package com.piramide.elwis.domain.salesmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Status Entity local home interface
 *
 * @author Fernando Montaño
 * @version $Id: StatusHome.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 14-01-2005 10:21:24 AM Fernando Montaño Exp $
 */


public interface StatusHome extends EJBLocalHome {
    Status findByPrimaryKey(Integer key) throws FinderException;

    Status create(ComponentDTO dto) throws CreateException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
