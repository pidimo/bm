package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SystemModuleHome.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 10:37:00 AM Fernando Montaño Exp $
 */


public interface SystemModuleHome extends EJBLocalHome {

    SystemModule findByPrimaryKey(Integer key) throws FinderException;

    Collection findAll() throws FinderException;
}
