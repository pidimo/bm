package com.piramide.elwis.utils.deduplication.exception;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class MergeAddressException extends Exception {
    private boolean isUserMergeError = false;

    public MergeAddressException() {
        super();
    }

    public MergeAddressException(String message) {
        super(message);
    }

    public MergeAddressException(Throwable cause) {
        super(cause);
    }

    public MergeAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public boolean isUserMergeError() {
        return isUserMergeError;
    }

    public void setUserMergeError(boolean userMergeError) {
        isUserMergeError = userMergeError;
    }
}
