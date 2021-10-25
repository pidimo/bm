package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: Role.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 06-01-2005 09:47:28 AM Fernando Montaño Exp $
 */


public interface Role extends EJBLocalObject {
    Integer getRoleId();

    void setRoleId(Integer roleId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Boolean getIsDefault();

    void setIsDefault(Boolean isDefault);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    String getRoleName();

    void setRoleName(String roleName);

    Integer getVersion();

    void setVersion(Integer version);

    AdminFreeText getDescriptionText();

    void setDescriptionText(AdminFreeText descriptionText);

    Collection getAccessRights();

    void setAccessRights(Collection accessRights);

    void setDescriptionText(EJBLocalObject descriptionText);
}
