package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: FunctionDependencyHome.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 11:04:14 AM Fernando Montaño Exp $
 */


public interface FunctionDependencyHome extends EJBLocalHome {
    FunctionDependency findByPrimaryKey(FunctionDependencyPK key) throws FinderException;
}
