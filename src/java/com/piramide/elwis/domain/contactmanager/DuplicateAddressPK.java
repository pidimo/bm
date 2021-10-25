package com.piramide.elwis.domain.contactmanager;

import java.io.Serializable;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DuplicateAddressPK implements Serializable {
    public Integer duplicateGroupId;
    public Integer addressId;

    public DuplicateAddressPK() {
    }

    public DuplicateAddressPK(Integer duplicateGroupId, Integer addressId) {
        this.duplicateGroupId = duplicateGroupId;
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DuplicateAddressPK that = (DuplicateAddressPK) o;

        if (addressId != null ? !addressId.equals(that.addressId) : that.addressId != null) {
            return false;
        }
        if (duplicateGroupId != null ? !duplicateGroupId.equals(that.duplicateGroupId) : that.duplicateGroupId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = duplicateGroupId != null ? duplicateGroupId.hashCode() : 0;
        result = 31 * result + (addressId != null ? addressId.hashCode() : 0);
        return result;
    }
}
