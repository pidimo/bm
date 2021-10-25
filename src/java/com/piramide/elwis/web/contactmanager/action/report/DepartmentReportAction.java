package com.piramide.elwis.web.contactmanager.action.report;

import com.piramide.elwis.web.common.action.report.ReportAction;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Oct 21, 2005
 * Time: 2:52:40 PM
 * To change this template use File | Settings | File Templates.
 */

public class DepartmentReportAction extends ReportAction {
    private Log log = LogFactory.getLog(DepartmentReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--- DepartmentReportAction      execute  ....");

        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        searchForm.getParams().put("address_Id", request.getParameter("parameter(addressId)"));

        if (request.getParameter("parameter(generate)") != null) {
            return super.execute(mapping, searchForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}

