package com.piramide.elwis.domain.contactmanager;

import java.io.Serializable;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class UserAddressAccessPK implements Serializable {
    public Integer addressId;
    public Integer userGroupId;

    public UserAddressAccessPK() {
    }

    public UserAddressAccessPK(Integer addressId, Integer userGroupId) {
        this.addressId = addressId;
        this.userGroupId = userGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserAddressAccessPK that = (UserAddressAccessPK) o;

        if (addressId != null ? !addressId.equals(that.addressId) : that.addressId != null) {
            return false;
        }
        if (userGroupId != null ? !userGroupId.equals(that.userGroupId) : that.userGroupId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = addressId != null ? addressId.hashCode() : 0;
        result = 31 * result + (userGroupId != null ? userGroupId.hashCode() : 0);
        return result;
    }
}
