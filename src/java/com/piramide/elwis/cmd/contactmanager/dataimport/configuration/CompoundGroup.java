package com.piramide.elwis.cmd.contactmanager.dataimport.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class CompoundGroup extends Group {
    List<Group> groups = new ArrayList<Group>();

    public CompoundGroup(Integer groupId, String resorceKey, GroupType type) {
        super.setGroupId(groupId);
        super.setResourceKey(resorceKey);
        super.setGroupType(type);
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public Column getColumn(Integer groupId, Integer columnId) {
        if (this.getGroupId().equals(groupId)) {
            for (Column column : getColumns()) {
                if (column.getColumnId().equals(columnId)) {
                    return column;
                }
            }
        }

        Column element = null;
        for (Group group : groups) {
            if (group instanceof CompoundGroup) {
                element = ((CompoundGroup) group).getColumn(groupId, columnId);
            }

            if (group.getGroupId().equals(groupId) && group instanceof SingleGroup) {
                element = ((SingleGroup) group).getColumn(columnId);
            }

            if (null != element) {
                return element;
            }
        }

        return null;
    }

    public List<Column> getRequiredColumns() {
        List<Column> result = new ArrayList<Column>();
        for (Column column : getColumns()) {
            if (column.isRequired()) {
                result.add(column);
            }
        }

        for (Group group : groups) {
            if (group instanceof SingleGroup) {
                result.addAll(((SingleGroup) group).getRequiredColumns());
            }

            if (group instanceof CompoundGroup) {
                result.addAll(((CompoundGroup) group).getRequiredColumns());
            }
        }
        return result;
    }

    public List<Column> getAllColumns() {
        List<Column> result = new ArrayList<Column>();
        for (Column column : getColumns()) {
            result.add(column);
        }

        for (Group group : groups) {
            if (group instanceof SingleGroup) {
                result.addAll(((SingleGroup) group).getColumns());
            }

            if (group instanceof CompoundGroup) {
                result.addAll(((CompoundGroup) group).getAllColumns());
            }
        }
        return result;
    }
}
