package com.piramide.elwis.dto.catalogmanager.categoryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class WebCategoryGroup {
    private Integer categoryGroupId;
    private String label;
    private String table;
    private List<Integer> categories;
    private boolean onlySubCategories = false;

    public WebCategoryGroup() {
        categories = new ArrayList<Integer>();
    }

    public Integer getCategoryGroupId() {
        return categoryGroupId;
    }

    public void setCategoryGroupId(Integer categoryGroupId) {
        this.categoryGroupId = categoryGroupId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void addCategory(Integer categoryId) {
        categories.add(categoryId);
    }

    public boolean isOnlySubCategories() {
        return onlySubCategories;
    }

    public void setOnlySubCategories(boolean onlySubCategories) {
        this.onlySubCategories = onlySubCategories;
    }
}
