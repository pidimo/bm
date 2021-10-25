package com.piramide.elwis.web.financemanager.action.report;

import com.jatun.titus.reportgenerator.util.ReportData;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.jatun.titus.web.util.ReportVariable;
import com.jatun.titus.web.util.SubReportInfo;
import com.jatun.titus.web.util.SubReportParameter;
import com.piramide.elwis.cmd.salesmanager.ContractTypeCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.ReportActionUtil;
import com.piramide.elwis.web.common.util.ReportTemplateUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.controller.Parameters;
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
 * Action to generate report to contract to invoice
 *
 * @author Miky
 * @version $Id: ContractInvoiceReportAction.java 17-dic-2008 13:40:54 $
 */
public class ContractInvoiceReportAction extends ReportAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContractInvoiceReportAction.... " + request.getParameterMap());
        super.initializeFilter();
        ReportGeneratorForm searchForm = (ReportGeneratorForm) form;
        ActionErrors errors = searchForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Success");
        }

        ActionForward actionForward = null;

        if (searchForm.getParams().get("generate") != null) {

            ReportActionUtil reportActionUtil = new ReportActionUtil();

            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            Locale locale = new Locale(user.getValue("locale").toString());
            DateTimeZone timeZone = (DateTimeZone) user.getValueMap().get("dateTimeZone");
            ResourceBundle resourceBundle = reportActionUtil.getResourceBundle(locale.toString());
            Integer companyId = new Integer(user.getValue(Constants.COMPANYID).toString());

            String reportFormat = searchForm.getReportFormat();
            String reportPageSize = searchForm.getReportPageSize();
            String reportPageOrientation = ReportGeneratorConstants.PAGE_ORIENTATION_LANDSCAPE;
            String tempDirectory = reportActionUtil.getTempDirectory();

            HashMap reportParams = reportActionUtil.getReportParams(timeZone.toString(), locale.getLanguage(), searchForm);

            //add contract type filter and report parameter
            String contracTypeParameter = (String) searchForm.getParameter("contractTypeParam");
            String contractTypeNameParam = "CONTRACTTYPENAME_PARAM";
            reportParams.put(contractTypeNameParam, "");
            if (!GenericValidator.isBlankOrNull(contracTypeParameter)) {
                if (SalesConstants.CONTRACTS_TO_BE_INVOICED.equals(contracTypeParameter)) {
                    searchForm.getParams().put("toBeInvoiced", "1");//this field is boolean representation
                    reportParams.put(contractTypeNameParam, JSPHelper.getMessage(request, "ContractToInvoice.report.allToBeInvoiced"));

                } else if (SalesConstants.CONTRACTS_NOT_TO_BE_INVOICED.equals(contracTypeParameter)) {
                    searchForm.getParams().put("toBeInvoiced", "0");
                    reportParams.put(contractTypeNameParam, JSPHelper.getMessage(request, "ContractToInvoice.report.allNotToBeInvoiced"));

                } else {
                    searchForm.getParams().put("contractTypeId", contracTypeParameter);
                    reportParams.put(contractTypeNameParam, readContractTypeName(new Integer(contracTypeParameter)));
                }
            }

            //add evaluate filter, calculate as real or virtual income
            searchForm.getParams().put("calculateAsRealIncome", "");
            searchForm.getParams().put("calculateAsVirtualIncome", "");

            if (SalesConstants.ContractIncome.REAL_INCOME.equal((String) searchForm.getParameter("contractIncome"))) {
                searchForm.getParams().put("calculateAsRealIncome", "true");
            } else {
                searchForm.getParams().put("calculateAsVirtualIncome", "true");
            }

            //add filter, contracts only not invoiced
            if (searchForm.getParameter("withoutInvoiced") != null && !"".equals(searchForm.getParameter("withoutInvoiced").toString())) {
                searchForm.getParams().put("notInvoiced", "true");

                //add in report params, to sum total of only not invoiced
                reportParams.put("onlyNotInvoiced", "true");
            } else {
                reportParams.put("onlyNotInvoiced", "false");
            }

            //contract invoice summary subreport
            //Fantabulous list parameters
            Parameters parameters = new Parameters();
            parameters.addSearchParameter(Constants.COMPANYID, companyId.toString());
            parameters.addSearchParameter(Constants.USERID, user.getValue(Constants.USERID).toString());
            parameters.addSearchParameters(getParameters(searchForm.getParams()));
            parameters.addSearchParameters(getParameters(getStaticFilter()));
            parameters.addSearchParameters(getParameters(getFilter()));
            InputStream templateInputStreamContractInvoiceSummary = request.getSession().getServletContext().getResourceAsStream(
                    "/common/finance/jaspertemplates/ContractInvoiceSummarySubReport.jrxml");

            HashMap subReportContractInvoiceSummaryConfigParams = reportActionUtil.getReportConfig(templateInputStreamContractInvoiceSummary, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CONTRACTINVOICESUMMARY", true,
                    resourceBundle);
            ReportData subReportContractInvoiceSummaryData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportContractInvoiceSummaryConfigParams, reportParams,
                    "ContractInvoiceSummaryReportList", parameters);
            SubReportInfo subReportInfo = new SubReportInfo("SUBREPORT_CONTRACTINVOICESUMMARY", subReportContractInvoiceSummaryData.getJasperReport(),
                    ReportGeneratorConstants.BAND_SUMMARY, 0, -1, 500, 10);
            subReportInfo.getSubReportParameters().add(new SubReportParameter("SUMMARYTOTAL_PARAM", "java.util.LinkedHashMap", "$V{contractTotalVar}"));
            subReportInfo.setShowWhenExpression("$V{contractTotalVar}.isEmpty() ? Boolean.FALSE : Boolean.TRUE");
            reportData.addSubreport("SUBREPORT_CONTRACTINVOICESUMMARY", subReportInfo);


            //*****************Variables.....
            ArrayList<ReportVariable> variablesList = new ArrayList<ReportVariable>();
            variablesList.add(new ReportVariable("contractTotalVar", ReportGeneratorConstants.RESET_TYPE_REPORT,
                    ReportGeneratorConstants.RESET_TYPE_REPORT, ReportGeneratorConstants.CALCULATION_SYSTEM,
                    "new java.util.LinkedHashMap()",
                    "java.util.Map"));

            variablesList.add(new ReportVariable("contractIncomeVar", ReportGeneratorConstants.RESET_TYPE_REPORT,
                    ReportGeneratorConstants.RESET_TYPE_REPORT, ReportGeneratorConstants.CALCULATION_SYSTEM,
                    "null",
                    "java.math.BigDecimal"));
            reportData.setVariablesList(variablesList);

            //Scriptlet
            reportData.setScriptletClass("com.piramide.elwis.web.financemanager.action.report.scriptlet.ContractInvoiceAmountsScriplet");

            //add Parameters in report
            String startDate = (String) searchForm.getParameter("fromDate");
            String endDate = (String) searchForm.getParameter("toDate");
            //range date param
            reportData.getReportParams().put("fromDate", startDate);
            reportData.getReportParams().put("toDate", endDate);
            reportData.getReportParams().put("RANGEDATE_PARAM", composeRangeDate(startDate, endDate, request));
            //income type
            reportData.getReportParams().put("totalIncomeType", searchForm.getParameter("contractIncome"));
            //copy params..
            reportData.getReportParams().put(contractTypeNameParam, reportParams.get(contractTypeNameParam));
            reportData.getReportParams().put("onlyNotInvoiced", reportParams.get("onlyNotInvoiced"));

            //Subtitle
            String subTitle = "";
            subTitle = reportData.getReportParams().get("RANGEDATE_PARAM").toString();
            if (reportParams.get(contractTypeNameParam).toString().length() > 0) {
                subTitle += ", " + reportParams.get(contractTypeNameParam).toString();
            }
            reportData.getReportConfigParams().put("REPORT_SUBTITLE", subTitle);
            actionForward = super.execute(mapping, searchForm, request, response);

        } else {
            actionForward = mapping.findForward("Success");
        }
        return (actionForward);
    }

    private String composeRangeDate(String startDate, String endDate, HttpServletRequest request) {
        StringBuffer resultStr = new StringBuffer();
        String datePattern = JSPHelper.getMessage(request, "datePattern");
        String from = JSPHelper.getMessage(request, "Common.from");
        String to = JSPHelper.getMessage(request, "Common.to");

        if (!GenericValidator.isBlankOrNull(startDate)) {
            Date date = DateUtils.integerToDate(new Integer(startDate));
            resultStr.append(from).append(" ").append(DateUtils.parseDate(date, datePattern)).append(" ");
        }

        if (!GenericValidator.isBlankOrNull(endDate)) {
            Date date = DateUtils.integerToDate(new Integer(endDate));
            resultStr.append(to).append(" ").append(DateUtils.parseDate(date, datePattern));
        }

        return resultStr.toString();
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
        log.debug("Resize template document...." + pageSize + "-" + pageOrientation);
        ReportTemplateUtil templateUtil = new ReportTemplateUtil();

        //add move elements
        templateUtil.addMoveElementKey(ReportTemplateUtil.PAGEFOOTER_BAND, "PAGE_FOOTER_NUMBER");

        //add resize elements
        templateUtil.addResizeElementKey(ReportTemplateUtil.TITLE_BAND, "TITLE_REPORT_ELEMENT");
        templateUtil.addResizeElementKey(ReportTemplateUtil.TITLE_BAND, "rangeDate_text");
        templateUtil.addResizeElementKey(ReportTemplateUtil.PAGEFOOTER_BAND, "PAGE_FOOTER_LINE");

        //add table resize
        ReportTemplateUtil.TemplateTableColumn tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "contact_column");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "contact_field");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "customer_column");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "customer_field");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "product_column");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "product_field");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "startDate_column");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "startDate_field");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "endDate_column");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "endDate_field");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "income_column");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "income_field");
        templateUtil.addResizeTableColumn(tableColumn);

        tableColumn = templateUtil.new TemplateTableColumn();
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "currency_column");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "currency_field");
        templateUtil.addResizeTableColumn(tableColumn);

        return templateUtil.resizeTemplate(templateInputStream, pageSize, pageOrientation);
    }

    private String readContractTypeName(Integer contractTypeId) {
        String contractTypeName = "";
        ContractTypeCmd contractTypeCmd = new ContractTypeCmd();
        contractTypeCmd.putParam("contractTypeId", contractTypeId);
        contractTypeCmd.putParam("op", "read");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(contractTypeCmd, null);
            if (!resultDTO.isFailure()) {
                contractTypeName = (String) resultDTO.get("name");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute ContractTypeCmd..", e);
        }
        return contractTypeName;
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

 //add contract type filter and report parameter
 String contracTypeParameter = (String) searchForm.getParameter("contractTypeParam");
 String contractTypeNameParam = "CONTRACTTYPENAME_PARAM";
 reportParams.put(contractTypeNameParam, "");
 if (!GenericValidator.isBlankOrNull(contracTypeParameter)) {
 if (SalesConstants.CONTRACTS_TO_BE_INVOICED.equals(contracTypeParameter)) {
 parameters.addSearchParameter("toBeInvoiced", "1");  //this field is boolean representation
 reportParams.put(contractTypeNameParam, JSPHelper.getMessage(request, "ContractToInvoice.report.allToBeInvoiced"));

 } else if (SalesConstants.CONTRACTS_NOT_TO_BE_INVOICED.equals(contracTypeParameter)) {
 parameters.addSearchParameter("toBeInvoiced", "0");
 reportParams.put(contractTypeNameParam, JSPHelper.getMessage(request, "ContractToInvoice.report.allNotToBeInvoiced"));

 } else {
 parameters.addSearchParameter("contractTypeId", contracTypeParameter);
 reportParams.put(contractTypeNameParam, readContractTypeName(new Integer(contracTypeParameter)));
 }
 }

 //add filter, contracts only not invoiced
 if (searchForm.getParameter("withoutInvoiced") != null && !"".equals(searchForm.getParameter("withoutInvoiced").toString())) {
 parameters.addSearchParameter("notInvoiced", "true");

 //add in report params, to sum total of only not invoiced
 reportParams.put("onlyNotInvoiced", "true");
 } else {
 reportParams.put("onlyNotInvoiced", "false");
 }

 //contract invoice summary subreport
 InputStream templateInputStreamContractInvoiceSummary = request.getSession().getServletContext().getResourceAsStream(
 "/common/finance/jaspertemplates/ContractInvoiceSummarySubReport.jrxml");

 HashMap subReportContractInvoiceSummaryConfigParams = reportActionUtil.getReportConfig(templateInputStreamContractInvoiceSummary, reportFormat,
 tempDirectory, reportPageSize,
 locale.toString(), reportPageOrientation,
 "SUBREPORT_CONTRACTINVOICESUMMARY", true,
 resourceBundle);
 ReportData subReportContractInvoiceSummaryData = reportActionUtil.generateSubReport(
 getServlet(), request,
 subReportContractInvoiceSummaryConfigParams, reportParams,
 "ContractInvoiceSummaryReportList", parameters);
 subreports.put("SUBREPORT_CONTRACTINVOICESUMMARY", subReportContractInvoiceSummaryData);


 //add Parameters in report
 String startDate = (String) searchForm.getParameter("fromDate");
 String endDate = (String) searchForm.getParameter("toDate");
 //range date param
 reportParams.put("fromDate", startDate);
 reportParams.put("toDate", endDate);
 reportParams.put("RANGEDATE_PARAM", composeRangeDate(startDate, endDate, request));
 //income type
 reportParams.put("totalIncomeType", searchForm.getParameter("contractIncome"));

 //MasterReport
 InputStream templateInputStream = request.getSession().getServletContext().getResourceAsStream(
 "/common/finance/jaspertemplates/ContractInvoiceReportTemplate.jrxml");

 //resize template if is necessary
 templateInputStream = resizeTemplate(templateInputStream, reportPageSize, reportPageOrientation);

 HashMap reportConfigParams = reportActionUtil.getReportConfig(templateInputStream, reportFormat,
 tempDirectory, reportPageSize,
 locale.toString(), reportPageOrientation,
 "ContractInvoice_report", false,
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
 "contractToInvoiceReportList", parameters,
 subreports);
 //End master report


 } else {
 actionForward = mapping.findForward("Success");
 }
 return (actionForward);
 */


//Predefined

/*
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

            HashMap reportParams = reportActionUtil.getReportParams(timeZone.toString(), locale.getLanguage(), searchForm);

            //add contract type filter and report parameter
            String contracTypeParameter = (String) searchForm.getParameter("contractTypeParam");
            String contractTypeNameParam = "CONTRACTTYPENAME_PARAM";
            reportParams.put(contractTypeNameParam, "");
            if (!GenericValidator.isBlankOrNull(contracTypeParameter)) {
                if (SalesConstants.CONTRACTS_TO_BE_INVOICED.equals(contracTypeParameter)) {
                    parameters.addSearchParameter("toBeInvoiced", "1");  //this field is boolean representation
                    reportParams.put(contractTypeNameParam, JSPHelper.getMessage(request, "ContractToInvoice.report.allToBeInvoiced"));

                } else if (SalesConstants.CONTRACTS_NOT_TO_BE_INVOICED.equals(contracTypeParameter)) {
                    parameters.addSearchParameter("toBeInvoiced", "0");
                    reportParams.put(contractTypeNameParam, JSPHelper.getMessage(request, "ContractToInvoice.report.allNotToBeInvoiced"));

                } else {
                    parameters.addSearchParameter("contractTypeId", contracTypeParameter);
                    reportParams.put(contractTypeNameParam, readContractTypeName(new Integer(contracTypeParameter)));
                }
            }

            //add filter, contracts only not invoiced
            if (searchForm.getParameter("withoutInvoiced") != null && !"".equals(searchForm.getParameter("withoutInvoiced").toString())) {
                parameters.addSearchParameter("notInvoiced", "true");

                //add in report params, to sum total of only not invoiced
                reportParams.put("onlyNotInvoiced", "true");
            } else {
                reportParams.put("onlyNotInvoiced", "false");
            }

            //contract invoice summary subreport
            InputStream templateInputStreamContractInvoiceSummary = request.getSession().getServletContext().getResourceAsStream(
                    "/common/finance/jaspertemplates/ContractInvoiceSummarySubReport.jrxml");

            HashMap subReportContractInvoiceSummaryConfigParams = reportActionUtil.getReportConfig(templateInputStreamContractInvoiceSummary, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CONTRACTINVOICESUMMARY", true,
                    resourceBundle);
            ReportData subReportContractInvoiceSummaryData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportContractInvoiceSummaryConfigParams, reportParams,
                    "ContractInvoiceSummaryReportList", parameters);
            SubReportInfo subReportInfo=new SubReportInfo("SUBREPORT_CONTRACTINVOICESUMMARY", subReportContractInvoiceSummaryData.getJasperReport(),
            ReportGeneratorConstants.BAND_SUMMARY, 0,-1,500,10);
            subReportInfo.getSubReportParameters().add(new SubReportParameter("SUMMARYTOTAL_PARAM","java.util.LinkedHashMap","$V{contractTotalVar}"));
            subReportInfo.setShowWhenExpression("$V{contractTotalVar}.isEmpty() ? Boolean.FALSE : Boolean.TRUE");
            reportData.addSubreport("SUBREPORT_CONTRACTINVOICESUMMARY",subReportInfo);


            //*****************Variables.....
            ArrayList<ReportVariable> variablesList=new ArrayList<ReportVariable>();
            variablesList.add(new ReportVariable("contractTotalVar", ReportGeneratorConstants.RESET_TYPE_REPORT,
                                                 ReportGeneratorConstants.RESET_TYPE_REPORT, ReportGeneratorConstants.CALCULATION_SYSTEM,
                                                 "new java.util.LinkedHashMap()",
                                                 "java.util.Map"));

            variablesList.add(new ReportVariable("contractIncomeVar", ReportGeneratorConstants.RESET_TYPE_REPORT,
                                                 ReportGeneratorConstants.RESET_TYPE_REPORT, ReportGeneratorConstants.CALCULATION_SYSTEM,
                                                 "null",
                                                 "java.math.BigDecimal"));
            reportData.setVariablesList(variablesList);

            //Scriptlet
            reportData.setScriptletClass("com.piramide.elwis.web.financemanager.action.report.scriptlet.ContractInvoiceAmountsScriplet");

            //add Parameters in report
            String startDate = (String) searchForm.getParameter("fromDate");
            String endDate = (String) searchForm.getParameter("toDate");
            //range date param
            reportData.getReportParams().put("fromDate", startDate);
            reportData.getReportParams().put("toDate", endDate);
            reportData.getReportParams().put("RANGEDATE_PARAM", composeRangeDate(startDate, endDate, request));
            //income type
            reportData.getReportParams().put("totalIncomeType", searchForm.getParameter("contractIncome"));
            //copy params..
            reportData.getReportParams().put(contractTypeNameParam, reportParams.get(contractTypeNameParam));
            reportData.getReportParams().put("onlyNotInvoiced", reportParams.get("onlyNotInvoiced"));

            //Subtitle
            String subTitle="";
            subTitle=reportData.getReportParams().get("RANGEDATE_PARAM").toString();
            if(reportParams.get(contractTypeNameParam).toString().length()>0)
                    subTitle+=", "+reportParams.get(contractTypeNameParam).toString();
            reportData.getReportConfigParams().put("REPORT_SUBTITLE",subTitle);
            actionForward=super.execute(mapping, searchForm, request, response);

        } else {
            actionForward = mapping.findForward("Success");
        }
        return (actionForward);
*/