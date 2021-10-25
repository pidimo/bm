package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;
import com.piramide.elwis.utils.DateUtils;

import java.util.Date;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class DateConverter implements Converter {
    public Object parse(DataImportConfiguration configuration, String value) {
        Date date = DateUtils.formatDate(value, configuration.getDatePattern());
        return DateUtils.dateToInteger(date);
    }
}
