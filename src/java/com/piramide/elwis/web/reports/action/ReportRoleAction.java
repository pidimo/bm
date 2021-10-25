package com.piramide.elwis.web.reports.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class ReportRoleAction extends ReportManagerAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }
        return super.execute(mapping, form, request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
