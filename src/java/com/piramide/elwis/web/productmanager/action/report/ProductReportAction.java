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
 * Date: Oct 10, 2005
 * Time: 6:16:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class ProductReportAction extends ReportAction {
    private Log log = LogFactory.getLog(ProductReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("--- ProductReportAction      execute  ....");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        ActionErrors errors = new ActionErrors();
        errors = searchForm.validate(mapping, request);
        saveErrors(request, errors);
        String price1, price2, priceGross1, priceGross2 = "";

        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
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
        if (request.getParameter("parameter(priceGross1)") != null) {
            priceGross1 = searchForm.getParameter("priceGross1").toString();
            searchForm.getParams().put("priceGross1", null);
            searchForm.getParams().put("priceGross1", priceGross1);
        }
        if (request.getParameter("parameter(priceGross2)") != null) {
            priceGross2 = searchForm.getParameter("priceGross2").toString();
            searchForm.getParams().put("priceGross2", null);
            searchForm.getParams().put("priceGross2", priceGross2);
        }

        if (request.getParameter("parameter(generate)") != null) {
            return super.execute(mapping, searchForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}
