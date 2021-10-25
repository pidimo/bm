package com.piramide.elwis.domain.salesmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Freetext
 *
 * @author Fernando Montaño
 * @version $Id: SalesFreeTextHome.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface SalesFreeTextHome extends EJBLocalHome {
    SalesFreeText findByPrimaryKey(Integer key) throws FinderException;

    SalesFreeText create(byte[] value, Integer companyId, Integer type) throws CreateException;
}
