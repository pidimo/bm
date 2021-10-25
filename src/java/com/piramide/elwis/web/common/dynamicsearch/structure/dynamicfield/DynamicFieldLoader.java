package com.piramide.elwis.web.common.dynamicsearch.structure.dynamicfield;

import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;

import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public interface DynamicFieldLoader {

    public DynamicSearchConstants.CategoryFieldType getCategoryFieldType();

    public List<CategoryField> getCategoryFields(Map params);
}
