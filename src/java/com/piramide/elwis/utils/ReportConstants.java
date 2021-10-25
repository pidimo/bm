package com.piramide.elwis.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author : ivan
 * @version : $Id ReportConstants ${time}
 */
public class ReportConstants {
    //JNDI Names
    public static final String JNDI_FILTER = "Elwis.ReportFilter";
    public static final String JNDI_REPORT = "Elwis.Report";
    public static final String JNDI_COLUMN = "Elwis.ReportColumn";
    public static final String JNDI_COLUMNGROUP = "Elwis.ReportColumnGroup";
    public static final String JNDI_TOTALIZE = "Elwis.ReportTotalize";
    public static final String JNDI_COLUMNTOTALIZE = "Elwis.ReportColumnTotalize";
    public static final String JNDI_CHART = "Elwis.ReportChart";
    public static final String JNDI_REPORTFREETEXT = "Elwis.ReportFreeText";
    public static final String JNDI_FILTERVALUE = "Elwis.FilterValue";
    public static final String JNDI_REPORTROLE = "Elwis.ReportRole";
    public static final String JNDI_REPORTQUERYPARAM = "Elwis.ReportQueryParam";
    public static final String JNDI_REPORTARTIFACT = "Elwis.ReportArtifact";

    //Table mapping names
    public static final String TABLE_FILTER = "reportfilter";
    public static final String TABLE_REPORT = "report";
    public static final String TABLE_COLUMN = "reportcolumn";
    public static final String TABLE_COLUMNGROUP = "columngroup";
    public static final String TABLE_TOTALIZE = "reporttotalize";
    public static final String TABLE_COLUMNTOTALIZE = "columntotalize";
    public static final String TABLE_CHART = "reportchart";
    public static final String TABLE_REPORTFREETEXT = "freetext";
    public static final String TABLE_FILTERVALUE = "filtervalue";
    public static final String TABLE_REPORTROLE = "reportrole";
    public static final String TABLE_REPORTQUERYPARAM = "reportqueryparam";
    public static final String TABLE_REPORTARTIFACT = "reportartifact";

    //Report type Constant
    public static final Integer SUMMARY_TYPE = Integer.valueOf("1");
    public static final Integer MATRIX_TYPE = Integer.valueOf("2");
    public static final Integer SINGLE_TYPE = Integer.valueOf("3");

    //ColumnGroup order
    public static final Boolean ASCENDING_ORDER = Boolean.valueOf(true);
    public static final Boolean DESCENDING_ORDER = Boolean.valueOf(false);


    //Constants of GroupDate by
    public static final Integer GROUP_DATE_BY_YEAR = Integer.valueOf("1");
    public static final Integer GROUP_DATE_BY_MONTH = Integer.valueOf("2");
    /**
     * @deprecated replaced by GROUP_DATE_BY_WEEK
     */
    public static final Integer GROUP_DATE_BY_DAY = Integer.valueOf("3");
    public static final Integer GROUP_DATE_BY_WEEK = Integer.valueOf("3");

    // operator types
    public static final Integer EQUAL = Integer.valueOf("1");
    public static final Integer DISTINCT = Integer.valueOf("2");
    public static final Integer LESS = Integer.valueOf("3");
    public static final Integer GREATER = Integer.valueOf("4");
    public static final Integer LESS_EQUAL = Integer.valueOf("5");
    public static final Integer GREATER_EQUAL = Integer.valueOf("6");
    public static final Integer BETWEEN = Integer.valueOf("7");


    public static final Integer COLUMN_GROUP_AXIS_Y = Integer.valueOf("1");
    public static final Integer COLUMN_GROUP_AXIS_X = Integer.valueOf("2");

    public static final Integer TOTALIZER_TYPE_CUSTOM = Integer.valueOf("-1");
    public static final Integer TOTALIZER_TYPE_SUM = Integer.valueOf("0");
    public static final Integer TOTALIZER_TYPE_AVERAGE = Integer.valueOf("1");
    public static final Integer TOTALIZER_TYPE_LARGUESTVALUE = Integer.valueOf("2");
    public static final Integer TOTALIZER_TYPE_SMALLESTVALUE = Integer.valueOf("3");
    public static final Integer TOTALIZER_TYPE_SUMRECORDS = Integer.valueOf("-2");

    public static List TOTALIZER_NONCUSTOM_TYPES = Arrays.asList(new Object[]{
            TOTALIZER_TYPE_SUM, TOTALIZER_TYPE_AVERAGE,
            TOTALIZER_TYPE_LARGUESTVALUE, TOTALIZER_TYPE_SMALLESTVALUE
    });

    //columns
    public static final String KEY_COLUMNSEPARATOR_ID = "$ID$";
    public static final String KEY_COLUMNSEPARATOR_PATH = "$PATH$";
    public static final String KEY_COLUMNSEPARATOR_COLUMNREF = "$COLUMN$";
    public static final String KEY_COLUMNSEPARATOR_TABLEREF = "$TABLE$";
    public static final String KEY_COLUMNSEPARATOR_LABEL = "#LABEL#";
    public static final String KEY_COLUMNSEPARATOR_SEQUENCE = "$SEQ$";
    public static final String KEY_COLUMNSEPARATOR_VERSION = "$VER$";
    public static final String KEY_COLUMNSEPARATOR_ISTOTALIZER = "$TO$";
    public static final String KEY_COLUMNSEPARATOR_TYPE = "$TYPE$";
    public static final String KEY_COLUMNSEPARATOR_ORDER = "$ORDERC$";
    public static final String KEY_COLUMNSEPARATOR_CATEGORYID = "$CATID$";

    public static final String KEY_RELATION_COLUMNGROUP = "group_relation";
    public static final String KEY_RELATION_COLUMNTOTALIZE = "totalize_relation";
    public static final String KEY_RELATION_CHART = "chart_relation";
    public static final String KEY_LABEL = "label";
    public static final String KEY_RELATION = "relation";

    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";

    //begin filter constants
    //constants
    public static final String ALIAS_FILTER = "F";
    public static final String FILTERVALUE_SEPARATOR = "|@-@|";
    public static final String FILTERVALUE_SEPARATOR_REGULAREXP = "\\|@-@\\|";

    public static final String PRIMARYKEY_SEPARATOR = "|@PK@|";
    public static final String PRIMARYKEY_SEPARATOR_REGULAREXP = "\\|@PK@\\|";

    //keys
    public static final String KEY_SEPARATOR = "$-$";
    public static final String KEY_SEPARATOR_LABEL = "$L$";
    public static final String KEY_SEPARATOR_VALUE = "$V$";
    public static final String KEY_EMPTY = "$EMPTY$";
    public static final String KEY_SEPARATOR_OP = "$OP$";
    public static final String KEY_SEPARATOR_SHOWVIEW = "$SW$";
    public static final String KEY_OPERATOR = "OP";
    public static final String KEY_NOT_OPERATOR = "NOT_OP";
    public static final String KEY_OPLABEL = "OPL";
    public static final String KEY_SHOWVIEW = "SV";
    public static final String KEY_ISDATETYPE = "$ISDATETYPE$";
    public static final String KEY_ISNUMERICTYPE = "$ISNUMERICTYPE$";

    //show value type
    public static final Integer SHOW_ONE_BOX = new Integer(1);
    public static final Integer SHOW_TWO_BOX = new Integer(2);
    public static final Integer SHOW_SELECT = new Integer(3);
    public static final Integer SHOW_MULTIPLESELECT = new Integer(4);
    public static final Integer SHOW_EMPTY = new Integer(5);
    public static final Integer SHOW_POPUP = new Integer(6);

    //filter type
    public static final Integer FILTER_WITH_CONSTANT_VALUE = new Integer(0);
    public static final Integer FILTER_WITH_DB_VALUE = new Integer(1);

    //others
    public static final String DBFILTER_SHOWINSELECT = "select";
    //end filter constants


    //report status 0 = Preparation, 1 = Ready
    public static final Integer REPORT_STATUS_PREPARATION = new Integer(0);
    public static final Integer REPORT_STATUS_READY = new Integer(1);

    //converter constants
    public static final String LONGDATE_AS_SHORT_KEY = "shortdate";
    public static final String LONGDATE_AS_SHORT_VALUE = "true";

    //start chart constants
    public static final String KEY_CHARTVIEW_SERIE = "$SER$";
    public static final String KEY_CHARTVIEW_XVALUE = "$XVAL$";
    public static final String KEY_CHARTVIEW_YVALUE = "$YVAL$";
    public static final String KEY_CHARTVIEW_ZVALUE = "$ZVAL$";
    public static final String KEY_CHARTVIEW_CATEGORY = "$CAT$";
    public static final String KEY_CHARTVIEW_XLABEL = "$XLAB$";
    public static final String KEY_CHARTVIEW_YLABEL = "$YLAB$";

    public static final String KEY_CHARTVIEW_NONE_ORIENTATION = "$NOORIENT$";
    //end chart consttants

    public static final String JRXMLREPORT_ALL_FILTER = String.valueOf(0);

    public static enum CSVFieldDelimiter {
        COMMA(",", "Report.csvFieldDelimiter.comma"),
        SEMICOLON(";", "Report.csvFieldDelimiter.semicolon");

        private String delimiter;
        private String resource;

        private CSVFieldDelimiter(String delimiter, String resource) {
            this.delimiter = delimiter;
            this.resource = resource;
        }

        public String getDelimiter() {
            return delimiter;
        }

        public String getResource() {
            return resource;
        }
    }

    public static enum ReportCharset {
        ISO_8859_1("ISO-8859-1", "Report.charset.ISO_8859_1"),
        UTF_8("UTF-8", "Report.charset.UTF_8"),
        UTF_16("UTF-16", "Report.charset.UTF_16");

        private String constant;
        private String resource;

        private ReportCharset(String constant, String resource) {
            this.constant = constant;
            this.resource = resource;
        }

        public String getConstant() {
            return constant;
        }

        public String getResource() {
            return resource;
        }
    }

    public static enum SourceType {
        INTERNAL(1),
        JRXML(2);

        private int constant;

        private SourceType(int constant) {
            this.constant = constant;
        }

        public int getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return String.valueOf(getConstant());
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }

        public boolean equal(String constant) {
            return getConstantAsString().equals(constant);
        }
    }

}
