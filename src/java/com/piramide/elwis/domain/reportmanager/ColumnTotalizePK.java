package com.piramide.elwis.domain.reportmanager;

import java.io.Serializable;

/**
 * @author : ivan
 * @version : $Id ColumnTotalizePK ${time}
 */
public class ColumnTotalizePK implements Serializable {
    public Integer columnId;
    public Integer totalizeId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnTotalizePK)) {
            return false;
        }

        final ColumnTotalizePK columnTotalizePK = (ColumnTotalizePK) o;

        if (columnId != null ? !columnId.equals(columnTotalizePK.columnId) : columnTotalizePK.columnId != null) {
            return false;
        }
        if (totalizeId != null ? !totalizeId.equals(columnTotalizePK.totalizeId) : columnTotalizePK.totalizeId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (columnId != null ? columnId.hashCode() : 0);
        result = 29 * result + (totalizeId != null ? totalizeId.hashCode() : 0);
        return result;
    }
}
