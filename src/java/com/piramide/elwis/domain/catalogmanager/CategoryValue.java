package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the  CategoryValue Entity Bean
 *
 * @author Ivan
 * @version $Id: CategoryValue.java 8261 2008-06-02 23:44:41Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface CategoryValue extends EJBLocalObject {
    Integer getCategoryValueId();

    void setCategoryValueId(Integer categoryValueId);

    String getCategoryValueName();

    void setCategoryValueName(String name);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Category getCategory();

    void setCategory(Category category);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    String getTableId();

    void setTableId(String tableId);

    java.util.Collection getCategoryRelations();

    void setCategoryRelations(java.util.Collection categoryRelations);
}
