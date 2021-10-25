package com.piramide.elwis.web.common.dynamicsearch.structure;

import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class FieldOperator {
    private DynamicSearchConstants.Operator operator;
    private String parameterName;
    private boolean isParameter;
    private boolean isDefault;

    public FieldOperator() {
        this.isParameter = false;
        this.isDefault = false;
    }

    public DynamicSearchConstants.Operator getOperator() {
        return operator;
    }

    public void setOperator(DynamicSearchConstants.Operator operator) {
        this.operator = operator;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public boolean isParameter() {
        return isParameter;
    }

    public void setIsParameter(boolean parameter) {
        isParameter = parameter;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
