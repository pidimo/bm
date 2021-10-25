package com.piramide.elwis.domain.productmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Product Local Home interface
 *
 * @author Fernando Montaño
 * @version $Id: ProductHome.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface ProductHome extends EJBLocalHome {
    Product findByPrimaryKey(Integer key) throws FinderException;

    Product create(ComponentDTO dto) throws CreateException;
}
