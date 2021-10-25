package com.piramide.elwis.domain.admin;

import java.io.Serializable;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class RolePasswordChangePK implements Serializable {
    public Integer passwordChangeId;
    public Integer roleId;

    public RolePasswordChangePK() {
    }

    public RolePasswordChangePK(Integer passwordChangeId, Integer roleId) {
        this.passwordChangeId = passwordChangeId;
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RolePasswordChangePK that = (RolePasswordChangePK) o;

        if (passwordChangeId != null ? !passwordChangeId.equals(that.passwordChangeId) : that.passwordChangeId != null) {
            return false;
        }
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = passwordChangeId != null ? passwordChangeId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        return result;
    }
}
