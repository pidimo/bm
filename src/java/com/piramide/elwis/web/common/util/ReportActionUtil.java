package com.piramide.elwis.web.common.util;

import com.jatun.titus.listgenerator.db.connection.ConnectionFactory;
import com.jatun.titus.reportgenerator.ReceiptGenerator;
import com.jatun.titus.reportgenerator.ReportJrxmlGenerator;
import com.jatun.titus.reportgenerator.SubReportGenerator;
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
import com.piramide.elwis.web.reports.util.ReportExecuteException;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenerator;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGeneratorManager;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionServlet;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Jatun Team
 *
 * @author Alvaro
 * @version $Id: ReportActionUtil, 11-jun-2008 12:09:57
 */
public class ReportActionUtil {
    private Log log = LogFactory.getLog(this.getClass());

    public ReportData generateSubReport(ActionServlet actionServlet, HttpServletRequest request, HashMap subReportConfigParams,
                                        HashMap subReportParams, String listName,
                                        Parameters parameters) throws IOException, ListStructureNotFoundException {
        parameters.setGenerateHiddenOrders(false);
        return generateSubReport(actionServlet, request, subReportConfigParams, subReportParams, listName, parameters, new HashMap<String, ReportData>());
    }

    public ReportData generateSubReport(ActionServlet actionServlet, HttpServletRequest request, HashMap subReportConfigParams,
                                        HashMap subReportParams, String listName,
                                        Parameters parameters,
                                        HashMap<String, ReportData> subreports) throws IOException, ListStructureNotFoundException {
        parameters.setGenerateHiddenOrders(false);
        //Get the query
        FantabulousManager manager;
        ListStructure listStructure = null;
        manager = FantabulousManager.loadFantabulousManager(actionServlet.getServletContext(), request);

        listStructure = manager.getList(listName);
        listStructure = addAccessRightSecurity(listStructure, getUserId(request));

        SqlGenerator generator = SqlGeneratorManager.newInstance(); //sql generator name is declared in fantabulous-config.properties
        String sql;
        if (parameters.getPageParam() != null && parameters.getPageParam().getPageSize() > 0 &&
                parameters.getPageParam().getPageNumber() > 0) {
            sql = generator.generateSQLValues(listStructure, parameters);
        } else {
            sql = generator.generate(listStructure, parameters);
        }
        log.debug("SubReport query, list:" + listName + "\n" + sql);
        ReportData reportData = new ReportData();
        reportData.setReportConfigParams(subReportConfigParams);
        reportData.setReportParams(subReportParams);
        reportData.putReportConfigParam("SqlQuery", sql);
        reportData.putReportConfigParam("ListStructure", listStructure);
        reportData.putReportParam("FANTABULOUS_PARAMETERS", parameters);

        //Merge columns
        Iterator i = subreports.keySet().iterator();
        while (i.hasNext()) {
            String subreport_name = (String) i.next();
            ReportData subReportData = subreports.get(subreport_name);
            reportData.putReportParam(subreport_name, subReportData.getJasperReport());
            mergeColumns(reportData.getReportParams(), subReportData.getReportParams());
        }

        reportData = new SubReportGenerator().generateSubReport(reportData);

        return (reportData);
    }

    public synchronized void generateReport(ActionServlet actionServlet, HttpServletRequest request, HttpServletResponse response,
                                            HashMap reportConfigParams, HashMap reportParams,
                                            String listName, Parameters parameters,
                                            HashMap<String, ReportData> subreports) throws IOException, ListStructureNotFoundException {
        parameters.setGenerateHiddenOrders(false);
        //Get the query
        FantabulousManager manager;
        ListStructure listStructure = null;
        manager = FantabulousManager.loadFantabulousManager(actionServlet.getServletContext(), request);
        listStructure = manager.getList(listName);
        listStructure = addAccessRightSecurity(listStructure, getUserId(request));

        SqlGenerator generator = SqlGeneratorManager.newInstance(); //sql generator name is declared in fantabulous-config.properties
        String sql;
        if (parameters.getPageParam() != null && parameters.getPageParam().getPageSize() > 0 &&
                parameters.getPageParam().getPageNumber() > 0) {
            sql = generator.generateSQLValues(listStructure, parameters);
        } else {
            sql = generator.generate(listStructure, parameters);
        }
        log.debug("Report query, list:" + listName + "\n" + sql);
        ReportData reportData = new ReportData();
        reportData.setReportConfigParams(reportConfigParams);
        reportData.setReportParams(reportParams);
        reportData.putReportConfigParam("SqlQuery", sql);
        reportData.putReportConfigParam("ListStructure", listStructure);
        reportData.putReportParam("FANTABULOUS_PARAMETERS", parameters);

        //Merge columns
        Iterator i = subreports.keySet().iterator();
        while (i.hasNext()) {
            String subreport_name = (String) i.next();
            ReportData subReportData = subreports.get(subreport_name);
            reportData.putReportParam(subreport_name, subReportData.getJasperReport());
            mergeColumns(reportData.getReportParams(), subReportData.getReportParams());
        }
        Connection conn = null;
        try {
            conn = ConnectionFactory.getDBConnection().getConnection();
            reportData.putReportConfigParam("DBConnection", conn);

            //Execute the report
            reportData = new ReceiptGenerator().generateReport(reportData);

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
        String fileName;
        if (reportData.getReportParams().get("REPORT_TITLE") != null) {
            fileName = reportData.getReportParams().get("REPORT_TITLE").toString() + "." + reportConfigParams.get("reportFormat");
        } else {
            fileName = reportConfigParams.get("reportId") + "." + reportConfigParams.get("reportFormat");
        }

        String contentType = MimeTypeUtil.getMimetype(fileName);

        response.setContentType(contentType);
        response.setHeader("Content-disposition",
                "attachment; filename=\""
                        + fileName + "\"");
        response.setHeader("Cache-Control",
                "max-age=0");
        try {
            exporter.exportReport();
        } catch (JRException jex) {
            log.debug("ERROR in exporting the report....\n");
            jex.printStackTrace();
        }
        catch (ExceptionConverter ec) {
            log.debug("The download of the report was cancelled " + ec.getException());
        }
    }

    public synchronized void generateReportJrxml(HttpServletRequest request, HttpServletResponse response,
                                            HashMap reportConfigParams, HashMap reportParams) throws IOException, ReportExecuteException {

        ReportData reportData = new ReportData();
        reportData.setReportConfigParams(reportConfigParams);
        reportData.setReportParams(reportParams);

        Connection conn = null;
        try {
            conn = ConnectionFactory.getDBConnection().getConnection();
            reportData.putReportConfigParam("DBConnection", conn);

            //Execute the report
            ReportJrxmlGenerator reportJrxmlGenerator = new ReportJrxmlGenerator();
            reportData = reportJrxmlGenerator.generateReport(reportData);
            if (reportJrxmlGenerator.getErrors().containsKey("Error")) {
                throw new ReportExecuteException(reportJrxmlGenerator.getErrors().get("Error"));
            }
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
        String fileName;
        if (reportData.getReportParams().get("REPORT_TITLE") != null) {
            fileName = reportData.getReportParams().get("REPORT_TITLE").toString() + "." + reportConfigParams.get("reportFormat");
        } else {
            fileName = reportConfigParams.get("reportId") + "." + reportConfigParams.get("reportFormat");
        }

        String contentType = MimeTypeUtil.getMimetype(fileName);

        response.setContentType(contentType);
        response.setHeader("Content-disposition",
                "attachment; filename=\""
                        + fileName + "\"");
        response.setHeader("Cache-Control",
                "max-age=0");
        try {
            exporter.exportReport();
        } catch (JRException jex) {
            log.debug("ERROR in exporting the report....\n");
            jex.printStackTrace();
        }
        catch (ExceptionConverter ec) {
            log.debug("The download of the report was cancelled " + ec.getException());
        }
    }

    public synchronized void generateProtectedXls(ActionServlet actionServlet, HttpServletRequest request, HttpServletResponse response,
                                                  HashMap reportConfigParams, HashMap reportParams,
                                                  String listName, Parameters parameters,
                                                  HashMap<String, ReportData> subreports,
                                                  ArrayList<Integer> colsToRemove, boolean removeLastRow) throws IOException, ListStructureNotFoundException {
        parameters.setGenerateHiddenOrders(false);
        //Get the query
        FantabulousManager manager;
        ListStructure listStructure = null;
        manager = FantabulousManager.loadFantabulousManager(actionServlet.getServletContext(), request);
        listStructure = manager.getList(listName);
        listStructure = addAccessRightSecurity(listStructure, getUserId(request));

        SqlGenerator generator = SqlGeneratorManager.newInstance(); //sql generator name is declared in fantabulous-config.properties
        String sql = generator.generate(listStructure, parameters);
        log.debug("Report query, list:" + listName + "\n" + sql);
        ReportData reportData = new ReportData();
        reportData.setReportConfigParams(reportConfigParams);
        reportData.setReportParams(reportParams);
        reportData.putReportConfigParam("SqlQuery", sql);
        reportData.putReportConfigParam("ListStructure", listStructure);
        reportData.putReportParam("FANTABULOUS_PARAMETERS", parameters);

        //Merge columns
        Iterator i = subreports.keySet().iterator();
        while (i.hasNext()) {
            String subreport_name = (String) i.next();
            ReportData subReportData = subreports.get(subreport_name);
            reportData.putReportParam(subreport_name, subReportData.getJasperReport());
            mergeColumns(reportData.getReportParams(), subReportData.getReportParams());
        }

        //Executing the report
        ReportExecuteCmd reportExecuteCmd = new ReportExecuteCmd();
        reportExecuteCmd.putParam("op", "execute");
        reportExecuteCmd.putParam("reportData", reportData);
        Connection conn = null;
        try {
            conn = ConnectionFactory.getDBConnection().getConnection();
            reportData.putReportConfigParam("DBConnection", conn);

            //Execute the report
            reportData = new ReceiptGenerator().generateReport(reportData);

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
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        try {
            exporter.exportReport();
        } catch (JRException e) {
            log.debug("ERROR in exporting the report....\n");
            e.printStackTrace();
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(byteArrayInputStream);

        HSSFWorkbook wb = new HSSFWorkbook(poifsFileSystem);
        HSSFSheet hssfSheet = wb.getSheet(wb.getSheetName(0));
        hssfSheet.setMargin(HSSFSheet.TopMargin, 0.7);
        hssfSheet.setMargin(HSSFSheet.BottomMargin, 0.7);
        hssfSheet.setMargin(HSSFSheet.LeftMargin, 0.7);
        hssfSheet.setMargin(HSSFSheet.RightMargin, 0.7);
        hssfSheet.protectSheet(String.valueOf(Calendar.getInstance().getTimeInMillis()));//Password

        for (Integer col : colsToRemove) {
            hssfSheet.setColumnWidth(new Short(col.toString()), new Short("0"));
        }
        if (removeLastRow) {
            hssfSheet.removeRow(hssfSheet.getRow(hssfSheet.getPhysicalNumberOfRows() - 1));
        }

        String fileName = reportConfigParams.get("reportId") + "." + reportConfigParams.get("reportFormat");
        String contentType = MimeTypeUtil.getMimetype(fileName);

        response.setContentType(contentType);
        response.setHeader("Content-disposition",
                "attachment; filename=\""
                        + fileName + "\"");
        response.setHeader("Cache-Control",
                "max-age=0");
        try {
            wb.write(response.getOutputStream());
        }
        catch (ExceptionConverter ec) {
            log.debug("The download of the report was cancelled " + ec.getException());
        }
    }


    public HashMap getReportConfig(InputStream templateInputStream,
                                   String ReportFormat,
                                   String reportTempDirectory,
                                   String pageSize,
                                   String locale,
                                   String pageOrientation,
                                   String reportId,
                                   Boolean onlyCompile,
                                   ResourceBundle resourceBundle) {

        HashMap reportConfigParams = new HashMap();
        reportConfigParams.put("templateInputStream", templateInputStream);
        reportConfigParams.put("reportLocale", locale);
        reportConfigParams.put("reportPageOrientation", pageOrientation);
        reportConfigParams.put("reportId", reportId);
        reportConfigParams.put("reportFormat", ReportFormat);
        reportConfigParams.put("reportName", "templateReport");
        reportConfigParams.put("reportPageSize", pageSize);
        reportConfigParams.put("onlyCompile", onlyCompile);
        //reportConfigParams.put("resourceBundle",resourceBundle);
        if (reportTempDirectory != null) {
            reportConfigParams.put("ReportTempDirectory", reportTempDirectory);
        }
        return (reportConfigParams);
    }

    public HashMap getReportJrxmlConfig(String mainJrxmlFilePath,
                                        String ReportFormat,
                                        String locale,
                                        String reportName) {
        HashMap reportConfigParams = new HashMap();
        putMapValue("jrxmlFilePath", mainJrxmlFilePath, reportConfigParams);
        putMapValue("reportLocale", locale, reportConfigParams);
        putMapValue("reportId", reportName, reportConfigParams);
        putMapValue("reportFormat", ReportFormat, reportConfigParams);
        putMapValue("reportName", reportName, reportConfigParams);

        return (reportConfigParams);
    }

    private void putMapValue(String key, Object value, Map map) {
        if (map != null && value != null) {
            map.put(key, value);
        }
    }

    public HashMap getCsvReportConfigParams(String csvFieldDelimiter, String reportCharset) {
        HashMap reportConfigParams = new HashMap();
        if (!GenericValidator.isBlankOrNull(csvFieldDelimiter)) {
            reportConfigParams.put("csvFieldDelimiter", csvFieldDelimiter);
        }

        if (!GenericValidator.isBlankOrNull(reportCharset)) {
            reportConfigParams.put("characterEncoding", reportCharset);
        }
        return reportConfigParams;
    }

    public HashMap getReportParams(String reportTitleKey,
                                   String localeKey,
                                   String reportTimeZone,
                                   String sqlQueryLimit) {
        HashMap reportParams = new HashMap();
        Locale locale = new Locale(localeKey);
        String reportTitle = JSPHelper.getMessage(locale, reportTitleKey);
        reportParams.put("REPORT_TITLE", reportTitle);
        reportParams.put("REPORT_TIMEZONE", reportTimeZone);
        reportParams.put("LIMIT_SQL", sqlQueryLimit);
        return (reportParams);
    }

    public HashMap getReportParams(String reportTimeZone) {
        HashMap reportParams = new HashMap();
        reportParams.put("REPORT_TIMEZONE", reportTimeZone);
        return (reportParams);
    }

    public void addFileResolverReportParam(String jrxmlFilesDirPath, Map reportParams) {
        //add FILE RESOLVER report param, this is to find the sub report and images in resolver defined folder
        File reportsDir = new File(jrxmlFilesDirPath);
        reportParams.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));
    }

    public HashMap getReportParams(String reportTimeZone, String localeKey, ReportGeneratorForm generatorForm) {

        HashMap reportParams = getReportParams(reportTimeZone);
        Locale locale = new Locale(localeKey);
        String reportTitle = generatorForm.getReportTitle();
        Map reportConfigurations = generatorForm.getReportConfigParams();
        if ((reportTitle == null || reportTitle.trim().length() == 0) && reportConfigurations.get("title") != null) {
            reportTitle = JSPHelper.getMessage(locale, reportConfigurations.get("title").toString());
        }
        reportParams.put("REPORT_TITLE", reportTitle);
        return (reportParams);
    }

    public ResourceBundle getResourceBundle(String locale) {
        ResourceBundle resourceBundle;
        if (locale != null) {
            resourceBundle = ResourceBundle.getBundle(Constants.APPLICATION_RESOURCES, new Locale(locale));
        } else {
            resourceBundle = ResourceBundle.getBundle(Constants.APPLICATION_RESOURCES);
        }
        return (resourceBundle);
    }

    public String getTempDirectory() {
        //Get the temp directory
        String reportTempDirectory = null;
        String tempDirectory = ConfigurationFactory.getConfigurationManager().getValue("elwis.temp.folder");
        File tempDirectoryFile = new File(tempDirectory);
        if (tempDirectoryFile.exists()) {
            reportTempDirectory = tempDirectory;
        }
        return (reportTempDirectory);
    }

    /**
     * Retrieve the user company params for the report (sizes, stream, if the logo exists)
     *
     * @param request
     * @param showCompanyLogo
     * @return
     */
    public HashMap getCompanLogoInfo(HttpServletRequest request, boolean showCompanyLogo) {
        HashMap reportData = new HashMap();
        //Loading the company logo image..
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        CompanyCmd companyCmd = new CompanyCmd();
        companyCmd.putParam("addressId", user.getValue("companyId"));
        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(companyCmd, request);
        } catch (AppLevelException aplex) {
            log.error("ERROR in Executing the CompanyCmd: " + aplex.getMessage());

        }

        if (resultDTO != null) {
            Object logoId = resultDTO.get("logoId");
            if (logoId != null && showCompanyLogo) {
                //Get the image Stream
                ArrayByteWrapper image = null;
                try {
                    FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
                    FreeText freeText = freeTextHome.findByPrimaryKey(new Integer(logoId.toString()));
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(freeText.getValue());
                    reportData.put("IS_TITLE_IMAGE_STREAM", Boolean.valueOf(true));
                    reportData.put("TITLE_IMAGE_STREAM", byteArrayInputStream);
                    image = new ArrayByteWrapper(freeText.getValue());
                } catch (FinderException fex) {
                }
                //GET the image size
                if (image != null) {
                    try {
                        ImageIcon imageIcon = new ImageIcon(image.getFileData());
                        Image newImage = imageIcon.getImage();
                        reportData.put("TITLE_IMAGE_WIDTH", new Integer(newImage.getWidth(null)));
                        reportData.put("TITLE_IMAGE_HEIGHT", new Integer(newImage.getHeight(null)));
                    } catch (Exception ex) {
                        log.debug("ERROR, trying to get the logo image size");
                    }
                }
            } else {//If the company don't have a image
                reportData.put("IS_TITLE_IMAGE_STREAM", Boolean.valueOf(false));
                reportData.put("TITLE_IMAGE_STREAM", ReportGeneratorConstants.VOID_STREAM);
            }
        }
        return (reportData);
    }

    private void mergeColumns(HashMap reportData, HashMap subReportData) {
        Iterator keysIterator = subReportData.keySet().iterator();
        while (keysIterator.hasNext()) {
            String key = (String) keysIterator.next();
            if (key.startsWith("COLUMN_FORMAT_")) {
                reportData.put(key, subReportData.get(key));
            }
        }
    }

    private ListStructure addAccessRightSecurity(ListStructure sourceListStructure, Integer userId) {
        return AccessRightDataLevelSecurity.i.processAllAccessRightByList(sourceListStructure, userId);
    }

    private Integer getUserId(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        return new Integer(user.getValue(Constants.USERID).toString());
    }

}