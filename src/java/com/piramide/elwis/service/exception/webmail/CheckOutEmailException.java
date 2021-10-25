package com.piramide.elwis.service.exception.webmail;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CheckOutEmailException extends Exception {
    public CheckOutEmailException() {
    }

    public CheckOutEmailException(Throwable cause) {
        super(cause);
    }

    public CheckOutEmailException(String message) {
        super(message);
    }

    public CheckOutEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
