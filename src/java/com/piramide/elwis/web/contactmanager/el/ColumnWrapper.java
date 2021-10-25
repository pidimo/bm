package com.piramide.elwis.web.contactmanager.el;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ColumnWrapper {
    String columnId;
    String groupId;
    String groupName;
    String columnName;
    String value;
    Integer uiPosition;

    public ColumnWrapper(String columnId, String groupId, String groupName, String columnName, String value, Integer uiPosition) {
        this.columnId = columnId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.columnName = columnName;
        this.value = value;
        this.uiPosition = uiPosition;
    }

    @Override
    public String toString() {
        return "(columnId=" + columnId + ", groupId=" + groupId + ", groupName=" + groupName + ",columnName=" + columnName + ", value=" + value + ")";
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getUiPosition() {
        return uiPosition;
    }

    public void setUiPosition(Integer uiPosition) {
        this.uiPosition = uiPosition;
    }
}