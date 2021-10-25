package com.piramide.elwis.web.reports.dynamiccolumns;

import com.piramide.elwis.utils.ContactConstants;

/**
 * Jatun S.R.L.
 * product categories dynamic column loader
 *
 * @author Miky
 * @version $Id: ProductCategoryColumnLoader.java 10-sep-2008 15:01:00 $
 */
public class ProductCategoryColumnLoader extends CategoryColumnLoader {
    public String getTableId() {
        return ContactConstants.PRODUCT_CATEGORY;
    }
}
