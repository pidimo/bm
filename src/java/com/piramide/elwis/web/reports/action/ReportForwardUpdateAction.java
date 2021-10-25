package com.piramide.elwis.web.reports.action;

import com.jatun.titus.listgenerator.view.TableTreeView;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ReportForwardUpdateAction.java 9695 2009-09-10 21:34:43Z fernando $
 */

public class ReportForwardUpdateAction extends ReportManagerAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ReportForwardUpdateAction................");

        ActionForward forward;
        forward = super.execute(mapping, form, request, response);

        if (forward != null && "Success".equals(forward.getName())) {
            TableTreeView.removeInstance(request.getParameter("reportId"), request.getSession());
        }
        return forward;
    }

}
