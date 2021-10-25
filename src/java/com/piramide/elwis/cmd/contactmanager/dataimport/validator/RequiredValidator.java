package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class RequiredValidator implements Validator {
    public void validate(DataImportConfiguration configuration,
                         Column column,
                         Integer rowIndex,
                         String value) throws ValidationException {
        if (null == value || "".equals(value.trim())) {
            throw new ValidationException(column.getPosition(), rowIndex, column, value, this);
        }
    }
}
