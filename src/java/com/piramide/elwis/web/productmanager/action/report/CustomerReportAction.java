package com.piramide.elwis.web.productmanager.action.report;

import com.piramide.elwis.web.common.action.report.ReportAction;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: yumi
 * Date: Oct 12, 2005
 * Time: 11:05:54 AM
 * To change this template use File | Settings | File Templates.
 */

public class CustomerReportAction extends ReportAction {
    private Log log = LogFactory.getLog(CustomerReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("--- CustomerReportAction      execute  ....");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        String amount1, amount2;
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("parameter(generate)") != null) {
            errors = searchForm.validate(mapping, request);
            saveErrors(request, errors);
        }


        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        } else {
            if ("1".equals(request.getParameter("parameter(isActive)"))) {
                addFilter("active", "1");
            }
            if ("2".equals(request.getParameter("parameter(isActive)"))) {
                addFilter("active", "0");
            }

            if (request.getParameter("parameter(amount1)") != null) {
                amount1 = searchForm.getParameter("amount1").toString();
                searchForm.getParams().put("amount1", null);
                searchForm.setParameter("amount1", amount1);
            }
            if (request.getParameter("parameter(amount2)") != null) {
                amount2 = searchForm.getParameter("amount2").toString();
                searchForm.getParams().put("amount2", null);
                searchForm.setParameter("amount2", amount2);
            }
        }

        if (request.getParameter("parameter(generate)") != null) {
            return super.execute(mapping, searchForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}

