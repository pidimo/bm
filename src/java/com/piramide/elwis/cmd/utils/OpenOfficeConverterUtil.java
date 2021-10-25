package com.piramide.elwis.cmd.utils;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.ConnectException;

/**
 * Jatun S.R.L.
 * Util to convert documents using open office service
 *
 * @author Miky
 * @version $Id: OpenOfficeConverterUtil.java 30-sep-2008 15:31:22 $
 */
public class OpenOfficeConverterUtil {
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Convert word 97 document especification to pdf
     *
     * @param inputStream  word document as inputStream
     * @param outputStream outputStream to save pdf converted document
     * @throws ConnectException         can't connect to open office service
     * @throws DocumentConvertException error in convert
     */
    public void wordToPdf(InputStream inputStream, OutputStream outputStream) throws ConnectException, DocumentConvertException {
        // connect to an OpenOffice.org instance running
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(ConfigurationFactory.getValue("elwis.openoffice.host"),
                Integer.parseInt(ConfigurationFactory.getValue("elwis.openoffice.port").trim()));

        connection.connect();

        // get document formats
        DefaultDocumentFormatRegistry defaultFormatRegistry = new DefaultDocumentFormatRegistry();
        DocumentFormat wordFormat = defaultFormatRegistry.getFormatByFileExtension("doc");
        DocumentFormat pdfFormat = defaultFormatRegistry.getFormatByFileExtension("pdf");

        // convert
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        try {
            converter.convert(inputStream, wordFormat, outputStream, pdfFormat);
        } catch (Exception e) {
            log.debug("Error in convert Document...", e);
            connection.disconnect();
            throw new DocumentConvertException(e);
        }

        // close the connection
        connection.disconnect();
    }

    /**
     * Convert word 97 document especification to pdf
     *
     * @param wordDocument word document as byte[]
     * @return byte[]
     * @throws ConnectException
     * @throws DocumentConvertException
     */
    public byte[] wordToPdf(byte[] wordDocument) throws ConnectException, DocumentConvertException {

        byte[] pdfDoc = new byte[0];
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(wordDocument);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        wordToPdf(arrayInputStream, arrayOutputStream);
        try {
            pdfDoc = arrayOutputStream.toByteArray();
            arrayInputStream.close();
            arrayOutputStream.close();
        } catch (IOException e) {
            log.debug("Error in close stream.." + e);
            throw new DocumentConvertException("Error in close stream..", e);
        }
        return pdfDoc;
    }

    /**
     * Convert input File to output File
     *
     * @param inputFile  File extension define input document format
     * @param outputFile File extension define output document format
     * @throws ConnectException
     * @throws DocumentConvertException
     */
    public void convert(File inputFile, File outputFile) throws ConnectException, DocumentConvertException {
        // connect to an OpenOffice.org instance running
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(ConfigurationFactory.getValue("elwis.openoffice.host"),
                Integer.parseInt(ConfigurationFactory.getValue("elwis.openoffice.port").trim()));

        connection.connect();

        // convert
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        try {
            converter.convert(inputFile, outputFile);
        } catch (Exception e) {
            log.debug("Error in convert document:", e);
            connection.disconnect();
            throw new DocumentConvertException(e);
        }

        // close the connection
        connection.disconnect();
    }
}
