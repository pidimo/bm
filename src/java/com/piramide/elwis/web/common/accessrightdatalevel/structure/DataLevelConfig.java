package com.piramide.elwis.web.common.accessrightdatalevel.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class DataLevelConfig {

    private String name;
    private List<AccessRightList> accessRightLists;
    private List<Table> tableList;

    public DataLevelConfig(String name) {
        this.name = name;
        accessRightLists = new ArrayList<AccessRightList>();
        tableList = new ArrayList<Table>();
    }

    public List<AccessRightList> getAccessRightLists() {
        return accessRightLists;
    }

    public void setAccessRightLists(List<AccessRightList> accessRightLists) {
        this.accessRightLists = accessRightLists;
    }

    public void addAccessRightList(AccessRightList accessRightList) {
        accessRightLists.add(accessRightList);
    }

    public AccessRightList findAccessRightList(String listName) {

        for (AccessRightList accessRightList : accessRightLists) {
            if (accessRightList.getListName().equals(listName)) {
                return accessRightList;
            }
        }
        return null;
    }

    public List<Table> getTableList() {
        return tableList;
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    public void addTable(Table table) {
        tableList.add(table);
    }

    public Table findTable(String tableName) {
        for (Table table : tableList) {
            if (table.getTableName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
