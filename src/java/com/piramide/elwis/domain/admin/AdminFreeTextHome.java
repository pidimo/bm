package com.piramide.elwis.domain.admin;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * AdminFreeText home interface
 *
 * @author Fernando Montaño
 * @version $Id: AdminFreeTextHome.java 9121 2009-04-17 00:28:59Z fernando $
 */
public interface AdminFreeTextHome extends EJBLocalHome {

    public AdminFreeText create(byte[] value, Integer companyId, Integer type) throws CreateException;

    AdminFreeText findByPrimaryKey(Integer key) throws FinderException;
}
