package com.piramide.elwis.web.common.dynamicsearch.structure.dynamicfield;

import com.piramide.elwis.cmd.catalogmanager.CategoriesReadCmd;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.xml.BuildSearchStructureOfXml;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public abstract class CategoryFieldLoader implements DynamicFieldLoader {
    protected Log log = LogFactory.getLog(this.getClass());

    public abstract DynamicSearchConstants.CategoryFieldType getCategoryFieldType();

    public List<CategoryField> getCategoryFields(Map params) {
        log.debug("Load dynamic search category fields... " + params);

        List<CategoryField> categoryFields = new ArrayList<CategoryField>();

        DynamicSearchConstants.CategoryFieldType categoryFieldType = getCategoryFieldType();

        CategoriesReadCmd categoriesReadCmd = new CategoriesReadCmd();
        categoriesReadCmd.putParam("companyId", params.get("companyId"));
        categoriesReadCmd.putParam("tableId", categoryFieldType.getTableId());
        if (categoryFieldType.getSecondTableId() != null) {
            categoriesReadCmd.putParam("secondTableId", categoryFieldType.getSecondTableId());
        }

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(categoriesReadCmd, null);
            if (!resultDTO.isFailure() && resultDTO.containsKey("categoryList")) {
                List categories = (List) resultDTO.get("categoryList");
                for (Iterator iterator = categories.iterator(); iterator.hasNext();) {
                    Map categoryMap = (Map) iterator.next();

                    Integer categoryId = new Integer(categoryMap.get("categoryId").toString());
                    CatalogConstants.CategoryType categoryType = CatalogConstants.CategoryType.findCategoryType(categoryMap.get("categoryType").toString());
                    DynamicSearchConstants.FieldType fieldType = getFieldTypeFromCategoryType(categoryType);

                    if (fieldType != null) {
                        String joinFieldAlias = (String) params.get("joinFieldAlias");
                        String joinFieldAlias2 = (String) params.get("joinFieldAlias2");

                        CategoryField categoryField = new CategoryField();
                        categoryField.setAlias(composeAlias(categoryFieldType, categoryId));
                        categoryField.setCategoryId(categoryId);
                        categoryField.setName(categoryMap.get("categoryName").toString());
                        categoryField.setTable(categoryMap.get("table").toString());
                        categoryField.setType(fieldType);
                        categoryField.setCategoryFieldType(categoryFieldType);
                        categoryField.setCategoryType(categoryType);

                        categoryField.setJoinFieldAlias(joinFieldAlias);
                        categoryField.setJoinFieldAlias2(joinFieldAlias2);

                        //set operators
                        BuildSearchStructureOfXml.addFieldOperatorsByType(categoryField);

                        categoryFields.add(categoryField);
                    }
                }
            }
        } catch (AppLevelException e) {
            log.error("Error in execute cmd...", e);
        }

        return categoryFields;
    }

    private String composeAlias(DynamicSearchConstants.CategoryFieldType categoryFieldType, Integer categoryId) {
        return categoryFieldType.getConstant() + categoryId.toString();
    }

    private DynamicSearchConstants.FieldType getFieldTypeFromCategoryType(CatalogConstants.CategoryType categoryType) {
        DynamicSearchConstants.FieldType fieldType = null;

        if (CatalogConstants.CategoryType.ATTACH.equals(categoryType)
                || CatalogConstants.CategoryType.TEXT.equals(categoryType)
                || CatalogConstants.CategoryType.LINK_VALUE.equals(categoryType)
                || CatalogConstants.CategoryType.FREE_TEXT.equals(categoryType)) {

            fieldType = DynamicSearchConstants.FieldType.STRING;
        } else if (CatalogConstants.CategoryType.SINGLE_SELECT.equals(categoryType)
                ||CatalogConstants.CategoryType.COMPOUND_SELECT.equals(categoryType)) {

            fieldType = DynamicSearchConstants.FieldType.CONSTANT;
        } else if (CatalogConstants.CategoryType.DATE.equals(categoryType)) {
            fieldType = DynamicSearchConstants.FieldType.DATE;
        } else if (CatalogConstants.CategoryType.DECIMAL.equals(categoryType)) {
            fieldType = DynamicSearchConstants.FieldType.DECIMAL;
        } else if (CatalogConstants.CategoryType.INTEGER.equals(categoryType)) {
            fieldType = DynamicSearchConstants.FieldType.INTEGER;
        }

        return fieldType;
    }

}
