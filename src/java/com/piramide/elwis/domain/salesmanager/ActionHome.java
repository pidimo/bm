package com.piramide.elwis.domain.salesmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Action local home interface
 *
 * @author Fernando Montaño
 * @version $Id: ActionHome.java 9565 2009-08-18 22:27:38Z ivan $
 */


public interface ActionHome extends EJBLocalHome {

    Action findByPrimaryKey(ActionPK key) throws FinderException;

    Action create(ComponentDTO dto) throws CreateException;

    Integer selectMaxActionNumber(Integer companyId, Integer processId) throws FinderException;

    Collection findByNumber(Integer companyId, Integer actionTypeId, String number) throws FinderException;
}
