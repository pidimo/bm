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
 * Date: Oct 14, 2005
 * Time: 11:18:56 AM
 * To change this template use File | Settings | File Templates.
 */

public class CompetitorReportAction extends ReportAction {
    private Log log = LogFactory.getLog(CompetitorReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("--- CompetitorReportAction      execute  ....");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        String amount1, amount2;
        ActionErrors errors = new ActionErrors();


        errors = searchForm.validate(mapping, request);
        saveErrors(request, errors);

        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        } else {
            if (request.getParameter("parameter(price1)") != null) {
                amount1 = searchForm.getParameter("price1").toString();
                searchForm.getParams().put("price1", null);
                searchForm.setParameter("price1", amount1);
            }
            if (request.getParameter("parameter(price2)") != null) {
                amount2 = searchForm.getParameter("price2").toString();
                searchForm.getParams().put("price2", null);
                searchForm.setParameter("price2", amount2);
            }
        }

        if (request.getParameter("parameter(generate)") != null) {
            return super.execute(mapping, searchForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}


