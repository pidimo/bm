package com.piramide.elwis.web.reports.dynamiccolumns;

import com.jatun.titus.listgenerator.structure.dynamiccolumn.DynamicColumnField;

/**
 * Jatun S.R.L.
 * Implementation for dynamic column field, required from titus structure configuration
 *
 * @author Miky
 * @version $Id: CategoryDynamicColumn.java 04-sep-2008 15:49:47 $
 */
public class CategoryDynamicColumnField implements DynamicColumnField {
    private String label;
    private Integer key;
    private String fieldTitusPath;

    public void setLabel(String label) {
        this.label = label;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public void setFieldTitusPath(String fieldTitusPath) {
        this.fieldTitusPath = fieldTitusPath;
    }

    public String getLabel() {
        return label;
    }

    public Integer getKey() {
        return key;
    }

    public String getRelativeFieldTitusPath() {
        return fieldTitusPath;
    }
}
