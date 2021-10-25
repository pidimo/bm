package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.web.common.action.ListAction;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * add error of report not found in list
 *
 * @author Miky
 * @version $Id: AddErrorInReportListAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class AddErrorInReportListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing AddErrorInReportListAction......");

        ActionErrors errors = new ActionErrors();
        errors.add("report", new ActionError("Report.NotFound"));
        saveErrors(request.getSession(), errors);

        return super.execute(mapping, form, request, response);
    }
}
