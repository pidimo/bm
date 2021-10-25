package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * * Jatun S.R.L.
 * To be used in other modules because in not every module there is a "MainSearch" global
 * forward this action returns Fail forward if no report is found.
 *
 * @author : Fernando
 * @version : $Id ReportExternalManagerAction.java
 */
public class ReportExternalManagerAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

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

        ActionForward actionForward = super.execute(mapping, form, request, response);
        ActionForwardParameters reportParameters = new ActionForwardParameters();
        Map dtoMap = ((DefaultForm) form).getDtoMap();
        if (dtoMap != null) {
            if (dtoMap.containsKey("pageSize") && dtoMap.get("pageSize") != null) {
                reportParameters.add("dto(pageSize)", dtoMap.get("pageSize").toString());
            }

            if (dtoMap.containsKey("pageOrientation") && dtoMap.get("pageOrientation") != null) {
                reportParameters.add("dto(pageOrientation)", dtoMap.get("pageOrientation").toString());
            }

            if (dtoMap.containsKey("reportFormat") && dtoMap.get("reportFormat") != null) {
                reportParameters.add("dto(reportFormat)", dtoMap.get("reportFormat").toString());
            }
            actionForward = reportParameters.forward(actionForward);
        }
        return actionForward;
    }

}