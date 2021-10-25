package com.piramide.elwis.domain.admin;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: CompanyModuleHome.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 11:14:40 AM Fernando Montaño Exp $
 */


public interface CompanyModuleHome extends EJBLocalHome {

    CompanyModule findByPrimaryKey(CompanyModulePK key) throws FinderException;

    public CompanyModule create(Integer companyId, Integer moduleId, Integer tableLimit) throws CreateException;
}
