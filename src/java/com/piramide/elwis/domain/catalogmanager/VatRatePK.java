package com.piramide.elwis.domain.catalogmanager;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: VatRatePK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class VatRatePK implements Serializable {
    public Integer validFrom;
    public Integer vatId;

    public VatRatePK() {
    }

    public VatRatePK(Integer validFrom, Integer vatId) {
        this.validFrom = validFrom;
        this.vatId = vatId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VatRatePK)) {
            return false;
        }

        final VatRatePK vatRatePK = (VatRatePK) o;

        if (validFrom != null ? !validFrom.equals(vatRatePK.validFrom) : vatRatePK.validFrom != null) {
            return false;
        }
        if (vatId != null ? !vatId.equals(vatRatePK.vatId) : vatRatePK.vatId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (validFrom != null ? validFrom.hashCode() : 0);
        result = 29 * result + (vatId != null ? vatId.hashCode() : 0);
        return result;
    }

}
