package com.piramide.elwis.web.common.dynamicsearch.structure.dynamicfield;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.structure.Field;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class CategoryField extends Field {

    private Integer categoryId;
    private String name;
    private DynamicSearchConstants.CategoryFieldType categoryFieldType;
    private CatalogConstants.CategoryType categoryType;
    private String table;

    private String joinFieldAlias;
    private String joinFieldAlias2;

    public CategoryField() {
        super();
    }

    @Override
    public boolean isCategoryField() {
        return true;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DynamicSearchConstants.CategoryFieldType getCategoryFieldType() {
        return categoryFieldType;
    }

    public void setCategoryFieldType(DynamicSearchConstants.CategoryFieldType categoryFieldType) {
        this.categoryFieldType = categoryFieldType;
    }

    public CatalogConstants.CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CatalogConstants.CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public String getJoinFieldAlias() {
        return joinFieldAlias;
    }

    public void setJoinFieldAlias(String joinFieldAlias) {
        this.joinFieldAlias = joinFieldAlias;
    }

    public String getJoinFieldAlias2() {
        return joinFieldAlias2;
    }

    public void setJoinFieldAlias2(String joinFieldAlias2) {
        this.joinFieldAlias2 = joinFieldAlias2;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
