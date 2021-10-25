package com.piramide.elwis.cmd.contactmanager.dataimport.filemanager;

/**
 * Interface presents two methods that allow know the number of available rows and get a selected row
 * from tabulated data file.
 * eg.
 * csv or xls files
 *
 * @author Ivan Alban
 * @version 4.2.1
 */
public interface DataReader {
    /**
     * Returns the number of rows available in the tabulated data file
     *
     * @return int value
     */
    public int getNumberOfRows();

    /**
     * Returns the i-th DataRow object from tabulated data file.
     *
     * @param i The number of row to return.
     * @return DataRow object associated to  i parameter
     */
    public DataRow getRow(int i);
}
