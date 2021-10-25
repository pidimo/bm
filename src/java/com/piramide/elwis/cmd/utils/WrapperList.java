package com.piramide.elwis.cmd.utils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 3, 2004
 * Time: 8:30:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class WrapperList {
    private List list;

    public WrapperList(List list) {
        this.list = list;
    }

    public int getSize() {
        return list.size();
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String toString() {
        return "WrapperList{" +
                "list=" + list +
                "}";
    }
}
