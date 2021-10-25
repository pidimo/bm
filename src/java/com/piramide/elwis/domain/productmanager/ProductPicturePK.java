package com.piramide.elwis.domain.productmanager;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: ProductPicturePK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ProductPicturePK implements Serializable {
    public Integer freeTextId;
    public Integer productId;

    public ProductPicturePK() {
    }

    public ProductPicturePK(Integer freeTextId, Integer productId) {
        this.freeTextId = freeTextId;
        this.productId = productId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductPicturePK)) {
            return false;
        }

        final ProductPicturePK productPicturePK = (ProductPicturePK) o;

        if (freeTextId != null ? !freeTextId.equals(productPicturePK.freeTextId) : productPicturePK.freeTextId != null) {
            return false;
        }
        if (productId != null ? !productId.equals(productPicturePK.productId) : productPicturePK.productId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (freeTextId != null ? freeTextId.hashCode() : 0);
        result = 29 * result + (productId != null ? productId.hashCode() : 0);
        return result;
    }

}
