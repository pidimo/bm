package com.piramide.elwis.web.bmapp.util;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.5
 */
public class RESTConstants {

    public static enum MailListIconType {
        UNKNOWN(1),
        KNOWN_WITHOUT_PICTURE(2),
        KNOWN_WITH_PICTURE(3),
        RECIPIENTS_UNKNOWN(4),
        RECIPIENTS_AT_LEAST_ONE_KNOWN(5),
        RECIPIENTS_KNOWN(6);

        private int constant;

        MailListIconType(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return this.constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }
    }

}
