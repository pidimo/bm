package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SystemFunctionHome.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 10:50:21 AM Fernando Montaño Exp $
 */


public interface SystemFunctionHome extends EJBLocalHome {
    SystemFunction findByPrimaryKey(Integer key) throws FinderException;

    SystemFunction findByCode(String code) throws FinderException;
}
