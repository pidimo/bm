package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.web.reports.el.ReportSettingUtil;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 2.9
 */
public class ReportExecuteForwardAction extends ReportManagerAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        setDefaultReportSettings(form, request);
        return super.execute(mapping, form, request, response);
    }

    private void setDefaultReportSettings(ActionForm form, HttpServletRequest request) {
        DefaultForm executeForm = (DefaultForm) form;

        String csvDelimiter = ReportSettingUtil.readUserCsvDelimiterConfig(request);
        executeForm.setDto("csvDelimiter", csvDelimiter);
        executeForm.setDto("reportCharset", ReportSettingUtil.readUserReportCharsetConfig(request));
    }
}
