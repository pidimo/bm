package com.piramide.elwis.cmd.contactmanager.dataimport.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class Group {

    public static enum GroupType {
        ORGANIZATION(1),
        PERSON(2),
        CONTACT_PERSON(3);

        private Integer constant;

        private GroupType(Integer constant) {
            this.constant = constant;
        }

        public Integer getConstant() {
            return constant;
        }
    }

    private Integer groupId;
    private List<Column> columns = new ArrayList<Column>();
    private String resourceKey;

    private GroupType groupType;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public Column getColumn(Integer columnId) {
        for (Column column : columns) {
            if (column.getColumnId().equals(columnId)) {
                return column;
            }
        }

        return null;
    }

    public void addColumns(List<Column> columns) {
        if (null != columns && !columns.isEmpty()) {
            this.columns.addAll(columns);
        }
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }
}
