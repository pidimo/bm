package com.piramide.elwis.web.reports.action;

import com.jatun.titus.customreportgenerator.reportstructure.ReportDesign;
import com.jatun.titus.customreportgenerator.util.ChartUtil;
import com.jatun.titus.listgenerator.Titus;
import com.jatun.titus.listgenerator.engine.FantabulousStructureCompleter;
import com.jatun.titus.listgenerator.engine.fantabulous.CustomConditionManager;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.StructureConstants;
import com.jatun.titus.listgenerator.structure.StructureManager;
import com.jatun.titus.listgenerator.structure.Table;
import com.jatun.titus.listgenerator.structure.converter.ConverterManager;
import com.jatun.titus.listgenerator.structure.externalbuilder.FantabulousCreateColumn;
import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.jatun.titus.listgenerator.util.TitusPathUtil;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.CustomReportGeneratorExecutor;
import com.jatun.titus.web.StrutsResourceBundle;
import com.piramide.elwis.cmd.contactmanager.CompanyCmd;
import com.piramide.elwis.cmd.reports.ReaderFieldCmd;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.reports.el.ReportSettingUtil;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenerator;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGeneratorManager;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ReportExecuteAction.java 10522 2015-03-16 23:06:32Z miguel $
 */

public class ReportExecuteAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.debug("Executing ReportExecuteAction............");
        //validate report id
        ActionErrors errors = new ActionErrors();
        if (null != request.getParameter("reportId")) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_REPORT, "reportid",
                    request.getParameter("reportId"), errors, new ActionError("Report.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }
        } else {
            errors.add("reportid", new ActionError("Report.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        return processReport(mapping, form, request, response);

    }

    protected ActionForward processReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //execute process
        DefaultForm executeForm = (DefaultForm) form; //todo: change when will be implemented
        String reportPageSize = (String) executeForm.getDto("pageSize");
        String reportFormat = (String) executeForm.getDto("reportFormat");
        String reportPageOrientation = (String) executeForm.getDto("pageOrientation");
        String csvDelimiter = (String) executeForm.getDto("csvDelimiter");
        String reportCharset = (String) executeForm.getDto("reportCharset");

        InputStream templateInputStream = request.getSession().getServletContext().getResourceAsStream("/common/reports/customreporttemplate.jrxml");
        ConverterManager converterManager = Titus.getConverterManager(request.getSession().getServletContext());
        CustomConditionManager customConditionManager = Titus.getCustomConditionManager(request.getSession().getServletContext());

        Integer reportId = Integer.valueOf(request.getParameter("reportId"));
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue("companyId");
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());

        ReaderFieldCmd cmd = new ReaderFieldCmd();
        cmd.putParam("reportId", reportId);
        cmd.putParam("companyId", companyId);

        ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
        List columnDtoList = (List) resultDTO.get("columns");
        List filterDtoList = (List) resultDTO.get("filters");

        log.debug("Filters:" + filterDtoList);
        //update filter changed values in form
        filterDtoList = updateFilterValuesChangedInForm(filterDtoList, request);
        log.debug("New values to filters:" + filterDtoList);

        //validate report columns
        if (columnDtoList.size() <= 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("NoColumns", new ActionError("report.error.nocolumns"));
            saveErrors(request, actionErrors);
            return mapping.findForward("FailColumn");
        }

        //structure manager
        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());

        //complete list structure
        List titusColumnDataList = composeTitusColumnData(columnDtoList);
        filterDtoList = buildFilterDtosWithPKValues(filterDtoList, structureManager);

        log.debug("COLUMNSS:::::::::::::::::" + titusColumnDataList);

        Locale reportLocale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        DateTimeZone reportTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
        ResourceBundleWrapper resourceBundleWrapper = new StrutsResourceBundle(Constants.APPLICATION_RESOURCES, reportLocale);
        Map converterParametersMap = new HashMap();
        converterParametersMap.put("locale", reportLocale);
        converterParametersMap.put("timezone", reportTimeZone);

        Map customFiltererParamMap = new HashMap();
        customFiltererParamMap.put("timeZone", reportTimeZone);

        FantabulousStructureCompleter.i.complete(filterDtoList,
                titusColumnDataList,
                structureManager,
                converterManager,
                converterParametersMap,
                resourceBundleWrapper,
                customConditionManager,
                customFiltererParamMap);

        ListStructure fantaListStructure = FantabulousStructureCompleter.i.getListStructure();
        //add access right security on data level
        fantaListStructure = AccessRightDataLevelSecurity.i.processAllAccessRightByTable(fantaListStructure, userId);

        Map fieldAliasMap = FantabulousStructureCompleter.i.getFieldAliasMap();

        Parameters parameters = new Parameters();
        SqlGenerator generator = SqlGeneratorManager.newInstance();
        parameters.addSearchParameter("companyId", companyId.toString());
        String sqlQuery = generator.generate(fantaListStructure, parameters);
        log.debug("SQL LIST:::::::::::\n" + sqlQuery);
        //End of the generation of the SQL query
        log.debug("LISTTTTTTT:" + fantaListStructure);

        //chart part
        boolean sqlChartHasData = false; //todo: this is flag to chart execute or no
        String sqlChartQuery = null;
        log.debug("CHARTTTTT:" + resultDTO.get("chartData"));
        if (resultDTO.get("chartData") != null) {
            Map chartDtoMap = (Map) resultDTO.get("chartData");
            Map chartColumnGroupsData = getChartColumnGroupsData(chartDtoMap);
            List totalizersColumnPaths = (List) chartDtoMap.get("totalizersPath");
            int chartType = Integer.parseInt(chartDtoMap.get("chartType").toString());

            log.debug("COLCHARTTTTTTTTTTT:" + chartColumnGroupsData);

            //list structure to chart
            fantaListStructure = ChartUtil.getSpecialListStructureToChart(fantaListStructure,
                    chartColumnGroupsData,
                    totalizersColumnPaths,
                    fieldAliasMap,
                    structureManager,
                    chartType,
                    customConditionManager);
            sqlChartQuery = generator.generate(fantaListStructure, parameters);
            log.debug("SQL TO CHART:::::::::::\n" + sqlChartQuery);

            //flag to sql chart
            sqlChartHasData = ChartUtil.listStructureHasData(fantaListStructure, parameters);
        }
        //chart part

        /**Report generation*/
        String reportTitleStr = (String) executeForm.getDto("newReportTitle");
        if (GenericValidator.isBlankOrNull(reportTitleStr)) {
            reportTitleStr = ((Map) resultDTO.get("reportData")).get("name").toString();
        } else {
            ((Map) resultDTO.get("reportData")).put("name", reportTitleStr);
        }
        HashMap companyImageData = getReportImage(request);
        CustomReportGeneratorExecutor reportExecutor = new CustomReportGeneratorExecutor();

        HashMap reportGenerationParams = getReportFillParams(reportLocale, resourceBundleWrapper, reportTimeZone, companyImageData, reportFormat, csvDelimiter, reportCharset);
        ReportDesign reportDesign = reportExecutor.createReportStructure(resultDTO, templateInputStream, fieldAliasMap, structureManager, sqlQuery,
                reportPageSize, reportGenerationParams, reportLocale, reportTimeZone,
                companyImageData, reportPageOrientation, resourceBundleWrapper, sqlChartQuery, sqlChartHasData);


        JasperDesign jasperDesign = reportExecutor.generateReport(fieldAliasMap, reportDesign, resourceBundleWrapper, converterManager, reportFormat, reportGenerationParams);
        JRAbstractExporter abstractExporter = reportExecutor.showReport(jasperDesign, response, reportGenerationParams, reportFormat, reportTitleStr,
                MimeTypeUtil.getMimetype(reportTitleStr + "." + reportFormat));

        ActionForward actionForward = null;
        if (reportExecutor.isError()) {
            actionForward = mapping.findForward("FailButStay");
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("badReportGeneration", new ActionError("Report.customReportGeneration.error"));
            saveErrors(request, actionErrors);
        } else {
            abstractExporter.exportReport();
            //save some settings
            saveUserExportSettings(reportFormat, csvDelimiter, reportCharset, request);
        }
        return actionForward;
    }

    ///begin miky
    /**
     * compose titus column data list
     *
     * @param columnDtoList
     * @return List
     */
    private List composeTitusColumnData(List columnDtoList) {
        List resultList = new LinkedList();
        for (Iterator iterator = columnDtoList.iterator(); iterator.hasNext();) {
            Map columnMap = (Map) iterator.next();
            Map titusColumnDataMap = new HashMap();

            titusColumnDataMap.put(FantabulousCreateColumn.KEY_COLUMNPATH, columnMap.get("path"));
            titusColumnDataMap.put(FantabulousCreateColumn.KEY_ISCOLUMNGROUP, columnMap.get("isGrouped"));

            if (((Boolean) columnMap.get("isGrouped")).booleanValue()) {

                titusColumnDataMap.put(FantabulousCreateColumn.KEY_COLUMNGROUPSEQUENCE, columnMap.get("groupSequence"));
                if (columnMap.get("groupOrder") != null) {
                    Boolean order = (Boolean) columnMap.get("groupOrder");
                    if (order.booleanValue() == ReportConstants.ASCENDING_ORDER.booleanValue()) {
                        titusColumnDataMap.put(FantabulousCreateColumn.KEY_COLUMNGROUPORDER, StructureConstants.OrderType.ASCENDENT);
                    } else {
                        titusColumnDataMap.put(FantabulousCreateColumn.KEY_COLUMNGROUPORDER, StructureConstants.OrderType.DESCENDENT);
                    }
                }
            }

            if (columnMap.get("columnOrder") != null) {
                Boolean order = (Boolean) columnMap.get("columnOrder");
                if (order.booleanValue() == ReportConstants.ASCENDING_ORDER.booleanValue()) {
                    titusColumnDataMap.put(FantabulousCreateColumn.KEY_COLUMNORDER, StructureConstants.OrderType.ASCENDENT);
                } else {
                    titusColumnDataMap.put(FantabulousCreateColumn.KEY_COLUMNORDER, StructureConstants.OrderType.DESCENDENT);
                }
            }
            //add data to list
            resultList.add(titusColumnDataMap);
        }
        return resultList;
    }

    /**
     * build filter dtos when the value of filter is an primary key
     *
     * @param filterDtoList
     * @param structureManager
     * @return List
     */
    private List buildFilterDtosWithPKValues(List filterDtoList, StructureManager structureManager) {
        List tempFilterList = new ArrayList();

        for (Iterator iterator = filterDtoList.iterator(); iterator.hasNext();) {
            Map filterMap = (Map) iterator.next();
            if (ReportConstants.FILTER_WITH_DB_VALUE.equals((Integer) filterMap.get("filterType"))) {

                //remove this dto
                iterator.remove();

                Table titusTable = structureManager.getTable(filterMap.get("tableReference").toString());
                String filterFieldPath = filterMap.get("path").toString();

                //is relation filter
                String relationFilterTableName = TitusPathUtil.getTableNameOwner(filterFieldPath);
                log.debug("TABLE-NAME:" + titusTable.getName());
                log.debug("RELATION-TABLE-NAME:" + relationFilterTableName);
                log.debug("TITUS-PATH:" + filterFieldPath);
                boolean isRelationFilterTable;
                if (!relationFilterTableName.equals(titusTable.getName())) {
                    titusTable = structureManager.getTable(relationFilterTableName);
                    isRelationFilterTable = true;
                } else {
                    isRelationFilterTable = false;
                }

                int pkCount = 0;
                Map primaryKeyPathMap = TitusPathUtil.getPrimaryKeysTitusPathFromField(titusTable, filterFieldPath, isRelationFilterTable);
                List allValues = (List) filterMap.get("values");
                List pkFilterList = new ArrayList();
                for (Iterator iterator2 = primaryKeyPathMap.keySet().iterator(); iterator2.hasNext();) {
                    String primaryKeyPath = (String) iterator2.next();
                    Field primaryField = (Field) primaryKeyPathMap.get(primaryKeyPath);

                    Map newFilterMap = new HashMap();
                    newFilterMap.putAll(filterMap);
                    //compose filterDTO to primary keys
                    newFilterMap.put("tableReference", titusTable.getName());
                    newFilterMap.put("columnReference", primaryField.getName());
                    newFilterMap.put("columnType", new Integer(primaryField.getType().getType()));
                    newFilterMap.put("path", primaryKeyPath);
                    newFilterMap.put("values", getPkFilterValues(pkCount, primaryKeyPathMap.size(), allValues));

                    //add to new list primary key dtos
                    pkFilterList.add(newFilterMap);
                    pkCount++;
                }

                if (primaryKeyPathMap.size() == 1) {
                    //this is onle one field as PK, So add as MAP
                    tempFilterList.add(pkFilterList.get(0));
                } else {
                    //this is composed pk, So add as List
                    tempFilterList.add(pkFilterList);
                }
            }
        }
        //add temp filters dtos
        filterDtoList.addAll(tempFilterList);
        log.debug("FILTER-LIST:" + filterDtoList);

        return filterDtoList;
    }

    private List getPkFilterValues(int pkFieldPosition, int tablePKSize, List allValues) {
        List pkValues = new ArrayList();
        if (tablePKSize == 1) {
            pkValues = allValues;
        } else {
            for (int i = pkFieldPosition; i < allValues.size(); i += tablePKSize) {
                pkValues.add(allValues.get(i));
            }
        }
        return pkValues;
    }

    /**
     * get chart colums groups data from chart dto
     *
     * @param chartDtoMap
     * @return Map
     */
    private Map getChartColumnGroupsData(Map chartDtoMap) {
        Map resultMap = new HashMap();
        if (chartDtoMap.containsKey("seriePath")) {
            resultMap.put(ChartUtil.KEY_SERIE_COLUMNPATH, chartDtoMap.get("seriePath"));
            //order
            Boolean order = (Boolean) chartDtoMap.get("serieOrder");
            if (order.booleanValue() == ReportConstants.ASCENDING_ORDER.booleanValue()) {
                resultMap.put(ChartUtil.KEY_SERIE_ORDER, StructureConstants.OrderType.ASCENDENT);
            } else {
                resultMap.put(ChartUtil.KEY_SERIE_ORDER, StructureConstants.OrderType.DESCENDENT);
            }
        }
        if (chartDtoMap.containsKey("categoryPath")) {
            resultMap.put(ChartUtil.KEY_CATEGORY_COLUMNPATH, chartDtoMap.get("categoryPath"));
            //order
            Boolean order = (Boolean) chartDtoMap.get("categoryOrder");
            if (order.booleanValue() == ReportConstants.ASCENDING_ORDER.booleanValue()) {
                resultMap.put(ChartUtil.KEY_CATEGORY_ORDER, StructureConstants.OrderType.ASCENDENT);
            } else {
                resultMap.put(ChartUtil.KEY_CATEGORY_ORDER, StructureConstants.OrderType.DESCENDENT);
            }
        }
        return resultMap;
    }

    //end miky
    //RIGHT HERE--------------------------- IS MINE----------------------------------
    /**
     * Gets the company logo as a Input Stream and returns his size
     *
     * @param request
     * @return The company logo and his size
     */
    private HashMap getReportImage(HttpServletRequest request) {
        HashMap res = new HashMap();
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
            if (logoId != null) {
                //Get the image Stream
                ArrayByteWrapper image = null;
                try {
                    FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
                    FreeText freeText = freeTextHome.findByPrimaryKey(new Integer(logoId.toString()));
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(freeText.getValue());
                    res.put("EXISTS_IMAGE_STREAM", Boolean.valueOf(true));
                    res.put("IMAGE_STREAM", byteArrayInputStream);
                    image = new ArrayByteWrapper(freeText.getValue());
                } catch (FinderException fex) {
                }
                //GET the image size
                if (image != null) {
                    try {
                        ImageIcon imageIcon = new ImageIcon(image.getFileData());
                        Image newImage = imageIcon.getImage();
                        res.put("IMAGE_WIDTH", new Integer(newImage.getWidth(null)));
                        res.put("IMAGE_HEIGHT", new Integer(newImage.getHeight(null)));
                    } catch (Exception ex) {
                        log.debug("ERROR, trying to get the logo image size");
                    }
                }
            } else {//If the company dont have a image
                res.put("EXISTS_IMAGE_STREAM", Boolean.valueOf(false));
            }
        }
        return (res);
    }

    /**
     * Adds params needed to fill the report to the parameters map
     *
     * @param locale
     * @param resourceBundleWrapper
     * @param dateTimeZone
     * @return the map with the params
     */
    private HashMap getReportFillParams(Locale locale, ResourceBundleWrapper resourceBundleWrapper,
                                        DateTimeZone dateTimeZone, HashMap companyImageData, String reportFormat,
                                        String csvFieldDelimiter, String reportCharset) {
        HashMap parameters = new HashMap();
        parameters.put("RESOURCE_BUNDLE_WRAPPER", resourceBundleWrapper);
        HashMap converterParams = new HashMap();
        converterParams.put("locale", locale);
        converterParams.put("timezone", dateTimeZone);

        parameters.put("CONVERTER_PARAMS", converterParams);
        parameters.put("REPORT_LOCALE", locale);
        parameters.put("REPORT_TIMEZONE", dateTimeZone);
        if ((Boolean.valueOf(companyImageData.get("EXISTS_IMAGE_STREAM").toString()).booleanValue())) {
            parameters.put("TITLE_IMAGE_STREAM", (InputStream) companyImageData.get("IMAGE_STREAM"));
        }

        //config for csv export format
        if (ReportGeneratorConstants.REPORT_FORMAT_CSV.equals(reportFormat)) {
            if (!GenericValidator.isBlankOrNull(csvFieldDelimiter)) {
                parameters.put("csvFieldDelimiter", csvFieldDelimiter);
            }

            if (!GenericValidator.isBlankOrNull(reportCharset)) {
                parameters.put("characterEncoding", reportCharset);
            }
        }

        return (parameters);
    }

    /**
     * Update filter changed values in the form
     *
     * @param filterDtoList
     * @param request
     * @return list
     */
    private List updateFilterValuesChangedInForm(List filterDtoList, HttpServletRequest request) {
        Map filterValueChangedMap = (Map) request.getAttribute("filterValuesMap");

        if (filterValueChangedMap != null) {
            for (Iterator iterator = filterDtoList.iterator(); iterator.hasNext();) {
                Map filterDto = (Map) iterator.next();
                String filterId = filterDto.get("filterId").toString();
                if (filterValueChangedMap.containsKey(filterId)) {
                    filterDto.put("values", filterValueChangedMap.get(filterId));
                }
            }
        }

        return filterDtoList;
    }

    private void saveUserExportSettings(String reportFormat, String csvDelimiter, String reportCharset, HttpServletRequest request) {
        //save user csv delimiter configuration
        if (ReportGeneratorConstants.REPORT_FORMAT_CSV.equals(reportFormat)) {
            ReportSettingUtil.saveUserReportSettingConfig(csvDelimiter, reportCharset, request);
        }
    }
}
