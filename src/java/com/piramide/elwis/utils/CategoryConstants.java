package com.piramide.elwis.utils;

/**
 * Constants to manage category fields
 * @author Miguel A. Rojas Cardenas
 * @version 6.1.1
 */
public class CategoryConstants {

    /**
     * Constants to identifier category fields in the database
     */
    public static enum CategoryFieldIdentifier {
        CUSTOMER_BUSINESS_AREA("CUSTOMER_BUSINESS_AREA");

        private final String constant;

        CategoryFieldIdentifier(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }

        public boolean equal(String constant) {
            return getConstant().equals(constant);
        }

        public static CategoryFieldIdentifier findCategoryFieldIdentifier(String constant) {
            for (int i = 0; i < CategoryFieldIdentifier.values().length; i++) {
                CategoryFieldIdentifier categoryFieldIdentifier = CategoryFieldIdentifier.values()[i];
                if (categoryFieldIdentifier.equal(constant)) {
                    return categoryFieldIdentifier;
                }
            }
            return null;
        }
    }

}
