package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.4.2
 */
public class ImportErrorsException extends Exception {
    private List<ValidationException> validationExceptionList = new ArrayList<ValidationException>();

    public ImportErrorsException(List<ValidationException> validationExceptionList) {
        super();
        this.validationExceptionList = validationExceptionList;
    }
    public ImportErrorsException() {
        super();
    }

    public ImportErrorsException(String message) {
        super(message);
    }

    public ImportErrorsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportErrorsException(Throwable cause) {
        super(cause);
    }

    public void addValidationException(ValidationException e) {
        validationExceptionList.add(e);
    }

    public List<ValidationException> getValidationExceptionList() {
        return validationExceptionList;
    }
}
