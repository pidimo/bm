package com.piramide.elwis.domain.admin;

import java.io.Serializable;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class UserPasswordChangePK implements Serializable {
    public Integer passwordChangeId;
    public Integer userId;

    public UserPasswordChangePK() {
    }

    public UserPasswordChangePK(Integer passwordChangeId, Integer userId) {
        this.passwordChangeId = passwordChangeId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserPasswordChangePK that = (UserPasswordChangePK) o;

        if (passwordChangeId != null ? !passwordChangeId.equals(that.passwordChangeId) : that.passwordChangeId != null) {
            return false;
        }
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = passwordChangeId != null ? passwordChangeId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
