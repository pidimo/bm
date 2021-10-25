package com.piramide.elwis.web.common.dynamicsearch.structure;

import com.piramide.elwis.web.common.dynamicsearch.structure.dynamicfield.DynamicField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearch {
    private String name;
    private List<Field> fields;
    private List<DynamicField> dynamicFields;

    //this var is used to cache all fields, because when read category fields this is slow
    private List<Field> allSearchFields;

    public DynamicSearch() {
        fields = new ArrayList<Field>();
        dynamicFields = new ArrayList<DynamicField>();
        allSearchFields = new ArrayList<Field>();
    }

    public DynamicSearch(String name) {
        this.name = name;
        fields = new ArrayList<Field>();
        dynamicFields = new ArrayList<DynamicField>();
        allSearchFields = new ArrayList<Field>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public List<DynamicField> getDynamicFields() {
        return dynamicFields;
    }

    public void setDynamicFields(List<DynamicField> dynamicFields) {
        this.dynamicFields = dynamicFields;
    }

    public void addDynamicField(DynamicField dynamicField) {
        dynamicFields.add(dynamicField);
    }

    public List<Field> getAllFields(Map params) {
        List<Field> fieldList = new ArrayList<Field>();
        fieldList.addAll(fields);

        for (DynamicField dynamicField : dynamicFields) {
            fieldList.addAll(dynamicField.getCategoryFields(params));
        }

        //cache all fields
        allSearchFields = new ArrayList<Field>(fieldList);
        return fieldList;
    }

    public Field findField(String alias, Map params) {
        List<Field> fieldList;
        if (!allSearchFields.isEmpty()) {
            fieldList = new ArrayList<Field>(allSearchFields);
        } else {
            fieldList = getAllFields(params);
        }

        for (Field field : fieldList) {
            if (field.getAlias().equals(alias)) {
                return field;
            }
        }
        return null;
    }

    public void resetAllFields() {
        allSearchFields = new ArrayList<Field>();
    }
}
