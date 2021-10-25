package com.piramide.elwis.domain.salesmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Action Type Home local interface
 *
 * @author Fernando Montaño
 * @version $Id: ActionTypeHome.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 14-01-2005 10:41:23 AM Fernando Montaño Exp $
 */


public interface ActionTypeHome extends EJBLocalHome {
    ActionType findByPrimaryKey(Integer key) throws FinderException;

    ActionType create(ComponentDTO dto) throws CreateException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
