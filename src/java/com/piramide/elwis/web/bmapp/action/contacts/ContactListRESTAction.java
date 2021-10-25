package com.piramide.elwis.web.bmapp.action.contacts;

import com.piramide.elwis.web.bmapp.el.Functions;
import com.piramide.elwis.web.contactmanager.action.ContactListAction;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public class ContactListRESTAction extends ContactListAction {

    @Override
    protected Integer getRowsPerPage(HttpServletRequest request) {
        return Functions.getFantabulousRowsPerPage();
    }

}
