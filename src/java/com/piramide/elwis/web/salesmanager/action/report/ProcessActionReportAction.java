package com.piramide.elwis.web.salesmanager.action.report;

import com.piramide.elwis.cmd.salesmanager.SalesProcessCmd;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.MessagesUtil;
import net.java.dev.strutsejb.web.BusinessDelegate;
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
 * Time: 11:18:05 AM
 * To change this template use File | Settings | File Templates.
 */

public class ProcessActionReportAction extends ReportAction {
    private Log log = LogFactory.getLog(ProcessActionReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--- ProcessActionReportAction      execute  ....");

        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        ActionErrors errors = new ActionErrors();
        String discount1, discount2 = "";

        if (request.getParameter("parameter(generate)") != null) {
            errors = searchForm.validate(mapping, request);
        } else if (request.getParameter("parameter(processId)") != null && !"".equals(request.getParameter("parameter(processId)"))) {
            SalesProcessCmd cmd = new SalesProcessCmd();
            cmd.putParam("op", "");
            cmd.putParam("processId", request.getParameter("parameter(processId)"));
            BusinessDelegate.i.execute(cmd, request);
            errors = MessagesUtil.i.convertToActionErrors(mapping, request, cmd.getResultDTO());
            searchForm.getParams().put("addressProcessId", cmd.getResultDTO().get("addressId"));
        }
        saveErrors(request, errors);

        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        } else if (request.getParameter("parameter(generate)") != null) {
            if ("1".equals(request.getParameter("parameter(isActive)"))) {
                searchForm.getParams().put("active", request.getParameter("parameter(isActive)"));
            } else if ("2".equals(request.getParameter("parameter(isActive)"))) {
                searchForm.getParams().put("active", "0");
            } else if ("0".equals(request.getParameter("parameter(isActive)"))) {
                searchForm.getParams().put("active", null);
            }

            if (request.getParameter("parameter(discount1)") != null) {
                discount1 = searchForm.getParameter("discount1").toString();
                searchForm.getParams().put("discount1", null);
                searchForm.setParameter("discount1", discount1);
            }
            if (request.getParameter("parameter(discount2)") != null) {
                discount2 = searchForm.getParameter("discount2").toString();
                searchForm.getParams().put("discount2", null);
                searchForm.setParameter("discount2", discount2);
            }
            return super.execute(mapping, searchForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}
