package com.piramide.elwis.cmd.contactmanager.dataimport.validator;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;

import java.util.regex.Matcher;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class EmailValidator implements Validator {
    private static final String ATOM = "[^\\x00-\\x1F^\\(^\\)^\\<^\\>^\\@^\\,^\\;^\\:^\\\\^\\\"^\\.^\\[^\\]^\\s]";
    private static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
    private static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";
    private final static java.util.regex.Pattern pattern;

    static {
        pattern = java.util.regex.Pattern.compile(
                "^" + ATOM + "+(\\." + ATOM + "+)*@"
                        + DOMAIN
                        + "|"
                        + IP_DOMAIN
                        + ")$",
                java.util.regex.Pattern.CASE_INSENSITIVE
        );
    }

    public void validate(DataImportConfiguration configuration,
                         Column column,
                         Integer rowIndex,
                         String value) throws ValidationException {
        if (null != value && !isValid(value)) { //it was not possible to call a Singleton object within a BMT EJB 2.1
            throw new ValidationException(column.getPosition(), rowIndex, column, value, this);
        }

    }

    private boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof String)) {
            return false;
        }
        String string = (String) value;
        if (string.length() == 0) {
            return true;
        }
        Matcher m = pattern.matcher(string);
        return m.matches();
    }

}
