package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class ReportJrxmlExternalExecuteAction extends ReportJrxmlExecuteAction {


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.debug("Executing ReportJrxmlExternalExecuteAction............");
        //validate report id
        ActionErrors errors = new ActionErrors();
        if (null != request.getParameter("reportId")) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_REPORT, "reportid",
                    request.getParameter("reportId"), errors, new ActionError("Report.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
                return mapping.findForward("Fail");
            }
        } else {
            errors.add("reportid", new ActionError("Report.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail");
        }

        return processReport(mapping, form, request, response);

    }
}