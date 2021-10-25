package com.piramide.elwis.domain.admin;

import java.io.Serializable;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class UserRolePK implements Serializable {
    public Integer userId;
    public Integer roleId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserRolePK that = (UserRolePK) o;

        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) {
            return false;
        }
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "userId=" + userId + " roleId=" + roleId;
    }
}
