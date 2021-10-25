package com.piramide.elwis.domain.salesmanager;

import java.io.Serializable;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ActionTypeSequencePK implements Serializable {
    public Integer actionTypeId;
    public Integer numberId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActionTypeSequencePK that = (ActionTypeSequencePK) o;

        if (actionTypeId != null ? !actionTypeId.equals(that.actionTypeId) : that.actionTypeId != null) {
            return false;
        }
        if (numberId != null ? !numberId.equals(that.numberId) : that.numberId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = actionTypeId != null ? actionTypeId.hashCode() : 0;
        result = 31 * result + (numberId != null ? numberId.hashCode() : 0);
        return result;
    }
}
