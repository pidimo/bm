package com.piramide.elwis.utils;

/**
 * Contains general application constants
 *
 * @author Fernando Montaño
 * @version $Id: Constants.java 10775 2015-08-31 19:08:02Z milver $
 */
public abstract class Constants {

    /**
     * SESSION EJB's JNDI NAMES SECTION
     */
    public static final String JNDI_SEQUENCE = "Elwis.Sequence";
    public static final String JNDI_FILEFREETEXT = "Elwis.FileFreeText";
    public static final String JNDI_USERSESSION = "Elwis.UserSession";
    public static final String JNDI_USERSESSION_PARAM = "Elwis.UserSessionParam";
    public static final String USER_KEY = "user";
    public static final String COMPANYID = "companyId";
    public static final String USERID = "userId";
    public static final String USER_ADDRESSID = "userAddressId";
    public static final String USER_TYPE = "userType";
    public static final String LIST_COMPANYID = "listCompanyId";
    public static final String JNDI_ELWISDS = "java:/elwisDS";
    public static final String APPLICATION_RESOURCES = "com.piramide.elwis.web.resources.ApplicationResources";
    public static final String JNDI_SYSTEM_CONSTANT = "Elwis.SystemConstant";

    public static final String JNDI_MAILSESSION = "java:/Elwis.Mail";
    public static final String REMINDER_RESOURCES = "com.piramide.elwis.service.scheduler.ReminderResources";

    public static final String JNDI_ATTACHMENT = "Elwis.Attachment";
    public static final String TABLE_ATTACHMENT = "attachment";
    public static final String TABLE_FILEFREETEXT = "freetext";

    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";

    /**
     * Used to add to all urls (called with response.encodeURL) in the application. So, the urlEncrytFilter must be
     * enable in order to this work fine. The customRequestProcessor does use this.
     */
    public static final String REQUEST_COMPANY_ID = "REQUESTCOMPANYID";

    public static final String CHARSET_ENCODING = "UTF-8";

    /**
     * used to save popup messages in the session
     */
    public static final String POPUP_MESSAGE_KEY = "POPUPMESSAGEKEY";

    /**
     * attachment type constants
     */
    public static enum AttachmentType {
        CAMPGENERATION(1);

        private final int constant;

        AttachmentType(int val) {
            constant = val;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }
    }

    public static enum MessageContextType {
        ERROR(1),
        WARNING(2),
        INFO(3),
        SUCCESS(4);

        private final int constant;

        MessageContextType(int val) {
            constant = val;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }
    }

    public static enum UIMode {
        BOOTSTRAP("bootstrap");

        private final String constant;

        UIMode(String val) {
            constant = val;
        }

        public String getConstant() {
            return constant;
        }

    }
}
