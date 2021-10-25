package com.piramide.elwis.web.contactmanager.action.report;

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
 * Date: Oct 21, 2005
 * Time: 2:52:29 PM
 * To change this template use File | Settings | File Templates.
 */

public class CommunicationReportAction extends ReportAction {
    private Log log = LogFactory.getLog(CommunicationReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("---          CommunicationReportAction      execute  ....");

        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("parameter(generate)") != null) {
            errors = searchForm.validate(mapping, request); // validate
        }
        saveErrors(request, errors);
        searchForm.getParams().put("address_Id", request.getParameter("parameter(addressId)"));
        if ("1".equals(request.getParameter("parameter(in_Out)"))) {
            searchForm.getParams().put("inOut", "1");
        } else if ("2".equals(request.getParameter("parameter(in_Out)"))) {
            searchForm.getParams().put("inOut", "0");
        }
        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        }

        if (request.getParameter("parameter(generate)") != null) {
            return super.execute(mapping, searchForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}
