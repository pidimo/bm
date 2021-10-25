package com.piramide.elwis.domain.contactmanager;

import java.io.Serializable;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class RecordColumnPK implements Serializable {
    public Integer importColumnId;
    public Integer importRecordId;

    public RecordColumnPK() {
    }

    public RecordColumnPK(Integer importColumnId, Integer importRecordId) {
        this.importColumnId = importColumnId;
        this.importRecordId = importRecordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RecordColumnPK that = (RecordColumnPK) o;

        if (importColumnId != null ? !importColumnId.equals(that.importColumnId) : that.importColumnId != null) {
            return false;
        }
        if (importRecordId != null ? !importRecordId.equals(that.importRecordId) : that.importRecordId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = importColumnId != null ? importColumnId.hashCode() : 0;
        result = 31 * result + (importRecordId != null ? importRecordId.hashCode() : 0);
        return result;
    }
}
