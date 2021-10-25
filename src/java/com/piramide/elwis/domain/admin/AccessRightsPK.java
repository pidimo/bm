package com.piramide.elwis.domain.admin;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: AccessRightsPK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class AccessRightsPK implements Serializable {
    public Integer functionId;
    public Integer roleId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessRightsPK)) {
            return false;
        }

        final AccessRightsPK accessRightsPK = (AccessRightsPK) o;

        if (functionId != null ? !functionId.equals(accessRightsPK.functionId) : accessRightsPK.functionId != null) {
            return false;
        }
        if (roleId != null ? !roleId.equals(accessRightsPK.roleId) : accessRightsPK.roleId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (functionId != null ? functionId.hashCode() : 0);
        result = 29 * result + (roleId != null ? roleId.hashCode() : 0);
        return result;
    }
}
