package com.piramide.elwis.cmd.salesmanager.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: PeriodicPaymentException.java 01-dic-2008 11:26:08 $
 */
public class PeriodicPaymentException extends Exception {
    private List<String> errorMessageKeys;

    public PeriodicPaymentException() {
        super();
    }

    public PeriodicPaymentException(String message) {
        super(message);
    }

    public PeriodicPaymentException(Throwable cause) {
        super(cause);
    }

    public PeriodicPaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public void addErrorMessageKey(String messageKey) {
        if (errorMessageKeys == null) {
            errorMessageKeys = new ArrayList<String>();
        }
        errorMessageKeys.add(messageKey);
    }

    public List<String> getErrorMessageKeys() {
        return errorMessageKeys;
    }

    public void setErrorMessageKeys(List<String> errorMessageKeys) {
        this.errorMessageKeys = errorMessageKeys;
    }

    public boolean hasErrorMessages() {
        return (errorMessageKeys != null && !errorMessageKeys.isEmpty());
    }
}
