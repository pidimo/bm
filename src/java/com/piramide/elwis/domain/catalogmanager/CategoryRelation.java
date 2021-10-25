/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface CategoryRelation extends EJBLocalObject {
    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    Integer getCategoryValueId();

    void setCategoryValueId(Integer categoryValueId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Category getCategory();

    void setCategory(Category category);

    CategoryValue getCategoryValue();

    void setCategoryValue(CategoryValue categoryValue);
}
