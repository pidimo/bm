package com.piramide.elwis.web.reports.dynamiccolumns;

import com.piramide.elwis.utils.ContactConstants;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class SalesProcessCategoryColumnLoader extends CategoryColumnLoader {

    @Override
    public String getTableId() {
        return ContactConstants.SALES_PROCESS_CATEGORY;
    }
}
