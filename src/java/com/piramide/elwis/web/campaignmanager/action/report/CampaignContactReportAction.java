package com.piramide.elwis.web.campaignmanager.action.report;

import com.piramide.elwis.web.common.action.report.ReportAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: yumi
 * Date: 14-nov-2006
 * Time: 9:45:24
 * To change this template use File | Settings | File Templates.
 */

public class CampaignContactReportAction extends ReportAction {

    private Log log = LogFactory.getLog(CampaignContactReportAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("    ...  CampaignContactReportAction  execute    ...    ");
        super.initializeFilter();
        ActionForward forward = mapping.findForward("Success");
        if (request.getParameter("parameter(generate)") != null) {
            forward = super.execute(mapping, form, request, response);
        }
        return forward;
    }
}