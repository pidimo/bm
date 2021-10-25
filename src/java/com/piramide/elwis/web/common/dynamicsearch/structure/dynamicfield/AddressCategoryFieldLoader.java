package com.piramide.elwis.web.common.dynamicsearch.structure.dynamicfield;

import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class AddressCategoryFieldLoader extends CategoryFieldLoader {
    @Override
    public DynamicSearchConstants.CategoryFieldType getCategoryFieldType() {
        return DynamicSearchConstants.CategoryFieldType.ADDRESS;
    }
}
