/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

public interface UserRole extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getUserId();

    void setUserId(Integer userId);

    Integer getRoleId();

    void setRoleId(Integer roleId);

    Role getRole();

    void setRole(Role role);
}
