package com.piramide.elwis.web.reports.util;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class ReportExecuteException extends Exception {
    public ReportExecuteException() {
        super();
    }

    public ReportExecuteException(Throwable cause) {
        super(cause);
    }

    public ReportExecuteException(String message) {
        super(message);
    }

    public ReportExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
