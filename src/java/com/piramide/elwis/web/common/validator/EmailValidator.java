package com.piramide.elwis.web.common.validator;

import java.util.regex.Matcher;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class EmailValidator {
    public static final EmailValidator i = new EmailValidator();

    //this is based in email validator of Hibernate
    private static final String ATOM = "[^\\x00-\\x1F^\\(^\\)^\\<^\\>^\\@^\\,^\\;^\\:^\\\\^\\\"^\\.^\\[^\\]^\\s]";
    private static final String ATOM_EXT = "[^\\x00-\\x1F^\\(^\\)^\\<^\\>^\\@^\\,^\\;^\\:^\\\\^\\\"^\\.^\\[^\\]^\\s^-]+";
    //private static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
    private static final String DOMAIN = "(" + ATOM_EXT + ATOM + "*(\\." + ATOM + "+)*";
    private static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

    private java.util.regex.Pattern pattern;

    private EmailValidator() {
        initialize();
    }

    public boolean isValid(Object value) {
        if (value == null) return true;
        if (!(value instanceof String)) return false;
        String string = (String) value;
        if (string.length() == 0) return true;
        Matcher m = pattern.matcher(string);
        return m.matches();
    }

    private void initialize() {
        pattern = java.util.regex.Pattern.compile(
                "^" + ATOM + "+(\\." + ATOM + "+)*@"
                        + DOMAIN
                        + "|"
                        + IP_DOMAIN
                        + ")$",
                java.util.regex.Pattern.CASE_INSENSITIVE
        );
    }
}
