/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface CategoryGroup extends EJBLocalObject {
    Integer getCategoryGroupId();

    void setCategoryGroupId(Integer categotyGroupId);

    Integer getCategoryTabId();

    void setCategoryTabId(Integer categoryTabId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getLabel();

    void setLabel(String label);

    Integer getSequence();

    void setSequence(Integer sequence);

    String getTable();

    void setTable(String table);

    Integer getVersion();

    void setVersion(Integer version);

    CategoryTab getCategoryTab();

    void setCategoryTab(CategoryTab categoryTab);

    java.util.Collection getCategories();

    void setCategories(java.util.Collection categories);
}
