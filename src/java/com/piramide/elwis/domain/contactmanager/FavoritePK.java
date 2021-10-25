package com.piramide.elwis.domain.contactmanager;

import java.io.Serializable;

/**
 * Favorite Entity Bean primary key class
 *
 * @author Fernando Montaño
 * @version $Id: FavoritePK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class FavoritePK implements Serializable {
    public Integer userId;
    public Integer addressId;

    public FavoritePK() {
    }

    public FavoritePK(Integer userId, Integer addressId) {
        this.userId = userId;
        this.addressId = addressId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FavoritePK)) {
            return false;
        }

        final FavoritePK favoritePK = (FavoritePK) o;

        if (addressId != null ? !addressId.equals(favoritePK.addressId) : favoritePK.addressId != null) {
            return false;
        }
        if (userId != null ? !userId.equals(favoritePK.userId) : favoritePK.userId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (userId != null ? userId.hashCode() : 0);
        result = 29 * result + (addressId != null ? addressId.hashCode() : 0);
        return result;
    }
}
