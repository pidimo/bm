package com.piramide.elwis.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: WebMailConstants.java 12562 2016-07-20 23:43:31Z miguel $
 */
public class WebMailConstants {

    public static final String JNDI_ATTACH = "Elwis.Attach";
    public static final String JNDI_BODY = "Elwis.Body";
    public static final String JNDI_CONDITION = "Elwis.Condition";
    public static final String JNDI_FILTER = "Elwis.Filter";
    public static final String JNDI_FOLDER = "Elwis.Folder";
    public static final String JNDI_MAIL = "Elwis.Mail";
    public static final String JNDI_SIGNATURE = "Elwis.Signature";
    public static final String JNDI_USERMAIL = "Elwis.UserMail";
    public static final String JNDI_MAILGROUPADDR = "Elwis.MailGroupAddr";
    public static final String JNDI_ADDRESSGROUP = "Elwis.AddressGroup";
    public static final String JNDI_MAILRECIPIENT = "Elwis.MailRecipient";
    public static final String JNDI_SENDSIMPLEMAILMDB = "queue/Elwis.SendSimpleMail";
    public static final String JNDI_MAILSERVICE = "Elwis.MailService";
    public static final String JNDI_MAILCONTACT = "Elwis.MailContact";
    public static final String JNDI_MAILACCOUNT = "Elwis.MailAccount";
    public static final String JNDI_EMAILRECIPIENTADDRESS = "Elwis.EmailRecipientAddress";
    public static final String JNDI_EMAILACCOUNTERROR = "Elwis.EmailAccountError";
    public static final String JNDI_EMAILSOURCE = "Elwis.EmailSource";
    public static final String JNDI_IMAGESTORE = "Elwis.ImageStore";
    public static final String JNDI_UIDLTRACK = "Elwis.UidlTrack";
    public static final String JNDI_SIGNATURE_IMAGE = "Elwis.SignatureImage";
    public static final String JNDI_EMAILACCOUNTERRORDETAIL = "Elwis.EmailAccountErrorDetail";


    public static final String WEBMAIL_SERVICE_RESOURCES = "com.piramide.elwis.service.webmail.WebmailServiceResources";
    public static final String JNDI_EMAIL_SERVICE = "Elwis.EmailService";
    public static final String JNDI_SAVE_EMAIL_SERVICE = "Elwis.SaveEmailService";
    public static final String JNDI_DOWNLOAD_EMAIL_SERVICE = "Elwis.DownloadEmailService";
    public static final String JNDI_SENT_EMAIL_SERVICE = "Elwis.SentEmailService";
    public static final String JNDI_UTIL_SERVICE = "Elwis.UtilService";
    public static final String JNDI_EMAIL_SOURCE_SERVICE = "Elwis.EmailSourceService";

    public static final String JNDI_QUEUE_SEARCH_EMAIL_USER_IN_BACKGROUND = "queue/Elwis.SentEmailsInBackground";
    public static final String JNDI_QUEUE_DOWNLOAD_EMAILS_USER_IN_BACKGROUND = "queue/Elwis.DownloadEmailsInBackground";
    public static final String JNDI_QUEUE_SENT_EMAIL = "queue/Elwis.SentEmail";
    public static final String JNDI_QUEUE_DOWNLOAD_EMAIL = "queue/Elwis.DownloadEmail";
    public static final String JNDI_QUEUE_DELETE_EMAIL = "queue/Elwis.DeleteEmail";

    public static final String TABLE_ATTACH = "attach";
    public static final String TABLE_BODY = "body";
    public static final String TABLE_CONDITION = "condition";
    public static final String TABLE_FILTER = "filter";
    public static final String TABLE_FOLDER = "folder";
    public static final String TABLE_MAIL = "mail";
    public static final String TABLE_SIGNATURE = "signature";
    public static final String TABLE_USERMAIL = "usermail";
    public static final String TABLE_MAILGROUPADDR = "mailgroupaddr";
    public static final String TABLE_ADDRESSGROUP = "addressgroup";
    public static final String TABLE_MAILRECIPIENT = "mailrecipient";
    public static final String TABLE_MAILCONTACT = "mailcontact";
    public static final String TABLE_MAILACCOUNT = "mailaccount";
    public static final String TABLE_RECIPIENTRADDRESS = "recipaddress";
    public static final String TABLE_EMAILACCOUNTERROR = "mailaccerror";
    public static final String TABLE_EMAILSOURCE = "emailsource";
    public static final String TABLE_IMAGESTORE = "imagestore";
    public static final String TABLE_UIDLTRACK = "mailuidltrack";
    public static final String TABLE_SIGNATURE_IMAGE = "signatureimg";
    public static final String TABLE_EMAILACCOUNTERRORDETAIL = "mailaccerrdetail";

    //Constants for the folders
    public static final String SYSTEM_FOLDER_NAME = "System Folder";


    public static final String FOLDER_DEFAULT = "0";
    public static final String FOLDER_INBOX = "1";
    public static final String FOLDER_SENDITEMS = "2";
    public static final String FOLDER_DRAFTITEMS = "3";
    public static final String FOLDER_TRASH = "4";
    public static final String FOLDER_OUTBOX = "5";

    public static final String[] SYSTEM_FOLDER_KEYS = {FOLDER_INBOX, FOLDER_SENDITEMS,
            FOLDER_DRAFTITEMS, FOLDER_TRASH, FOLDER_OUTBOX};
    /**
     * @deprecated can be used UserMailDTO.KEY_USERMAILID;
     */
    public static final String MAIL_USER_KEY = "userMailId";

    public static final String BODY_TYPE_HTML = "1";
    public static final String BODY_TYPE_TEXT = "2";

    public static final String TO_TYPE_DEFAULT = "1";
    public static final String TO_TYPE_CC = "2";
    public static final String TO_TYPE_BCC = "3";

/*
    public static final String MAIL_STATE_UNREAD = "0";
    public static final String MAIL_STATE_READ = "1";
    public static final String MAIL_STATE_UNANSWERED = "2";
    public static final String MAIL_STATE_ANSWERED = "3";
    public static final String MAIL_STATE_SEND = "4";
    public static final String MAIL_STATE_FORWARD = "5";
*/

    public static final String MAIL_PRIORITY_DEFAULT = "0";
    public static final String MAIL_PRIORITY_HIGHT = "1";

    public static final int IN_VALUE = 1;
    public static final int OUT_VALUE = 0;
    /*public static final char MAIL_INCOMING = '0';
    public static final char MAIL_OUTGOING = '1';*/

    public static final String MAIL_FILTER_ALL = "0";
    public static final String MAIL_FILTER_UNREAD = "1";
    public static final String MAIL_FILTER_HIGHPRIORITY = "2";
    public static final String MAIL_FILTER_NOTANSWERED = "3";
    public static final String MAIL_FILTER_ANSWERED = "4";

    public static final String[] MAIL_FILTERS = {MAIL_FILTER_ALL,
            MAIL_FILTER_UNREAD,
            MAIL_FILTER_HIGHPRIORITY,
            MAIL_FILTER_NOTANSWERED,
            MAIL_FILTER_ANSWERED};

    public static final String MAIL_ADVANCEDFILTER_UNREAD = "0";
    public static final String MAIL_ADVANCEDFILTER_READ = "1";
    public static final String MAIL_ADVANCEDFILTER_UNANSWERED = "2";
    public static final String MAIL_ADVANCEDFILTER_ANSWERED = "3";
    public static final String MAIL_ADVANCEDFILTER_FORWARD = "4";
    public static final String MAIL_ADVANCEDFILTER_UNFORWARD = "5";
    public static final String MAIL_ADVANCEDFILTER_WITHATTACH = "6";
    public static final String MAIL_ADVANCEDFILTER_WITHOUTATTACH = "7";
    public static final String MAIL_ADVANCEDFILTER_WITHCOMMUNICATION = "8";
    public static final String MAIL_ADVANCEDFILTER_WITHOUTCOMMUNICATION = "9";

    public static final String DASHBOARD_MAIL_FILTER_UNREAD = "0";
    public static final String DASHBOARD_MAIL_FILTER_ALL = "1";


    //constants for list of conditions
    /**
     * @deprecated remplaced by getConditionsKeys() in Functions.java
     */
    public static final String[] CONDITIONS = {"Webmail.condition.condition1",
            "Webmail.condition.condition2",
            "Webmail.condition.condition3",
            "Webmail.condition.condition4"};

    public static final String CONDITION_CONTAIN = "1";
    public static final String CONDITION_NOT_CONTAIN = "2";
    public static final String CONDITION_BEGIN_WITH = "3";
    public static final String CONDITION_TERMINATED_IN = "4";

    /**
     * @deprecated remplaced by getConditionsKeys() in Functions.java
     */
    public static final String[] VALUE_CONDITIONS = {CONDITION_CONTAIN,
            CONDITION_NOT_CONTAIN,
            CONDITION_BEGIN_WITH,
            CONDITION_TERMINATED_IN};

    /**
     * @deprecated remplaced by getConditionNameKeys() in Functions.java
     */
    public static final String[] NAME_KEYS = {"Webmail.condition.conditionNameKey1",
            "Webmail.condition.conditionNameKey2",
            "Webmail.condition.conditionNameKey3"};
    public static final String FROM_PART = "1";
    public static final String TO_o_CC_PART = "2";
    public static final String SUBJECT_PART = "3";

    /**
     * @deprecated remplaced by getConditionNameKeys() in Functions.java
     */
    public static final String[] VALUE_NAME_KEYS = {FROM_PART,
            TO_o_CC_PART,
            SUBJECT_PART};
    public static final String SEARCH_ALL_MESSAGE = "0";
    public static final String SEARCH_ONLY_FROM = "1";
    public static final String SEARCH_ONLY_SUBJECT = "2";
    public static final String SEARCH_ONLY_TO = "3";
    public static final String SEARCH_ONLY_CONTENT = "4";

    public static final String SEARCH_ALL_FOLDERS = "0";

    public static final String TEXT_EDITMODE = "1";
    public static final String HTML_EDITMODE = "2";

    public static final String SEND_TO_ALL_TELECOMS = "1";
    public static final String SEND_TO_A_TELECOM = "0";

    public static final String DATE_TIME_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

    //to blank key
    public static final String BLANK_KEY = "blank";
    public static final String UNKNOW = "unknown";


    public static enum MailAccountType {
        POP(1),
        IMAP(2);
        private Integer type;

        MailAccountType(Integer type) {
            this.type = type;
        }

        public Integer getType() {
            return this.type;
        }
    }

    public static enum MailOperations {
        COMPOSE("COMPOSE"),
        REPLY("REPLY");
        private String operation;

        MailOperations(String op) {
            this.operation = op;
        }

        public String getOperation() {
            return this.operation;
        }
    }

    public static final String downloadImageURL = "/webmail/downloadImage.do";
    public static final String attachIdKey = "attachId";
    public static final String fileNameKey = "fileName";
    public static final String personalKey = "personal";
    public static final String emailKey = "email";

    //string key to manage temporal image id
    public static final String TEMPORALIMAGESTOREID_KEY = "temporalImageStoreId";

    /**
     * Type to identify store image: temporal or related
     */
    public static enum ImageStoreType {
        TEMPORAL_IMAGE(1),
        RELATION_IMAGE(2);

        private int constant;

        ImageStoreType(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return this.constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }
    }

    /**
     * Font constants from TinyMCE html editor
     */
    public static final LinkedHashMap<String, String> EDITORFONT_MAP;

    static {
        EDITORFONT_MAP = new LinkedHashMap<String, String>();
        EDITORFONT_MAP.put("Andale Mono", "andale mono,times");
        EDITORFONT_MAP.put("Arial", "arial,helvetica,sans-serif");
        EDITORFONT_MAP.put("Arial Black", "arial black,avant garde");
        EDITORFONT_MAP.put("Book Antiqua", "book antiqua,palatino");
        EDITORFONT_MAP.put("Century Gothic", "century gothic,sans-serif");
        EDITORFONT_MAP.put("Comic Sans MS", "comic sans ms,sans-serif");
        EDITORFONT_MAP.put("Courier New", "courier new,courier");
        EDITORFONT_MAP.put("Georgia", "georgia,palatino");
        EDITORFONT_MAP.put("Helvetica", "helvetica");
        EDITORFONT_MAP.put("Impact", "impact,chicago");
        EDITORFONT_MAP.put("Segoe UI", "segoe ui,sans-serif");
        EDITORFONT_MAP.put("Segoe UI Light", "segoe ui light,sans-serif");
        EDITORFONT_MAP.put("Symbol", "symbol");
        EDITORFONT_MAP.put("Tahoma", "tahoma,arial,helvetica,sans-serif");
        EDITORFONT_MAP.put("Terminal", "terminal,monaco");
        EDITORFONT_MAP.put("Times New Roman", "times new roman,times");
        EDITORFONT_MAP.put("Trebuchet MS", "trebuchet ms,geneva");
        EDITORFONT_MAP.put("Verdana", "verdana,geneva");
        EDITORFONT_MAP.put("Webdings", "webdings");
        EDITORFONT_MAP.put("Wingdings", "wingdings,zapf dingbats");
    }

    /**
     * Font Size constants from TinyMCE html editor, this is defined in
     * initTinyMCEditor.tag how 'font_size_style_values' attribute
     */
    public static final LinkedHashMap<String, String> EDITORFONTSIZE_MAP;

    static {
        EDITORFONTSIZE_MAP = new LinkedHashMap<String, String>();
        EDITORFONTSIZE_MAP.put("1 (8 pt)", "8pt");
        EDITORFONTSIZE_MAP.put("2 (10 pt)", "10pt");
        EDITORFONTSIZE_MAP.put("3 (12 pt)", "12pt");
        EDITORFONTSIZE_MAP.put("4 (14 pt)", "14pt");
        EDITORFONTSIZE_MAP.put("5 (18 pt)", "18pt");
        EDITORFONTSIZE_MAP.put("6 (24 pt)", "24pt");
        EDITORFONTSIZE_MAP.put("7 (36 pt)", "36pt");
    }

    public static enum ColumnToShow {
        FROM(1),
        TO(2),
        FROM_TO(3);
        private int constant;

        ColumnToShow(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return this.constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }
    }

    public static final Map<String, ColumnToShow> SYSTEM_FOLDER_COLUMNTOSHOW_MAP;

    static {
        SYSTEM_FOLDER_COLUMNTOSHOW_MAP = new HashMap<String, ColumnToShow>();
        SYSTEM_FOLDER_COLUMNTOSHOW_MAP.put(FOLDER_INBOX, ColumnToShow.FROM);
        SYSTEM_FOLDER_COLUMNTOSHOW_MAP.put(FOLDER_SENDITEMS, ColumnToShow.TO);
        SYSTEM_FOLDER_COLUMNTOSHOW_MAP.put(FOLDER_DRAFTITEMS, ColumnToShow.TO);
        SYSTEM_FOLDER_COLUMNTOSHOW_MAP.put(FOLDER_TRASH, ColumnToShow.FROM);
        SYSTEM_FOLDER_COLUMNTOSHOW_MAP.put(FOLDER_OUTBOX, ColumnToShow.TO);
    }

    public static enum EmailAccountErrorType {
        SMTP_AUTHENTICATION(1),
        SMTP_PROVIDER(2),
        SMTP_SERVICE(3),
        SMTP_SENDFAILED(7),
        POP_AUTHENTICATION(4),
        POP_PROVIDER(5),
        POP_SERVICE(6);

        private int constant;

        EmailAccountErrorType(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return this.constant;
        }

        public String getConstantAsString() {
            return String.valueOf(this.constant);
        }
    }

    public static enum MarkAs {
        UNREAD("0"),
        READ("1");
        private String constant;

        MarkAs(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return this.constant;
        }

        public boolean equal(String constant) {
            return getConstant().equals(constant);
        }
    }

    public static enum TokenFieldType {
        TO("to"),
        CC("cc"),
        BCC("bcc");
        private String constant;

        TokenFieldType(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return this.constant;
        }

        public boolean equal(String constant) {
            return getConstant().equals(constant);
        }
    }

}