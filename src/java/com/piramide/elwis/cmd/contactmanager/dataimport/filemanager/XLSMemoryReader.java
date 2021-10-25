package com.piramide.elwis.cmd.contactmanager.dataimport.filemanager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class XLSMemoryReader implements DataReader {
    private Log log = LogFactory.getLog(this.getClass());
    private int numberOfRows = 0;
    private String filePath;
    private POIFSFileSystem fileSystem;
    private HSSFWorkbook workBook;
    private short maxColIx = 0;

    public XLSMemoryReader(String filePath) {
        this.filePath = filePath;
        try {
            POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(filePath));
            workBook = new HSSFWorkbook(fileSystem);
            HSSFSheet sheet = workBook.getSheetAt(0);
            numberOfRows = sheet.getLastRowNum() + 1;
            log.debug("-> available rows=" + numberOfRows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public DataRow getRow(int i) {
        LinkedList<String> columns = new LinkedList<String>();
        HSSFSheet sheet = workBook.getSheetAt(0);
        HSSFRow row = sheet.getRow(i);
        short minColIx = 0;
        if (null != row) {
            maxColIx = row.getLastCellNum();
            for (short j = minColIx; j <= maxColIx; j++) {
                HSSFCell cell = row.getCell(j);
                String value = null;
                if (null != cell) {
                    if (HSSFCell.CELL_TYPE_BLANK == cell.getCellType()) {
                        log.debug("-> Reading empty cell");
                    }
                    if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                        value = parseNumericCell(cell.getNumericCellValue());
                        cell.getDateCellValue();
                    }
                    if (HSSFCell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
                        log.debug("-> Reading boolean cell");
                    }
                    if (HSSFCell.CELL_TYPE_ERROR == cell.getCellType()) {
                        log.debug("-> Reading error cell");
                    }
                    if (HSSFCell.CELL_TYPE_STRING == cell.getCellType()) {
                        value = cell.getStringCellValue();
                    }
                }
                columns.add(j, value);
            }
        } else {
            for (int j = 0; j <= maxColIx; j++) {
                columns.add(null);
            }
        }
        if (isValidRow(columns)) {
            DataRow dataRow = new DataRow(columns);
            dataRow.setRowNumber(i);
            return dataRow;
        }

        return null;
    }

    private String parseNumericCell(Double cellValue) {
        if (null == cellValue) {
            return null;
        }

        boolean isInt = isInteger(cellValue);
        if (isInt) {
            BigDecimal auxNumber = new BigDecimal(cellValue);
            Integer number = auxNumber.intValue();
            return number.toString();
        } else {
            BigDecimal auxNumber = new BigDecimal(cellValue.toString());
            return auxNumber.toString();
        }
    }

    private boolean isInteger(Double value) {
        try {
            return value % 2 == 0 || value % 2 == 1 || value % 2 == -1;
        } catch (Exception e) {
            return false;
        }
    }

    private Date getCellValueAsDate(double value, boolean isUsing1904) {
        return HSSFDateUtil.getJavaDate(value, isUsing1904);
    }


    private boolean isValidRow(LinkedList<String> columns) {

        for (String column : columns) {
            if (null != column) {
                return true;
            }
        }

        return false;
    }
}
