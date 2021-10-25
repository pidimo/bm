package com.piramide.elwis.domain.contactmanager;

import java.io.Serializable;

/**
 * Represents a RecentEntityBean primary key.
 *
 * @author Fernando Montaño
 * @version $Id: RecentPK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class RecentPK implements Serializable {
    public Integer addressId;
    public Integer userId;

    public RecentPK() {
    }

    public RecentPK(Integer addressId, Integer userId) {
        this.addressId = addressId;
        this.userId = userId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecentPK)) {
            return false;
        }

        final RecentPK recentPK = (RecentPK) o;

        if (addressId != null ? !addressId.equals(recentPK.addressId) : recentPK.addressId != null) {
            return false;
        }
        if (userId != null ? !userId.equals(recentPK.userId) : recentPK.userId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (addressId != null ? addressId.hashCode() : 0);
        result = 29 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
