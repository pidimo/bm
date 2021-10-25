package com.piramide.elwis.utils;

/**
 * Contains catalog module constants.
 *
 * @author Fernando Montaño
 * @version $Id: CatalogConstants.java 11332 2015-10-27 23:44:46Z miguel $
 */
public abstract class CatalogConstants {
    /**
     * TABLE NAMES
     */
    public static final String TABLE_BANK = "bank";
    public static final String TABLE_BRANCH = "branch";
    public static final String TABLE_CITY = "city";
    public static final String TABLE_COUNTRY = "country";
    public static final String TABLE_COMPANY = "company";
    public static final String TABLE_CURRENCY = "currency";
    public static final String TABLE_LANGTEXT = "langtext";
    public static final String TABLE_LANGUAGE = "language";
    public static final String TABLE_PAYCONDITION = "paycondition";
    public static final String TABLE_PAYMORALITY = "paymorality";
    public static final String TABLE_PERSONTYPE = "persontype";
    public static final String TABLE_PRIORITY = "priority";
    public static final String TABLE_RESOURCE = "resource";
    public static final String TABLE_SALUTATION = "salutation";
    public static final String TABLE_TITLE = "title";
    public static final String TABLE_ADDRESSSOURCE = "addresssource";
    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_CATEGORYVALUE = "categoryvalue";
    public static final String TABLE_CUSTOMERTYPE = "customertype";
    public static final String TABLE_ADDRESSBANK = "address";
    public static final String TABLE_TELECOMTYPE = "telecomtype";
    public static final String TABLE_ADDRESS = "address";
    public static final String TABLE_FREETEXT = "freetext";
    public static final String TABLE_TEMPLATE = "template";
    public static final String TABLE_SUPPLIERTYPE = "suppliertype";
    public static final String TABLE_COSTCENTER = "costcenter";
    public static final String TABLE_CUSTOMER = "customer";
    public static final String TABLE_TEMPLATETEXT = "templatetext";
    public static final String TABLE_TEMPLATETEXTIMG = "templatetextimg";

    public static final String TABLE_VAT = "vat";
    public static final String TABLE_VATRATE = "vatrate";
    public static final String TABLE_PRODUCTUNIT = "productunit";
    public static final String TABLE_PRODUCTGROUP = "productgroup";
    public static final String TABLE_PRODUCTTYPE = "producttype";
    public static final String TABLE_CATEGORYFIELDVALUE = "categfieldvalue";
    public static final String TABLE_CAMPAIGNTYPE = "camptype";
    public static final String TABLE_CATEGORYGROUP = "categorygroup";
    public static final String TABLE_CATEGORYTAB = "categorytab";
    public static final String TABLE_CATEGORYRELATION = "categoryrelation";
    public static final String TABLE_PAYCONDITIONTEXT = "payconditiontext";
    public static final String TABLE_ADDRESSRELATIONTYPE = "addressreltype";
    public static final String TABLE_WEBDOCUMENT = "webdocument";
    public static final String TABLE_WEBPARAMETER = "webparameter";

    /**
     * SESSION EJB's JNDI NAMES SECTION
     */
    public static final String SEQUENCE_SERVICE = "SequenceService";
    public static final String JNDI_LANGUAGE = "Elwis.Language";
    public static final String JNDI_ADDRESSSOURCE = "Elwis.AddressSource";
    public static final String JNDI_BANK = "Elwis.Bank";
    public static final String JNDI_BRANCH = "Elwis.Branch";
    public static final String JNDI_CATEGORY = "Elwis.Category";
    public static final String JNDI_CATEGORYVALUE = "Elwis.CategoryValue";
    public static final String JNDI_CURRENCY = "Elwis.Currency";
    public static final String JNDI_TITLE = "Elwis.Title";
    public static final String JNDI_CUSTOMERTYPE = "Elwis.CustomerType";
    public static final String JNDI_ADDRESSCATALOG = "Elwis.AddressCatalog";
    public static final String JNDI_PAYCONDITION = "Elwis.PayCondition";
    public static final String JNDI_PAYMORALITY = "Elwis.PayMorality";
    public static final String JNDI_ADDRESSBANK = "Elwis.AddressBank";
    public static final String JNDI_COUNTRY = "Elwis.Country";
    public static final String JNDI_CITY = "Elwis.City";
    public static final String JNDI_PERSONTYPE = "Elwis.PersonType";
    public static final String JNDI_PRIORITY = "Elwis.Priority";
    public static final String JNDI_TELECOMTYPE = "Elwis.TelecomType";
    public static final String JNDI_SALUTATION = "Elwis.Salutation";
    public static final String JNDI_FREETEXT = "Elwis.FreeText";
    public static final String JNDI_LANGTEXT = "Elwis.Langtext";
    public static final String JNDI_TEMPLATE = "Elwis.Template";
    public static final String JNDI_SUPPLIERTYPE = "Elwis.SupplierType";
    public static final String JNDI_COSTCENTER = "Elwis.CostCenter";
    public static final String JNDI_TEMPLATETEXT = "Elwis.TemplateText";
    public static final String JNDI_VAT = "Elwis.Vat";
    public static final String JNDI_VATRATE = "Elwis.VatRate";
    public static final String JNDI_PRODUCTUNIT = "Elwis.ProductUnit";
    public static final String JNDI_PRODUCTGROUP = "Elwis.ProductGroup";
    public static final String JNDI_PRODUCTTYPE = "Elwis.ProductType";
    public static final String JNDI_CATEGORYFIELDVALUE = "Elwis.CategoryFieldValue";
    public static final String JNDI_CAMPAIGNTYPE = "Elwis.CampaignType";
    public static final String JNDI_CATEGORYGROUP = "Elwis.CategoryGroup";
    public static final String JNDI_CATEGORYTAB = "Elwis.CategoryTab";
    public static final String JNDI_CATEGORYRELATION = "Elwis.CategoryRelation";
    public static final String JNDI_PAYCONDITIONTEXT = "Elwis.PayConditionText";
    public static final String JNDI_TEMPLATETEXTIMG = "Elwis.TemplateTextImg";
    public static final String JNDI_ADDRESSRELATIONTYPE = "Elwis.AddressRelationType";
    public static final String JNDI_WEBDOCUMENT = "Elwis.WebDocument";
    public static final String JNDI_WEBPARAMETER = "Elwis.WebParameter";

    /* FACADE JNDI NAMES SECTION */
    public static final String JNDI_CATALOGSERVICE = "Elwis.CatalogService";

    /*Constants of category types
      integer number = 1
      decimal number = 2
      date = 3
      text = 4
      single selection = 5
      compound selection = 6
      link =7
      freeText = 8
      attach = 9.
    */
    public static enum CategoryType {
        INTEGER("1"),
        DECIMAL("2"),
        DATE("3"),
        TEXT("4"),
        SINGLE_SELECT("5"),
        COMPOUND_SELECT("6"),
        LINK_VALUE("7"),
        FREE_TEXT("8"),
        ATTACH("9");

        private final String constant;

        CategoryType(String val) {
            constant = val;
        }

        public String getConstant() {
            return constant;
        }

        public int getConstantAsInt() {
            return new Integer(constant);
        }

        public static CategoryType findCategoryType(String constant) {
            for (int i = 0; i < CategoryType.values().length; i++) {
                CategoryType categoryType = CategoryType.values()[i];
                if (categoryType.getConstant().equals(constant)) {
                    return categoryType;
                }
            }
            return null;
        }
    }

    /**
     * Template media types
     */
    public static enum MediaType {
        WORD(0),
        HTML(1);
        private final int constant;

        MediaType(int v) {
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

    public static enum AddressRelationTypeType {
        INVOICE_ADDRESS(1, "RelationType.invoiceAddress"),
        HIERACHY(2, "RelationType.hierachy"),
        OTHERS(3, "RelationType.others");

        private final int constant;
        private final String resource;

        AddressRelationTypeType(int v, String resource) {
            this.constant = v;
            this.resource = resource;
        }

        public int getConstant() {
            return constant;
        }

        public String getResource() {
            return resource;
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
                                 