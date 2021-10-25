package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;


/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ValidationException extends Exception {
    private Integer columnIndex = 0;
    private Integer rowIndex = 0;
    private Column column;
    private String value;
    private Validator validator;

    public ValidationException(Integer columnIndex,
                               Integer rowIndex,
                               Column column,
                               String value,
                               Validator validator) {

        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.column = column;
        this.value = value;
        this.validator = validator;
    }

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public Integer getColumnIndex() {
        return columnIndex + 1;
    }

    public Integer getRowIndex() {
        return rowIndex + 1;
    }

    public Column getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }

    public Validator getValidator() {
        return validator;
    }
}
