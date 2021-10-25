package com.piramide.elwis.web.financemanager.action.report;

import com.jatun.titus.reportgenerator.util.ReportData;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class CustomCSVExportReportAction extends ReportAction {
    private Log log = LogFactory.getLog(this.getClass());
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CustomCSVExportReportAction.... " + request.getParameterMap());

        ActionForward actionForward = null;
        super.initializeFilter();

        ReportGeneratorForm searchForm = (ReportGeneratorForm) form;
        ActionErrors errors = new ActionErrors();

        if (isMasterValidation()) {
            errors = searchForm.validate(mapping, request);
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Success");
        }

        if (searchForm.getParams().get("generate") != null) {
            //add custom configuration
            setCustomCsvConfiguration(searchForm);
            actionForward = super.execute(mapping, searchForm, request, response);
        } else {
            actionForward = mapping.findForward("Success");
        }

        return (actionForward);
    }

    protected boolean isMasterValidation() {
        return true;
    }

    @Override
    protected String getFileName(ReportData reportData, HttpServletRequest request) {
        return "csvReportExport.txt";
    }

    private void setCustomCsvConfiguration(ReportGeneratorForm searchForm) {
        searchForm.setReportFormat(ReportGeneratorConstants.REPORT_FORMAT_CSV);
        searchForm.setReportPageSize(ReportGeneratorConstants.PAGE_CUSTOM);

        reportData.getReportConfigParams().put("csvFieldDelimiter", ";");
        reportData.getReportConfigParams().put("csvRecordDelimiter", "\r\n");
        //set ANSI character set
        reportData.getReportConfigParams().put("characterEncoding", "Windows-1252");
    }

    protected String composeRangeDate(HttpServletRequest request) {
        String rangeDateValue = "";
        String startDate = request.getParameter("parameter(startInvoiceDate)");
        String endDate = request.getParameter("parameter(endInvoiceDate)");

        if (startDate != null && startDate.length() > 0) {
            rangeDateValue = startDate;
        }

        if (endDate != null && endDate.length() > 0) {
            rangeDateValue = rangeDateValue + "-" + endDate;
        }
        return rangeDateValue;
    }

    protected String composeRangeDate(Integer startDate, Integer endDate, HttpServletRequest request) {
        String dateValue = "";
        String datePattern = JSPHelper.getMessage(request, "datePattern");

        if (startDate != null) {
            dateValue = DateUtils.parseDate(startDate, datePattern);
        }

        if (endDate != null) {
            dateValue = dateValue + "-" + DateUtils.parseDate(endDate, datePattern);
        }
        return dateValue;
    }

    protected Integer getSearchParameterAsInt(ReportGeneratorForm searchForm, String parameterKey) {
        Integer valueInteger = null;
        if (searchForm.getParameter(parameterKey) != null && !GenericValidator.isBlankOrNull(searchForm.getParameter(parameterKey).toString())) {
            valueInteger = Integer.valueOf(searchForm.getParameter(parameterKey).toString());
        }
        return valueInteger;
    }
}
