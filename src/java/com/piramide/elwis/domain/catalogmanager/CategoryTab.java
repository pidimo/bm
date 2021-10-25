/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface CategoryTab extends EJBLocalObject {
    Integer getCategoryTabId();

    void setCategoryTabId(Integer categoryTabId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getLabel();

    void setLabel(String label);

    Integer getSequence();

    void setSequence(Integer sequence);

    Integer getVersion();

    void setVersion(Integer version);

    String getTable();

    void setTable(String table);

    java.util.Collection getCategoryGroups();

    void setCategoryGroups(java.util.Collection categoryGroups);
}
