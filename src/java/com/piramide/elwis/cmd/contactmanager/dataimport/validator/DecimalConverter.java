package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class DecimalConverter implements Converter {
    public Object parse(DataImportConfiguration configuration, String value) {
        Number number = formatDecimal(value, configuration.getDecimalPattern());

        return new BigDecimal(number.doubleValue());
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
