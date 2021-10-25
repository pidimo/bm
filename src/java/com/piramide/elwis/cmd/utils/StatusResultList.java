package com.piramide.elwis.cmd.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: StatusResultList.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class StatusResultList implements Serializable {
    int page;
    int maxPageSize;
    Integer languageId;
    List pageSizeList;
    int resultSize;

    public StatusResultList(int maxpageSize, Integer languageId) {
        page = -1;
        maxPageSize = maxpageSize;
        resultSize = 0;
        pageSizeList = new ArrayList();
        this.languageId = languageId;
        addPage();
    }

    public int getCurrentPageSize() {
        return ((Integer) pageSizeList.get(page)).intValue();
    }

    public int getPage() {
        return page;
    }

    public boolean incrementPageSizeFromCurrentPage() {
        resultSize++;
        Integer i = ((Integer) pageSizeList.get(page));
        if (i.intValue() < maxPageSize) {
            i = new Integer(i.intValue() + 1);
            pageSizeList.set(page, i);
            return false;
        } else {
            addPage();
        }
        return true;
    }

    public boolean incrementPageSizeFromCurrentPageNoPagination() {
        resultSize++;
        Integer i = ((Integer) pageSizeList.get(page));
        i = new Integer(i.intValue() + 1);
        pageSizeList.set(page, i);
        return true;
    }


    private void addPage() {
        page++;
        pageSizeList.add(new Integer(0));
    }

    public String toString() {
        return "StatusResultList{" +
                "pageNumber=" + page +
                ", pageSizeList=" + pageSizeList +
                "}";
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public int getSize() {
        return resultSize;
    }

    public void setResultSize(int resultSize) {
        this.resultSize = resultSize;
    }
}

