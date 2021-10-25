package com.piramide.elwis.web.salesmanager.action.report;

import com.jatun.titus.reportgenerator.util.ReportData;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.form.ReportGeneratorForm;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ContractsOverviewReportAction.java 07-ene-2009 14:32:06 $
 */
public class ContractsOverviewReportAction extends ReportAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContractsOverviewReportAction.... " + request.getParameterMap());

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

            //Fantabulous list parameters
            Parameters parameters = new Parameters();
            parameters.addSearchParameter(Constants.COMPANYID, companyId.toString());
            parameters.addSearchParameter(Constants.USERID, user.getValue(Constants.USERID).toString());
            parameters.addSearchParameters(getParameters(searchForm.getParams()));
            parameters.addSearchParameters(getParameters(getStaticFilter()));
            parameters.addSearchParameters(getParameters(getFilter()));

            //add parameter to evaluate conditions
            if (!GenericValidator.isBlankOrNull((String) searchForm.getParameter("inputDate"))) {
                parameters.addSearchParameter("hasInputDate", "true");
            }

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

            //contract invoice summary subreport
            InputStream templateInputStreamContractOverviewSummary = request.getSession().getServletContext().getResourceAsStream(
                    "/common/sales/jaspertemplates/ContractsOverviewSummarySubReport.jrxml");

            HashMap subReportContractOverviewSummaryConfigParams = reportActionUtil.getReportConfig(templateInputStreamContractOverviewSummary, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CONTRACTINVOICESUMMARY", true,
                    resourceBundle);
            ReportData subReportContractInvoiceSummaryData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportContractOverviewSummaryConfigParams, reportParams,
                    "ContractOverviewSummaryReportList", parameters);
            subreports.put("SUBREPORT_CONTRACTINVOICESUMMARY", subReportContractInvoiceSummaryData);


            //add range date param
            String inputDate = (String) searchForm.getParameter("inputDate");
            reportParams.put("RANGEDATE_PARAM", composeDate(inputDate, request));

            //MasterReport
            InputStream templateInputStream = request.getSession().getServletContext().getResourceAsStream(
                    "/common/sales/jaspertemplates/ContractsOverviewReportTemplate.jrxml");

            //resize template if is necessary
            templateInputStream = resizeTemplate(templateInputStream, reportPageSize, reportPageOrientation);

            HashMap reportConfigParams = reportActionUtil.getReportConfig(templateInputStream, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "ContractOverview_report", false,
                    resourceBundle);


            //Loading the company logo image..
            HashMap companyLogoInfo = reportActionUtil.getCompanLogoInfo(request, true);

            reportParams.put("IS_TITLE_IMAGE_STREAM", companyLogoInfo.get("IS_TITLE_IMAGE_STREAM"));
            reportParams.put("TITLE_IMAGE_STREAM", companyLogoInfo.get("TITLE_IMAGE_STREAM"));
            if (new Boolean(companyLogoInfo.get("IS_TITLE_IMAGE_STREAM").toString())) {
                reportConfigParams.put("TITLE_IMAGE_WIDTH", companyLogoInfo.get("TITLE_IMAGE_WIDTH"));
                reportConfigParams.put("TITLE_IMAGE_HEIGHT", companyLogoInfo.get("TITLE_IMAGE_HEIGHT"));
            }

            reportConfigParams.put("reportTitleString", searchForm.getReportTitle());
            reportActionUtil.generateReport(getServlet(),
                    request, response,
                    reportConfigParams, reportParams,
                    "contractOverviewReportList", parameters,
                    subreports);
            //End master report

        } else {
            actionForward = mapping.findForward("Success");
        }
        return (actionForward);

    }

    private String composeDate(String dateInteger, HttpServletRequest request) {
        StringBuffer resultStr = new StringBuffer();
        String datePattern = JSPHelper.getMessage(request, "datePattern");

        if (!GenericValidator.isBlankOrNull(dateInteger)) {
            Date date = DateUtils.integerToDate(new Integer(dateInteger));
            resultStr.append(DateUtils.parseDate(date, datePattern));
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
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "orderDate_column");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "orderDate_field");
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
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.COLUMNHEADER_BAND, "invoicedUntil_column");
        tableColumn.addTableColumnElementKey(ReportTemplateUtil.DETAIL_BAND, "invoicedUntil_field");
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