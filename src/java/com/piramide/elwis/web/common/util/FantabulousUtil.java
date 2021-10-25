package com.piramide.elwis.web.common.util;

import org.alfacentauro.fantabulous.controller.OrderParam;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.controller.SearchParameter;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.queryexecutor.Query;
import org.alfacentauro.fantabulous.queryexecutor.SQLExecutorException;
import org.alfacentauro.fantabulous.result.ExecutorException;
import org.alfacentauro.fantabulous.result.FieldResultList;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class FantabulousUtil {
    private Log log = LogFactory.getLog(FantabulousUtil.class);

    public static enum OrderValue {
        ascending("ASC"),
        descending("DESC");

        private String value;

        OrderValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private List searchParameters = new ArrayList();
    private List orderParameters = new ArrayList();
    private String module;


    public void addSearchParameter(String field, String value) {
        searchParameters.add(new SearchParameter(field, value));
    }

    public void addOrderParameter(String columnName, OrderValue orderValue) {
        boolean order = false;
        if (OrderValue.ascending.getValue().equals(orderValue.getValue())) {
            order = true;
        }

        orderParameters.add(new OrderParam(columnName, order));
    }

    protected FieldResultList executeFantabulousList(HttpServletRequest request, String listName) {

        FantabulousManager fantaManager;
        if (null == module) {
            fantaManager = FantabulousManager.loadFantabulousManager(request.getSession().getServletContext(), request);
        } else {
            fantaManager = FantabulousManager.loadFantabulousManager(request.getSession().getServletContext(), module);
        }

        ListStructure list = null;
        try {
            list = fantaManager.getList(listName);
        } catch (ListStructureNotFoundException e) {
            log.error("-> Read [list=" + listName + "] FAIL ", e);
        }

        if (null == list) {
            return null;
        }

        Parameters fantabulousParameters = new Parameters();
        if (!searchParameters.isEmpty()) {
            fantabulousParameters.setSearchParameters(searchParameters);
        }

        if (!orderParameters.isEmpty()) {
            fantabulousParameters.setOrderParameters(orderParameters);
        }

        try {
            log.debug("List to execute: " + list.getListName());
            return Query.i.executeQuery(list, fantabulousParameters);
        } catch (SQLExecutorException e) {
            e.printStackTrace();
        } catch (ExecutorException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Map> getData(FieldResultList elementResultList) {
        Collection fielResult = elementResultList.getFieldResults();
        List<Map> result = new ArrayList<Map>();
        for (Iterator it = fielResult.iterator(); it.hasNext();) {
            Map element = (Map) it.next();
            result.add(element);
        }
        return result;
    }

    public List<Map> getData(HttpServletRequest request, String listName) {
        FieldResultList resultList = executeFantabulousList(request, listName);
        if (null == resultList) {
            log.error("-> Execute Fantabulous list [listName=" + listName + "] FAIL");
            return new ArrayList<Map>();
        }
        return getData(resultList);
    }

    public void setModule(String module) {
        this.module = module;
    }
}

