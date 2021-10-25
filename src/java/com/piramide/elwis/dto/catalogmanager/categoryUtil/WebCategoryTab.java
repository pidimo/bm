package com.piramide.elwis.dto.catalogmanager.categoryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class WebCategoryTab {
    private Integer categoryTabId;
    private Integer sequence;
    private String table;
    private List<WebCategoryGroup> categoryGroups;

    public WebCategoryTab() {
        categoryGroups = new ArrayList<WebCategoryGroup>();
    }

    public Integer getCategoryTabId() {
        return categoryTabId;
    }

    public void setCategoryTabId(Integer categoryTabId) {
        this.categoryTabId = categoryTabId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<WebCategoryGroup> getCategoryGroups() {
        return categoryGroups;
    }

    public void setCategoryGroups(List<WebCategoryGroup> categoryGroups) {
        this.categoryGroups = categoryGroups;
    }

    public void addCategoryGroup(WebCategoryGroup categoryGroup) {
        categoryGroups.add(categoryGroup);
    }
}
