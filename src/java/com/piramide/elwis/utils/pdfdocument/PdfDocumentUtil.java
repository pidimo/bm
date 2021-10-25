package com.piramide.elwis.utils.pdfdocument;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.xml.xmp.XmpWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Jatun S.R.L.
 * pdf document util
 *
 * @author Miky
 * @version $Id: PdfDocumentUtil.java 23-ene-2009 15:35:43 $
 */
public class PdfDocumentUtil {
    private static Log log = LogFactory.getLog(PdfDocumentUtil.class);

    /**
     * Add title metada in pdf document
     *
     * @param pdfDocument pdf document
     * @param title       title
     * @return byte[]
     */
    public static byte[] addMetaData(byte[] pdfDocument, String title) throws PdfMetadataDocumentException {
        log.debug("Add title metada in pdf document...." + title);

        byte[] resultDocument = null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        try {
            // we create a PdfReader object
            PdfReader reader = new PdfReader(pdfDocument);
            PdfStamper stamper = new PdfStamper(reader, arrayOutputStream);

            HashMap info = reader.getInfo();
            info.put("Title", title);
            //info.put("Subject", "Hello World");
            //info.put("Author", "Bruno Lowagie");
            stamper.setMoreInfo(info);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XmpWriter xmp = new XmpWriter(baos, info);
            xmp.close();
            stamper.setXmpMetadata(baos.toByteArray());
            stamper.close();

            resultDocument = arrayOutputStream.toByteArray();
            arrayOutputStream.close();
        } catch (IOException e) {
            throw new PdfMetadataDocumentException("Error in pdf document, IO exception...", e);
        } catch (DocumentException e) {
            throw new PdfMetadataDocumentException("Error in Pdf document...", e);
        }

        return resultDocument;
    }
}
