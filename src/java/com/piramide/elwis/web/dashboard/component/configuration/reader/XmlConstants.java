package com.piramide.elwis.web.dashboard.component.configuration.reader;

/**
 * @author : ivan
 */
public class XmlConstants {

    public static class UtilConstants {
        public static final String attrTrue = "TRUE";
        public static final String attrFalse = "FALSE";
        public static final String attrClose = "CLOSE";
        public static final String attrConfiguration = "CONFIGURATION";
        public static final String attrAsc = "ASC";
        public static final String attrDesc = "DESC";
        public static final String attrNone = "NONE";
        public static final String attrInteger = "INTEGER";
        public static final String attrString = "STRING";
        public static final String attrDate = "DATE";
        public static final String attrBoolean = "BOOLEAN";
        public static final String attrText = "TEXT";
        public static final String attrSelect = "SELECT";
        public static final String attrCheck = "CHECK";
        public static final String attrRange = "RANGE";
        public static final String attrList = "LIST";
        public static final String attrCustom = "CUSTOM";
    }

    public static class Main {
        public static final String elementName = "components";
    }

    public static class DataProcessor {
        public static final String elementName = "dataProcessor";
    }

    public static class PersistenceProcessor {
        public static final String elementName = "persistenceProcessor";
    }

    public static class FilterPreProcessor {
        public static final String elementName = "filterPreProcessor";
    }

    public static class ConditionEvaluator {
        public static final String elementName = "conditionEvaluator";
    }

    public static enum ConditionTag {
        elementName("condition"),
        attrType("type"),
        attrOperator("operator"),
        attrValue("value"),
        attrStyle("style");

        private final String constant;

        ConditionTag(String v) {
            constant = v;
        }

        public String getConstant() {
            return constant;
        }
    }

    public static enum RowCounterFilterTag {
        elementName("rowCounterFilter"),
        attrName("name"),
        attrResourceKey("resourceKey"),
        attrValue("value");

        private final String constant;

        RowCounterFilterTag(String v) {
            constant = v;
        }

        public String getConstant() {
            return constant;
        }
    }

    public static class AccessUrl {
        public static final String elementName = "accessUrl";
        public static final String attrUrl = "url";
    }

    public static class Params {
        public static final String elementName = "params";
    }

    public static class Param {
        public static final String elementName = "param";
        public static final String attrName = "name";
        public static final String attrValue = "value";
        public static final String attrColumnId = "columnId";
    }

    public static class Component {
        public static final String elementName = "component";
        public static final String attrId = "id";
        public static final String attrName = "name";
        public static final String attrNameResource = "nameResource";
        public static final String attrFunctionality = "functionality";
        public static final String attrPermission = "permission";
    }

    public static final class Columns {
        public static final String elementName = "columns";
    }


    public static enum ColumnTag {
        elementName("column"),
        attrId("id"),
        attrName("name"),
        attrOrder("order"),
        attrDefault("default"),
        attrAccessColumn("accessColumn"),
        attrPropertyName("propertyName"),
        attrResourceKey("resourceKey"),
        attrPatternKey("patternKey"),
        attrConverter("converter"),
        attrSize("size"),
        attrInverseOrder("inverseOrder");

        private final String constant;

        ColumnTag(String v) {
            constant = v;
        }

        public String getConstant() {
            return constant;
        }
    }

    public static final class Constant {
        public static final String elementName = "constant";
        public static final String attrValue = "value";
        public static final String attrResourceKey = "resourceKey";
    }

    public static final class WindowOptions {
        public static final String elementName = "windowOptions";
    }

    public static final class WindowOption {
        public static final String elementName = "windowOption";
        public static final String attrName = "name";
        public static final String attrAction = "action";
        public static final String attrIconUrl = "iconUrl";
        public static final String attrResourceKey = "resourceKey";
    }

    public static final class Configurations {
        public static final String elementName = "configurations";
    }

    public static final class Filters {
        public static final String elementName = "filters";
    }

    public static final class Filter {
        public static final String elementName = "filter";
        public static final String attrName = "name";
        public static final String attrResourceKey = "resourceKey";
        public static final String attrSecondResourceKey = "secondResourceKey";
        public static final String attrType = "type";
        public static final String attrView = "view";
        public static final String attrSelectId = "selectId";
        public static final String attrSelectValue = "selectValue";
    }

    public static final class StaticFilter {
        public static final String elementName = "staticFilter";
        public static final String attrName = "name";
        public static final String attrMultipleValue = "multipleValue";
    }


    public static final class Values {
        public static final String elementName = "values";
    }

    public static final class Query {
        public static final String elementName = "query";
    }

    public static final class ConstantClass {
        public static final String elementName = "constantClass";
        public static final String attrClassName = "className";
        public static final String attrMethodName = "methodName";
    }

    public static final class Default {
        public static final String elementName = "default";
        public static final String attrValue = "value";
    }

    public static final class UiProcessor {
        public static final String elementName = "uiProcessor";
        public static final String attrType = "type";
        public static final String attrClass = "class";
    }

    public static final class Converters {
        public static final String elementName = "converters";
    }

    public static final class Converter {
        public static final String elementName = "converter";
        public static final String attrName = "name";
        public static final String attrClass = "class";
    }


    public static final class ComponentUrl {
        public static final String elementName = "componentUrl";
        public static final String attrUrl = "url";
    }
}
