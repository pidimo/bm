package com.piramide.elwis.utils.deduplication;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ContactMergeField {
    private Column column;
    private boolean mergeField;

    public ContactMergeField(Column column, boolean mergeField) {
        this.column = column;
        this.mergeField = mergeField;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public boolean isMergeField() {
        return mergeField;
    }

    public void setMergeField(boolean mergeField) {
        this.mergeField = mergeField;
    }
}
