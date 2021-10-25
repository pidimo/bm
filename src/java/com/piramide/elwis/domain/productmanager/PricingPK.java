package com.piramide.elwis.domain.productmanager;

import java.io.Serializable;


/**
 * AlfaCentauro Team
 *
 * @author Ernesto
 * @version $Id: PricingPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class PricingPK implements Serializable {
    public Integer productId;
    public Integer quantity;

    public PricingPK() {
    }

    public PricingPK(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PricingPK)) {
            return false;
        }

        final PricingPK pricingPK = (PricingPK) o;

        if (productId != null ? !productId.equals(pricingPK.productId) : pricingPK.productId != null) {
            return false;
        }
        if (quantity != null ? !quantity.equals(pricingPK.quantity) : pricingPK.quantity != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (productId != null ? productId.hashCode() : 0);
        result = 29 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }
}
