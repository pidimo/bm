package com.piramide.elwis.web.financemanager.action.report;

import com.jatun.titus.reportgenerator.util.ReportData;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class InvoicePartnerDataExportReportAction extends CustomCSVExportReportAction {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing InvoicePartnerDataExportReportAction.... " + request.getParameterMap());

        ReportGeneratorForm searchForm = (ReportGeneratorForm) form;
        ActionErrors errors = searchForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Success");
        }

        errors = exportDataValidation(searchForm, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        //add export type as param
        reportData.getParams().put("EXPORTDATATYPE_PARAM", InvoiceCSVExportReportUtil.EXPORTDATATYPE_PARTNER);
        return super.execute(mapping, form, request, response);
    }

    @Override
    protected boolean isMasterValidation() {
        return false;
    }

    @Override
    protected String getFileName(ReportData reportData, HttpServletRequest request) {
        String fileName = JSPHelper.getMessage(request, "Finance.report.invoicePartnerDataExport.fileName", composeRangeDate(request));
        return fileName.trim() + ".txt";
    }

    private ActionErrors exportDataValidation(ReportGeneratorForm searchForm, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        List<String> rulePartnerWithoutNumber = InvoiceCSVExportReportUtil.getSequenceRuleWithoutPartnerNumber(
                getSearchParameterAsInt(searchForm, "startInvoiceDate"),
                getSearchParameterAsInt(searchForm, "endInvoiceDate"),
                request);

        if (!rulePartnerWithoutNumber.isEmpty()) {
            errors.add("withoutNumber", new ActionError("InvoiceExport.error.msg"));
            request.setAttribute("sequenceRuleWithoutPartnerList", rulePartnerWithoutNumber);
        }
        return errors;
    }

}
