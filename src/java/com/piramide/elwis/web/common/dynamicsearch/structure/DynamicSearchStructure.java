package com.piramide.elwis.web.common.dynamicsearch.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchStructure {
    private List<DynamicSearch> dynamicSearchList;

    public DynamicSearchStructure() {
        dynamicSearchList = new ArrayList<DynamicSearch>();
    }

    public List<DynamicSearch> getDynamicSearchList() {
        return dynamicSearchList;
    }

    public void setDynamicSearchList(List<DynamicSearch> dynamicSearchList) {
        this.dynamicSearchList = dynamicSearchList;
    }

    public void addDynamicSearch(DynamicSearch dynamicSearch) {
        dynamicSearchList.add(dynamicSearch);
    }

    public DynamicSearch findDynamicSearch(String name) {
        for (int i = 0; i < dynamicSearchList.size(); i++) {
            DynamicSearch dynamicSearch = dynamicSearchList.get(i);
            if (dynamicSearch.getName().equals(name)) {
                return dynamicSearch;
            }
        }
        return null;
    }

    public List<Field> getDynamicSearchFields(String dynamicSearchName, Map params) {
        List<Field> fieldList = new ArrayList<Field>();
        DynamicSearch dynamicSearch = findDynamicSearch(dynamicSearchName);
        if (dynamicSearch != null) {
            fieldList = dynamicSearch.getAllFields(params);
        }
        return fieldList;
    }

    public List<FieldOperator> getDynamicSearchFieldOperators(String dynamicSearchName, String fieldAlias, Map params) {
        List<FieldOperator> fieldOperators = new ArrayList<FieldOperator>();
        DynamicSearch dynamicSearch = findDynamicSearch(dynamicSearchName);
        if (dynamicSearch != null) {
            Field field = dynamicSearch.findField(fieldAlias, params);
            if (field != null) {
                fieldOperators = field.getOperators();
            }
        }
        return fieldOperators;
    }

    public Field getDynamicSearchField(String dynamicSearchName, String fieldAlias, Map params) {
        Field field = null;
        DynamicSearch dynamicSearch = findDynamicSearch(dynamicSearchName);
        if (dynamicSearch != null) {
            field = dynamicSearch.findField(fieldAlias, params);
        }
        return field;
    }
}
