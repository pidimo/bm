package com.piramide.elwis.web.financemanager.util;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.*;
import com.piramide.elwis.cmd.campaignmanager.DownloadDocumentCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 * Util to merge pdf documents
 *
 * @author Miky
 * @version $Id: PdfDocumentMergeUtil.java 12-dic-2008 15:05:39 $
 */
public class PdfDocumentMergeUtil {
    private Log log = LogFactory.getLog(this.getClass());

    public PdfDocumentMergeUtil() {
    }

    /**
     * Merge pdf documents
     *
     * @param freeTextIdList freeTextIds of pdf documents
     * @return byte[]
     * @throws PdfDocumentMergeException e
     */
    public byte[] mergeDocument(List freeTextIdList) throws PdfDocumentMergeException {
        log.debug("Merge pdf documents....." + freeTextIdList);
        byte[] resultDocument = null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        if (freeTextIdList.size() == 1) {
            ArrayByteWrapper wrapper = readDocument(new Integer(freeTextIdList.get(0).toString()));
            if (wrapper != null) {
                resultDocument = wrapper.getFileData();
            }
        } else {

            int pageOffset = 0;
            ArrayList master = new ArrayList();
            Document document = null;
            PdfCopy writer = null;

            Integer documentId = null;
            try {
                for (Iterator iterator = freeTextIdList.iterator(); iterator.hasNext();) {
                    documentId = new Integer(iterator.next().toString());
                    ArrayByteWrapper wrapper = readDocument(documentId);

                    if (wrapper != null) {
                        // we create a reader for a certain document
                        PdfReader reader = new PdfReader(wrapper.getFileData());
                        reader.consolidateNamedDestinations();
                        // we retrieve the total number of pages
                        int n = reader.getNumberOfPages();
                        List bookmarks = SimpleBookmark.getBookmark(reader);
                        if (bookmarks != null) {
                            if (pageOffset != 0) {
                                SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                            }
                            master.addAll(bookmarks);
                        }
                        pageOffset += n;

                        if (document == null) {
                            // step 1: creation of a document-object
                            document = new Document(reader.getPageSizeWithRotation(1));
                            // step 2: we create a writer that listens to the document
                            writer = new PdfCopy(document, arrayOutputStream);
                            // step 3: we open the document
                            document.open();
                        }
                        // step 4: we add content
                        PdfImportedPage page;
                        for (int i = 0; i < n;) {
                            ++i;
                            page = writer.getImportedPage(reader, i);
                            writer.addPage(page);
                        }
                        PRAcroForm form = reader.getAcroForm();
                        if (form != null) {
                            writer.copyAcroForm(reader);
                        }
                    }
                }

                if (!master.isEmpty()) {
                    writer.setOutlines(master);
                }
                // step 5: we close the document
                document.close();

                resultDocument = arrayOutputStream.toByteArray();
                arrayOutputStream.close();
            } catch (Exception e) {
                log.error("Error in merge pdf document... when process freeTextId:" + documentId, e);
                throw new PdfDocumentMergeException("Error in merge pdf document... when process freeTextId:" + documentId, e);
            }
        }

        if (resultDocument == null) {
            throw new PdfDocumentMergeException("Error in merge pdf ... has not documents");
        }
        return resultDocument;
    }

    /**
     * Read freetext document
     *
     * @param freeTextId
     * @return ArrayByteWrapper
     */
    private ArrayByteWrapper readDocument(Integer freeTextId) {
        ArrayByteWrapper textByteWrapper = null;

        DownloadDocumentCmd downloadCmd = new DownloadDocumentCmd();
        downloadCmd.putParam("freeTextId", freeTextId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(downloadCmd, null);
            if (!resultDTO.isFailure()) {
                textByteWrapper = (ArrayByteWrapper) resultDTO.get("freeText");
            }
        } catch (AppLevelException e) {
            log.debug("Error in read document...", e);
        }

        return textByteWrapper;
    }

    /**
     * verify if rfeetext pdf document is protected
     * @param freeTextId
     * @return
     * @throws PdfDocumentProtectedException
     */
    public boolean isProtectedPDFDocument(Integer freeTextId) throws PdfDocumentProtectedException {
        boolean isProtected = false;

        ArrayByteWrapper wrapper = readDocument(freeTextId);

        if (wrapper == null) {
            throw new PdfDocumentProtectedException();
        }

        PdfReader reader = null;
        try {
            reader = new PdfReader(wrapper.getFileData());
            isProtected = reader.isEncrypted();

        } catch (NoClassDefFoundError e) {
            throw new PdfDocumentProtectedException();
        } catch (Exception e) {
            throw new PdfDocumentProtectedException();
        }
        return isProtected;
    }

}
