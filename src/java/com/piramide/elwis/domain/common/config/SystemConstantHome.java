package com.piramide.elwis.domain.common.config;

import java.util.Collection;

/**
 * System Constant
 *
 * @author Fernando Montaño
 * @version $Id: SystemConstantHome.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface SystemConstantHome extends javax.ejb.EJBLocalHome {

    SystemConstant findByPrimaryKey(SystemConstantPK key) throws javax.ejb.FinderException;

    Collection findConstantsByType(String type) throws javax.ejb.FinderException;
}
