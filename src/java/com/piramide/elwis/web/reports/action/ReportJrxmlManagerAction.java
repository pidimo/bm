package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class ReportJrxmlManagerAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Executing ReportJrxmlManagerAction................" + request.getParameterMap());

        ActionErrors errors = new ActionErrors();

        if (null != request.getParameter("reportId")) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_REPORT, "reportid",
                    request.getParameter("reportId"), errors, new ActionError("Report.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("ReportMainSearch");
            }

        } else {
            errors.add("reportid", new ActionError("Report.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("ReportMainSearch");
        }


        return super.execute(mapping, form, request, response);
    }

}
