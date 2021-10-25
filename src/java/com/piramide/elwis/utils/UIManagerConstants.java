package com.piramide.elwis.utils;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: UIManagerConstants.java 12260 2016-01-27 01:55:35Z miguel $
 */
public class UIManagerConstants {

    public static final String JNDI_STYLESHEET = "Elwis.StyleSheet";
    public static final String JNDI_STYLE = "Elwis.Style";
    public static final String JNDI_STYLEATTRIBUTE = "Elwis.StyleAttribute";

    public static final String TABLE_STYLESHEET = "stylesheet";
    public static final String TABLE_STYLE = "style";
    public static final String TABLE_STYLEATTRIBUTE = "styleattribute";

    //list of style attributes
    public static final String ATTRIBUTE_FONT_SIZE = "font-size";
    public static final String ATTRIBUTE_FONT_FAMILY = "font-family";
    public static final String ATTRIBUTE_BACKGROUND_COLOR = "background-color";
    public static final String ATTRIBUTE_BACKGROUND = "background";
    public static final String ATTRIBUTE_COLOR = "color";
    public static final String ATTRIBUTE_HEIGHT = "height";
    public static final String ATTRIBUTE_WIDTH = "width";
    public static final String ATTRIBUTE_IMG_TITLE = "img-title";
    public static final String ATTRIBUTE_BORDER_COLOR = "border-color";
    public static final String ATTRIBUTE_FONT_WEIGHT = "font-weight";
    public static final String ATTRIBUTE_BORDER = "border";
    public static final String ATTRIBUTE_BORDER_LEFT = "border-left";
    public static final String ATTRIBUTE_BORDER_RIGHT = "border-right";
    public static final String ATTRIBUTE_BORDER_TOP = "border-top";
    public static final String ATTRIBUTE_BORDER_BOTTOM = "border-bottom";
    public static final String ATTRIBUTE_BACKGROUND_IMAGE = "background-image";
    public static final String ATTRIBUTE_BOX_SHADOW = "box-shadow";
    public static final String ATTRIBUTE_WEBKIT_BOX_SHADOW = "-webkit-box-shadow";


    public static final String DEFAULT_KEY = "1";
    public static final String OP_READ = "read";

    //constants to select mosaic or color
    public static final String IS_MOSAIC_KEY = "0";
    public static final String IS_COLOR_KEY = "1";

    //context path
    public static final String CONTEXT_PATH = "CONTEXTPATH";

    //attribute separator key
    public static final String SEPARATOR_KEY = "<S>";

    //attribute composed value separator key
    public static final String VALUE_SEPARATOR_KEY = "[VS]";

    //company style key
    public static final String COMPANY_STYLE_KEY = "compStyle";

    //element without class name
    public static final String WITHOUT_CLASSNAME_KEY = "withoutClassName";

    //object xml style sheet key to session
    public static final String XMLSTYLESHEET_KEY = "xmlStyleSheet";

    //style attribute types key to session
    public static final String XMLSTYLETYPES_KEY = "xmlStyleTypes";

    //object bootstrap xml style sheet key to session
    public static final String XMLSTYLESHEET_BOOTSTRAP_KEY = "xmlStyleSheetBootstrap";

    //type of style sheet
    public static enum StyleSheetType {
        NORMAL(1),
        BOOTSTRAP(2);

        Integer constant;
        private StyleSheetType(Integer value) {
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

    public static enum StyleAttributeType {
        GRADIENT("GRADIENT_TYPE"),
        BOX_SHADOW("BOX_SHADOW_TYPE");

        String constant;
        private StyleAttributeType(String value) {
            this.constant = value;
        }

        public String getConstant() {
            return constant;
        }

        public boolean equal(String value) {
            return this.constant.equals(value);
        }
    }

}
