package com.piramide.elwis.web.reports.action;

import com.jatun.titus.reportgenerator.CompileJrxmlException;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.reportgenerator.util.ReportJrxmlCompilerUtil;
import com.piramide.elwis.cmd.campaignmanager.DownloadDocumentCmd;
import com.piramide.elwis.cmd.reports.ReportArtifactCmd;
import com.piramide.elwis.cmd.reports.ReportQueryParamCmd;
import com.piramide.elwis.cmd.reports.ReportReadCmd;
import com.piramide.elwis.cmd.utils.ReportJrxmlCacheManager;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ReportActionUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.reports.el.Functions;
import com.piramide.elwis.web.reports.el.ReportSettingUtil;
import com.piramide.elwis.web.reports.util.ReportExecuteException;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class ReportJrxmlExecuteAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.debug("Executing ReportJrxmlExecuteAction............" + request.getParameterMap());
        ActionForward forward;

        if (null != (forward = validateReportExistence(request, mapping))) {
            return forward;
        }

        return processReport(mapping, form, request, response);
    }

    protected ActionForward validateReportExistence(HttpServletRequest request, ActionMapping mapping) {
        //validate report id
        ActionErrors errors = new ActionErrors();
        if (null != request.getParameter("reportId")) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_REPORT, "reportid",
                    request.getParameter("reportId"), errors, new ActionError("Report.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("ReportMainSearch");
            }
        } else {
            errors.add("reportid", new ActionError("Report.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("ReportMainSearch");
        }
        return null;
    }


    protected ActionForward processReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        ActionErrors errors = new ActionErrors();

        //execute process
        DefaultForm executeForm = (DefaultForm) form; //todo: change when will be implemented
        String reportFormat = (String) executeForm.getDto("reportFormat");
        String csvDelimiter = (String) executeForm.getDto("csvDelimiter");
        String reportCharset = (String) executeForm.getDto("reportCharset");

        Integer reportId = Integer.valueOf(request.getParameter("reportId"));
        ReportActionUtil reportActionUtil = new ReportActionUtil();

        User user = RequestUtils.getUser(request);
        Integer userId = new Integer(user.getValue("userId").toString());
        Locale locale = new Locale(user.getValue("locale").toString());
        DateTimeZone timeZone = (DateTimeZone) user.getValueMap().get("dateTimeZone");


        //read report data
        ReportReadCmd reportReadCmd = new ReportReadCmd();
        reportReadCmd.putParam("reportId", reportId);

        ResultDTO resultDTO = BusinessDelegate.i.execute(reportReadCmd, request);
        String reportName = (String) resultDTO.get("name");
        Integer companyId = (Integer) resultDTO.get("companyId");
        ArrayByteWrapper jrxmlWrapper = (ArrayByteWrapper) resultDTO.get("fileWrapper");

        //clean cache dir
        ReportJrxmlCacheManager.deleteReportFolder(companyId, userId, reportId);
        String reportDirPath = ReportJrxmlCacheManager.pathReportFolder_CreateIfNotExist(companyId, userId, reportId, false);

        //save the main jrxml report in elwis cache
        String mainJrxmlFilePath = ReportJrxmlCacheManager.saveReportArtifact(companyId, userId, reportId, jrxmlWrapper.getFileData(), jrxmlWrapper.getFileName());

        try {
            //save the artifact jrxml files in elwis cache
            saveAndCompileJrxmlArtifactFiles(companyId, userId, reportId, request);
        } catch (CompileJrxmlException e) {
            log.debug("Error in compile jrxml files....", e);
            errors.add("CompileError", new ActionError("Report.jrxml.error.compile", e.getJrxmlFileName(), e.getMessage()));
        }

        //report config params
        HashMap reportConfigParams = reportActionUtil.getReportJrxmlConfig(mainJrxmlFilePath, reportFormat, locale.toString(), reportName);

        if (ReportGeneratorConstants.REPORT_FORMAT_CSV.equals(reportFormat)) {
            reportConfigParams.putAll(reportActionUtil.getCsvReportConfigParams(csvDelimiter, reportCharset));
        }

        //report params
        HashMap reportParams = reportActionUtil.getReportParams(timeZone.toString());
        //add the file resolver param
        reportActionUtil.addFileResolverReportParam(reportDirPath, reportParams);

        Map queryParams = getReportQueryParams(reportId, companyId, request);
        queryParams = updateQueryParamFilterValuesChangedInForm(queryParams, request);
        queryParams = fixNullQueryParamValues(queryParams);

        if (isValidReportQueryParams(reportId, request)) {
            reportParams.putAll(queryParams);
        } else {
            errors.add("valueError", new ActionError("Report.jrxml.error.paramValues"));
        }

        if (errors.isEmpty()) {
            try {
                //generate the report
                reportActionUtil.generateReportJrxml(request, response, reportConfigParams, reportParams);
                //save some settings
                saveUserExportSettings(reportFormat, csvDelimiter, reportCharset, request);

                //delete jrxml cache files
                ReportJrxmlCacheManager.deleteReportFolder(companyId, userId, reportId);
            } catch (ReportExecuteException e) {
                log.debug("Error in execute....", e);
                errors.add("genError", new ActionError("Report.jrxml.error.generate", e.getMessage() != null ? e.getMessage() : ""));
            }
        }

        if (!errors.isEmpty()) {
            actionForward = mapping.findForward("FailButStay");
            saveErrors(request, errors);
        }

        return actionForward;
    }

    private boolean isValidReportQueryParams(Integer reportId, HttpServletRequest request) {
        boolean isValid = false;

        ReportQueryParamCmd reportQueryParamCmd = new ReportQueryParamCmd();
        reportQueryParamCmd.setOp("isAllParamWithFilter");
        reportQueryParamCmd.putParam("reportId", reportId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(reportQueryParamCmd, request);
            if (resultDTO.get("isAllRelatedToFilter") != null) {
                isValid = (Boolean) resultDTO.get("isAllRelatedToFilter");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd.." + ReportQueryParamCmd.class);
        }

        return isValid;
    }

    /**
     * read all report query param values
     * All values should be of type String
     *
     * @param reportId
     * @param companyId
     * @param request
     * @return
     */
    private Map<String, String> getReportQueryParams(Integer reportId, Integer companyId, HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();

        ReportQueryParamCmd reportQueryParamCmd = new ReportQueryParamCmd();
        reportQueryParamCmd.setOp("readReportParamValues");
        reportQueryParamCmd.putParam("reportId", reportId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(reportQueryParamCmd, request);
            Map queryParamValues = (Map) resultDTO.get("queryParamValuesMap");
            if (queryParamValues != null) {
                params.putAll(queryParamValues);
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd.." + ReportQueryParamCmd.class);
        }

        //add default company id as param
        params.put("companyId", companyId.toString());
        return params;
    }

    private Map updateQueryParamFilterValuesChangedInForm(Map queryParams, HttpServletRequest request) {
        Map filterValueChangedMap = (Map) request.getAttribute("filterValuesMap");

        if (filterValueChangedMap != null) {

            for (Iterator iterator = filterValueChangedMap.keySet().iterator(); iterator.hasNext();) {
                String filterId = iterator.next().toString();
                Map reportQueryParamDtoMap = Functions.getReportQueryParamRelatedToFilter(filterId);

                if (!reportQueryParamDtoMap.isEmpty()) {
                    String parameterName = (String) reportQueryParamDtoMap.get("parameterName");
                    List valuesList = (List) filterValueChangedMap.get(filterId);
                    if (!valuesList.isEmpty() && queryParams.containsKey(parameterName)) {
                        Object value = valuesList.get(0);
                        queryParams.put(parameterName, (value != null) ? value.toString() : "");
                    }
                }
            }
        }

        return queryParams;
    }

    private Map fixNullQueryParamValues(Map queryParams) {
        for (Iterator iterator = queryParams.keySet().iterator(); iterator.hasNext();) {
            String paramKey = iterator.next().toString();
            Object value = queryParams.get(paramKey);

            if (value == null || GenericValidator.isBlankOrNull(value.toString())) {
                queryParams.put(paramKey, ReportConstants.JRXMLREPORT_ALL_FILTER);
            }
        }
        return queryParams;
    }

    private void saveAndCompileJrxmlArtifactFiles(Integer companyId, Integer userId, Integer reportId, HttpServletRequest request) throws IOException, CompileJrxmlException {

        ReportArtifactCmd artifactCmd = new ReportArtifactCmd();
        artifactCmd.setOp("findByReport");
        artifactCmd.putParam("reportId", reportId);

        List<Map> artifactMapList = null;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(artifactCmd, request);
            artifactMapList = (List<Map>) resultDTO.get("artifactInfoList");
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd.." + ReportArtifactCmd.class);
        }

        if (artifactMapList != null) {
            for (Map artifactMap : artifactMapList) {
                Integer fileId = (Integer) artifactMap.get("fileId");
                String fileName = (String) artifactMap.get("fileName");

                DownloadDocumentCmd downloadCmd = new DownloadDocumentCmd();
                downloadCmd.putParam("freeTextId", fileId);

                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(downloadCmd, request);
                    if (resultDTO.get("freeText") != null) {
                        ArrayByteWrapper fileWrapper = (ArrayByteWrapper) resultDTO.get("freeText");
                        //save the artifact file
                        ReportJrxmlCacheManager.saveReportArtifact(companyId, userId, reportId, fileWrapper.getFileData(), fileName);
                    }
                } catch (AppLevelException e) {
                    log.debug("Error in execute cmd.." + DownloadDocumentCmd.class);
                }
            }
        }

        //compile the jrxml files
        ReportJrxmlCompilerUtil.compileJrxmlDirectory(ReportJrxmlCacheManager.pathReportFolder_CreateIfNotExist(companyId, userId, reportId, false));
    }

    private void saveUserExportSettings(String reportFormat, String csvDelimiter, String reportCharset, HttpServletRequest request) {
        //save user csv delimiter configuration
        if (ReportGeneratorConstants.REPORT_FORMAT_CSV.equals(reportFormat)) {
            ReportSettingUtil.saveUserReportSettingConfig(csvDelimiter, reportCharset, request);
        }
    }
}
