package com.piramide.elwis.web.common.dynamicsearch.xml;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class XmlConstants {
    public static final String ELEMENT_ROOT = "dynamicsearchs";
    public static final String ELEMENT_DYNAMICSEARCH = "dynamicsearch";
    public static final String ATTRIB_DYNAMICSEARCH_NAME = "name";

    public static final String ELEMENT_FIELDS = "fields";
    public static final String ELEMENT_FIELD = "field";
    public static final String ATTRIB_FIELD_ALIAS = "alias";
    public static final String ATTRIB_FIELD_TYPE = "type";
    public static final String ATTRIB_FIELD_RESOURCE = "resource";
    public static final String ELEMENT_INNERFIELD = "innerfield";

    public static final String ELEMENT_OPERATORS = "operators";
    public static final String ELEMENT_OPERATOR = "operator";
    public static final String ATTRIB_OPERATOR_NAME = "name";
    public static final String ATTRIB_OPERATOR_PARAMETERNAME = "parametername";
    public static final String ATTRIB_OPERATOR_ISPARAMETER = "isparameter";
    public static final String ATTRIB_OPERATOR_ISDEFAULT = "isdefault";

    public static final String ELEMENT_DYNAMICFIELDS = "dynamicfields";
    public static final String ELEMENT_DYNAMICFIELD = "dynamicfield";
    public static final String ATTRIB_DYNAMICFIELD_LOADFIELDCLASS = "loadfieldclass";
    public static final String ATTRIB_DYNAMICFIELD_JOINFIELDALIAS = "joinfieldalias";
    public static final String ATTRIB_DYNAMICFIELD_JOINFIELDALIAS2 = "joinfieldalias2";

}
