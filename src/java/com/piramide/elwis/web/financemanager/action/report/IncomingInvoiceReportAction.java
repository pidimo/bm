package com.piramide.elwis.web.financemanager.action.report;

import com.jatun.titus.reportgenerator.util.ReportData;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.jatun.titus.web.util.ReportVariable;
import com.jatun.titus.web.util.SubReportInfo;
import com.jatun.titus.web.util.SubReportParameter;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.ReportActionUtil;
import com.piramide.elwis.web.common.util.ReportTemplateUtil;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.controller.SearchParameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingInvoiceReportAction.java 25-feb-2009 21:55:18
 */
public class IncomingInvoiceReportAction extends ReportAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing IncomingInvoiceReportAction.... " + request.getParameterMap());
        super.initializeFilter();
        ReportGeneratorForm reportGeneratorForm = (ReportGeneratorForm) form;
        ActionErrors errors = reportGeneratorForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Success");
        }
        ActionForward actionForward = null;

        if (reportGeneratorForm.getParams().get("generate") != null) {
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
            parameters.addSearchParameters(getParameters(reportGeneratorForm.getParams()));
            parameters.addSearchParameters(getParameters(getStaticFilter()));
            parameters.addSearchParameters(getParameters(getFilter()));

            String reportFormat = reportGeneratorForm.getReportFormat();
            String reportPageSize = reportGeneratorForm.getReportPageSize();
            String reportPageOrientation = ReportGeneratorConstants.PAGE_ORIENTATION_LANDSCAPE;
            String tempDirectory = reportActionUtil.getTempDirectory();

            HashMap reportParams = reportActionUtil.getReportParams(timeZone.toString(), locale.getLanguage(), reportGeneratorForm);
            //invoice summary subreport
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
            subReportInfo.getSubReportParameters().add(new SubReportParameter("SUMMARYTITLE_PARAM", "java.lang.String", "\"" + JSPHelper.getMessage(request, "Invoice.report.invoiceSummary") + "\""));
            subReportInfo.setShowWhenExpression("$V{invoiceTotalVar}.isEmpty() ? Boolean.FALSE : Boolean.TRUE");
            reportData.addSubreport("SUBREPORT_INVOICESUMMARY", subReportInfo);


            //credit note summary subreport
            //remove before parameter inserted
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
            subReportInfo1.getSubReportParameters().add(new SubReportParameter("SUMMARYTITLE_PARAM", "java.lang.String", "\"" + JSPHelper.getMessage(request, "Invoice.report.creditNoteSummary") + "\""));
            subReportInfo1.setShowWhenExpression("$V{creditNoteTotalVar}.isEmpty() ? Boolean.FALSE : Boolean.TRUE");
            reportData.addSubreport("SUBREPORT_CREDITNOTESUMMARY", subReportInfo1);
            //add range date param

            //Subtitle
            StringBuffer subTitle = new StringBuffer();
            String invoiceDateString = composeInvoiceRangeDate(reportGeneratorForm, request, "startInvoiceDate", "endInvoiceDate", "Finance.incomingInvoice.invoiceDate");
            String receiptDate = composeInvoiceRangeDate(reportGeneratorForm, request, "startReceiptDate", "endReceiptDate", "Finance.incomingInvoice.receiptDate");
            String paidUntilDate = composeInvoiceRangeDate(reportGeneratorForm, request, "startPaidUntilDate", "endPaidUntilDate", "Finance.incomingInvoice.paidUntil");

            subTitle.append(invoiceDateString);
            if (subTitle.length() > 0 && receiptDate.length() > 0) {
                subTitle.append("\"+\"\\n\"+\"");
            }
            subTitle.append(receiptDate);
            if (subTitle.length() > 0 && paidUntilDate.length() > 0) {
                subTitle.append("\"+\"\\n\"+\"");
            }
            subTitle.append(paidUntilDate);
            //Today date
            if (subTitle.length() > 0) {
                subTitle.append("\"+\"\\n\"+\"");
            }
            subTitle.append(" (")
                    .append(JSPHelper.getMessage(request, "Common.today"))
                    .append(" ")
                    .append(DateUtils.parseDate(new Date(), JSPHelper.getMessage(request, "datePattern")))
                    .append(")");
            reportData.getReportConfigParams().put("REPORT_SUBTITLE", subTitle.toString());
            actionForward = super.execute(mapping, reportGeneratorForm, request, response);
        } else {
            actionForward = mapping.findForward("Success");
        }

        return (actionForward);
    }

    private String composeInvoiceRangeDate(ReportGeneratorForm reportGeneratorForm, HttpServletRequest request,
                                           String startDateParamName, String endDateParamName,
                                           String resource) {
        StringBuffer resultStr = new StringBuffer();
        String datePattern = JSPHelper.getMessage(request, "datePattern");
        String from = JSPHelper.getMessage(request, "Common.from");
        String to = JSPHelper.getMessage(request, "Common.to");

        String startDate = (String) reportGeneratorForm.getParameter(startDateParamName);
        String endDate = (String) reportGeneratorForm.getParameter(endDateParamName);
        if (!GenericValidator.isBlankOrNull(startDate)) {
            Date date = DateUtils.integerToDate(new Integer(startDate));
            resultStr.append(from).append(" ").append(DateUtils.parseDate(date, datePattern)).append(" ");
        }

        if (!GenericValidator.isBlankOrNull(endDate)) {
            Date date = DateUtils.integerToDate(new Integer(endDate));
            resultStr.append(to).append(" ").append(DateUtils.parseDate(date, datePattern));
        }
        String result = "";
        if (resultStr.length() > 0) {
            result = JSPHelper.getMessage(request, resource) + " ";
        }

        result = result + resultStr.toString();
        return result;
    }

    /**
     * Resize main template
     *
     * @param templateInputStream
     * @param pageSize
     * @param pageOrientation
     * @return InputStream
     * @throws Exception
     */
    private InputStream resizeTemplate(InputStream templateInputStream, String pageSize, String pageOrientation) throws Exception {
        ReportTemplateUtil templateUtil = new ReportTemplateUtil();

        //add move elements

        templateUtil.addMoveElementKey(ReportTemplateUtil.PAGEFOOTER_BAND, "PAGE_FOOTER_NUMBER");

        //add resize elements
        templateUtil.addResizeElementKey(ReportTemplateUtil.TITLE_BAND, "TITLE_REPORT_ELEMENT");
        templateUtil.addResizeElementKey(ReportTemplateUtil.TITLE_BAND, "rangeDate_text1");
        templateUtil.addResizeElementKey(ReportTemplateUtil.TITLE_BAND, "rangeDate_text2");
        templateUtil.addResizeElementKey(ReportTemplateUtil.TITLE_BAND, "rangeDate_text3");
        templateUtil.addResizeElementKey(ReportTemplateUtil.PAGEFOOTER_BAND, "PAGE_FOOTER_LINE");

        //add table resize
        ReportTemplateUtil.TemplateTableColumn tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "invoiceNumberHeader");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "invoiceNumberField");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "supplierHeader");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "supplierField");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "invoiceDateHeader");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "invoiceDateField");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "paidUntilHeader");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "paidUntilField");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "typeHeader");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "typeField");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "currencyHeader");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "currencyField");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "amountGrossHeader");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "amountGrossField");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "amountNetHeader");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "amountNetField");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "openAmountHeader");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "openAmountField");
        templateUtil.addResizeTableColumn(tableColumn);
        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "vatAmountHeader");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "vatAmountField");
        templateUtil.addResizeTableColumn(tableColumn);

        return templateUtil.resizeTemplate(templateInputStream, pageSize, pageOrientation);
    }
}
/**
 super.initializeFilter();
 ReportGeneratorForm searchForm = (ReportGeneratorForm) form;
 ActionErrors errors = searchForm.validate(mapping, request);

 if (!errors.isEmpty()) {
 saveErrors(request, errors);
 return mapping.findForward("Success");
 }

 ActionForward actionForward=null;

 if (searchForm.getParams().get("generate") != null) {

 ReportActionUtil reportActionUtil = new ReportActionUtil();

 User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
 Locale locale = new Locale(user.getValue("locale").toString());
 DateTimeZone timeZone = (DateTimeZone) user.getValueMap().get("dateTimeZone");
 ResourceBundle resourceBundle = reportActionUtil.getResourceBundle(locale.toString());
 Integer companyId = new Integer(user.getValue(Constants.COMPANYID).toString());

 //Fantabulous list parameters
 Parameters parameters = new Parameters();
 parameters.addSearchParameter(Constants.COMPANYID, companyId.toString());
 parameters.addSearchParameter(Constants.USERID, user.getValue(Constants.USERID).toString());
 parameters.addSearchParameters(getParameters(searchForm.getParams()));
 parameters.addSearchParameters(getParameters(getStaticFilter()));
 parameters.addSearchParameters(getParameters(getFilter()));


 String reportFormat = searchForm.getReportFormat();
 String reportPageSize = searchForm.getReportPageSize();
 String reportPageOrientation = ReportGeneratorConstants.PAGE_ORIENTATION_LANDSCAPE;
 String tempDirectory = reportActionUtil.getTempDirectory();
 HashMap<String, ReportData> subreports = new HashMap<String, ReportData>();

 HashMap reportParams = reportActionUtil.getReportParams(timeZone.toString(), locale.getLanguage(), searchForm);

 //invoice summary subreport
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
 subreports.put("SUBREPORT_INVOICESUMMARY", subReportInvoiceSummaryData);

 //credit note summary subreport
 //remove before parameter inserted
 SearchParameter searchParameter= parameters.getSearchParameter("summaryType");
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
 subreports.put("SUBREPORT_CREDITNOTESUMMARY", subReportCreditSummaryData);

 //add range date param
 reportParams.put("RANGEDATE_INVOICE_PARAM", composeInvoiceRangeDate(searchForm, request,"startInvoiceDate","endInvoiceDate", "Finance.incomingInvoice.invoiceDate"));
 reportParams.put("RANGEDATE_RECEIPT_PARAM", composeInvoiceRangeDate(searchForm, request,"startReceiptDate","endReceiptDate", "Finance.incomingInvoice.receiptDate"));
 reportParams.put("RANGEDATE_PAIDUNTIL_PARAM", composeInvoiceRangeDate(searchForm, request,"startPaidUntilDate","endPaidUntilDate", "Finance.incomingInvoice.paidUntil"));

 //MasterReport
 InputStream templateInputStream = request.getSession().getServletContext().getResourceAsStream(
 "/common/finance/jaspertemplates/IncomingInvoiceReportTemplate.jrxml");

 //resize template if is necessary
 templateInputStream = resizeTemplate(templateInputStream, reportPageSize, reportPageOrientation);

 HashMap reportConfigParams = reportActionUtil.getReportConfig(templateInputStream, reportFormat,
 tempDirectory, reportPageSize,
 locale.toString(), reportPageOrientation,
 "IncomingInvoice_report", false,
 resourceBundle);


 //Loading the company logo image..
 HashMap companyLogoInfo=reportActionUtil.getCompanLogoInfo(request,true);

 reportParams.put("IS_TITLE_IMAGE_STREAM", companyLogoInfo.get("IS_TITLE_IMAGE_STREAM"));
 reportParams.put("TITLE_IMAGE_STREAM", companyLogoInfo.get("TITLE_IMAGE_STREAM"));
 if(new Boolean(companyLogoInfo.get("IS_TITLE_IMAGE_STREAM").toString())){
 reportConfigParams.put("TITLE_IMAGE_WIDTH", companyLogoInfo.get("TITLE_IMAGE_WIDTH"));
 reportConfigParams.put("TITLE_IMAGE_HEIGHT", companyLogoInfo.get("TITLE_IMAGE_HEIGHT"));
 }

 reportActionUtil.generateReport(getServlet(),
 request, response,
 reportConfigParams, reportParams,
 "incomingInvoiceReportList", parameters,
 subreports);
 //End master report


 } else {
 actionForward = mapping.findForward("Success");
 }
 return (actionForward);
 */
