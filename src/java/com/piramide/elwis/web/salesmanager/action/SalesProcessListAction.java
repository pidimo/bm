package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Fernando Montaño
 * @version SalesProcessListAction.java, v 2.0 Aug 24, 2004 5:09:51 PM
 */
public class SalesProcessListAction extends com.piramide.elwis.web.common.action.ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if (null != (forward = validateElementExistence(request, mapping))) {
            return forward;
        }

        ((SearchForm) form).getParams().remove("processId");

        addFilter(request);
        return super.execute(mapping, form, request, response);
    }

    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionErrors errors = Functions.existSalesProcess(request);
        if (null != errors) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        return null;
    }

    protected void addFilter(HttpServletRequest request) {
        //adding module processId filter value.
        addFilter("processId", request.getParameter("processId"));
        setModuleId(request.getParameter("processId"));
    }
}
