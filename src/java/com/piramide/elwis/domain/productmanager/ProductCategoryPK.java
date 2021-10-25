package com.piramide.elwis.domain.productmanager;

import java.io.Serializable;

/**
 * @author Titus
 * @version com.piramide.elwis.domain.productmanager.ProductCategoryPK.java, v 2.0 Aug 23, 2004 4:45:15 PM
 */
public class ProductCategoryPK implements Serializable {

    public Integer categoryId;
    public Integer categoryValueId;
    public Integer productId;

    public ProductCategoryPK() {
    }

    public ProductCategoryPK(Integer categoryId, Integer categoryValueId, Integer productId) {
        this.categoryId = categoryId;
        this.categoryValueId = categoryValueId;
        this.productId = productId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ProductCategoryPK)) {
            return false;
        }

        final ProductCategoryPK productCategoryPK = (ProductCategoryPK) other;

        if (categoryId != null ? !categoryId.equals(productCategoryPK.categoryId) : productCategoryPK.categoryId != null) {
            return false;
        }
        if (categoryValueId != null ? !categoryValueId.equals(productCategoryPK.categoryValueId) : productCategoryPK.categoryValueId != null) {
            return false;
        }
        if (productId != null ? !productId.equals(productCategoryPK.productId) : productCategoryPK.productId != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = (categoryId != null ? categoryId.hashCode() : 0);
        result = result + (categoryValueId != null ? categoryValueId.hashCode() : 0);
        result = 77 * result + (productId != null ? productId.hashCode() : 0);
        return result;

    }

}
