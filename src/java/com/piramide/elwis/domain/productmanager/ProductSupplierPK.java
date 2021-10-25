package com.piramide.elwis.domain.productmanager;

import java.io.Serializable;


/**
 * AlfaCentauro Team
 *
 * @author Ernesto
 * @version $Id: ProductSupplierPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ProductSupplierPK implements Serializable {
    public Integer supplierId;
    public Integer productId;

    public ProductSupplierPK() {
    }

    public ProductSupplierPK(Integer supplierId, Integer productId) {
        this.supplierId = supplierId;
        this.productId = productId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductSupplierPK)) {
            return false;
        }

        final ProductSupplierPK productSupplierPK = (ProductSupplierPK) o;

        if (productId != null ? !productId.equals(productSupplierPK.productId) : productSupplierPK.productId != null) {
            return false;
        }
        if (supplierId != null ? !supplierId.equals(productSupplierPK.supplierId) : productSupplierPK.supplierId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (supplierId != null ? supplierId.hashCode() : 0);
        result = 29 * result + (productId != null ? productId.hashCode() : 0);
        return result;
    }
}
