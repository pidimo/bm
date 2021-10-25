package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: AccessRightsHome.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 11:41:04 AM Fernando Montaño Exp $
 */


public interface AccessRightsHome extends EJBLocalHome {
    public AccessRights create(ComponentDTO dto) throws CreateException;

    public AccessRights create(Integer functionId, Integer roleId, Integer moduleId, Integer companyId) throws CreateException;

    AccessRights findByPrimaryKey(AccessRightsPK key) throws FinderException;

    AccessRights findByFunctionAndRole(Integer functionId, Integer roleId) throws FinderException;

    Collection findAllAccessRightsModule(Integer moduleId, Integer companyId) throws FinderException;

    Collection findAccessRightsByRole(Integer roleId, Integer companyId) throws FinderException;
}
