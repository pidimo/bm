package com.piramide.elwis.web.common.mapping;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 13-sep-2006
 * Time: 18:25:12
 * To change this template use File | Settings | File Templates.
 */

public class CheckEntriesMapping extends org.apache.struts.action.ActionMapping {
    boolean checkEntry;

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    String redirect;

    public boolean isCheckEntry() {
        return checkEntry;
    }

    public void setCheckEntry(boolean checkEntry) {
        this.checkEntry = checkEntry;
    }

    public String getMainTable() {
        return mainTable;
    }

    public void setMainTable(String mainTable) {
        this.mainTable = mainTable;
    }

    String mainTable;
}
