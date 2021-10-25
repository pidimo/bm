package com.piramide.elwis.web.dashboard.component.configuration.structure;

/**
 * @author Ivan
 */
public class Action {

    private String action;
    private String resourcekey;
    private String iconUrl;

    public Action() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResourcekey() {
        return resourcekey;
    }

    public void setResourcekey(String resourcekey) {
        this.resourcekey = resourcekey;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String toString() {
        return "name=" + name + " action=" + action;
    }


    /**
     * @deprecated
     */
    private String name;

    /**
     * @deprecated
     */
    public Action(String name) {
        this.name = name;
    }

    /**
     * @deprecated
     */
    public String getName() {
        return name;
    }
}
