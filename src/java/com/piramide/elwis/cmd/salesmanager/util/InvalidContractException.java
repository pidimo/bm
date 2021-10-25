package com.piramide.elwis.cmd.salesmanager.util;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: InvalidContractException.java 24-nov-2008 16:16:58 $
 */
public class InvalidContractException extends Exception {
    public InvalidContractException() {
        super();
    }

    public InvalidContractException(String message) {
        super(message);
    }

    public InvalidContractException(Throwable cause) {
        super(cause);
    }

    public InvalidContractException(String message, Throwable cause) {
        super(message, cause);
    }
}
