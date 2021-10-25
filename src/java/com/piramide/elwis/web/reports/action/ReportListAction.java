package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun s.r.l.
 *
 * @author miky
 * @version $Id: ReportListAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ReportListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ReportListAction......");

        //cheking if report was not deleted by other user.
        log.debug("reporId for user request= " + request.getParameter("reportId"));
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("reportId") != null) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_REPORT, "reportid",
                    request.getParameter("reportId"), errors, new ActionError("Report.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }
        } else { //no report selected
            errors.add("reportid", new ActionError("Report.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        addFilter("reportId", request.getParameter("reportId")); //adding module reportId filter value.
        setModuleId(request.getParameter("reportId"));

        return super.execute(mapping, form, request, response);
    }

}
