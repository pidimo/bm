package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Supplier entity home interface.
 *
 * @author Fernando Montaño
 * @version $Id: SupplierHome.java 9121 2009-04-17 00:28:59Z fernando $
 */
public interface SupplierHome extends EJBLocalHome {

    public Supplier create(ComponentDTO dto) throws CreateException;

    Supplier findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;
}
