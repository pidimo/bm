package com.piramide.elwis.cmd.contactmanager.dataimport.filemanager;

import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class XLSReader implements DataReader {
    private int numberOfRows = 0;
    private String filePath;

    public XLSReader(String filePath) {
        this.filePath = filePath;

        try {
            FileInputStream inputStream = new FileInputStream(this.filePath);
            POIFSFileSystem poifs = new POIFSFileSystem(inputStream);
            InputStream din = poifs.createDocumentInputStream("Workbook");
            HSSFRequest req = new HSSFRequest();
            POIHSSRowListener rowListener = new POIHSSRowListener();
            req.addListenerForAllRecords(rowListener);
            HSSFEventFactory factory = new HSSFEventFactory();
            factory.processEvents(req, din);
            numberOfRows = rowListener.getRowCounter();

            inputStream.close();
            din.close();
        } catch (IOException e) {
            //POIFSFileSystem(inputStream) exception
            //poifs.createDocumentInputStream("Workbook") exception
            //factory.processEvents(req, din) exception
            e.printStackTrace(); //WTF is this?
        }
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public DataRow getRow(int i) {
        LinkedList<String> result = new LinkedList<String>();
        try {
            FileInputStream inputStream = new FileInputStream(this.filePath);
            POIFSFileSystem poifs = new POIFSFileSystem(inputStream);
            InputStream din = poifs.createDocumentInputStream("Workbook");
            HSSFRequest req = new HSSFRequest();
            POIHSSFListener rowListener = new POIHSSFListener(i);
            req.addListenerForAllRecords(rowListener);
            HSSFEventFactory factory = new HSSFEventFactory();
            factory.processEvents(req, din);
            result = rowListener.getColumns();

            inputStream.close();
            din.close();
        } catch (IOException e) {
            //POIFSFileSystem(inputStream) exception
            //poifs.createDocumentInputStream("Workbook") exception
            //factory.processEvents(req, din) exception
            e.printStackTrace();
        }

        DataRow dataRow = new DataRow(result);
        dataRow.setRowNumber(i);
        return dataRow;
    }
}

class POIHSSRowListener implements HSSFListener {
    private int rowCounter = 0;

    public void processRecord(Record record) {
        if (RowRecord.sid == record.getSid()) {
            rowCounter++;
        }
    }

    public int getRowCounter() {
        return rowCounter;
    }
}

class POIHSSFListener implements HSSFListener {
    private SSTRecord sstrec;
    private int row = 0;

    POIHSSFListener(int row) {
        this.row = row;
    }

    LinkedList<String> columns = new LinkedList<String>();

    public void processRecord(Record record) {

        if (SSTRecord.sid == record.getSid()) {
            sstrec = (SSTRecord) record;
        }

        if (NumberRecord.sid == record.getSid()) {
            NumberRecord numberRecord = (NumberRecord) record;
            if (numberRecord.getRow() == row) {
                if (columns.size() - 1 == numberRecord.getColumn()) {
                    String value = String.valueOf(Math.round(numberRecord.getValue()));
                    columns.add(numberRecord.getColumn(), value);
                } else {
                    int range = numberRecord.getColumn() - columns.size();
                    int actualSize = columns.size();
                    for (int i = 0; i < range; i++) {
                        columns.add(actualSize + i, null);
                    }
                    String value = String.valueOf(Math.round(numberRecord.getValue()));
                    columns.add(numberRecord.getColumn(), value);
                }

            }
        }

        if (LabelSSTRecord.sid == record.getSid()) {
            LabelSSTRecord labelSSTRecord = (LabelSSTRecord) record;
            if (labelSSTRecord.getRow() == row) {
                String unicodeString = sstrec.getString(labelSSTRecord.getSSTIndex()).getString();
                String stringValue = null;
                if (null != unicodeString) {
                    stringValue = unicodeString;
                }

                if (columns.size() - 1 == labelSSTRecord.getColumn()) {
                    columns.add(labelSSTRecord.getColumn(), stringValue);
                } else {
                    int range = labelSSTRecord.getColumn() - columns.size();
                    int actualSize = columns.size();
                    for (int i = 0; i < range; i++) {
                        columns.add(actualSize + i, null);
                    }
                    columns.add(labelSSTRecord.getColumn(), stringValue);
                }
            }
        }
    }

    public LinkedList<String> getColumns() {
        return columns;
    }
}
