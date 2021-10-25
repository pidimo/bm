package com.piramide.elwis.web.salesmanager.action.report;

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
 *
 * @author alvaro
 * @version $Id: ContractsOverviewPredefinedReportAction.java 23-abr-2009 12:21:19
 */
public class ContractsOverviewPredefinedReportAction extends ReportAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContractsOverviewReportAction.... ................" + request.getParameterMap());
        ReportGeneratorForm reportGeneratorForm = (ReportGeneratorForm) form;
        //add contract type filter and report parameter
        super.initializeFilter();
        ReportGeneratorForm searchForm = (ReportGeneratorForm) form;
        ActionErrors errors = searchForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Success");
        }

        if (searchForm.getParams().get("generate") != null) {
            //add parameter to evaluate conditions
            if (!GenericValidator.isBlankOrNull((String) searchForm.getParameter("inputDate"))) {
                reportGeneratorForm.getParams().put("hasInputDate", "true");
            }
            String contracTypeParameter = (String) reportGeneratorForm.getParameter("contractTypeParam");
            String contractTypeParameterLabel = null;
            reportData.getReportParams().put("CONTRACTTYPENAME_PARAM", "");
            if (!GenericValidator.isBlankOrNull(contracTypeParameter)) {
                if (SalesConstants.CONTRACTS_TO_BE_INVOICED.equals(contracTypeParameter)) {
                    reportGeneratorForm.getParams().put("toBeInvoiced", "1");  //this field is boolean representation
                    contractTypeParameterLabel = JSPHelper.getMessage(request, "ContractToInvoice.report.allToBeInvoiced");
                    reportData.getReportParams().put("CONTRACTTYPENAME_PARAM", contractTypeParameterLabel);

                } else if (SalesConstants.CONTRACTS_NOT_TO_BE_INVOICED.equals(contracTypeParameter)) {
                    reportGeneratorForm.getParams().put("toBeInvoiced", "0");
                    contractTypeParameterLabel = JSPHelper.getMessage(request, "ContractToInvoice.report.allNotToBeInvoiced");
                    reportData.getReportParams().put("CONTRACTTYPENAME_PARAM", contractTypeParameterLabel);

                } else {
                    reportGeneratorForm.getParams().put("contractTypeId", contracTypeParameter);
                    contractTypeParameterLabel = readContractTypeName(new Integer(contracTypeParameter));
                    reportData.getReportParams().put("CONTRACTTYPENAME_PARAM", contractTypeParameterLabel);
                }
            }

            //*****************Variables.....
            ArrayList<ReportVariable> variablesList = new ArrayList<ReportVariable>();
            variablesList.add(new ReportVariable("overviewTotalVar", ReportGeneratorConstants.RESET_TYPE_REPORT,
                    ReportGeneratorConstants.RESET_TYPE_REPORT, ReportGeneratorConstants.CALCULATION_SYSTEM,
                    "new java.util.LinkedHashMap()",
                    "java.util.Map"));

            variablesList.add(new ReportVariable("overviewIncomeVar", ReportGeneratorConstants.RESET_TYPE_REPORT,
                    ReportGeneratorConstants.RESET_TYPE_REPORT, ReportGeneratorConstants.CALCULATION_SYSTEM,
                    "null",
                    "java.math.BigDecimal"));
            reportData.setVariablesList(variablesList);

            //Scriptlet
            reportData.setScriptletClass("com.piramide.elwis.web.salesmanager.action.report.scriptlet.ContractOverviewAmountsScriptlet");

            //Subtitle
            String subTitle = "";
            if (reportGeneratorForm.getParameter("inputDate") != null) {
                subTitle = composeDate(reportGeneratorForm.getParameter("inputDate").toString(), request);
            }

            if (contractTypeParameterLabel != null) {
                if (subTitle.length() > 1) {
                    subTitle += ", ";
                }
                subTitle += contractTypeParameterLabel;
            }
            reportData.getReportConfigParams().put("REPORT_SUBTITLE", subTitle);

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
            InputStream templateInputStreamContractOverviewSummary = request.getSession().getServletContext().getResourceAsStream(
                    "/common/sales/jaspertemplates/ContractsOverviewSummarySubReport.jrxml");

            HashMap subReportInvoiceSummaryConfigParams = reportActionUtil.getReportConfig(templateInputStreamContractOverviewSummary, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CONTRACTINVOICESUMMARY", true,
                    resourceBundle);
            ReportData subReportInvoiceSummaryData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportInvoiceSummaryConfigParams, reportParams,
                    "ContractOverviewSummaryReportList", parameters);
            SubReportInfo subReportInfo = new SubReportInfo("SUBREPORT_CONTRACTINVOICESUMMARY", subReportInvoiceSummaryData.getJasperReport(),
                    ReportGeneratorConstants.BAND_SUMMARY, 0, -1, 500, 10);
            subReportInfo.getSubReportParameters().add(new SubReportParameter("SUMMARYTOTAL_PARAM", "java.util.LinkedHashMap", "$V{overviewTotalVar}"));
            subReportInfo.setShowWhenExpression("$V{overviewTotalVar}.isEmpty() ? Boolean.FALSE : Boolean.TRUE");
            reportData.addSubreport("SUBREPORT_CONTRACTINVOICESUMMARY", subReportInfo);
        }

        return (super.execute(mapping, reportGeneratorForm, request, response));
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

    private String composeDate(String dateInteger, HttpServletRequest request) {
        StringBuffer resultStr = new StringBuffer();
        String datePattern = JSPHelper.getMessage(request, "datePattern");

        if (!GenericValidator.isBlankOrNull(dateInteger)) {
            Date date = DateUtils.integerToDate(new Integer(dateInteger));
            resultStr.append(DateUtils.parseDate(date, datePattern));
        }
        return resultStr.toString();
    }
}
