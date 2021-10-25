package com.piramide.elwis.service.exception.dataimport;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class RequiredException extends DataImportException {
    public RequiredException() {
    }

    public RequiredException(String message) {
        super(message);
    }

    public RequiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredException(Throwable cause) {
        super(cause);
    }
}
