package com.piramide.elwis.web.financemanager.action.report;

import com.jatun.titus.web.form.ReportGeneratorForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class CustomCSVExportReportForwardAction extends ForwardAction {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing CustomCSVExportReportForwardAction.... " );

        //define exported property as default
        ReportGeneratorForm searchForm = (ReportGeneratorForm) form;
        searchForm.setParameter("exported", Boolean.TRUE);

        return super.execute(mapping, form, request, response);
    }
}
