package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.web.reports.form.ChartForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Dec 8, 2005
 * Time: 9:33:19 AM
 * To change this template use File | Settings | File Templates.
 */

public class ChartAction extends ReportManagerAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ChartAction................");

        ActionForward forward;
        ActionErrors errors = new ActionErrors();
        ChartForm chartForm = (ChartForm) form;
        if (chartForm.getDto("delete") != null) {
            log.debug("chart delete confirmation....");
            return mapping.findForward("Confirmation");
        }

        forward = super.execute(mapping, form, request, response);

        return forward;
    }
}

