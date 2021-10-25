package com.piramide.elwis.domain.catalogmanager;

import java.io.Serializable;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class PayConditionTextPK implements Serializable {
    public Integer payConditionId;
    public Integer languageId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PayConditionTextPK that = (PayConditionTextPK) o;

        if (languageId != null ? !languageId.equals(that.languageId) : that.languageId != null) {
            return false;
        }
        if (payConditionId != null ? !payConditionId.equals(that.payConditionId) : that.payConditionId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (payConditionId != null ? payConditionId.hashCode() : 0);
        result = 31 * result + (languageId != null ? languageId.hashCode() : 0);
        return result;
    }
}
