package com.piramide.elwis.web.financemanager.action.report;

import com.jatun.titus.reportgenerator.util.ReportData;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.jatun.titus.web.util.ReportVariable;
import com.jatun.titus.web.util.SubReportInfo;
import com.jatun.titus.web.util.SubReportParameter;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.ReportActionUtil;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.controller.SearchParameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingInvoiceHibridReportAction.java 09-abr-2009 12:20:29
 */
public class IncomingInvoiceHibridReportAction extends ReportAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing IncomingInvoiceReportAction.... " + request.getParameterMap());

        super.initializeFilter();
        ReportGeneratorForm searchForm = (ReportGeneratorForm) form;
        ActionErrors errors = searchForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Success");
        }
        ActionForward actionForward = null;

        if (searchForm.getParams().get("generate") != null) {
            //*****************Variables.....
            ArrayList<ReportVariable> variablesList = new ArrayList<ReportVariable>();
            variablesList.add(new ReportVariable("invoiceTotalVar", ReportGeneratorConstants.RESET_TYPE_REPORT,
                    ReportGeneratorConstants.RESET_TYPE_REPORT, ReportGeneratorConstants.CALCULATION_SYSTEM,
                    "new java.util.LinkedHashMap()",
                    "java.util.Map"));

            variablesList.add(new ReportVariable("creditNoteTotalVar", ReportGeneratorConstants.RESET_TYPE_REPORT,
                    ReportGeneratorConstants.RESET_TYPE_REPORT, ReportGeneratorConstants.CALCULATION_SYSTEM,
                    "new java.util.LinkedHashMap()",
                    "java.util.Map"));
            reportData.setVariablesList(variablesList);
            //******************Scriptlet...
            reportData.setScriptletClass("com.piramide.elwis.web.financemanager.action.report.scriptlet.IncomingInvoiceAmountsScriptlet");
            //********************Subreports
            ReportActionUtil reportActionUtil = new ReportActionUtil();
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            Locale locale = new Locale(user.getValue("locale").toString());
            DateTimeZone timeZone = (DateTimeZone) user.getValueMap().get("dateTimeZone");
            ResourceBundle resourceBundle = reportActionUtil.getResourceBundle(locale.toString());

            Parameters parameters = new Parameters();
            parameters.addSearchParameter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());
            parameters.addSearchParameter(Constants.USERID, user.getValue(Constants.USERID).toString());
            parameters.addSearchParameters(getParameters(searchForm.getParams()));
            parameters.addSearchParameters(getParameters(getStaticFilter()));
            parameters.addSearchParameters(getParameters(getFilter()));


            String reportFormat = searchForm.getReportFormat();
            String reportPageSize = searchForm.getReportPageSize();
            String reportPageOrientation = ReportGeneratorConstants.PAGE_ORIENTATION_LANDSCAPE;
            String tempDirectory = reportActionUtil.getTempDirectory();

            HashMap reportParams = reportActionUtil.getReportParams(timeZone.toString(), locale.getLanguage(), searchForm);
            //For invoiceSummary
            parameters.addSearchParameter("summaryType", FinanceConstants.InvoiceType.Invoice.getConstantAsString());
            InputStream templateInputStreamInvoiceSummary = request.getSession().getServletContext().getResourceAsStream(
                    "/common/finance/jaspertemplates/IncomingInvoiceSummarySubReport.jrxml");

            HashMap subReportInvoiceSummaryConfigParams = reportActionUtil.getReportConfig(templateInputStreamInvoiceSummary, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_INVOICESUMMARY", true,
                    resourceBundle);
            ReportData subReportInvoiceSummaryData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportInvoiceSummaryConfigParams, reportParams,
                    "incomingInvoiceSummaryReportList", parameters);
            SubReportInfo subReportInfo = new SubReportInfo("SUBREPORT_INVOICESUMMARY", subReportInvoiceSummaryData.getJasperReport(),
                    ReportGeneratorConstants.BAND_SUMMARY, 0, -1, 500, 10);
            subReportInfo.getSubReportParameters().add(new SubReportParameter("SUMMARYTOTAL_PARAM", "java.util.LinkedHashMap", "$V{invoiceTotalVar}"));
            subReportInfo.getSubReportParameters().add(new SubReportParameter("SUMMARYTITLE_PARAM", "java.lang.String", "\"BURRONOKE..\""));
            reportData.addSubreport("SUBREPORT_INVOICESUMMARY", subReportInfo);


            //For CreditNoteSummary
            SearchParameter searchParameter = parameters.getSearchParameter("summaryType");
            if (parameters.getSearchParameters().contains(searchParameter)) {
                parameters.getSearchParameters().remove(searchParameter);
            }
            parameters.addSearchParameter("summaryType", FinanceConstants.InvoiceType.CreditNote.getConstantAsString());

            InputStream templateInputStreamCreditSummary = request.getSession().getServletContext().getResourceAsStream(
                    "/common/finance/jaspertemplates/IncomingInvoiceSummarySubReport.jrxml");
            HashMap subReportCreditSummaryConfigParams = reportActionUtil.getReportConfig(templateInputStreamCreditSummary, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CREDITNOTESUMMARY", true,
                    resourceBundle);
            ReportData subReportCreditSummaryData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportCreditSummaryConfigParams, reportParams,
                    "incomingInvoiceSummaryReportList", parameters);

            SubReportInfo subReportInfo1 = new SubReportInfo("SUBREPORT_CREDITNOTESUMMARY", subReportCreditSummaryData.getJasperReport(),
                    ReportGeneratorConstants.BAND_SUMMARY, 0, -1, 500, 10);
            subReportInfo1.getSubReportParameters().add(new SubReportParameter("SUMMARYTOTAL_PARAM", "java.util.LinkedHashMap", "$V{creditNoteTotalVar}"));
            subReportInfo1.getSubReportParameters().add(new SubReportParameter("SUMMARYTITLE_PARAM", "java.lang.String", "$R{Invoice.report.creditNoteSummary}"));
            reportData.addSubreport("SUBREPORT_CREDITNOTESUMMARY", subReportInfo1);

        }


        return (super.execute(mapping, form, request, response));
    }
}