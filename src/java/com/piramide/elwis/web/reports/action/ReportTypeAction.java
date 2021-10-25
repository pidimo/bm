package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.cmd.reports.LightlyReportCmd;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 * @version : $Id ReportTypeAction ${time}
 */
public class ReportTypeAction extends ReportManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward = new ActionForward();


        ActionErrors errors = new ActionErrors();

        LightlyReportCmd reportCmd = new LightlyReportCmd();
        reportCmd.putParam("reportId", Integer.valueOf(request.getParameter("reportId")));
        reportCmd.setOp("read");

        ResultDTO resultDTO = BusinessDelegate.i.execute(reportCmd, request);

        if (!resultDTO.isFailure()) {
            Integer actualReportType = Integer.valueOf(request.getParameter("reportType"));
            Integer entityReportType = (Integer) resultDTO.get("reportType");

            if (!actualReportType.equals(entityReportType)) {
                request.setAttribute("reportType", entityReportType.toString());

                if (ReportConstants.SUMMARY_TYPE.equals(entityReportType) ||
                        ReportConstants.MATRIX_TYPE.equals(entityReportType)) {

                    errors.add("reportTypeId", new ActionError("Common.error.concurrency"));
                    saveErrors(request, errors);
                    return mapping.findForward("Fail");
                } else {

                    errors.add("reportTypeId", new ActionError("ReportType.changed"));
                    saveErrors(request, errors);
                    return mapping.findForward("Detail");

                }
            }
        }

        forward = super.execute(mapping, form, request, response);

        return forward;
    }
}
