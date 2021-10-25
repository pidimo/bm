package com.piramide.elwis.domain.salesmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: ActionPositionHome.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 24-01-2005 03:54:57 PM Fernando Montaño Exp $
 */


public interface ActionPositionHome extends EJBLocalHome {

    public Collection findAll() throws FinderException;

    ActionPosition findByPrimaryKey(Integer key) throws FinderException;

    public ActionPosition create(ComponentDTO dto) throws CreateException;

    Collection findByProcessAndContactId(Integer processId, Integer contactId) throws FinderException;

    Integer selectMaxPositionNumber(Integer processId, Integer contactId, Integer companyId) throws FinderException;
}
