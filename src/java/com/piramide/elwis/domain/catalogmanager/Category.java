package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * This Class represents the Local interface of the  Category Entity Bean
 *
 * @author Ivan
 * @version $Id: Category.java 12573 2016-08-10 17:02:39Z miguel ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface Category extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    String getCategoryName();

    void setCategoryName(String nameId);

    Integer getVersion();

    void setVersion(Integer versionId);

    String getTable();

    void setTable(String table);

    Integer getLangTextId();

    void setLangTextId(Integer langTextId);

    Integer getCategoryType();

    void setCategoryType(Integer categoryType);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    FreeText getFreeText();

    void setFreeText(FreeText freeText);

    public void setFreeText(EJBLocalObject freeText);

    Collection getCategoryValue();

    void setCategoryValue(Collection categoryValue);

    CategoryGroup getCategoryGroup();

    void setCategoryGroup(CategoryGroup categoryGroup);

    Integer getCategoryGroupId();

    void setCategoryGroupId(Integer categoryGroupId);

    Integer getSequence();

    void setSequence(Integer sequence);

    Integer getParentCategory();

    void setParentCategory(Integer parentcategory);

    Boolean getHasSubCategories();

    void setHasSubCategories(Boolean hasSubCategories);

    Collection getCategory();

    void setCategory(Collection category);

    Collection getChildrenCategories();

    void setChildrenCategories(Collection childrenCategories);

    Collection getCategoryFieldValues();

    void setCategoryFieldValues(Collection categoryFieldValues);

    Collection getCategoryRelations();

    void setCategoryRelations(Collection categoryRelations);

    Category getParentCategoryObj();

    void setParentCategoryObj(Category parentCategoryObj);

    Integer getSecondGroupId();

    void setSecondGroupId(Integer secondGroupId);

    CategoryGroup getSecondCategoryGroup();

    void setSecondCategoryGroup(CategoryGroup secondCategoryGroup);

    String getFieldIdentifier();

    void setFieldIdentifier(String fieldIdentifier);
}
