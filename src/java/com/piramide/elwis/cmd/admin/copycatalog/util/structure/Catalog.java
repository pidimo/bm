package com.piramide.elwis.cmd.admin.copycatalog.util.structure;

/**
 * @author: ivan
 */
public class Catalog {

    private String copyClassName;

    public Catalog() {
    }

    public String getCopyClassName() {
        return copyClassName;
    }

    public void setCopyClassName(String copyClassName) {
        this.copyClassName = copyClassName;
    }

    public String toString() {
        return "Catalog('" + copyClassName + "')";
    }
}
