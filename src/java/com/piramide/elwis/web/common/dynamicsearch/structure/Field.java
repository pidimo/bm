package com.piramide.elwis.web.common.dynamicsearch.structure;

import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class Field {
    private String alias;
    private DynamicSearchConstants.FieldType type;
    private String resource;
    private List<FieldOperator> operators;

    private Field innerField;

    public Field() {
        operators = new ArrayList<FieldOperator>();
    }

    public boolean isCategoryField() {
        return false;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public DynamicSearchConstants.FieldType getType() {
        return type;
    }

    public void setType(DynamicSearchConstants.FieldType type) {
        this.type = type;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public List<FieldOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<FieldOperator> operators) {
        this.operators = operators;
    }

    public void addOperator(FieldOperator fieldOperator) {
        operators.add(fieldOperator);
    }

    public Field getInnerField() {
        return innerField;
    }

    public void setInnerField(Field innerField) {
        this.innerField = innerField;
    }

    public FieldOperator findFieldOperator(DynamicSearchConstants.Operator operator) {
        if (operator != null) {
            for (FieldOperator fieldOperator : operators) {
                if (operator.equals(fieldOperator.getOperator())) {
                    return fieldOperator;
                }
            }
        }
        return null;
    }

    public FieldOperator findDefaultFieldOperator() {
        for (FieldOperator fieldOperator : operators) {
            if (fieldOperator.isDefault()) {
                return fieldOperator;
            }
        }
        return null;
    }
}
