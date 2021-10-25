package com.piramide.elwis.web.productmanager.action.report;

import com.piramide.elwis.utils.Constants;
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
 * Time: 5:51:15 PM
 * To change this template use File | Settings | File Templates.
 */

public class SupplierContractReportAction extends ReportAction {
    private Log log = LogFactory.getLog(ProductReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("--- SupplierContractReportAction  Supplier/Customer fucntionality    execute  ....");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        ActionErrors errors = new ActionErrors();
        String price1, price2, sellOpenAmount1, sellOpenAmount2, discount1, discount2, installment1, installment2 = "";


        if ("0".equals(request.getParameter("parameter(payMethod)"))) {
            request.setAttribute("all", Constants.TRUE_VALUE);
        } else if ("1".equals(request.getParameter("parameter(payMethod)"))) {
            request.setAttribute("facturation", Constants.TRUE_VALUE);
        } else if ("2".equals(request.getParameter("parameter(payMethod)"))) {
            request.setAttribute("instalation", Constants.TRUE_VALUE);
        } else {
            request.setAttribute("all", Constants.TRUE_VALUE);
        }

        if (request.getParameter("parameter(generate)") != null) {
            errors = searchForm.validate(mapping, request);
            saveErrors(request, errors);
        }

        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        }

        if (request.getParameter("parameter(generate)") != null && errors.isEmpty()) {

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
            if (request.getParameter("parameter(openAmount1)") != null) {
                sellOpenAmount1 = searchForm.getParameter("openAmount1").toString();
                searchForm.getParams().put("openAmount1", null);
                searchForm.getParams().put("openAmount1", sellOpenAmount1);
            }
            if (request.getParameter("parameter(openAmount2)") != null) {
                sellOpenAmount2 = searchForm.getParameter("openAmount2").toString();
                searchForm.getParams().put("openAmount2", null);
                searchForm.getParams().put("openAmount2", sellOpenAmount2);
            }
            if (request.getParameter("parameter(discount1)") != null) {
                discount1 = searchForm.getParameter("discount1").toString();
                searchForm.getParams().put("discount1", null);
                searchForm.getParams().put("discount1", discount1);
            }
            if (request.getParameter("parameter(discount2)") != null) {
                discount2 = searchForm.getParameter("discount2").toString();
                searchForm.getParams().put("discount2", null);
                searchForm.getParams().put("discount2", discount2);
            }
            if (request.getParameter("parameter(instalation1)") != null) {
                installment1 = searchForm.getParameter("instalation1").toString();
                searchForm.getParams().put("instalation1", null);
                searchForm.getParams().put("instalation1", installment1);
            }
            if (request.getParameter("parameter(instalation2)") != null) {
                installment2 = searchForm.getParameter("instalation2").toString();
                searchForm.getParams().put("instalation2", null);
                searchForm.getParams().put("instalation2", installment2);
            }
        }

        if (request.getParameter("parameter(generate)") != null) {
            return super.execute(mapping, searchForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}

