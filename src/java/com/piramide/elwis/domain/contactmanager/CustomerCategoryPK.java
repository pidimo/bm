package com.piramide.elwis.domain.contactmanager;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CustomerCategoryPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CustomerCategoryPK implements Serializable {
    public Integer categoryId;
    public Integer categoryValueId;
    public Integer customerId;

    public CustomerCategoryPK() {
    }

    public CustomerCategoryPK(Integer categoryId, Integer categoryValueId, Integer customerId) {
        this.categoryId = categoryId;
        this.categoryValueId = categoryValueId;
        this.customerId = customerId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CustomerCategoryPK)) {
            return false;
        }

        final CustomerCategoryPK customerCategoryPK = (CustomerCategoryPK) other;

        if (categoryId != null ? !categoryId.equals(customerCategoryPK.categoryId) : customerCategoryPK.categoryId != null) {
            return false;
        }
        if (categoryValueId != null ? !categoryValueId.equals(customerCategoryPK.categoryValueId) : customerCategoryPK.categoryValueId != null) {
            return false;
        }
        if (customerId != null ? !customerId.equals(customerCategoryPK.customerId) : customerCategoryPK.customerId != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = (categoryId != null ? categoryId.hashCode() : 0);
        result = result + (categoryValueId != null ? categoryValueId.hashCode() : 0);
        result = 66 * result + (customerId != null ? customerId.hashCode() : 0);
        return result;

    }
}
