package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class TextSizeValidator implements Validator {
    private int limit = 10;

    public TextSizeValidator(int limit) {
        this.limit = limit;
    }

    public void validate(DataImportConfiguration configuration,
                         Column column,
                         Integer rowIndex,
                         String value) throws ValidationException {
        if (null != value && !"".equals(value.trim())) {
            if (value.length() > limit) {
                throw new ValidationException(column.getPosition(), rowIndex, column, value, this);
            }
        }
    }

    public int getLimit() {
        return limit;
    }
}
