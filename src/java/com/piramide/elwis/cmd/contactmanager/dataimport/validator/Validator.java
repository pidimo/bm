package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;

/**
 * Interface present a method that allow validate the value associated to <code>Column</code> object.
 *
 * @author Ivan Alban
 * @version 4.2.1
 */
public interface Validator {
    /**
     * Validate the <code>value</code> associated to <code>Column</code> object.
     *
     * @param configuration <code>DataImportConfiguration</code> object that contain helpful information
     *                      to make the validation
     * @param column        <code>Column</code> associated to <code>value</code>.
     * @param rowIndex      Row number
     * @param value         <code>String</code> object to validate
     * @throws ValidationException If the method cannot validate the <code>String</code> value
     */
    public void validate(DataImportConfiguration configuration,
                         Column column,
                         Integer rowIndex,
                         String value) throws ValidationException;
}
