package com.piramide.elwis.web.common.accessrightdatalevel;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class AccessRightDataLevelConstants {
    public static final String DTD_FILE = "com/piramide/elwis/web/common/accessrightdatalevel/xml/accessRightDataLevel-config.dtd";
    public static final String CONFIG_FILE = "/WEB-INF/common/accessrightdatalevel/accessRightDataLevel.xml";

    public static enum DataLevelAccessConfiguration {
        ADDRESS_ACCESS("addressAccess"),
        ARTICLE_ACCESS("articleAccess");

        String constant;
        private DataLevelAccessConfiguration(String value) {
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
