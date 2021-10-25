package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class DateValidator implements Validator {
    public void validate(DataImportConfiguration configuration,
                         Column column,
                         Integer rowIndex,
                         String value) throws ValidationException {
        if (null != value && !"".equals(value.trim())) {
            Date formattedDate = formatDate(value, configuration.getDatePattern(), true);
            if (formattedDate == null) {
                throw new ValidationException(column.getPosition(), rowIndex, column, value, this);
            }
        }
    }

    private Date formatDate(String value, String datePattern, boolean strict) {
        Date date = null;
        if (value != null && datePattern != null && datePattern.length() > 0) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
                formatter.setLenient(false);
                date = formatter.parse(value);
                if (strict && datePattern.length() != value.length()) {
                    date = null;
                }
            }
            catch (ParseException e) {
                //
            }
        }
        return date;
    }
}
