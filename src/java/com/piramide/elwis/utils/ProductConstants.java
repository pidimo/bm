package com.piramide.elwis.utils;

/**
 * Product constants values
 *
 * @author Fernando Montaño
 * @version $Id: ProductConstants.java 11758 2015-12-09 00:16:33Z miguel $
 */
public abstract class ProductConstants {

    public static final String TABLE_PRODUCT = "product";
    public static final String TABLE_COMPETITORPRODUCT = "competitorproduct";
    public static final String TABLE_PRODUCTSUPLIER = "prodsupplier";
    public static final String TABLE_PRICING = "pricing";
    public static final String TABLE_PRODUCTPICTURE = "productpicture";
    public static final String TABLE_PRODUCTSUPPLIER = "prodsupplier";
    public static final String TABLE_PRODSUPPLIER = "prodsupplier";
    public static final String TABLE_PRODUCTTEXT = "producttext";

    public static final String JNDI_PRODUCT = "Elwis.Product";
    public static final String JNDI_PRODUCT_FREETEXT = "Elwis.ProductFreeText";
    public static final String JNDI_COMPETITORPRODUCT = "Elwis.CompetitorProduct";
    public static final String JNDI_PRICING = "Elwis.Pricing";
    public static final String JNDI_PRODUCTPICTURE = "Elwis.ProductPicture";
    public static final String JNDI_PRODUCTSUPPLIER = "Elwis.ProductSupplier";
    public static final String JNDI_PRODUCTTEXT = "Elwis.ProductText";

    /*          for reports         */
    public static final String PRODUCT_REPORT_LIST = "1";
    public static final String CUSTOMER_REPORT_LIST = "2";
    public static final String SUPPLIER_REPORT_LIST = "3";
    public static final String COMPETITOR_REPORT_LIST = "4";
    public static final String CONTRACT_CUSTOMER_REPORT_LIST = "5";
    public static final String CONTRACT_SUPPLIER_REPORT_LIST = "6";


    public static final String PRODUCTTYPE_TYPE_EVENT_NAME = "[EVENT]";

    public static enum ProductTypeType {
        NORMAL(1),
        EVENT(2);

        Integer constant;
        private ProductTypeType(Integer value) {
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

}
