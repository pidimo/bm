package com.piramide.elwis.domain.catalogmanager;

import java.io.Serializable;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryRelationPK implements Serializable {
    public Integer categoryId;
    public Integer categoryValueId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CategoryRelationPK that = (CategoryRelationPK) o;

        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) {
            return false;
        }
        if (categoryValueId != null ? !categoryValueId.equals(that.categoryValueId) : that.categoryValueId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (categoryValueId != null ? categoryValueId.hashCode() : 0);
        return result;
    }
}
