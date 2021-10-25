package com.piramide.elwis.web.admin.action.report;

import com.piramide.elwis.web.common.action.report.ReportAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 * @version : $Id AdminReportAction ${time}
 */
public class AdminReportAction extends ReportAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        return super.execute(mapping, form, request, response);
    }
}
