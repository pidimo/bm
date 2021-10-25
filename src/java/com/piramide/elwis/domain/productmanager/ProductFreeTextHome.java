package com.piramide.elwis.domain.productmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Product FreeText Local Home interface
 *
 * @author Fernando Montaño
 * @version $Id: ProductFreeTextHome.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface ProductFreeTextHome extends EJBLocalHome {
    ProductFreeText findByPrimaryKey(Integer key) throws FinderException;

    ProductFreeText create(byte[] text, Integer companyId, Integer type) throws CreateException;
}
