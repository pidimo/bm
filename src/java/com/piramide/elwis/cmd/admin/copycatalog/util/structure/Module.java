package com.piramide.elwis.cmd.admin.copycatalog.util.structure;

import java.util.List;

/**
 * @author: ivan
 */
public class Module {
    private String path;
    private String moduleId = null;
    private List<Catalog> catalogs;

    public Module() {
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    public String toString() {
        return "Module(path=" + path + ",catalogs=" + catalogs + ")";
    }
}
