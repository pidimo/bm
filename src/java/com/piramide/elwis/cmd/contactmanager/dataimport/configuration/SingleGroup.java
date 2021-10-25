package com.piramide.elwis.cmd.contactmanager.dataimport.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SingleGroup extends Group {
    public SingleGroup(Integer groupId, String resourceKey, GroupType type) {
        super.setGroupId(groupId);
        super.setResourceKey(resourceKey);
        super.setGroupType(type);
    }

    public List<Column> getRequiredColumns() {
        List<Column> result = new ArrayList<Column>();
        for (Column column : getColumns()) {
            if (column.isRequired()) {
                result.add(column);
            }
        }

        return result;
    }

    @Override
    public Column getColumn(Integer columnId) {
        return super.getColumn(columnId);
    }
}
