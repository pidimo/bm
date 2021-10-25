package com.piramide.elwis.utils.deduplication;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DeduplicationItemWrapper {

    private Integer position;
    private String value;
    private String key;
    private String checkboxName;
    private String checkboxValue;
    private String ejbFieldName;

    private boolean isDateValue;

    public DeduplicationItemWrapper() {
        this.value = "";
        this.isDateValue = false;
    }

    public DeduplicationItemWrapper(Integer position) {
        this.position = position;
        this.value = "";
        this.isDateValue = false;
    }

    public DeduplicationItemWrapper(Integer position, String value) {
        this.position = position;
        this.value = value;
        this.isDateValue = false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCheckboxName() {
        return checkboxName;
    }

    public void setCheckboxName(String checkboxName) {
        this.checkboxName = checkboxName;
    }

    public String getCheckboxValue() {
        return checkboxValue;
    }

    public void setCheckboxValue(String checkboxValue) {
        this.checkboxValue = checkboxValue;
    }

    public boolean getIsDateValue() {
        return isDateValue;
    }

    public void setIsDateValue(boolean isDateValue) {
        this.isDateValue = isDateValue;
    }

    public String getEjbFieldName() {
        return ejbFieldName;
    }

    public void setEjbFieldName(String ejbFieldName) {
        this.ejbFieldName = ejbFieldName;
    }
}
