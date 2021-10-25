package com.piramide.elwis.cmd.admin.dropcompany;

import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class Table {
    private String name;
    private List<String> references;

    public Table() {
        references = new ArrayList<String>();
    }

    public void addReference(String tableName) {
        references.add(tableName);
    }

    public void addReferences(List<String> tables) {
        references.addAll(tables);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getReferences() {
        return references;
    }

    @Override
    public String toString() {
        return "Table{" + name + "}";
    }
}
