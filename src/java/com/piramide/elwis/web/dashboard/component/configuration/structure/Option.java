package com.piramide.elwis.web.dashboard.component.configuration.structure;

import java.util.List;

/**
 * @author : ivan
 */
public class Option {
    private String name;
    private String value;

    private List<Parameter> params;

    public Option(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Parameter> getParams() {
        return params;
    }

    public void setParams(List<Parameter> params) {
        this.params = params;
    }

    public String toString() {
        return "Option(name=" + name + " value=" + value + " params=" + params + ")";
    }
}
