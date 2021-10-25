package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.salesmanager.el.Functions;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SaleManagerForwardActon extends DefaultForwardAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionErrors errors = Functions.existsSale(request);
        if (null != errors) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("SaleList");
        }

        return null;
    }
}
