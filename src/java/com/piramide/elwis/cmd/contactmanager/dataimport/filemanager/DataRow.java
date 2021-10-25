package com.piramide.elwis.cmd.contactmanager.dataimport.filemanager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class DataRow {
    private Log log = LogFactory.getLog(this.getClass());
    int rowNumber = 0;

    LinkedList<String> columns;

    public DataRow(LinkedList<String> columns) {
        this.columns = columns;
    }

    public LinkedList<String> getColumns() {
        return columns;
    }

    public String getValue(Integer index) {
        String value = null;
        try {
            value = columns.get(index);
        } catch (IndexOutOfBoundsException e) {
            log.debug("-> Not Found data for index=" + index);
        }

        if (null != value) {
            return value.trim();
        }

        return value;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
}
