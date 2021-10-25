package com.piramide.elwis.web.projects.form;

import com.piramide.elwis.cmd.projects.ProjectUtilsCmd;
import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.dto.projects.ProjectTimeLimitDTO;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FantabulousUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.projects.el.Functions;
import com.piramide.elwis.web.projects.util.ProjectUserExistenceValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProjectTimeForm extends DefaultForm {
    private ProjectDTO projectDTO;

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate ProjectTimeForm......." + getDtoMap());

        if (!Functions.existsProject(getDto("projectId"))) {
            return new ActionErrors();
        }

        ProjectUserExistenceValidator existenceValidator = new ProjectUserExistenceValidator();
        if (null != existenceValidator.isUserOfProject(request, mapping)) {
            return new ActionErrors();
        }

        projectDTO = Functions.getProject(request, getDto("projectId").toString());

        ActionErrors errors = super.validate(mapping, request);

        ActionError invoiceStatusChangeError = validateInvoiceStatusChange(request);
        if (null != invoiceStatusChangeError) {
            errors.add("invoiceStatusChangeError", invoiceStatusChangeError);
        }

        ActionError projectActivityRequiredError = validateProjectActivity(request);
        if (null != projectActivityRequiredError) {
            errors.add("projectActivityRequiredError", projectActivityRequiredError);
        }

        ActionError subProjectRequiredError = validateSubProject(request);
        if (null != subProjectRequiredError) {
            errors.add("subProjectRequiredError", subProjectRequiredError);
        }

        ActionError projectTimeDateError = validateProjectTimeDate(request);
        if (null != projectTimeDateError) {
            errors.add("projectTimeDateError", projectTimeDateError);
        }

        ActionError projectTimeError = validateTime(request);
        if (null != projectTimeError) {
            errors.add("projectTimeError", projectTimeError);
        }

        ActionError totalTimesError = validateTotalInvoicedTimes(request);
        if (null != totalTimesError) {
            errors.add("totalTimesError", totalTimesError);
        }

        //validate from to time and set in dto the millis
        validateTimeRangeFromTo(errors, request);

        validateTimeLimitExceededByAssigneeSubProject(errors, request);

        return errors;
    }

    private ActionError validateInvoiceStatusChange(HttpServletRequest request) {
        if (!isInvoiceButtonPressed(request)) {
            return null;
        }

        Object toBeInvoiced = getDto("toBeInvoiced");
        if (null != toBeInvoiced && "true".equals(toBeInvoiced.toString())) {
            return null;
        }

        return new ActionError("errors.required", JSPHelper.getMessage(request, "ProjectTime.toBeInvoiced"));
    }

    private ActionError validateProjectActivity(HttpServletRequest request) {
        String projectId = (String) getDto("projectId");

        String projectActivityId = (String) getDto("activityId");

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("projectId", projectId);
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        List<Map> data = fantabulousUtil.getData(request, "projectActivityList");

        if (null != data && !data.isEmpty() && GenericValidator.isBlankOrNull(projectActivityId)) {
            return new ActionError("errors.required", JSPHelper.getMessage(request, "ProjectTime.activityName"));
        }

        return null;
    }

    private ActionError validateSubProject(HttpServletRequest request) {
        String projectId = (String) getDto("projectId");


        String subProjectId = (String) getDto("subProjectId");

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("projectId", projectId);
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        List<Map> data = fantabulousUtil.getData(request, "subProjectList");
        if (null != data && !data.isEmpty() && GenericValidator.isBlankOrNull(subProjectId)) {
            return new ActionError("errors.required", JSPHelper.getMessage(request, "ProjectTime.subProjectName"));
        }

        return null;
    }

    private ActionError validateProjectTimeDate(HttpServletRequest request) {
        Object date = getDto("date");
        if (null == date || !(date instanceof Integer)) {
            return null;
        }

        Integer projectStartDate = (Integer) projectDTO.get("startDate");
        Integer projectEndDate = (Integer) projectDTO.get("endDate");

        if (null != projectEndDate) {
            if (projectStartDate <= ((Integer) date) && ((Integer) date) <= projectEndDate) {
                return null;
            }

            return new ActionError("ProjectTime.date.rangeError",
                    DateUtils.parseDate(projectStartDate, JSPHelper.getMessage(request, "datePattern")),
                    DateUtils.parseDate(projectEndDate, JSPHelper.getMessage(request, "datePattern")));
        }

        if (projectStartDate <= ((Integer) date)) {
            return null;
        }

        return new ActionError("ProjectTime.date.error",
                DateUtils.parseDate(projectStartDate, JSPHelper.getMessage(request, "datePattern")));
    }

    private ActionError validateTime(HttpServletRequest request) {
        Object time = getDto("time");
        if (null == time || !(time instanceof BigDecimal)) {
            return null;
        }

        Object date = getDto("date");
        if (null == date || !(date instanceof Integer)) {
            return null;
        }

        String assigneeId = (String) getDto("assigneeId");
        if (GenericValidator.isBlankOrNull(assigneeId)) {
            return null;
        }

        User user = RequestUtils.getUser(request);
        ProjectUtilsCmd projectUtilsCmd = new ProjectUtilsCmd();
        projectUtilsCmd.setOp("sumProjectTimesByDate");
        projectUtilsCmd.putParam("projectId", Integer.valueOf(getDto("projectId").toString()));
        projectUtilsCmd.putParam("assigneeId", Integer.valueOf(assigneeId));
        projectUtilsCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        projectUtilsCmd.putParam("projectTimeId", null);

        String projectTimeId = (String) getDto("timeId");
        if (!GenericValidator.isBlankOrNull(projectTimeId)) {
            projectUtilsCmd.putParam("projectTimeId", Integer.valueOf(projectTimeId));
        }

        projectUtilsCmd.putParam("date", date);
        projectUtilsCmd.putParam("operator", getDto("op"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(projectUtilsCmd, request);
            BigDecimal actualTimes = (BigDecimal) resultDTO.get("sumProjectTimesByDate");

            BigDecimal sum = BigDecimalUtils.sum(actualTimes, (BigDecimal) time);
            if (sum.compareTo(new BigDecimal(24.0)) == 1) {
                return new ActionError("ProjectTime.time.sumExceedTwentyFourError",
                        DateUtils.parseDate((Integer) date, JSPHelper.getMessage(request, "datePattern")));
            }

        } catch (AppLevelException e) {
            log.error("-> Execute " + ProjectUtilsCmd.class.getName() + "FAIL ", e);
        }

        //validate decimal part as minutes
        int decimalSecondDigit = getTimeDecimalPartSecondDigit((BigDecimal) time);
        if (decimalSecondDigit != 0 && decimalSecondDigit != 5) {
            return new ActionError("ProjectTime.time.minutesRangeError", JSPHelper.getMessage(request, "ProjectTime.time"));
        }

        return null;
    }

    private int getTimeDecimalPartSecondDigit(BigDecimal timeBigDecimal) {
        int digit = 0;
        if (timeBigDecimal != null) {
            double num = timeBigDecimal.doubleValue();
            String numStr = String.valueOf(num).trim();

            int index = numStr.lastIndexOf(".");
            if (index > 0) {
                String decimalStr = numStr.substring(index+1);
                char[] digits = decimalStr.toCharArray();
                if (digits.length == 2) {
                    digit = Integer.valueOf(String.valueOf(digits[1]));
                }
            }
        }
        return digit;
    }

    private ActionError validateTotalInvoicedTimes(HttpServletRequest request) {
        String status = (String) getDto("status");

        if (!isReleaseButtonPressed(request) &&
                !isReleaseAndNewButtonPressed(request) &&
                !isConfirmButtonPressed(request) &&
                !isEditButtonPressed(request) &&
                !isNewButtonPressed(request) && !isSaveAndNewButtonPressed(request)) {
            return null;
        }

        // If the projectTime was updating return null
        if (ProjectConstants.ProjectTimeStatus.ENTERED.getAsString().equals(status) &&
                (isEditButtonPressed(request))) {
            return null;
        }

        // If the project have not configurated the hasTimeLimit Flag, return null
        if (!(Boolean) projectDTO.get("hasTimeLimit")) {
            return null;
        }

        //the 'time' value in the form must be a valid BigDecimal object.
        Object time = getDto("time");
        if (null == time || !(time instanceof BigDecimal)) {
            return null;
        }

        //the project is not have a limit of time
        if (!((Boolean) projectDTO.get("hasTimeLimit"))) {
            return null;
        }

        String toBeInvoiced = (String) getDto("toBeInvoiced");
        if (GenericValidator.isBlankOrNull(toBeInvoiced)) {
            toBeInvoiced = "false";
        }

        User user = RequestUtils.getUser(request);

        ProjectUtilsCmd projectUtilsCmd = new ProjectUtilsCmd();
        projectUtilsCmd.setOp("sumProjectTimesByProject");
        projectUtilsCmd.putParam("projectId", Integer.valueOf(getDto("projectId").toString()));
        projectUtilsCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

        if (!GenericValidator.isBlankOrNull((String) getDto("timeId"))) {
            projectUtilsCmd.putParam("projectTimeId", Integer.valueOf(getDto("timeId").toString()));
        }

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(projectUtilsCmd, request);
            BigDecimal totalToBeInvoicedEqualTrue = (BigDecimal) resultDTO.get("toBeInvoicedEqualTrue");
            BigDecimal totalToBeInvoiceEqualFalse = (BigDecimal) resultDTO.get("toBeInvoicedEqualFalse");

            if ("true".equals(toBeInvoiced)) {
                BigDecimal total = BigDecimalUtils.sum(totalToBeInvoicedEqualTrue, (BigDecimal) time);
                if (total.compareTo((BigDecimal) projectDTO.get("plannedInvoice")) == 1) {
                    return new ActionError("ProjectTime.sumTimes.totalInvoiceExceed");
                }
            }

            if ("false".equals(toBeInvoiced)) {
                BigDecimal total = BigDecimalUtils.sum(totalToBeInvoiceEqualFalse, (BigDecimal) time);
                if (total.compareTo((BigDecimal) projectDTO.get("plannedNoInvoice")) == 1) {
                    return new ActionError("ProjectTime.sumTimes.totalNoInvoiceExceed");
                }
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + ProjectUtilsCmd.class.getName() + "FAIL ", e);
        }

        if (ProjectConstants.ProjectTimeStatus.ENTERED.getAsString().equals(status) &&
                (isEditButtonPressed(request) ||
                        isNewButtonPressed(request) ||
                        isSaveAndNewButtonPressed(request) ||
                        isReleaseButtonPressed(request) || isReleaseAndNewButtonPressed(request))) {

            if ("true".equals(toBeInvoiced)) {
                BigDecimal totalInvoice = (BigDecimal) projectDTO.get("totalInvoice");
                BigDecimal plannedInvoice = (BigDecimal) projectDTO.get("plannedInvoice");
                if ((null != totalInvoice && plannedInvoice.compareTo(totalInvoice) == 0)) {
                    return new ActionError("ProjectTime.projectTime.totalInvoice.completed");
                }
            }

            if ("false".endsWith(toBeInvoiced)) {
                BigDecimal totalNoInvoice = (BigDecimal) projectDTO.get("totalNoInvoice");
                BigDecimal plannedNoInvoice = (BigDecimal) projectDTO.get("plannedNoInvoice");
                if (null != totalNoInvoice && plannedNoInvoice.compareTo(totalNoInvoice) == 0) {
                    return new ActionError("ProjectTime.projectTime.totalNoInvoice.completed");
                }
            }
        }

        return null;
    }

    private void validateTimeRangeFromTo(ActionErrors errors, HttpServletRequest request) {
        log.debug("Validate time range..." + getDtoMap());

        String fromHour = (String) getDto("fromHour");
        String fromMin = (String) getDto("fromMin");
        String toHour = (String) getDto("toHour");
        String toMin = (String) getDto("toMin");

        if (GenericValidator.isBlankOrNull(fromHour)
                && GenericValidator.isBlankOrNull(fromMin)
                && GenericValidator.isBlankOrNull(toHour)
                && GenericValidator.isBlankOrNull(toMin)) {
            return;
        }

        Object timeObj = getDto("time");
        BigDecimal time = null;
        if (timeObj != null && timeObj instanceof BigDecimal) {
            time = (BigDecimal) timeObj;
        }

        Object dateObj = getDto("date");
        Integer date = null;
        if (dateObj != null && dateObj instanceof Integer) {
            date = (Integer) dateObj;
        }

        if (date != null) {

            if (!GenericValidator.isBlankOrNull(fromHour)
                    && !GenericValidator.isBlankOrNull(fromMin)
                    && !GenericValidator.isBlankOrNull(toHour)
                    && !GenericValidator.isBlankOrNull(toMin)) {

                DateTime fromDateTime = DateUtils.integerToDateTime(date, Integer.parseInt(fromHour), Integer.parseInt(fromMin));
                DateTime toDateTime = DateUtils.integerToDateTime(date, Integer.parseInt(toHour), Integer.parseInt(toMin));

                //set in dto to save millis
                setDto("fromDateTime", fromDateTime.getMillis());
                setDto("toDateTime", toDateTime.getMillis());

                if (fromDateTime.isBefore(toDateTime)) {
                    if (time != null) {
                        BigDecimal maxRangeTime = calculateTimeRangeFromToInHours(Integer.parseInt(fromHour), Integer.parseInt(fromMin), Integer.parseInt(toHour), Integer.parseInt(toMin));

                        if (time.doubleValue() > maxRangeTime.doubleValue()) {
                            errors.add("exceededError", new ActionError("ProjectTime.time.exceededError", JSPHelper.getMessage(request, "ProjectTime.timeSpent")));
                        }
                    }
                } else {
                    errors.add("rangeError", new ActionError("ProjectTime.time.fromToRangeError", JSPHelper.getMessage(request, "ProjectTime.fromToTime")));
                }
            } else {
                errors.add("valuesError", new ActionError("ProjectTime.time.fromToValuesError", JSPHelper.getMessage(request, "ProjectTime.fromToTime")));
            }
        }
    }

    private BigDecimal calculateTimeRangeFromToInHours(int startHour, int startMin, int endHour, int endMin) {
        BigDecimal totalHours = BigDecimal.ZERO;

        if ((endHour > startHour) || (endHour == startHour && endMin > startMin)) {

            int totalHourAsMin = (endHour - startHour) * 60;

            if (startMin > endMin) {
                int minutes = startMin - endMin;
                totalHours = BigDecimalUtils.divide(new BigDecimal(totalHourAsMin - minutes), new BigDecimal(60));
            } else {
                int minutes = endMin - startMin;
                totalHours = BigDecimalUtils.divide(new BigDecimal(totalHourAsMin + minutes), new BigDecimal(60));
            }
        }
        return totalHours;
    }

    private void validateTimeLimitExceededByAssigneeSubProject(ActionErrors errors, HttpServletRequest request) {
        if (!errors.isEmpty()) {
            return;
        }

        boolean toBeInvoiced = false;
        if ("true".equals(getDto("toBeInvoiced"))) {
            toBeInvoiced = true;
        }

        //when is update
        Integer timeId = null;
        if (getDto("timeId") != null && !GenericValidator.isBlankOrNull(getDto("timeId").toString())) {
            timeId = new Integer(getDto("timeId").toString());
        }

        Object timeObj = getDto("time");
        BigDecimal time = null;
        if (timeObj != null && timeObj instanceof BigDecimal) {
            time = (BigDecimal) timeObj;
        }

        if (time != null) {
            Object projectId = getDto("projectId");
            Object assigneeId = getDto("assigneeId");
            Object subProjectId = getDto("subProjectId");

            if (projectId != null && !GenericValidator.isBlankOrNull(projectId.toString())
                    && assigneeId != null && !GenericValidator.isBlankOrNull(assigneeId.toString())
                    && subProjectId != null && !GenericValidator.isBlankOrNull(subProjectId.toString())) {

                ProjectTimeLimitDTO readTimeLimitDto = Functions.readProjectTimeLimitByAssigneeSubProject(projectId, assigneeId, subProjectId);

                if (readTimeLimitDto != null) {
                    Boolean hasTimeLimit = (Boolean) readTimeLimitDto.get("hasTimeLimit");

                    if (hasTimeLimit) {
                        Map previousTotalMap = Functions.calculateProjectTimesByAssigneeSubProject(projectId, assigneeId, subProjectId, timeId);

                        BigDecimal invoiceLimit = (BigDecimal) readTimeLimitDto.get("invoiceLimit");
                        BigDecimal noInvoiceLimit = (BigDecimal) readTimeLimitDto.get("noInvoiceLimit");

                        if (toBeInvoiced) {
                            BigDecimal total = BigDecimalUtils.sum(time, (BigDecimal) previousTotalMap.get("totalInvoiceTime"));
                            checkLimitExceeded(total, invoiceLimit, errors, request);
                        } else {
                            BigDecimal total = BigDecimalUtils.sum(time, (BigDecimal) previousTotalMap.get("totalNoInvoiceTime"));
                            checkLimitExceeded(total, noInvoiceLimit, errors, request);
                        }
                    }
                }
            }
        }
    }

    private void checkLimitExceeded(BigDecimal total, BigDecimal limit, ActionErrors errors, HttpServletRequest request) {
        if (total!= null && limit != null) {
            if (total.compareTo(limit) == 1) {

                Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
                String limitLabel = FormatUtils.formatDecimal(limit, locale, JSPHelper.getMessage(locale, "numberFormat.2DecimalPlaces"));

                errors.add("limit", new ActionError("ProjectTime.time.error.assigneeLimitExceeded", limitLabel));
            }
        }
    }

    private boolean isReleaseButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("releaseButton");
    }

    private boolean isConfirmButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("confirmButton");
    }

    private boolean isEditButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("editButton");
    }

    private boolean isNewButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("newButton");
    }

    private boolean isSaveAndNewButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("SaveAndNew");
    }

    private boolean isInvoiceButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("invoiceButton");
    }

    private boolean isReleaseAndNewButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("releaseAndNewButton");
    }

}
