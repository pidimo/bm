package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class IntegerConverter implements Converter {
    public Object parse(DataImportConfiguration configuration, String value) {
        Double number = new Double(value);
        return new Integer(number.intValue());
    }
}
