package com.piramide.elwis.web.common.dynamicsearch;

import com.piramide.elwis.utils.ContactConstants;
import org.alfacentauro.fantabulous.common.Constants;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchConstants {
    //public static final String DTD_FILE = "/WEB-INF/common/dynamicsearch/dynamicSearch-config.dtd";
    public static final String DTD_FILE = "com/piramide/elwis/web/common/dynamicsearch/xml/dynamicSearch-config.dtd";
    public static final String[] CONFIG_FILES = {"/WEB-INF/common/dynamicsearch/dynamicSearch-contacts.xml"};

    public static enum FieldType {
        STRING("string"),
        INTEGER("integer"),
        DECIMAL("decimal"),
        DATE("date"),
        DATABASE("database"),
        CONSTANT("constant"),
        BITWISE("bitwise"),
        UNKNOWN("unknown");

        private final String constant;

        FieldType(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }

        public boolean equal(String constant) {
            return getConstant().equals(constant);
        }

        public static FieldType findFieldType(String constant) {
            for (int i = 0; i < FieldType.values().length; i++) {
                FieldType fieldType = FieldType.values()[i];
                if (fieldType.equal(constant)) {
                    return fieldType;
                }
            }
            return null;
        }
    }

    public static enum Operator {
        EQUAL("EQUAL", "Report.filter.op.equal", Constants.OPERATOR_EQUAL),
        NOT_EQUAL("NOT_EQUAL", "Report.filter.op.notEqual", Constants.OPERATOR_DISTINCT),
        BETWEEN("BETWEEN", "Report.filter.op.between", null),

        CONTAIN("CONTAIN", "Report.filter.op.contain", Constants.OPERATOR_CONTAIN),
        START_WITH("START_WITH", "Report.filter.op.startWith", Constants.OPERATOR_START_WITH),
        END_WITH("END_WITH", "Report.filter.op.endWith", Constants.OPERATOR_END_WITH),

        LESS_THAN("LESS_THAN", "Report.filter.op.lessThan", Constants.OPERATOR_LESS),
        GREATER_THAN("GREATER_THAN", "Report.filter.op.greaterThan", Constants.OPERATOR_GREATER),

        IS("IS", "Report.filter.op.is", Constants.OPERATOR_EQUAL),
        ONE_OF("ONE_OF", "Report.filter.op.oneOf", null),
        IS_NOT("IS_NOT", "Report.filter.op.isNot", Constants.OPERATOR_DISTINCT),
        NOT_ONE_OF("NOT_ONE_OF", "Report.filter.op.notOneOf", null),

        ANDBIT("ANDBIT", "Report.filter.op.is", Constants.OPERATOR_ANDBIT),

        ON("ON", "Report.filter.op.on", Constants.OPERATOR_EQUAL),
        BEFORE("BEFORE", "Report.filter.op.before", Constants.OPERATOR_LESS),
        AFTER("AFTER", "Report.filter.op.after", Constants.OPERATOR_GREATER),
        NOT_ON("NOT_ON", "Report.filter.op.notOn", Constants.OPERATOR_DISTINCT),
        YESTERDAY("YESTERDAY", "Report.filter.op.yesterday", null),
        TODAY("TODAY", "Report.filter.op.today", null),
        TOMORROW("TOMORROW", "Report.filter.op.tomorrow", null),
        LAST_7_DAYS("LAST_7_DAYS", "Report.filter.op.last7Days", null),
        NEXT_7_DAYS("NEXT_7_DAYS", "Report.filter.op.next7Days", null),
        LAST_MONTH("LAST_MONTH", "Report.filter.op.lastMonth", null),
        THIS_MONTH("THIS_MONTH", "Report.filter.op.thisMonth", null),
        NEXT_MONTH("NEXT_MONTH", "Report.filter.op.nextMonth", null),
        LAST_30_DAYS("LAST_30_DAYS", "Report.filter.op.last30Days", null),
        NEXT_30_DAYS("NEXT_30_DAYS", "Report.filter.op.next30Days", null),
        LAST_YEAR("LAST_YEAR", "Report.filter.op.lastYear", null),
        THIS_YEAR("THIS_YEAR", "Report.filter.op.thisYear", null),
        NEXT_YEAR("NEXT_YEAR", "Report.filter.op.nextYear", null);

        private final String constant;
        private final String resource;
        private final String fantabulousOperator;

        Operator(String constant, String resource, String fantabulousOperator) {
            this.constant = constant;
            this.resource = resource;
            this.fantabulousOperator = fantabulousOperator;
        }

        public String getConstant() {
            return constant;
        }

        public String getResource() {
            return resource;
        }

        public String getFantabulousOperator() {
            return fantabulousOperator;
        }

        public boolean equal(String constant) {
            return getConstant().equals(constant);
        }

        public static Operator findOperator(String constant) {
            for (int i = 0; i < Operator.values().length; i++) {
                Operator operator = Operator.values()[i];
                if (operator.equal(constant)) {
                    return operator;
                }
            }
            return null;
        }
    }

    public static enum CategoryFieldType {
        ADDRESS("CAT_ADDRESS", ContactConstants.ADDRESS_CATEGORY, ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY, "CategoryField.address.postfix"),
        CONTACTPERSON("CAT_CONTACTPERSON", ContactConstants.CONTACTPERSON_CATEGORY, ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY, "CategoryField.contactPerson.postfix"),
        CUSTOMER("CAT_CUSTOMER", ContactConstants.CUSTOMER_CATEGORY, null, "CategoryField.customer.postfix"),
        PRODUCT("CAT_PRODUCT", ContactConstants.PRODUCT_CATEGORY, null, "CategoryField.product.postfix"),
        SALESPROCESS("CAT_SALESPROCESS", ContactConstants.SALES_PROCESS_CATEGORY, null, "CategoryField.salesProcess.postfix");

        private final String constant;
        private final String tableId;
        private final String secondTableId;
        private final String postfixResource;


        CategoryFieldType(String constant, String tableId, String secondTableId, String postfixResource) {
            this.constant = constant;
            this.tableId = tableId;
            this.secondTableId = secondTableId;
            this.postfixResource = postfixResource;
        }

        public String getConstant() {
            return constant;
        }

        public String getTableId() {
            return tableId;
        }

        public String getSecondTableId() {
            return secondTableId;
        }

        public String getPostfixResource() {
            return postfixResource;
        }

        public boolean equal(String constant) {
            return getConstant().equals(constant);
        }

        public static CategoryFieldType findCategoryFieldType(String constant) {
            for (int i = 0; i < CategoryFieldType.values().length; i++) {
                CategoryFieldType categoryFieldType = CategoryFieldType.values()[i];
                if (categoryFieldType.equal(constant)) {
                    return categoryFieldType;
                }
            }
            return null;
        }
    }

}
