package com.piramide.elwis.web.bmapp.action.scheduler;

import com.piramide.elwis.web.bmapp.el.Functions;
import com.piramide.elwis.web.schedulermanager.action.AppointmentListAction;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.7
 */
public class AppointmentListRESTAction extends AppointmentListAction {

    @Override
    protected Integer getRowsPerPage(HttpServletRequest request) {
        //default for REST list
        return Functions.getFantabulousRowsPerPage();
    }
}
