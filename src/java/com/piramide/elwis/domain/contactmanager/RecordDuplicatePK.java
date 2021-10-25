package com.piramide.elwis.domain.contactmanager;

import java.io.Serializable;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class RecordDuplicatePK implements Serializable {
    public Integer importRecordId;
    public Integer addressId;

    public RecordDuplicatePK() {
    }

    public RecordDuplicatePK(Integer importRecordId, Integer addressId) {
        this.importRecordId = importRecordId;
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

        RecordDuplicatePK that = (RecordDuplicatePK) o;

        if (addressId != null ? !addressId.equals(that.addressId) : that.addressId != null) {
            return false;
        }
        if (importRecordId != null ? !importRecordId.equals(that.importRecordId) : that.importRecordId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = importRecordId != null ? importRecordId.hashCode() : 0;
        result = 31 * result + (addressId != null ? addressId.hashCode() : 0);
        return result;
    }
}
