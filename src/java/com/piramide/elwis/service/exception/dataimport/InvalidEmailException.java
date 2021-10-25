package com.piramide.elwis.service.exception.dataimport;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class InvalidEmailException extends DataImportException {
    public InvalidEmailException() {
    }

    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEmailException(Throwable cause) {
        super(cause);
    }
}
