package com.piramide.elwis.domain.schedulermanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Recurrence loca home interface
 *
 * @author Fernando Montaño
 * @version $Id: RecurrenceHome.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface RecurrenceHome extends EJBLocalHome {


    public Collection findAll() throws FinderException;

    Recurrence findByPrimaryKey(Integer key) throws FinderException;

    public Recurrence create(ComponentDTO dto) throws CreateException;

}
