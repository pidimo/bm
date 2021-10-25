package com.piramide.elwis.domain.common.session;

import java.io.Serializable;

/**
 * UserSessionParamPK
 *
 * @author Fernando Montaño
 * @version $Id: UserSessionParamPK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class UserSessionParamPK implements Serializable {
    public Integer userId;
    public String statusName;
    public String module;
    public String paramName;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSessionParamPK)) {
            return false;
        }

        final UserSessionParamPK userSessionParamPK = (UserSessionParamPK) o;

        if (module != null ? !module.equals(userSessionParamPK.module) : userSessionParamPK.module != null) {
            return false;
        }
        if (paramName != null ? !paramName.equals(userSessionParamPK.paramName) : userSessionParamPK.paramName != null) {
            return false;
        }
        if (statusName != null ? !statusName.equals(userSessionParamPK.statusName) : userSessionParamPK.statusName != null) {
            return false;
        }
        if (userId != null ? !userId.equals(userSessionParamPK.userId) : userSessionParamPK.userId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (userId != null ? userId.hashCode() : 0);
        result = 29 * result + (statusName != null ? statusName.hashCode() : 0);
        result = 29 * result + (module != null ? module.hashCode() : 0);
        result = 29 * result + (paramName != null ? paramName.hashCode() : 0);
        return result;
    }
}
