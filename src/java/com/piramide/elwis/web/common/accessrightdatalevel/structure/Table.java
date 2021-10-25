package com.piramide.elwis.web.common.accessrightdatalevel.structure;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class Table {
    private String tableName;
    private String relationField;

    public Table() {
    }

    public Table(String tableName, String relationField) {
        this.tableName = tableName;
        this.relationField = relationField;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRelationField() {
        return relationField;
    }

    public void setRelationField(String relationField) {
        this.relationField = relationField;
    }
}
