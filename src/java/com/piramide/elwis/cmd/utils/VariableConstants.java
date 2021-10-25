package com.piramide.elwis.cmd.utils;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 10, 2004
 * Time: 4:46:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariableConstants {

    public static final int FIELD_ADDRESS_CITY = 0;
    public static final int FIELD_ADDRESS_COMPLETE = 1;
    public static final int FIELD_ADDRESS_COUNTRY = 2;
    public static final int FIELD_ADDRESS_COUNTRYCODE = 3;
    public static final int FIELD_ADDRESS_NAME = 4;
    public static final int FIELD_ADDRESS_NAME1 = 5;
    public static final int FIELD_ADDRESS_NAME2 = 6;
    public static final int FIELD_ADDRESS_NAME3 = 7;
    public static final int FIELD_ADDRESS_NAMES = 8;
    public static final int FIELD_ADDRESS_STREET = 9;
    public static final int FIELD_ADDRESS_ZIP = 10;
    public static final int FIELD_PERSON_ADDRESSNAME = 11;
    public static final int FIELD_PERSON_ADDRESSSALUTATION = 12;
    public static final int FIELD_PERSON_FIRSTNAME = 13;
    public static final int FIELD_PERSON_LASTNAME = 14;
    public static final int FIELD_PERSON_LETTERNAME = 15;
    public static final int FIELD_PERSON_LETTERSALUTATION = 16;
    public static final int FIELD_PERSON_NAME = 17;
    public static final int FIELD_PERSON_TITLE = 18;
    public static final int FIELD_DATE = 19;
    public static final int FIELD_ADDRESS_CUSTOMERNUMBER = 20;

    public static final int FIELD_COMPANY_ADDRESS = 0;
    public static final int FIELD_COMPANY_CITY = 1;
    public static final int FIELD_COMPANY_COUNTRY = 2;
    public static final int FIELD_COMPANY_COUNTRYCODE = 3;
    public static final int FIELD_COMPANY_NAME = 4;
    public static final int FIELD_COMPANY_NAME1 = 5;
    public static final int FIELD_COMPANY_NAME2 = 6;
    public static final int FIELD_COMPANY_NAME3 = 7;
    public static final int FIELD_COMPANY_STREET = 8;
    public static final int FIELD_COMPANY_ZIP = 9;
    public static final int FIELD_EMPLOYEE_FIRSTNAME = 10;
    public static final int FIELD_EMPLOYEE_INITIALS = 11;
    public static final int FIELD_EMPLOYEE_LASTNAME = 12;
    public static final int FIELD_EMPLOYEE_NAME = 13;
    public static final int FIELD_EMPLOYEE_TITLE = 14;
    public static final int FIELD_EMPLOYEE_FUNCTION = 15;
    public static final int FIELD_EMPLOYEE_DEPARTMENT = 16;


    public static final int FIELD_SALESPROCESS_ACTION_NUMBER = 0;
    public static final int FIELD_SALESPROCESS_ACTION_TYPE = 1;
    public static final int FIELD_SALESPROCESS_ACTION_DATE = 2;
    public static final int FIELD_SALESPROCESS_ACTION_CURRENCY = 3;
    public static final int FIELD_SALESPROCESS_ACTION_VALUE = 4;
    public static final int FIELD_SALESPROCESS_ACTION_TOTALAMOUNT = 5;


    public static final int FIELD_SALESPROCESS_ACTIONPOSITION_NUMBER = 0;
    public static final int FIELD_SALESPROCESS_ACTIONPOSITION_NAME = 1;
    public static final int FIELD_SALESPROCESS_ACTIONPOSITION_TEXT = 2;
    public static final int FIELD_SALESPROCESS_ACTIONPOSITION_PRODUCTNUMBER = 3;
    public static final int FIELD_SALESPROCESS_ACTIONPOSITION_UNIT = 4;
    public static final int FIELD_SALESPROCESS_ACTIONPOSITION_PRICE = 5;
    public static final int FIELD_SALESPROCESS_ACTIONPOSITION_QUANTITY = 6;
    public static final int FIELD_SALESPROCESS_ACTIONPOSITION_TOTAL = 7;

    /**
     * Invoice variables
     */
    public static final int FIELD_INVOICE_NUMBER = 0;
    public static final int FIELD_INVOICE_DATE = 1;
    public static final int FIELD_INVOICE_PAY_CONDITION_TEXT = 2;
    public static final int FIELD_INVOICE_CURRENCY = 3;
    public static final int FIELD_INVOICE_PAYMENT_DATE = 5;
    public static final int FIELD_INVOICE_PAYMENT_TEXT = 6;
    public static final int FIELD_INVOICE_OPEN_AMOUNT = 7;
    public static final int FIELD_INVOICE_TOTAL_AMOUNT_NET = 8;
    public static final int FIELD_INVOICE_TOTAL_AMOUNT_GROSS = 9;
    public static final int FIELD_INVOICE_REMINDER_LAST_LEVEL = 10;
    public static final int FIELD_INVOICE_REMINDER_NEXT_DATE = 11;
    public static final int FIELD_INVOICE_REMINDER_DATE = 12;
    public static final int FIELD_INVOICE_REMINDER_LEVEL_TITLE = 13;
    public static final int FIELD_INVOICE_REMINDER_TEXT = 14;
    public static final int FIELD_INVOICE_REMINDER_NEXT_PAYMENT_DATE = 15;
    public static final int FIELD_INVOICE_REMINDER_FEE = 16;
    public static final int FIELD_INVOICE_VOUCHER_TYPE = 17;
    public static final int FIELD_INVOICE_REMINDER_OPENAMOUNTPLUSFEE = 18;
    /**
     * Invoice positions variables
     */
    public static final int FIELD_INVOICE_POSITION_NUMBER = 0;
    public static final int FIELD_INVOICE_POSITION_NAME = 1;
    public static final int FIELD_INVOICE_POSITION_TEXT = 2;
    public static final int FIELD_INVOICE_POSITION_PRICE = 3;
    public static final int FIELD_INVOICE_POSITION_QUANTITY = 4;
    public static final int FIELD_INVOICE_POSITION_VAT_NAME = 5;
    public static final int FIELD_INVOICE_POSITION_VAT_RATE = 6;
    public static final int FIELD_INVOICE_POSITION_TOTAL = 7;
    public static final int FIELD_INVOICE_POSITION_UNIT = 8;
    public static final int FIELD_INVOICE_POSITION_PRODUCTNUMBER = 9;
    /**
     * Invoice vat variables
     */
    public static final int FIELD_INVOICE_VAT_NAME = 0;
    public static final int FIELD_INVOICE_VAT_RATE = 1;
    public static final int FIELD_INVOICE_VAT_AMOUNT = 2;
    public static final int FIELD_INVOICE_VAT_AMOUNT_TO = 3;

    /**
     * Categories variable prefix
     */
    public static final String FIELD_CATEGORY_ADDRESS_PREFIX = "Address_cat_";


    public static final String[] ADDRESS_PERSON_FIELDS = {
            "address_city",
            "address_complete",
            "address_country",
            "address_countrycode",
            "address_name",
            "address_name1",
            "address_name2",
            "address_name3",
            "address_names",
            "address_street",
            "address_zip",
            "person_addressname",
            "person_addresssalutation",
            "person_firstname",
            "person_lastname",
            "person_lettername",
            "person_lettersalutation",
            "person_name",
            "person_title",
            "date",
            "customer_number"
    };

    public static final String[] COMPANY_EMPLOYEE_FIELDS = {
            "company_address",
            "company_city",
            "company_country",
            "company_countrycode",
            "company_name",
            "company_name1",
            "company_name2",
            "company_name3",
            "company_street",
            "company_zip",
            "employee_firstname",
            "employee_initials",
            "employee_lastname",
            "employee_name",
            "employee_title",
            "employee_function",
            "employee_department"
    };

    public static final String[] SALESPROCESS_ACTION_FIELDS = {
            "action_number",
            "action_type",
            "action_date",
            "action_currency",
            "action_value",
            "action_total_amount"
    };

    public static final String[] SALESPROCESS_ACTIONPOSITION_FIELDS = {
            "action_position_number",
            "action_position_name",
            "action_position_text",
            "action_position_productnumber",
            "action_position_unit",
            "action_position_price",
            "action_position_quantity",
            "action_position_total"
    };
    /**
     * Invoice variables, including reminders ones
     */
    public static final String[] INVOICE_FIELDS = {
            "invoice_number",
            "invoice_date",
            "invoice_pay_condition_text",
            "invoice_currency",
            "invoice_vat",
            "invoice_payment_date",
            "invoice_text",
            "invoice_open_amount",
            "invoice_total_amount_net",
            "invoice_total_amount_gross",
            "invoice_reminder_last_level",
            "invoice_reminder_next_date",
            "invoice_reminder_date",
            "invoice_reminder_level_title",
            "invoice_reminder_text",
            "invoice_reminder_next_payment_date",
            "invoice_reminder_fee",
            "invoice_vouchertype",
            "invoice_reminder_openamountplusfee"
    };
    /**
     * Invoice position variables
     */
    public static final String[] INVOICE_POSITION_FIELDS = {
            "invoice_position_number",
            "invoice_position_name",
            "invoice_position_text",
            "invoice_position_price",
            "invoice_position_quantity",
            "invoice_position_vat_name",
            "invoice_position_vat_rate",
            "invoice_position_total",
            "invoice_position_unit",
            "invoice_position_productnumber"
    };
    /**
     * Invoice vats variables
     */
    public static final String[] INVOICE_VAT_FIELDS = {
            "invoice_vat_name",
            "invoice_vat_rate",
            "invoice_vat_amount",
            "invoice_vat_amount_to"
    };


    public static enum VariableType {
        CONTACT(1, "CONTACT_VARS"),
        COMPANY(2, "COMPANY_VARS"),
        EMPLOYEE(3, "EMPLOYEE_VARS");

        private Integer constant;
        private String literal;

        private VariableType(Integer value, String literal) {
            this.constant = value;
            this.literal = literal;
        }

        public Integer getConstant() {
            return constant;
        }

        public String getLiteral() {
            return literal;
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }

        public static VariableType findVariableType(String constant) {
            for (int i = 0; i < VariableType.values().length; i++) {
                VariableType variableType = VariableType.values()[i];
                if (variableType.equal(constant)) {
                    return variableType;
                }
            }
            return null;
        }

        public static String findLiteral(String constant) {
            VariableType variableType = findVariableType(constant);
            if (variableType != null) {
                return variableType.getLiteral();
            }
            return null;
        }
    }

}


