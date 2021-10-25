package com.piramide.elwis.web.common.action.report;

import com.jatun.titus.listgenerator.db.connection.ConnectionFactory;
import com.jatun.titus.reportgenerator.ReportGenerator;
import com.jatun.titus.reportgenerator.util.ReportData;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.lowagie.text.ExceptionConverter;
import com.piramide.elwis.cmd.contactmanager.CompanyCmd;
import com.piramide.elwis.cmd.reports.ReportExecuteCmd;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.reports.el.ReportSettingUtil;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenerator;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGeneratorManager;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: ReportAction.java 10566 2015-04-29 21:07:54Z miguel $
 */

public class ReportAction extends ReportBaseAction {
    private Log log = LogFactory.getLog(this.getClass());

    //The reportData
    protected ReportData reportData = new ReportData();

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("........................Executing ReportManagerAction.....................");
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());

        ReportGeneratorForm reportGeneratorForm = (ReportGeneratorForm) form;
        FantabulousManager manager;
        ListStructure listStructure = null;
        manager = FantabulousManager.loadFantabulousManager(getServlet().getServletContext(), request);
        listStructure = manager.getList(mapping.getParameter());

        //add access right data level security if is required
        listStructure = addAccessRightSecurity(listStructure, userId);

        Parameters parameters = new Parameters();
        reportGeneratorForm.setParameter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());
        reportGeneratorForm.setParameter(Constants.USERID, user.getValue(Constants.USERID).toString());
        reportGeneratorForm.setParameter(Constants.USER_ADDRESSID, user.getValue(Constants.USER_ADDRESSID).toString());
        parameters.addSearchParameters(getParameters(reportGeneratorForm.getParams()));
        parameters.addSearchParameters(getParameters(getStaticFilter()));
        parameters.addSearchParameters(getParameters(getFilter()));
        parameters.setGenerateHiddenOrders(false);
        //Change grouping
        super.processSortingAndGrouping(reportGeneratorForm, listStructure,
                new Locale(request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()));

        SqlGenerator generator = SqlGeneratorManager.newInstance();

        String sql = generator.generate(listStructure, parameters);
        InputStream templateInputStream = request.getSession().getServletContext().getResourceAsStream("/common/reports/template.jrxml");
        String reportFormat = reportGeneratorForm.getReportFormat();
        String reportPageSize = reportGeneratorForm.getReportPageSize();
        String csvDelimiter = reportGeneratorForm.getCsvDelimiter();
        String reportCharset = reportGeneratorForm.getReportCharset();

        //Get the temp directory
        String reportTempDirectory = null;
        String tempDirectory = ConfigurationFactory.getConfigurationManager().getValue("elwis.temp.folder");
        File tempDirectoryFile = new File(tempDirectory);
        if (tempDirectoryFile.exists()) {
            reportTempDirectory = tempDirectory;
        }


        //Set configuration params
        setReportConfig(reportData, reportGeneratorForm.getReportConfigParams(), reportGeneratorForm.getReportFieldParams(),
                templateInputStream, reportFormat, reportTempDirectory, reportPageSize, csvDelimiter, reportCharset, request);

        //Set report params
        setReportParams(reportData, reportGeneratorForm.getReportConfigParams(), reportGeneratorForm.getReportTitle(),
                request, reportGeneratorForm.getReportGroupingDateFilter());

        //Set some parameters
        reportData.putReportConfigParam("SqlQuery", sql);
        reportData.putReportConfigParam("ListStructure", listStructure);
        reportData.putReportParam("FANTABULOUS_PARAMETERS", parameters);
        //Generate the report
        generateReport(response, request, reportData, reportGeneratorForm);
        return (null);
    }

    private synchronized void generateReport(HttpServletResponse response, HttpServletRequest request, ReportData reportData, ReportGeneratorForm reportGeneratorForm) throws IOException {
        //Executing the report

        ReportExecuteCmd reportExecuteCmd = new ReportExecuteCmd();
        reportExecuteCmd.putParam("op", "execute");
        reportExecuteCmd.putParam("reportData", reportData);
        /*ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(reportExecuteCmd, request);
        } catch (AppLevelException aplex) {
            log.debug("ERROR in executing the ReportExecuteCmd\n");
            aplex.printStackTrace();
        }*/
        //If all is OK, then generate the report
        //if (resultDTO != null) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getDBConnection().getConnection();
            reportData.putReportConfigParam("DBConnection", conn);

            //Execute the report
            reportData = new ReportGenerator().generateReport(reportData);

        } catch (SQLException e) {
            log.error("ERROR IN Getting the connection from Jboss.....", e);
        }
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            log.error(e);
        }

        JRAbstractExporter exporter = reportData.getJasperExporter();
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
        String fileName = getFileName(reportData, request);

        String contentType = MimeTypeUtil.getMimetype(fileName);

        response.setContentType(contentType);
        response.setHeader("Content-disposition",
                "attachment; filename=\""
                        + fileName + "\"");
        response.setHeader("Cache-Control",
                "max-age=0");
        try {
            exporter.exportReport();
            operationsAfterReportGeneration(reportGeneratorForm, request);
            //save some settings
            saveUserExportSettings(reportData, request);

            reportData.reset();
        } catch (JRException jex) {
            log.debug("ERROR in exporting the report....\n");
            jex.printStackTrace();
        } catch (ExceptionConverter ec) {
            log.debug("The download of the report was cancelled " + ec.getException());
        }
        //}
    }

    protected String getFileName(ReportData reportData, HttpServletRequest request) {
        String fileName;
        HashMap reportConfigParams = reportData.getReportConfigParams();
        if (reportData.getReportParams().get("REPORT_TITLE") != null) {
            fileName = reportData.getReportParams().get("REPORT_TITLE").toString() + "." + reportConfigParams.get("reportFormat");
        } else {
            fileName = reportConfigParams.get("reportId") + "." + reportConfigParams.get("reportFormat");
        }
        return fileName;
    }

    private void setReportConfig(ReportData data, HashMap reportConfigurations, ArrayList fieldConfigurations,
                                 InputStream templateInputStream, String ReportFormat, String reportTempDirectory,
                                 String reportPageSize, String csvFieldDelimiter, String reportCharset, HttpServletRequest request) {

        Object resourceKey = null, resource = null, patternKey = null, pattern = null,
                patternKey2 = null, patternCondition = null, pattern2 = null, totalizatorKey = null;
        HashMap reportConfigParams = reportData.getReportConfigParams();
        if (reportConfigParams == null) {
            reportConfigParams = new HashMap();
        }
        reportConfigParams.put("templateInputStream", templateInputStream);
        reportConfigParams.put("reportLocale", reportConfigurations.get("locale"));
        reportConfigParams.put("reportPageOrientation", reportConfigurations.get("pageOrientation"));
        reportConfigParams.put("reportId", reportConfigurations.get("id"));
        reportConfigParams.put("reportFormat", ReportFormat);

        //config for csv export format
        if (ReportGeneratorConstants.REPORT_FORMAT_CSV.equals(ReportFormat)) {
            if (!GenericValidator.isBlankOrNull(csvFieldDelimiter)) {
                reportConfigParams.put("csvFieldDelimiter", csvFieldDelimiter);
            }
            if (!GenericValidator.isBlankOrNull(reportCharset)) {
                reportConfigParams.put("characterEncoding", reportCharset);
            }
        }

        //Set the resources
        setResources(request, data.getParams());
        //Get the pageSize from the configuration tag or the choose by the user
        if (reportPageSize != null) {
            reportConfigParams.put("reportPageSize", reportPageSize);
        } else {
            reportConfigParams.put("reportPageSize", reportConfigurations.get("pageSize"));
        }

        if (reportTempDirectory != null) {
            reportConfigParams.put("ReportTempDirectory", reportTempDirectory);
        }

        ArrayList fieldConfigParams = new ArrayList();

        HashMap fieldConfig = new HashMap();
        for (int i = 0; i < fieldConfigurations.size(); i++) {
            HashMap fieldConfig_i = (HashMap) fieldConfigurations.get(i);
            fieldConfig = new HashMap();
            fieldConfig.put("fieldName", fieldConfig_i.get("name"));
            fieldConfig.put("fieldType", fieldConfig_i.get("type"));
            fieldConfig.put("fieldWidth", fieldConfig_i.get("width"));
            fieldConfig.put("fieldAlign", fieldConfig_i.get("align"));
            fieldConfig.put("isStretchWithOverflow", fieldConfig_i.get("stretch"));
            fieldConfig.put("contentCondition", fieldConfig_i.get("conditionMethod"));
            fieldConfig.put("isGroupingColumn", fieldConfig_i.get("isGrouping"));
            fieldConfig.put("totalizatorOp", fieldConfig_i.get("totalizatorOp"));
            fieldConfig.put("showSymbol", fieldConfig_i.get("showSymbol"));
            fieldConfig.put("groupField", fieldConfig_i.get("groupField"));

            resourceKey = fieldConfig_i.get("resourceKey");
            patternKey = fieldConfig_i.get("patternKey");
            patternKey2 = fieldConfig_i.get("patternKey2");
            patternCondition = fieldConfig_i.get("patternCondition");
            totalizatorKey = fieldConfig_i.get("totalizatorKey");
            if (resourceKey != null) {
                resource = JSPHelper.getMessage(new Locale(reportConfigurations.get("locale").toString()), resourceKey.toString());
                fieldConfig.put("fieldLabel", resource);
            }
            if (patternKey != null) {
                pattern = JSPHelper.getMessage(new Locale(reportConfigurations.get("locale").toString()), patternKey.toString());
                fieldConfig.put("fieldPattern", pattern);
            }
            if (patternKey2 != null) {
                pattern2 = JSPHelper.getMessage(new Locale(reportConfigurations.get("locale").toString()), patternKey2.toString());
                fieldConfig.put("fieldPattern2", pattern2);
            }
            if (patternCondition != null) {
                fieldConfig.put("patternCondition", patternCondition);
            }

            if (totalizatorKey != null) {
                resource = JSPHelper.getMessage(new Locale(reportConfigurations.get("locale").toString()), totalizatorKey.toString());
                fieldConfig.put("fieldTotalizatorLabel", resource);
            }

            fieldConfigParams.add(fieldConfig);
        }
        reportConfigParams.put("FieldConfigurations", fieldConfigParams);
        data.setReportConfigParams(reportConfigParams);
    }

    private void setReportParams(ReportData reportData, HashMap reportConfigurations, String reportTitle,
                                 HttpServletRequest request, Object dateFilter) {

        Locale locale = new Locale(reportConfigurations.get("locale").toString());
        if ((reportTitle == null || (reportTitle != null && reportTitle.toString().trim().length() == 0)) &&
                reportConfigurations.get("title") != null) {
            reportTitle = JSPHelper.getMessage(locale,
                    reportConfigurations.get("title").toString());
        }

        reportData.putReportParam("REPORT_TITLE", reportTitle);
        reportData.putReportParam("REPORT_TIMEZONE", reportConfigurations.get("timeZone"));
        reportData.putReportParam("RECORDS_LABEL", getReportResourceBundle(request).getString("reportgenerator.totalRecords"));
        if (dateFilter != null) {
            int dateFilterInt = Integer.parseInt(dateFilter.toString());
            String datePattern = "";
            if (dateFilterInt == ReportGeneratorConstants.DATE_FILTER_WEEK) {
                datePattern = JSPHelper.getMessage(locale, "ReportGenerator.WeekDatePattern");
            } else if (dateFilterInt == ReportGeneratorConstants.DATE_FILTER_MONTH) {
                datePattern = JSPHelper.getMessage(locale, "ReportGenerator.MonthDatePattern");
            } else if (dateFilterInt == ReportGeneratorConstants.DATE_FILTER_YEAR) {
                datePattern = JSPHelper.getMessage(locale, "ReportGenerator.YearDatePattern");
            }
            reportData.getParams().put("REPORT_DATE_PATTERN", datePattern);
        }
        //Loading the company logo image..
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        CompanyCmd companyCmd = new CompanyCmd();
        companyCmd.putParam("addressId", user.getValue("companyId"));
        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(companyCmd, request);
        } catch (AppLevelException aplex) {
            log.debug("ERROR in Executing the CompanyCmd");
            aplex.printStackTrace();
        }

        if (resultDTO != null) {
            Object logoId = resultDTO.get("logoId");
            if (logoId != null && reportConfigurations.get("showCompanyLogo").toString().toLowerCase().equals("true")) {
                //Get the image Stream
                ArrayByteWrapper image = null;
                try {
                    FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
                    FreeText freeText = freeTextHome.findByPrimaryKey(new Integer(logoId.toString()));
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(freeText.getValue());
                    reportData.putReportParam("IS_TITLE_IMAGE_STREAM", Boolean.valueOf(true));
                    reportData.putReportParam("TITLE_IMAGE_STREAM", byteArrayInputStream);
                    image = new ArrayByteWrapper(freeText.getValue());
                } catch (FinderException fex) {
                }
                //GET the image size
                if (image != null) {
                    try {
                        ImageIcon imageIcon = new ImageIcon(image.getFileData());
                        Image newImage = imageIcon.getImage();
                        reportData.putParam("TITLE_IMAGE_WIDTH", new Integer(newImage.getWidth(null)));
                        reportData.putParam("TITLE_IMAGE_HEIGHT", new Integer(newImage.getHeight(null)));
                    } catch (Exception ex) {
                        log.debug("ERROR, trying to get the logo image size");
                    }
                }
            } else {//If the company dont have a image
                reportData.putReportParam("IS_TITLE_IMAGE_STREAM", Boolean.valueOf(false));
                reportData.putReportParam("TITLE_IMAGE_STREAM", ReportGeneratorConstants.VOID_STREAM);
            }
        }

    }

    private void setResources(HttpServletRequest request, HashMap params) {
        ResourceBundle resourceBundle = getReportResourceBundle(request);
        params.put("LABEL_" + ReportGeneratorConstants.CALCULATION_COUNT, resourceBundle.getString("reportgenerator.count_subtotal_label"));
        params.put("LABEL_" + ReportGeneratorConstants.CALCULATION_SUM, resourceBundle.getString("reportgenerator.sum_subtotal_label"));
        params.put("LABEL_" + ReportGeneratorConstants.CALCULATION_AVERAGE, resourceBundle.getString("reportgenerator.average_subtotal_label"));
        params.put("LABEL_GROUPED_BY", resourceBundle.getString("reportgenerator.grouped_by"));
        params.put("LABEL_TOTALIZED_BY", resourceBundle.getString("reportgenerator.totalized_by"));
        params.put("LABEL_TOTALS", resourceBundle.getString("reportgenerator.totals"));
    }

    private ResourceBundle getReportResourceBundle(HttpServletRequest request) {
        ResourceBundle resourceBundle;
        String locale = request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString();
        if (locale != null) {
            resourceBundle = ResourceBundle.getBundle(ReportGeneratorConstants.REPORTGENERATOR_RESOURCES, new Locale(locale));
        } else {
            resourceBundle = ResourceBundle.getBundle(ReportGeneratorConstants.REPORTGENERATOR_RESOURCES);
        }
        return (resourceBundle);
    }

    /**
     * Save some user export setting configuration
     * @param reportData
     * @param request
     */
    private void saveUserExportSettings(ReportData reportData, HttpServletRequest request) {
        HashMap reportConfigParams = reportData.getReportConfigParams();
        String reportFormat = (String) reportConfigParams.get("reportFormat");
        String csvFieldDelimiter = (String) reportConfigParams.get("csvFieldDelimiter");
        String reportCharset = (String) reportConfigParams.get("characterEncoding");

        //save user csv delimiter configuration
        if (ReportGeneratorConstants.REPORT_FORMAT_CSV.equals(reportFormat)) {
            ReportSettingUtil.saveUserReportSettingConfig(csvFieldDelimiter, reportCharset, request);
        }
    }

    private ListStructure addAccessRightSecurity(ListStructure sourceListStructure, Integer userId) {
        return AccessRightDataLevelSecurity.i.processAllAccessRightByList(sourceListStructure, userId);
    }

    /**
     * Method executed when report has been generated
     * @param reportGeneratorForm
     */
    protected void operationsAfterReportGeneration(ReportGeneratorForm reportGeneratorForm, HttpServletRequest request) {

    }
}
