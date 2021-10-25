package com.piramide.elwis.web.reports.form;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.piramide.elwis.cmd.reports.ChartCmd;
import com.piramide.elwis.cmd.reports.LightlyReportCmd;
import com.piramide.elwis.cmd.reports.ReaderChartFieldCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.reports.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ChartForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ChartForm extends DefaultForm {
    public void reset(ActionMapping actionMapping, HttpServletRequest request) {
        log.debug("Excecuting reset ChartForm.......");

        List totalizeList = new ArrayList(0);
        List columnGroupList = new ArrayList(0);

        ResultDTO resultDTO = new ResultDTO();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        ReaderChartFieldCmd chartFieldCmd = new ReaderChartFieldCmd();
        chartFieldCmd.putParam("reportId", request.getParameter("reportId"));
        chartFieldCmd.putParam("companyId", user.getValue("companyId"));

        try {
            resultDTO = BusinessDelegate.i.execute(chartFieldCmd, request);
            totalizeList = (List) resultDTO.get("totalizeList");
            columnGroupList = (List) resultDTO.get("columnList");
        } catch (AppLevelException e) {
            log.debug("Cannot retrieve chart fields...");
        }

        request.setAttribute("totalizeList", totalizeList);
        request.setAttribute("columnList", columnGroupList);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate ChartForm......." + getDtoMap());

        ActionErrors errors = new ActionErrors();
        if (getDto("delete") == null) {
            //validate form data
            errors = super.validate(mapping, request);

            if (errors.isEmpty()) {
                int chartType = Integer.parseInt(getDto("chartType").toString());
                validateChartTypeValues(chartType, errors, request);

                if (errors.isEmpty()) {
                    totalizersForeingKeyValidator(errors);
                    if (errors.isEmpty()) {
                        columnGroupsForeingKeyValidator(errors);
                    }
                }
            }
        }

        //external validate
        if (!errors.isEmpty() || getDto("delete") != null) {
            //validate report id
            ActionErrors reportErrors = new ActionErrors();
            reportErrors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_REPORT, "reportid",
                    request.getParameter("reportId"), reportErrors, new ActionError("Report.NotFound"));

            if (reportErrors.isEmpty()) {

                //verif if report type is single
                LightlyReportCmd cmd = new LightlyReportCmd();
                cmd.putParam("reportId", Integer.valueOf(request.getParameter("reportId")));
                cmd.setOp("read");

                Integer reportType = null;
                try {
                    BusinessDelegate.i.execute(cmd, request);
                    reportType = (Integer) cmd.getResultDTO().get("reportType");
                } catch (AppLevelException e) {
                    e.printStackTrace();
                }

                //validate if report as change how single report
                if (ReportConstants.SINGLE_TYPE.equals(reportType)) {
                    setDto("op", "read"); //validate in action
                    setDto("delete", null);
                    return new ActionErrors();  //empty errors
                }

                //validate if chart has be deleted
                if (getDto("chartId") != null) {
                    ChartCmd chartCmd = new ChartCmd();
                    chartCmd.putParam("op", "read");
                    chartCmd.putParam("chartId", getDto("chartId"));
                    chartCmd.putParam("reportId", request.getParameter("reportId"));
                    try {
                        BusinessDelegate.i.execute(chartCmd, request);
                        if (chartCmd.getResultDTO().isFailure()) {
                            errors.clear();
                            errors.add("chart", new ActionError("customMsg.NotFound", getDto("title")));
                            this.getDtoMap().clear();
                            return errors;
                        }
                    } catch (AppLevelException e) {
                        log.debug("Error in read chart...");
                    }
                }
            } else {
                setDto("delete", null);
                errors.clear(); //to find forward "MainSearch" in the action to show error message (report as deleted)
            }
        }
        return errors;
    }

    private void validateChartTypeValues(int chartType, ActionErrors errors, HttpServletRequest request) {

        int chartGroupType = CustomReportGeneratorHelper.getChartGroupType(chartType);

        if (CustomReportGeneratorConstants.CHARTGROUP_CATEGORIES == chartGroupType) {

            if (hasOrientation(errors, request)) {
                String orientation = String.valueOf(getDto("orientation"));
                Map resourcesMap = Functions.getResourcesOfChartGroupTypeValues(chartGroupType, orientation, request);
                if (GenericValidator.isBlankOrNull((String) getDto("serieId"))) {
                    errors.add("serie", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE)));
                }
                if (GenericValidator.isBlankOrNull((String) getDto("valueYId"))) {
                    errors.add("yvalueid", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE)));
                }

                validateGroupingValues(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE).toString(), resourcesMap.get(ReportConstants.KEY_CHARTVIEW_CATEGORY).toString(), errors);
            }

        } else if (CustomReportGeneratorConstants.CHARTGROUP_BUBBLES == chartGroupType) {

            if (hasOrientation(errors, request)) {
                String orientation = String.valueOf(getDto("orientation"));
                Map resourcesMap = Functions.getResourcesOfChartGroupTypeValues(chartGroupType, orientation, request);
                if (GenericValidator.isBlankOrNull((String) getDto("serieId"))) {
                    errors.add("serie", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE)));
                }
                if (GenericValidator.isBlankOrNull((String) getDto("valueXId"))) {
                    errors.add("xvalueid", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XVALUE)));
                }
                if (GenericValidator.isBlankOrNull((String) getDto("valueYId"))) {
                    errors.add("yvalueid", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE)));
                }
                if (GenericValidator.isBlankOrNull((String) getDto("valueZId"))) {
                    errors.add("zvalueid", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_ZVALUE)));
                }
            }

        } else if (CustomReportGeneratorConstants.CHARTGROUP_PIES == chartGroupType) {

            if (hasOrientation(errors, request)) {
                String orientation = String.valueOf(getDto("orientation"));
                Map resourcesMap = Functions.getResourcesOfChartGroupTypeValues(chartGroupType, orientation, request);
                if (GenericValidator.isBlankOrNull((String) getDto("serieId"))) {
                    errors.add("serie", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE)));
                }
                if (GenericValidator.isBlankOrNull((String) getDto("valueYId"))) {
                    errors.add("yvalueid", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE)));
                }

                //in pie not is required
                setDto("axisXLabel", null);
                setDto("axisYLabel", null);
            }

        } else if (CustomReportGeneratorConstants.CHARTGROUP_VALUESXY == chartGroupType) {

            if (hasOrientation(errors, request)) {
                String orientation = String.valueOf(getDto("orientation"));
                Map resourcesMap = Functions.getResourcesOfChartGroupTypeValues(chartGroupType, orientation, request);
                if (GenericValidator.isBlankOrNull((String) getDto("serieId"))) {
                    errors.add("serie", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE)));
                }
                if (GenericValidator.isBlankOrNull((String) getDto("valueXId"))) {
                    errors.add("xvalueid", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_XVALUE)));
                }
                if (GenericValidator.isBlankOrNull((String) getDto("valueYId"))) {
                    errors.add("yvalueid", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE)));
                }
            }

        } else if (CustomReportGeneratorConstants.CHARTGROUP_TIMESERIES == chartGroupType) {

            if (hasOrientation(errors, request)) {
                String orientation = String.valueOf(getDto("orientation"));
                Map resourcesMap = Functions.getResourcesOfChartGroupTypeValues(chartGroupType, orientation, request);
                if (GenericValidator.isBlankOrNull((String) getDto("serieId"))) {
                    errors.add("serie", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE)));
                }
                if (GenericValidator.isBlankOrNull((String) getDto("valueYId"))) {
                    errors.add("yvalueid", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_YVALUE)));
                }
                if (GenericValidator.isBlankOrNull((String) getDto("categoryId"))) {
                    errors.add("category", new ActionError("errors.required", resourcesMap.get(ReportConstants.KEY_CHARTVIEW_CATEGORY)));
                }

                validateGroupingValues(resourcesMap.get(ReportConstants.KEY_CHARTVIEW_SERIE).toString(), resourcesMap.get(ReportConstants.KEY_CHARTVIEW_CATEGORY).toString(), errors);
            }
        }
    }

    private boolean hasOrientation(ActionErrors errors, HttpServletRequest request) {
        boolean res = true;
        if (GenericValidator.isBlankOrNull((String) getDto("orientation"))) {
            errors.add("orientation", new ActionError("errors.required", JSPHelper.getMessage(request, "Report.chart.orientation")));
            res = false;
        }
        return res;
    }

    private void validateGroupingValues(String serieLabel, String categoryLabel, ActionErrors errors) {
        String serieValue = (String) getDto("serieId");
        String categoryValue = (String) getDto("categoryId");
        if (!GenericValidator.isBlankOrNull(categoryValue) && !GenericValidator.isBlankOrNull(serieValue)) {
            if (serieValue.equals(categoryValue)) {
                errors.add("values", new ActionError("Report.chart.error.differentGroup", serieLabel, categoryLabel));
            }
        }
    }

    /**
     * validate totalize foreing key
     *
     * @param errors
     */
    private void totalizersForeingKeyValidator(ActionErrors errors) {
        log.debug("XVALUE:" + getDto("valueXId"));
        log.debug("YVALUE:" + getDto("valueYId"));
        log.debug("ZVALUE:" + getDto("valueZId"));

        if (!GenericValidator.isBlankOrNull((String) getDto("valueXId"))) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_TOTALIZE, "reporttotalizeid",
                    getDto("valueXId"), errors, new ActionError("Report.chart.error.columnDeleted"));
            if (!errors.isEmpty()) {
                return;
            }
        }
        if (!GenericValidator.isBlankOrNull((String) getDto("valueYId"))) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_TOTALIZE, "reporttotalizeid",
                    getDto("valueYId"), errors, new ActionError("Report.chart.error.columnDeleted"));
            if (!errors.isEmpty()) {
                return;
            }
        }
        if (!GenericValidator.isBlankOrNull((String) getDto("valueZId"))) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_TOTALIZE, "reporttotalizeid",
                    getDto("valueZId"), errors, new ActionError("Report.chart.error.columnDeleted"));
            if (!errors.isEmpty()) {
                return;
            }
        }
    }

    /**
     * validate columnGroup foreing key
     *
     * @param errors
     */
    private void columnGroupsForeingKeyValidator(ActionErrors errors) {
        log.debug("SERIE:" + getDto("serieId"));
        log.debug("CATEGORY:" + getDto("categoryId"));

        if (!GenericValidator.isBlankOrNull((String) getDto("serieId"))) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_COLUMNGROUP, "columngroupid",
                    getDto("serieId"), errors, new ActionError("Report.chart.error.columnDeleted"));
            if (!errors.isEmpty()) {
                return;
            }
        }
        if (!GenericValidator.isBlankOrNull((String) getDto("categoryId"))) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_COLUMNGROUP, "columngroupid",
                    getDto("categoryId"), errors, new ActionError("Report.chart.error.columnDeleted"));
            if (!errors.isEmpty()) {
                return;
            }
        }
    }
}
