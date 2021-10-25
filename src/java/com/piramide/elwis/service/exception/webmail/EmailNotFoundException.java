package com.piramide.elwis.service.exception.webmail;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class EmailNotFoundException extends Exception {
    public EmailNotFoundException() {
        super();
    }

    public EmailNotFoundException(String message) {
        super(message);
    }

    public EmailNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotFoundException(Throwable cause) {
        super(cause);
    }
}
