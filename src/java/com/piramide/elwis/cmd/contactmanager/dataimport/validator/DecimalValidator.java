package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class DecimalValidator implements Validator {
    private Log log = LogFactory.getLog(DecimalValidator.class);

    public void validate(DataImportConfiguration configuration,
                         Column column,
                         Integer rowIndex,
                         String value) throws ValidationException {

        if (null != value && !"".equals(value.trim())) {
            Number number = formatDecimal(value, configuration.getDecimalPattern());
            if (null == number) {
                throw new ValidationException(column.getPosition(), rowIndex, column, value, this);
            }
        }
    }

    private Number formatDecimal(String value, String decimalPattern) {
        NumberFormat formatter = new DecimalFormat(decimalPattern);
        try {
            return formatter.parse(value);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }
}
