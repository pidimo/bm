package com.piramide.elwis.web.bmapp.action.webmail;

import com.piramide.elwis.web.bmapp.el.Functions;
import com.piramide.elwis.web.webmail.action.MailTrayAction;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class MailTrayRESTAction extends MailTrayAction {

    @Override
    protected Integer getRowsPerPage(HttpServletRequest request) {
        return Functions.getFantabulousRowsPerPage();
    }
}
