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
 * Date: Oct 11, 2005
 * Time: 6:34:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierReportAction extends ReportAction {
    private Log log = LogFactory.getLog(SupplierReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("--- SupplierReportAction      execute  ....");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        String price1, price2, discount1, discount2;
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

            if (request.getParameter("parameter(generate)") != null) {
                return super.execute(mapping, searchForm, request, response);
            } else {
                return mapping.findForward("Success");
            }
        }
    }
}
