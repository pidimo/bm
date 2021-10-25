package com.piramide.elwis.web.common.accessrightdatalevel.structure;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class AccessRightList {
    private String listName;
    private String aliasField;

    public AccessRightList() {
    }

    public AccessRightList(String listName, String aliasField) {
        this.listName = listName;
        this.aliasField = aliasField;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getAliasField() {
        return aliasField;
    }

    public void setAliasField(String aliasField) {
        this.aliasField = aliasField;
    }
}
