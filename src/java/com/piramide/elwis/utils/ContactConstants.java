package com.piramide.elwis.utils;

/**
 * Contains contact manager module constants.
 *
 * @author Fernando Montaño
 * @version $Id: ContactConstants.java 12586 2016-09-07 20:54:40Z miguel $
 */
public abstract class ContactConstants {
    /**
     * TABLE NAMES SECTIOn
     */
    public static final String TABLE_ADDRESS = "address";
    public static final String TABLE_CONTACT = "contact";
    public static final String TABLE_CONTACTPERSON = "contactperson";
    public static final String TABLE_DEPARTMENT = "department";
    public static final String TABLE_OFFICE = "office";
    public static final String TABLE_TELECOM = "telecom";
    public static final String TABLE_BANKACCOUNT = "bankaccount";
    public static final String TABLE_EMPLOYEE = "employee";
    public static final String TABLE_FREETEXT = "freetext";
    public static final String TABLE_SUPPLIER = "supplier";
    public static final String TABLE_CUSTOMER = "customer";
    public static final String TABLE_RECENT = "recent";
    public static final String TABLE_FAVORITE = "favorite";
    public static final String TABLE_USER = "elwisuser";
    public static final String TABLE_COMPANY = "company";
    public static final String TABLE_BANK = "bank";
    public static final String TABLE_CAMPAIGN = "campaign";
    public static final String TABLE_IMPORTPROFILE = "importprofile";
    public static final String TABLE_IMPORTCOLUMN = "importcolumn";
    public static final String TABLE_ADDITIONALADDRESS = "addaddress";
    public static final String TABLE_ADDRESSRELATION = "addressrelation";
    public static final String TABLE_USERADDRESSACCESS = "useraddressaccess";
    public static final String TABLE_IMPORTRECORD = "importrecord";
    public static final String TABLE_RECORDCOLUMN = "recordcolumn";
    public static final String TABLE_RECORDUPLICATE = "recordduplicate";
    public static final String TABLE_DEDUPLICONTACT = "deduplicontact";
    public static final String TABLE_DUPLICATEGROUP = "duplicategroup";
    public static final String TABLE_DUPLICATEADDRESS = "duplicateaddress";

    /**
     * JNDI SECTION *
     */
    public static final String JNDI_CONTACT = "Elwis.Contact";
    public static final String JNDI_ADDRESS = "Elwis.Address";
    public static final String JNDI_CONTACTPERSON = "Elwis.ContactPerson";
    public static final String JNDI_CONTACTATTACH = "Elwis.ContactAttach";
    public static final String JNDI_DEPARTMENT = "Elwis.Department";
    public static final String JNDI_EMPLOYEE = "Elwis.Employee";
    public static final String JNDI_CONTACTSERVICE = "Elwis.ContactService";
    public static final String JNDI_OFFICE = "Elwis.Office";
    public static final String JNDI_TELECOM = "Elwis.Telecom";
    public static final String JNDI_BANKACCOUNT = "Elwis.BankAccount";
    public static final String JNDI_CUSTOMER = "Elwis.Customer";
    public static final String JNDI_SUPPLIER = "Elwis.Supplier";
    public static final String JNDI_CONTACTFREETEXT = "Elwis.ContactFreeText";
    public static final String JNDI_FAVORITE = "Elwis.Favorite";
    public static final String JNDI_COMPANY = "Elwis.Company";
    public static final String JNDI_RECENT = "Elwis.Recent";
    public static final String JNDI_IMPORTPROFILE = "Elwis.ImportProfile";
    public static final String JNDI_IMPORTCOLUMN = "Elwis.ImportColumn";
    public static final String JNDI_ADDITIONALADDRESS = "Elwis.AdditionalAddress";
    public static final String JNDI_ADDRESSRELATION = "Elwis.AddressRelation";
    public static final String JNDI_USERADDRESSACCESS = "Elwis.UserAddressAccess";
    public static final String JNDI_IMPORTRECORD = "Elwis.ImportRecord";
    public static final String JNDI_RECORDCOLUMN = "Elwis.RecordColumn";
    public static final String JNDI_RECORDDUPLICATE = "Elwis.RecordDuplicate";
    public static final String JNDI_DEDUPLICONTACT = "Elwis.DedupliContact";
    public static final String JNDI_DUPLICATEGROUP = "Elwis.DuplicateGroup";
    public static final String JNDI_DUPLICATEADDRESS = "Elwis.DuplicateAddress";

    public static final String JNDI_DATAIMPORTSERVICE = "Elwis.DataImport";
    public static final String JNDI_DEDUPLICATIONADDRESSSERVICE = "Elwis.DeduplicationAddressService";
    public static final String JNDI_CONTACTMERGESERVICE = "Elwis.ContactMergeService";

    /**
     * CONSTANT VALUES
     */
    public static final String ADDRESSTYPE_PERSON = "1";
    public static final String ADDRESSTYPE_ORGANIZATION = "0";
    public static final Integer TELECOMTYPE_PHONE = new Integer(0);
    public static final String TELECOMTYPE_EMAIL = "EMAIL";
    /**
     * CONSTANTS CATEGORY
     */
    public static final String CUSTOMER_CATEGORY = "1";
    public static final String CONTACTPERSON_CATEGORY = "2";
    public static final String PRODUCT_CATEGORY = "3";
    public static final String ADDRESS_CATEGORY = "4";
    public static final String ADDRESS_CONTACTPERSON_CATEGORY = "5";
    public static final String SALES_PROCESS_CATEGORY = "6";
    public static final String SALE_POSITION_CATEGORY = "7";

    public static final String CONTACT_REPORT_LIST = "1";
    public static final String CONTACTPERSON_REPORT_LIST = "2";
    public static final String DEPARTMENT_REPORT_LIST = "3";
    public static final String CUSTOMER_REPORT_LIST = "4";
    public static final String SUPPLIER_REPORT_LIST = "5";
    public static final String EMPLOYEE_REPORT_LIST = "6";
    public static final String COMMUNICATION_REPORT_LIST = "7";

    //in/out values
    public static final String IN_VALUE = "1";
    public static final String OUT_VALUE = "2";

    public static final String TRUE_VALUE = "true";

    public static final String IS_PERSONAL = "1";
    public static final String ISNOT_PERSONAL = "0";

    public static final Integer CREATORUSER_ACCESSRIGHT = -1000;

    public static final String ADDITIONALADDRESS_MAINADDRESS_NAME = "MAIN ADDRESS";

    public static enum CommunicationType {
        INCOMING("1"),
        OUTGOING("0");

        private String type;

        private CommunicationType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public Integer getTypeAsInt() {
            return Integer.valueOf(type);
        }
    }

    public static enum ImportProfileType {
        ORGANIZATION(1),
        PERSON(2),
        ORGANIZATION_AND_CONTACT_PERSON(3);

        Integer constant;

        private ImportProfileType(Integer value) {
            this.constant = value;
        }

        public Integer getConstant() {
            return constant;
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }
    }

    public static enum DataImportGroup {
        ORGANIZATION(1),
        CONTACT(2),
        CONTACT_PERSON(3);

        Integer constant;

        private DataImportGroup(Integer value) {
            this.constant = value;
        }

        public Integer getConstant() {
            return constant;
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }
    }

    public static enum DinamicColumnType {
        TELECOMUNICATION(1),
        ADDRESS_CATEGORY(2),
        CUSTOMER_CATEGORY(3),
        CONTACTPERSON_CATEGORY(4);

        Integer constant;

        private DinamicColumnType(Integer value) {
            this.constant = value;
        }

        public Integer getConstant() {
            return constant;
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }
    }

    public static enum AdditionalAddressType {
        MAIN(1),
        NORMAL(2);

        Integer constant;
        private AdditionalAddressType(Integer value) {
            this.constant = value;
        }

        public Integer getConstant() {
            return constant;
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }
    }

    public static enum ImportRecordType {
        MAINRECORD(10),
        CONTACTPERSONRECORD(11);

        Integer constant;
        private ImportRecordType(Integer value) {
            this.constant = value;
        }

        public Integer getConstant() {
            return constant;
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }

        public static boolean contains(String constant) {
            if (constant != null) {
                for (int i = 0; i < ImportRecordType.values().length; i++) {
                    ImportRecordType importRecordType = ImportRecordType.values()[i];
                    if (importRecordType.getConstant().toString().equals(constant.trim())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static enum DedupliContactStatus {
        COMPLETED(1),
        PROCESS(2);

        Integer constant;
        private DedupliContactStatus(Integer value) {
            this.constant = value;
        }

        public Integer getConstant() {
            return constant;
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }

        public static boolean contains(String constant) {
            if (constant != null) {
                for (int i = 0; i < DedupliContactStatus.values().length; i++) {
                    DedupliContactStatus status = DedupliContactStatus.values()[i];
                    if (status.getConstant().toString().equals(constant.trim())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static enum CustomerInvoiceShipping {
        VIA_LETTER(1, "Customer.invoiceShipping.viaLetter"),
        VIA_EMAIL(2, "Customer.invoiceShipping.viaEmail"),
    	VIA_XRECHNUNG(3, "Customer.invoiceShipping.xRechnung");

        private final int constant;
        private final String resource;

        CustomerInvoiceShipping(int val, String resource) {
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

}
