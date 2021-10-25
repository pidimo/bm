package com.piramide.elwis.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Author: ivan
 * Date: Oct 13, 2006 - 11:16:59 AM
 */
public class CategoryUtil {
    private static Log log = LogFactory.getLog(CategoryUtil.class);

    public static List<Map> recoverCategoryValues(Map paramDTO) {
        List<String> pageCategoryIds = (List<String>) paramDTO.get("pageCategoryIds");
        if (null == pageCategoryIds) {
            pageCategoryIds = new ArrayList<String>();
            log.debug("->Initialize pageCategoryIds as empty list");
        }

        log.debug("->pageCategoryIds" + pageCategoryIds + " OK");

        List<Map> singleSelectElements = new ArrayList<Map>();
        List<Map> multipleSelectElements = new ArrayList<Map>();
        List<Map> defaultElements = new ArrayList<Map>();

        List<Map> structure = new ArrayList<Map>();
        for (String categoryId : pageCategoryIds) {
            String type = (String) paramDTO.get("categoryType_" + categoryId);
            Object value = paramDTO.get(categoryId);
            if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstant().equals(type) && !(value instanceof List)) {
                String selectedValues = (String) paramDTO.get("selected_" + categoryId);
                value = parseMultipleSelectValues(selectedValues);
            }
            String attachId = (String) paramDTO.get("attachId_" + categoryId);
            String categoryName = (String) paramDTO.get("categoryName_" + categoryId);
            Map element = new HashMap();
            element.put("categoryId", categoryId);
            element.put("type", type);
            element.put("name", categoryName);
            element.put("value", value);
            if (null != attachId && !"".equals(attachId.trim())) {
                element.put("attachId", attachId);
            } else {
                element.put("attachId", null);
            }

            if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstant().equals(type)) {
                multipleSelectElements.add(element);
            } else if (CatalogConstants.CategoryType.SINGLE_SELECT.getConstant().equals(type)) {
                singleSelectElements.add(element);
            } else {
                defaultElements.add(element);
            }
        }

        structure.addAll(singleSelectElements);
        structure.addAll(multipleSelectElements);
        structure.addAll(defaultElements);

        return structure;
    }

    public static List<String> parseKey(String key) {
        String[] values = key.split("(_)");
        return Arrays.asList(values);
    }

    private static List<String> parseMultipleSelectValues(String s) {

        if ("".equals(s.trim())) {
            return new ArrayList<String>();
        }

        String[] keys = s.split("(,)");
        return Arrays.asList(keys);
    }

    public static void removeKeyAndValueForMap(Map m, String keyPattern) {
        List<String> l = new ArrayList<String>();
        for (Iterator it = m.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String actualKey = entry.getKey().toString();
            if (actualKey.indexOf(keyPattern) == 0) {
                l.add(actualKey);
            }
        }
        for (String s : l) {
            m.remove(s);
        }
    }

    /**
     * Define conditions to use seconGroupId field of an category
     *
     * @param table       module table initial table
     * @param secondTable composed categories table
     * @return true or false
     */
    public static boolean useCategorySecondGroupId(String table, String secondTable) {
        if (ContactConstants.CONTACTPERSON_CATEGORY.equals(table) && ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY.equals(secondTable)) {
            return true;
        }
        return false;
    }
}
