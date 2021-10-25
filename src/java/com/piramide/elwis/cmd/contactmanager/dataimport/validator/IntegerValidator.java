package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class IntegerValidator implements Validator {
    private Log log = LogFactory.getLog(IntegerValidator.class);

    public void validate(DataImportConfiguration configuration,
                         Column column,
                         Integer rowIndex,
                         String value) throws ValidationException {
        if (null != value && !"".equals(value.trim())) {
            try {
                Double doubleNumber = new Double(value);

                if (doubleNumber % 2 != 0 && doubleNumber % 2 != 1) {
                    throw new ValidationException(column.getPosition(), rowIndex, column, value, this);
                }

            } catch (Exception e) {
                e.getStackTrace();
                throw new ValidationException(column.getPosition(), rowIndex, column, value, this);
            }
        }
    }
}
