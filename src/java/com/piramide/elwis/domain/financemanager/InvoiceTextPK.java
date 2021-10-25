package com.piramide.elwis.domain.financemanager;

import java.io.Serializable;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceTextPK implements Serializable {
    public Integer languageId;
    public Integer templateId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceTextPK that = (InvoiceTextPK) o;

        if (languageId != null ? !languageId.equals(that.languageId) : that.languageId != null) {
            return false;
        }
        if (templateId != null ? !templateId.equals(that.templateId) : that.templateId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (languageId != null ? languageId.hashCode() : 0);
        result = 31 * result + (templateId != null ? templateId.hashCode() : 0);
        return result;
    }
}
