package com.piramide.elwis.web.bmapp.action;

import com.piramide.elwis.web.bmapp.el.Functions;
import com.piramide.elwis.web.common.action.ListAction;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class ListRESTAction extends ListAction {

    @Override
    protected Integer getRowsPerPage(HttpServletRequest request) {
        //default for REST list
        return Functions.getFantabulousRowsPerPage();
    }
}