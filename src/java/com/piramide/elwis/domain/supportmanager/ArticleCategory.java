package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:50:46 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleCategory extends EJBLocalObject {
    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    String getCategoryName();

    void setCategoryName(String categoryName);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getParentCategoryId();

    void setParentCategoryId(Integer parentCategoryId);

    Integer getVersion();

    void setVersion(Integer version);
}
