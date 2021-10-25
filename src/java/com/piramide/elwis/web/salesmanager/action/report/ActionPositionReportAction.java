package com.piramide.elwis.web.salesmanager.action.report;

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
 * Date: Oct 20, 2005
 * Time: 11:18:23 AM
 * To change this template use File | Settings | File Templates.
 */

public class ActionPositionReportAction extends ReportAction {
    private Log log = LogFactory.getLog(ActionPositionReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--- ActionPositionReportAction      execute  ....");

        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        ActionErrors errors = new ActionErrors();
        String price1, price2, amount1, amount2, totalPrice1, totalPrice2 = "";

        if (request.getParameter("parameter(generate)") != null) {
            errors = searchForm.validate(mapping, request); // validate
        }
        saveErrors(request, errors);

        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        } else {
            if (request.getParameter("parameter(price1)") != null) {
                price1 = searchForm.getParameter("price1").toString();
                searchForm.getParams().put("price1", null);
                searchForm.setParameter("price1", price1);
            }
            if (request.getParameter("parameter(price2)") != null) {
                price2 = searchForm.getParameter("price2").toString();
                searchForm.getParams().put("price2", null);
                searchForm.setParameter("price2", price2);
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
            if (request.getParameter("parameter(totalPrice1)") != null) {
                totalPrice1 = searchForm.getParameter("totalPrice1").toString();
                searchForm.getParams().put("totalPrice1", null);
                searchForm.setParameter("totalPrice1", totalPrice1);
            }
            if (request.getParameter("parameter(totalPrice2)") != null) {
                totalPrice2 = searchForm.getParameter("totalPrice2").toString();
                searchForm.getParams().put("totalPrice2", null);
                searchForm.setParameter("totalPrice2", totalPrice2);
            }
        }
        if (request.getParameter("parameter(generate)") != null) {
            return super.execute(mapping, searchForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}