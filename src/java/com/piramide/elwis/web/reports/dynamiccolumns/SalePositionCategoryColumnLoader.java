package com.piramide.elwis.web.reports.dynamiccolumns;

import com.piramide.elwis.utils.ContactConstants;

/**
 * Sale position categories dynamic column loader
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.3
 */
public class SalePositionCategoryColumnLoader extends CategoryColumnLoader {
    public String getTableId() {
        return ContactConstants.SALE_POSITION_CATEGORY;
    }
}
