package com.piramide.elwis.service.exception.dataimport;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class DataImportException extends Exception {
    int column = 0;
    int row = 0;
    String columnResource;
    String columnTitle;

    public DataImportException() {
    }

    public DataImportException(String message) {
        super(message);
    }

    public DataImportException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataImportException(Throwable cause) {
        super(cause);
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getColumnResource() {
        return columnResource;
    }

    public void setColumnResource(String columnResource) {
        this.columnResource = columnResource;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }
}
