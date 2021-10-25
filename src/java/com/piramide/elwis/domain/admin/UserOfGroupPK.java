package com.piramide.elwis.domain.admin;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 22, 2005
 * Time: 3:47:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserOfGroupPK implements Serializable {
    public Integer userId;
    public Integer userGroupId;

    public UserOfGroupPK() {
    }

    public UserOfGroupPK(Integer userId, Integer userGroupId) {
        this.userId = userId;
        this.userGroupId = userGroupId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserOfGroupPK)) {
            return false;
        }

        final UserOfGroupPK userOfGroupPK = (UserOfGroupPK) o;

        if (userGroupId != null ? !userGroupId.equals(userOfGroupPK.userGroupId) : userOfGroupPK.userGroupId != null) {
            return false;
        }
        if (userId != null ? !userId.equals(userOfGroupPK.userId) : userOfGroupPK.userId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (userId != null ? userId.hashCode() : 0);
        result = 29 * result + (userGroupId != null ? userGroupId.hashCode() : 0);
        return result;
    }
}
