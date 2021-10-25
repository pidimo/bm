package com.piramide.elwis.web.financemanager.util;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 */
public class PdfDocumentProtectedException extends Exception {
    public PdfDocumentProtectedException() {
        super();
    }

    public PdfDocumentProtectedException(String message) {
        super(message);
    }

    public PdfDocumentProtectedException(Throwable cause) {
        super(cause);
    }

    public PdfDocumentProtectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
