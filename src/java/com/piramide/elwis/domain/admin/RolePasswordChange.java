package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public interface RolePasswordChange extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getPasswordChangeId();

    void setPasswordChangeId(Integer passwordChangeId);

    Integer getRoleId();

    void setRoleId(Integer roleId);

    PasswordChange getPasswordChange();

    void setPasswordChange(PasswordChange passwordChange);
}
