package com.piramide.elwis.cmd.contactmanager.dataimport.filemanager;

import com.piramide.elwis.cmd.contactmanager.dataimport.validator.CSVCharsetException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class read a CSV file and implements the interface <code>DataReader</code>.
 *
 * @author Ivan Alban
 * @version 4.2.1
 */
public class CSVReader implements DataReader {

    private Log log = LogFactory.getLog(CSVReader.class);

    private int numberOfRows = 0;
    private String filePath;
    private char delimiter = ',';
    List<String[]> fileData = new ArrayList<String[]>();

    public CSVReader(String filePath) throws CSVCharsetException {
        this.filePath = filePath;
        countRows();
    }

    /**
     * Detect the charset of CSV file after count the number of available rows from csv file
     * and fill the <code>fileData</code> object with every row, each row is readed as
     * <code>String[]</code> object.
     *
     * @throws CSVCharsetException When cannot detect the charset of the csv file.
     */
    private void countRows() throws CSVCharsetException {
        try {
            ApplicationCharsetDetector charsetDetector = new ApplicationCharsetDetector("utf-8");
            charsetDetector.addCharset("windows-1252");

            String charset = charsetDetector.getCharset(filePath);
            if (null == charset) {
                log.debug("Can not define the charset property for: " + filePath);
                throw new CSVCharsetException("Can not define the charset property for:" + filePath);
            }

            log.debug("charset for '" + filePath + "': " + charset);

            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);

            BufferedReader reader = new BufferedReader(inputStreamReader, delimiter);
            au.com.bytecode.opencsv.CSVReader util = new au.com.bytecode.opencsv.CSVReader(reader);

            fileData = util.readAll();
            numberOfRows = fileData.size();
            reader.close();
            inputStreamReader.close();
        } catch (FileNotFoundException e) {
            log.error("Can not process the file '" + filePath + "' because:", e);
        } catch (IOException e) {
            log.error("Can not process the file '" + filePath + "' because:", e);
        }

    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public DataRow getRow(int i) {
        LinkedList<String> data = new LinkedList<String>();
        String[] values = fileData.get(i);
        data.addAll(Arrays.asList(values));
        DataRow dataRow = new DataRow(data);
        dataRow.setRowNumber(i);
        return dataRow;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }
}
