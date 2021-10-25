package com.piramide.elwis.cmd.contactmanager.dataimport.filemanager;

import com.piramide.elwis.cmd.contactmanager.dataimport.validator.ValidationException;
import com.piramide.elwis.utils.ArrayByteWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class FileManager {
    private static String IMPORT_DATA = "contacts/import";

    private Log log = LogFactory.getLog(this.getClass());

    private int numberOfRows = 0;
    private int row_i = 0;

    private DataReader dataReader;

    private String cacheFolderPath;
    private String filePath;

    public FileManager(ArrayByteWrapper fileData,
                       String fileName,
                       String cachePath,
                       Integer companyId,
                       Integer userId) throws ValidationException {
        saveFileInCacheFolder(fileData, fileName, cachePath, companyId, userId);
    }

    public String getFilePath() {
        return filePath;
    }

    private void saveFileInCacheFolder(ArrayByteWrapper fileData,
                                       String fileName,
                                       String cachePath,
                                       Integer companyId,
                                       Integer userId) throws ValidationException {
        String userCacheFolder = createCacheFolder(cachePath, companyId, userId);
        filePath = userCacheFolder + "/" + fileName;

        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileData.getFileData());
            outputStream.close();
            log.debug("-> Save File " + fileName + " in cache folder OK");

            dataReader = ReaderFactory.getDataReader(filePath);
            numberOfRows = dataReader.getNumberOfRows();

        } catch (FileNotFoundException e) {
            log.debug("-> Create " + fileName + " in " + userCacheFolder + " FAIL", e);
        } catch (IOException e) {
            log.debug("-> Save " + fileName + " in " + userCacheFolder + " FAIL", e);
        }
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public DataRow getNextRow() {
        DataRow dataRow = dataReader.getRow(row_i);
        if (row_i < numberOfRows) {
            row_i++;
        }
        return dataRow;
    }

    public boolean hasMoreRows() {
        return row_i < numberOfRows;
    }


    public FileManager() {

    }


    private String createCacheFolder(String cachePath, Integer companyId, Integer userId) {
        cacheFolderPath = cachePath + "/" + IMPORT_DATA + "/" + companyId + "/" + userId;
        File cacheFolder = new File(cacheFolderPath);
        cacheFolder.mkdirs();
        log.debug("-> Create cache folder " + cacheFolderPath + " OK");
        return cacheFolderPath;
    }

    public void deleteFileFromCache() {
        File file = new File(filePath);
        if (file.delete()) {
            log.debug("-> Delete File " + filePath + " OK");
        }

        File directory = new File(cacheFolderPath);
        if (directory.delete()) {
            log.debug("-> Delete directory " + cacheFolderPath + " OK");
        }

    }
}
