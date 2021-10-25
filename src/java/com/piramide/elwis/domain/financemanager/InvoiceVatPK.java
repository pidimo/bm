package com.piramide.elwis.domain.financemanager;

import java.io.Serializable;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceVatPK implements Serializable {
    public Integer invoiceId;
    public Integer vatId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceVatPK that = (InvoiceVatPK) o;

        if (invoiceId != null ? !invoiceId.equals(that.invoiceId) : that.invoiceId != null) {
            return false;
        }
        if (vatId != null ? !vatId.equals(that.vatId) : that.vatId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (invoiceId != null ? invoiceId.hashCode() : 0);
        result = 31 * result + (vatId != null ? vatId.hashCode() : 0);
        return result;
    }
}
