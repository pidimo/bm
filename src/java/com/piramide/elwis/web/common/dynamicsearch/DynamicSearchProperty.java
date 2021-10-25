package com.piramide.elwis.web.common.dynamicsearch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchProperty {
    private String fieldAlias;
    private DynamicSearchConstants.Operator operator;
    private List values;

    private boolean isFunctionAlias = false;

    public DynamicSearchProperty(String fieldAlias, DynamicSearchConstants.Operator operator) {
        this.fieldAlias = fieldAlias;
        this.operator = operator;
        values = new ArrayList();
    }

    public DynamicSearchProperty(String fieldAlias, DynamicSearchConstants.Operator operator, List values) {
        this.fieldAlias = fieldAlias;
        this.operator = operator;
        this.values = values;
    }

    public String getFieldAlias() {
        return fieldAlias;
    }

    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public DynamicSearchConstants.Operator getOperator() {
        return operator;
    }

    public void setOperator(DynamicSearchConstants.Operator operator) {
        this.operator = operator;
    }

    public List getValues() {
        return values;
    }

    public void setValues(List values) {
        this.values = values;
    }

    public void addValue(Object value) {
        this.values.add(value);
    }

    public String getValue(int index) {
        String value = null;
        try {
            Object objValue = values.get(index);
            if (objValue != null) {
                value = objValue.toString();
            }
        } catch (IndexOutOfBoundsException iobe) {
        }
        return value;
    }

    public String getSimpleValue() {
        return getValue(0);
    }

    public boolean isFunctionAlias() {
        return isFunctionAlias;
    }

    public void setFunctionAlias(boolean functionAlias) {
        isFunctionAlias = functionAlias;
    }
}
