package com.piramide.elwis.domain.salesmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Priority Home local interface
 *
 * @author Fernando Montaño
 * @version $Id: PriorityHome.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface PriorityHome extends EJBLocalHome {
    Priority findByPrimaryKey(Integer key) throws FinderException;

    Priority create(ComponentDTO dto) throws CreateException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
