package com.piramide.elwis.web.common.urlencrypt;

/**
 * Exception thrown in the encryption or decryption process.
 *
 * @author Fernando Montaño
 * @version $Id: UrlCipherException.java 9124 2009-04-17 00:35:24Z fernando $
 */

public class UrlCipherException extends RuntimeException {
    public UrlCipherException() {
    }

    public UrlCipherException(String message) {
        super(message);
    }

    public UrlCipherException(String message, Throwable cause) {
        super(message, cause);
    }
}
