package com.piramide.elwis.web.dashboard.component.util;

/**
 * @author : ivan
 */
public class Constant {
    public static final String CHARSET_ENCODING = "UTF-8";

    public static final String AVAILABLE_COLUMNS = "availableColumns";
    public static final String SELECTED_COLUMNS = "selectedColumns";
    public static final String ORDERABLE_COLUMNS_REQ = "orderableColumns";

    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_STRING = "STRING";
    public static final String TYPE_DATE = "DATE";
    public static final String TYPE_BOOLEAN = "BOOLEAN";
    public static final String VIEW_TEXT = "TEXT";
    public static final String VIEW_SELECT = "SELECT";
    public static final String VIEW_CHECK = "CHECK";
    public static final String VIEW_RANGE = "RANGE";


    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String COLUMN_ORDER = "ORDER";
    public static final String COLUMN_XMLID = "XMLID";
    public static final String COLUMN_POSITION = "POSITION";
    public static final String COLUMN_ACCESS = "COLUMN_ACCESS";
    public static final String COLUMN_INVERSEORDER = "COLUMN_INVERSEORDER";

    public static final String FILTER_NAME = "FILTER_NAME";
    public static final String FILTER_VALUE = "VALUE";
    public static final String FILTER_ISRANGE = "ISRANGE";
    public static final String FILTER_OPERATE_OVER_VIEW = "OPERATE_OVER_VIEW";

    public static final String VISIBLE_COLUMNS = "VISIBLE_COLUMNS";
    public static final String ORDERABLE_COLUMNS = "ORDERABLE_COLUMNS";
    public static final String FILTERS = "FILTERS";
    public static final String STATIC_FILTERS = "STATIC_FILTERS";

    public static final String DATAPROCESSOR_PARAMETERS = "DATAPROCESSOR_PARAMETERS";

    public static final String ORDER_ASC = "ASC";
    public static final String ORDER_DESC = "DESC";
    public static final String ORDER_NONE = "NONE";

    public static final int SCREEN_SIZE = 940;

    public static enum BirthdayViewType {
        ALL_CONTACTS(1, "dashboard.birthDay.view.allContacts"),
        SELECTED_EMPLOYEE(2, "dashboard.birthDay.view.selectedEmployee");

        private Integer constant;
        private String resource;

        private BirthdayViewType(Integer value, String resource) {
            this.constant = value;
            this.resource = resource;
        }

        public Integer getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }

        public String getResource() {
            return resource;
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }
    }


}
