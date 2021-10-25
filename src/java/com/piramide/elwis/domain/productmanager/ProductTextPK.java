package com.piramide.elwis.domain.productmanager;

import java.io.Serializable;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ProductTextPK implements Serializable {
    public Integer productId;
    public Integer languageId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductTextPK that = (ProductTextPK) o;

        if (languageId != null ? !languageId.equals(that.languageId) : that.languageId != null) {
            return false;
        }
        if (productId != null ? !productId.equals(that.productId) : that.productId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (languageId != null ? languageId.hashCode() : 0);
        return result;
    }
}
