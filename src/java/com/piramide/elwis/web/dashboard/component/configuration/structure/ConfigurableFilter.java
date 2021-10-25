package com.piramide.elwis.web.dashboard.component.configuration.structure;

import com.piramide.elwis.web.dashboard.component.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : ivan
 */
public class ConfigurableFilter extends Filter {
    private String resourceKey;
    private String secondResourceKey = "";
    private String dataType;
    private String view;
    private String selectValue;
    private String selectId;
    private String className = null;
    private String methodName = null;
    private String initialValue;
    private String finalValue;

    private List<Parameter> parameters = new ArrayList<Parameter>();

    public ConfigurableFilter(String name, String nameResource, String dataType) {
        super(name);
        this.resourceKey = nameResource;
        this.dataType = dataType;
    }

    public ConfigurableFilter(String name) {
        super(name);
    }

    public String getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

    public String getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(String finalValue) {
        this.finalValue = finalValue;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String nameResource) {
        this.resourceKey = nameResource;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setClassAndMethodName(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }

    public String getSelectId() {
        return selectId;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

    public boolean readConstantValuesFromConstantClass() {
        return (null != className && null != methodName);
    }

    public boolean readConstantValuesFromDataBase() {
        return (null != parameters && !parameters.isEmpty());
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }


    public boolean isIntegerType() {
        return Constant.TYPE_INTEGER.equals(getDataType());
    }

    public boolean isBooleanType() {
        return Constant.TYPE_BOOLEAN.equals(getDataType());
    }

    public boolean isDateType() {
        return Constant.TYPE_DATE.equals(getDataType());
    }

    public boolean isStringType() {
        return Constant.TYPE_STRING.equals(getDataType());
    }

    public boolean isRangeView() {
        return Constant.VIEW_RANGE.equals(getView());
    }

    public boolean isSelectView() {
        return Constant.VIEW_SELECT.equals(getView());
    }

    public boolean isTextView() {
        return Constant.VIEW_TEXT.equals(getView());
    }

    public boolean isCheckView() {
        return Constant.VIEW_CHECK.equals(getView());
    }


    public String toString() {
        String cad = "name=" + getName() + " resourceKey=" + resourceKey + " dataType=" + dataType + " view=" + view;

        if (null != className && null != methodName) {
            cad += " className=" + className + " methodName=" + methodName;
        }
        if (null != parameters) {
            cad += " parameters=" + parameters;
        }
        cad += " initialValue=" + initialValue + " finalValue=" + finalValue;
        return cad;
    }


    public String getSecondResourceKey() {
        return secondResourceKey;
    }

    public void setSecondResourceKey(String secondResourceKey) {
        this.secondResourceKey = secondResourceKey;
    }

    public Object clone() {
        ConfigurableFilter clone;
        clone = (ConfigurableFilter) super.clone();

        List<Parameter> cloneParameters = new ArrayList<Parameter>();
        if (null != parameters) {
            for (Parameter parameter : parameters) {
                Parameter cloneParameter = (Parameter) parameter.clone();
                cloneParameters.add(cloneParameter);
            }
        }
        if (null != clone) {
            clone.setFinalValue(finalValue);
            clone.setInitialValue(initialValue);
            clone.setResourceKey(resourceKey);
            clone.setSelectId(selectId);
            clone.setSelectValue(selectValue);
            clone.setView(view);
            clone.setParameters(cloneParameters);
            clone.setSecondResourceKey(secondResourceKey);
        }

        return clone;
    }
}
