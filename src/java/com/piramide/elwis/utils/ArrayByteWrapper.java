package com.piramide.elwis.utils;

/**
 * Represents am Array byte wrapper.
 *
 * @author Yumi
 * @version $Id: ArrayByteWrapper.java 8320 2008-06-14 20:01:15Z ivan $
 */
public class ArrayByteWrapper {

    byte[] fileData;

    String fileName = "";

    public ArrayByteWrapper() {
    }

    public ArrayByteWrapper(byte[] fileData) {
        this.fileData = fileData;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
