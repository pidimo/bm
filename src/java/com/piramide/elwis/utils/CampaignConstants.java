package com.piramide.elwis.utils;

import com.piramide.elwis.utils.configuration.EARJndiProperty;

/**
 * @author Yumi
 * @version $Id: CampaignConstants.java 12701 2017-07-20 22:21:17Z miguel $
 */

public abstract class CampaignConstants {

    public static final String TABLE_CAMPAIGNCONTACT = "campcontact";
    public static final String TABLE_CAMPAIGNCRITERION = "campcriterion";
    public static final String TABLE_CAMPAIGN = "campaign";
    public static final String TABLE_EMPLOYEE = "employee";
    public static final String TABLE_ADDRESS = "address";
    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_CATEGORYVALUE = "categoryvalue";
    public static final String TABLE_CAMPAIGNFREETEXT = "freetext";
    public static final String TABLE_CAMPAIGNCRITERIONVALUE = "campcriterionvalue";
    public static final String TABLE_CAMPAIGNTEXT = "campaigntext";
    public static final String TABLE_CAMPAIGNACTIVITY = "campactivity";
    public static final String TABLE_ATTACH = "campattach";
    public static final String TABLE_CAMPAIGNACTIVITYUSER = "campactivityuser";
    public static final String TABLE_CAMPAIGN_TEMPLATE = "camptemplate";
    public static final String TABLE_CAMPAIGNACTIVITYCONTACT = "campactivcontact";
    public static final String TABLE_CAMPAIGNGENERATION = "campgeneration";
    public static final String TABLE_CAMPAIGNGENTEXT = "campgentext";
    public static final String TABLE_CAMPAIGNGENATTACH = "campgenattach";
    public static final String TABLE_CAMPAIGNTEXTIMG = "campaigntextimg";
    public static final String TABLE_CAMPAIGNGENTEXTIMG = "campgentextimg";
    public static final String TABLE_CAMPAIGNSENTLOG = "campsentlog";
    public static final String TABLE_SENTLOGCONTACT = "sentlogcontact";

    public static final String JNDI_CAMPAIGN = "Elwis.Campaign";
    public static final String JNDI_CAMPAIGNCONTACT = "Elwis.CampaignContact";
    public static final String JNDI_CAMPAIGNCRITERION = "Elwis.CampaignCriterion";
    public static final String JNDI_CATEGORYVALUE = "Elwis.CategoryValue";
    public static final String JNDI_CATEGORY = "Elwis.Category";
    public static final String JNDI_CAMPAIGN_FREETEXT = "Elwis.CampaignFreeText";
    public static final String JNDI_CAMPAIGN_TEXT = "Elwis.CampaignText";
    public static final String JNDI_CAMPAIGNCRITERIONVALUE = "Elwis.CampaignCriterionValue";
    public static final String JNDI_CAMPAIGNTEXT = "Elwis.CampaignText";
    public static final String JNDI_CAMPAIGN_ACTIVITY = "Elwis.CampaignActivity";
    public static final String JNDI_CAMPAIGN_ACTIVITY_USER = "Elwis.CampaignActivityUser";

    /**
     * Both below jndi are for an EJB3 deployed module (campaignservice) *
     */
    public static final String JNDI_DOCUMENT_GENERATESERVICE = EARJndiProperty.getEarName() + "DocumentGenerateService/localHome";
    public static final String JNDI_CAMPAIGNMAILERSERVICE = EARJndiProperty.getEarName() + "CampaignMailerServiceBean/local";

    public static final String JNDI_CAMPAIGNSENDBACKGROUND_SERVICE = EARJndiProperty.getEarName() + "CampaignSendBackgroundService/localHome";

    public static final String JNDI_ATTACH = "Elwis.CampaignAttach";
    public static final String JNDI_CAMPAIGN_TEMPLATE = "Elwis.CampaignTemplate";
    public static final String JNDI_CAMPAIGNACTIVITYCONTACT = "Elwis.CampaignActivityContact";
    public static final String JNDI_CAMPAIGNGENERATION = "Elwis.CampaignGeneration";
    public static final String JNDI_CAMPAIGNGENTEXT = "Elwis.CampaignGenText";
    public static final String JNDI_CAMPAIGNGENATTACH = "Elwis.CampaignGenAttach";
    public static final String JNDI_CAMPAIGNTEXTIMG = "Elwis.CampaignTextImg";
    public static final String JNDI_CAMPAIGNGENTEXTIMG = "Elwis.CampaignGenTextImg";
    public static final String JNDI_CAMPAIGNSENTLOG = "Elwis.CampaignSentLog";
    public static final String JNDI_SENTLOGCONTACT = "Elwis.SentLogContact";

    public static final String CAMPAIGN_SERVICE_RESOURCES = "com.piramide.elwis.service.campaign.CampaignServiceResources";

    public static final String APPLICATION_RESOURCES = "ApplicationResources";
    public static final String PREPARATION = "1";
    public static final String SENT = "2";
    public static final String CANCELED = "3";
    public static final String IN = "in";

    public static final String CUSTOMER_CATEGORY = "1";
    public static final String CONTACTPERSON_CATEGORY = "2";
    public static final String PRODUCT_CATEGORY = "3";
    public static final String ADDRESS_CATEGORY = "4";

    public static final String COUNTRYID = "countryId";
    public static final String RECORDDATE = "recordDate";
    public static final String ZIPCODE = "zip";

    public static final String FIELD_NUMBER = "1";
    public static final String FIELD_DECIMAL_NUMBER = "2";
    public static final String FIELD_DATE = "3";
    public static final String FIELD_TEXT = "4";
    public static final String FIELD_SIMPLE_SELECT = "5";
    public static final String FIELD_MULTIPLE_SELECT = "6";
    public static final String FIELD_RELATION_EXISTS = "7";

    public static final String COMPARATOR_BETWEEN = "BETWEEN";
    public static final String COMPARATOR_IN = "IN";
    public static final String COMPARATOR_NOTIN = "NOTIN";
    public static final String COMPARATOR_DISTINCT = "DISTINCT";
    public static final String COMPARATOR_NOT_IN = "NOT_IN";
    public static final String COMPARATOR_EQUAL = "EQUAL";

    public static enum CriteriaComparator {
        RELATION_EXISTS("RELATIONEXISTS", "CampaignCriteria.operator.relationExists"),
        NOT_CONTAIN("NOT_CONTAIN", "Report.filter.op.notContain");

        private final String constant;
        private final String resource;

        CriteriaComparator(String constant, String resource) {
            this.constant = constant;
            this.resource = resource;
        }

        public String getConstant() {
            return constant;
        }

        public String getResource() {
            return resource;
        }

        public boolean equal(String constant) {
            return getConstant().equals(constant);
        }
    }

    //add miky//
    public static final String CAMPAIGN_RECIPIENTS = "0";
    public static final String MANUAL_ASSIGN = "1";
    public static final String AUTOMATIC_ASSIGN = "2";
    public static final String ASSIGN_FROM_CUSTOMER = "1";
    public static final String ASSIGN_FROM_PERCENT = "2";

    public static final String CONFLICTASSIGN_ALL_CUSTOMERS = "1";
    public static final String CONFLICTASSIGN_JUST_WHAT_GETS = "2";

    public static final String KEY_SEPARATOR = "\\$-\\$";
    public static final String KEY_SEPARATOR_VALUE = "\\$V\\$";

    //responsible send type
    public static final String ACTIVITY_RESPONSIBLE = "1";
    public static final String CONTACT_RESPONSIBLE = "-1";
    public static final String CURRENT_USER_SEND = "3";

    //email sender employee
    public static final String DEFAULT_ADDRESS_OF_CONTACTRESPONSIBLE = "1";

    //recipients without email
    public static final String ADDRESS_WITHOUT_MAIL = "1";
    public static final String CONTACTPERSON_WITHOUT_MAIL = "2";

    //not valid user to create task
    public static final String CAMPAIGNRESPONSIBLE_IS_NOT_VALIDUSER = "0";

    //add end miky//
    public static final String CAMPAIGN_REPORT_LIST = "1";
    public static final String RECIPIENT_REPORT_LIST = "2";
    public static final String TRUEVALUE = "true";
    public static final String FALSEVALUE = "false";
    public static final String CEROVALUE = "0";
    public static final String EQUAL = "EQUAL";
    public static final String EMPTY = "";

    public static enum ActivityStatus {
        PLANNED(1),
        IN_PROGRESS(2),
        CONCLUDED(3);

        private final int constant;

        ActivityStatus(int val) {
            constant = val;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }
    }

    public static enum DocumentType {
        WORD(0),
        HTML(1);
        private final int constant;

        DocumentType(int v) {
            constant = v;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }
    }

    public static enum HasHasNotEmailCriteria {
        HASNOTEMAIL(0, "Campaign.criteria.hasNotEmail"),
        HASEMAIL(1, "Campaign.criteria.hasEmail"),
        EMPTY(-1, "");
        private final int constant;
        private final String resource;

        HasHasNotEmailCriteria(int value, String resource) {
            this.constant = value;
            this.resource = resource;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }

        public String getResource() {
            return resource;
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }
    }

    public static enum SenderPrefixType {
        NOPREFIX(1, "SenderPrefixType.noPrefix"),
        SENDERNAME(2, "SenderPrefixType.senderName"),
        MAILACCOUNTPREFIX(3, "SenderPrefixType.mailAccount"),
        DEFINEDBYUSER(4, "SenderPrefixType.definedByUser");

        private final int constant;
        private final String resource;

        SenderPrefixType(int value, String resource) {
            this.constant = value;
            this.resource = resource;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }

        public String getResource() {
            return resource;
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }
    }

    /**
     * Constants to define document generation ordered column, this columns
     * was be defined in "DocumentGenerationActivityContactList" fantabulous list
     */
    public static enum DocOrderedColumn {
        CONTACT_NAME(1, new String[]{"contactName1", "contactName2", "contactName3"}),
        CONTACTPERSON_NAME(2, new String[]{"contactPersonName1", "contactPersonName2"}),
        COUNTRY(3, new String[]{"countryName"}),
        CITY(4, new String[]{"cityName"}),
        ZIP(5, new String[]{"zip"}),
        STREET_HOUSENUMBER(6, new String[]{"street", "houseNumber"}),;

        private final int constant;
        private final String[] columnNames;

        DocOrderedColumn(int key, String[] fantabulosColumnNames) {
            constant = key;
            columnNames = fantabulosColumnNames;
        }

        public int getConstant() {
            return constant;
        }

        public String[] getColumnNames() {
            return columnNames;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }
    }

    //column order type
    public static final String ASCENDING_ORDER = "1";
    public static final String DESCENDING_ORDER = "0";

    public static enum SentLogContactStatus {
        SUCCESS(1, "SentLogContact.status.success"),
        FAILED(2,"SentLogContact.status.failed"),
        FAILED_WITHOUT_EMAIL(3,"SentLogContact.status.failedWithoutEmail"),
        FAILED_UNKNOWN(4,"SentLogContact.status.failedUnknown"),
        FAILED_RESPONSIBLE(5,"SentLogContact.status.failedResponsible"),
        WAITING_TO_BE_SENT_IN_BACKGROUND(6, "SentLogContact.status.waitingToSendInBackground"),
        SENDING_IN_BACKGROUND(7, "SentLogContact.status.sendingInBackground");

        private final int constant;
        private final String resource;

        SentLogContactStatus(int val, String resource) {
            this.constant = val;
            this.resource = resource;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }

        public String getResource() {
            return resource;
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }
    }


    public static enum CampaignCriteriaType {
        CUSTOMER(Integer.valueOf(ContactConstants.CUSTOMER_CATEGORY)),
        CONTACTPERSON(Integer.valueOf(ContactConstants.CONTACTPERSON_CATEGORY)),
        PRODUCT(Integer.valueOf(ContactConstants.PRODUCT_CATEGORY)),
        ADDRESS(Integer.valueOf(ContactConstants.ADDRESS_CATEGORY)),
        ADDRESS_CONTACTPERSON(Integer.valueOf(ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY));

        private final int constant;

        CampaignCriteriaType(int value) {
            this.constant = value;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }
    }


}
