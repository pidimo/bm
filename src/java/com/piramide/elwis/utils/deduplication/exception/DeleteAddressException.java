package com.piramide.elwis.utils.deduplication.exception;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DeleteAddressException extends Exception {
    public DeleteAddressException() {
        super();
    }

    public DeleteAddressException(String message) {
        super(message);
    }

    public DeleteAddressException(Throwable cause) {
        super(cause);
    }

    public DeleteAddressException(String message, Throwable cause) {
        super(message, cause);
    }
}

