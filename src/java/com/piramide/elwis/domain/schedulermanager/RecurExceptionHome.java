package com.piramide.elwis.domain.schedulermanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * RecurException local home interface
 *
 * @author Fernando Montaño
 * @version $Id: RecurExceptionHome.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface RecurExceptionHome extends EJBLocalHome {
    RecurException findByPrimaryKey(Integer key) throws FinderException;

    Collection findByAppointmentId(Integer key) throws FinderException;

    public RecurException create(ComponentDTO dto) throws CreateException;
}
