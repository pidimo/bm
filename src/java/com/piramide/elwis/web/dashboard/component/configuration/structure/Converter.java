package com.piramide.elwis.web.dashboard.component.configuration.structure;

/**
 * @author : ivan
 */
public class Converter {
    private String name;
    private String clazz;

    public Converter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String toString() {
        return "Converter(name=" + name + ")";
    }
}
