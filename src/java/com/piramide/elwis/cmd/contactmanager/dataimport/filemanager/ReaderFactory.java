package com.piramide.elwis.cmd.contactmanager.dataimport.filemanager;

import com.piramide.elwis.cmd.contactmanager.dataimport.validator.ValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class ReaderFactory {
    private static Log log = LogFactory.getLog(ReaderFactory.class);

    public static DataReader getDataReader(String filePath) throws ValidationException {
        DataReader dataReader = null;

        int lastPoint = filePath.lastIndexOf('.');

        if (lastPoint == -1) {
            return dataReader;
        }

        String fileEx = filePath.substring(lastPoint + 1, filePath.length());
        if ("csv".equals(fileEx.toLowerCase())) {
            dataReader = new CSVReader(filePath);
        }
        if ("xls".equals(fileEx.toLowerCase())) {
            dataReader = new XLSMemoryReader(filePath);
        }

        log.debug("-> Build DataReader for " + fileEx + " File OK");
        return dataReader;
    }
}
