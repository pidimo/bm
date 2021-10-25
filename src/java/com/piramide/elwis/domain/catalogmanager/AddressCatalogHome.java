package com.piramide.elwis.domain.catalogmanager;

/**
 * @author Ernesto
 * @version $Id: AddressCatalogHome.java 7936 2007-10-27 16:08:39Z fernando $
 * @see com.piramide.elwis.domain.contactmanager.AddressBean
 */

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface AddressCatalogHome extends EJBLocalHome {

    com.piramide.elwis.domain.catalogmanager.AddressCatalog findByPrimaryKey(Integer key) throws FinderException;
}
