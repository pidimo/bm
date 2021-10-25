package com.piramide.elwis.domain.supportmanager;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:25:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class SupportUserPK implements Serializable {
    public Integer productId;
    public Integer userId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SupportUserPK that = (SupportUserPK) o;

        if (productId != null ? !productId.equals(that.productId) : that.productId != null) {
            return false;
        }
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (productId != null ? productId.hashCode() : 0);
        result = 29 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
