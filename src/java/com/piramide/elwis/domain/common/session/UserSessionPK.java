package com.piramide.elwis.domain.common.session;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: UserSessionPK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class UserSessionPK implements Serializable {
    public Integer userId;
    public String statusName;
    public String module;

    public UserSessionPK() {
    }

    public UserSessionPK(Integer userId, String statusName, String module) {
        this.userId = userId;
        this.statusName = statusName;
        this.module = module;
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSessionPK)) {
            return false;
        }

        final UserSessionPK userSessionPK = (UserSessionPK) o;

        if (module != null ? !module.equals(userSessionPK.module) : userSessionPK.module != null) {
            return false;
        }
        if (statusName != null ? !statusName.equals(userSessionPK.statusName) : userSessionPK.statusName != null) {
            return false;
        }
        if (userId != null ? !userId.equals(userSessionPK.userId) : userSessionPK.userId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (userId != null ? userId.hashCode() : 0);
        result = 29 * result + (statusName != null ? statusName.hashCode() : 0);
        result = 29 * result + (module != null ? module.hashCode() : 0);
        return result;
    }
}
