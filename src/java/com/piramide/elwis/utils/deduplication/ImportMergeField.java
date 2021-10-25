package com.piramide.elwis.utils.deduplication;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ImportMergeField {
    private Column column;
    private boolean keepImportField;

    public ImportMergeField(Column column, boolean keepImportField) {
        this.column = column;
        this.keepImportField = keepImportField;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public boolean isKeepImportField() {
        return keepImportField;
    }

    public void setKeepImportField(boolean keepImportField) {
        this.keepImportField = keepImportField;
    }
}
