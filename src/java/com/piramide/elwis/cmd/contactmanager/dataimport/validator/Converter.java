package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public interface Converter {
    public Object parse(DataImportConfiguration configuration, String value);
}
