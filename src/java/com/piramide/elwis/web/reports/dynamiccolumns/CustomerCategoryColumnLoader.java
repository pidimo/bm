package com.piramide.elwis.web.reports.dynamiccolumns;

import com.piramide.elwis.utils.ContactConstants;

/**
 * Jatun S.R.L.
 * Customer categories dynamic column loader
 *
 * @author Miky
 * @version $Id: CustomerCategoryColumnLoader.java 10-sep-2008 14:42:19 $
 */
public class CustomerCategoryColumnLoader extends CategoryColumnLoader {
    public String getTableId() {
        return ContactConstants.CUSTOMER_CATEGORY;
    }
}
