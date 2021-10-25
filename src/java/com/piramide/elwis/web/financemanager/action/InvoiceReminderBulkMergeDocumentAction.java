package com.piramide.elwis.web.financemanager.action;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.*;
import com.piramide.elwis.cmd.campaignmanager.DownloadDocumentCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: InvoiceReminderBulkMergeDocumentAction.java 07-nov-2008 11:19:40 $
 */
public class InvoiceReminderBulkMergeDocumentAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing InvoiceReminderBulkMergeDocumentAction........" + request.getParameterMap());

        ActionErrors errors = new ActionErrors();

        String documentIds = request.getParameter("reminderDocIds");
        List documentIdList = new ArrayList();
        if (!GenericValidator.isBlankOrNull(documentIds)) {
            String[] printArray = documentIds.split(",");
            documentIdList = Arrays.asList(printArray);

            if (!documentIdList.isEmpty()) {
                byte[] document = mergeDocument(documentIdList, errors);
                if (errors.isEmpty()) {
                    setDocumentInResponse(document, response, request);
                }
            }
        } else {
            errors.add("error", new ActionError("InvoiceReminder.bulkCreate.mergeError"));
        }

        if (!errors.isEmpty()) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("FailMerge");
        }

        return null;
    }

    /**
     * Merge invoice reminder documents
     *
     * @param documentIdList document ids
     * @param errors         errors
     * @return byte[]
     */
    private byte[] mergeDocument(List documentIdList, ActionErrors errors) {
        log.debug("Merge reminder documents....." + documentIdList);
        byte[] resultDocument = null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        if (documentIdList.size() == 1) {
            ArrayByteWrapper wrapper = readDocument(documentIdList.get(0).toString());
            if (wrapper != null) {
                resultDocument = wrapper.getFileData();
            }
        } else {

            int pageOffset = 0;
            ArrayList master = new ArrayList();
            Document document = null;
            PdfCopy writer = null;

            String documentId = null;
            try {
                for (Iterator iterator = documentIdList.iterator(); iterator.hasNext();) {
                    documentId = (String) iterator.next();
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
                errors.add("merge", new ActionError("InvoiceReminder.bulkCreate.mergeError"));
                return null;
            }
        }

        if (resultDocument == null) {
            errors.add("merge", new ActionError("InvoiceReminder.bulkCreate.mergeError"));
        }
        return resultDocument;
    }

    /**
     * Read invoice reminder document
     *
     * @param documentId
     * @return ArrayByteWrapper
     */
    private ArrayByteWrapper readDocument(String documentId) {
        ArrayByteWrapper textByteWrapper = null;

        DownloadDocumentCmd downloadCmd = new DownloadDocumentCmd();
        downloadCmd.putParam("freeTextId", documentId);

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
     * set in response merged document
     *
     * @param mergeDocument
     * @param response
     * @throws Exception
     */
    private void setDocumentInResponse(byte[] mergeDocument, HttpServletResponse response, HttpServletRequest request) throws Exception {
        log.debug("Sending document..");
        String localizedFileName = JSPHelper.getMessage(request, "InvoiceReminders.file.name");
        String fileName = localizedFileName + ".pdf";
        int fileSize = mergeDocument.length;
        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

        ServletOutputStream os = response.getOutputStream();
        os.write(mergeDocument);
        os.flush();
        os.close();
    }

}
