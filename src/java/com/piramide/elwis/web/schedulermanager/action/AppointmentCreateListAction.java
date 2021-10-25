package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.admin.UserAdminCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yumi
 * @version 4.3.7
 */

public class AppointmentCreateListAction extends ListAction {

    private String viewAppointment = SchedulerConstants.PROCESS_ALL_APP;

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        initializeSchedulerUserId(request);

        ActionForward accessRightsForward = accessRightsValidator(mapping, form, request);
        if (null != accessRightsForward) {
            return accessRightsForward;
        }

        DateTimeZone dateTimeZone = getSchedulerUserDateTimeZone(mapping, request);
        if (null != dateTimeZone) {
            request.setAttribute("timeZone", dateTimeZone);
        }

        if (SchedulerConstants.TRUE_VALUE.equals(request.getParameter("error"))) {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Common.message.checkEntries"));
            saveErrors(request, errors);
        }

        super.initializeFilter();

        if (!SchedulerConstants.PROCESS_ALL_APP.equals(viewAppointment)) {
            addFilter("isPrivate", (SchedulerConstants.PROCESS_ONLYPRIVATE_APP.equals(viewAppointment) ? "1" : "0"));
        }

        SearchForm searchForm = (SearchForm) form;

        String condition = "StartDate";
        if (request.getParameter("simple") != null) {
            User user = RequestUtils.getUser(request);
            DateTimeZone zone = (DateTimeZone) user.getValue("dateTimeZone");
            DateTime time = new DateTime(zone);
            addFilter("startDateMillis",
                    String.valueOf(new DateTime(time.getYear(), time.getMonthOfYear(), time.getDayOfMonth(), 0, 0, 1, 0, zone).getMillis()));
        } else {
            String startDate = (String) searchForm.getParameter("startDateMillis");
            String endDate = (String) searchForm.getParameter("endDateMillis");
            boolean hasStartDate = startDate != null && startDate.length() > 0;
            boolean hasEndDate = endDate != null && endDate.length() > 0;

            if (hasStartDate && hasEndDate) {
                condition = "RangeDate";
            } else if (hasEndDate && !hasStartDate) {
                condition = "EndDate";
            }
        }

        if ("on".equals(request.getParameter("parameter(isRecurrence)"))) {
            addFilter("is_recurrence", "1");
        }

        if (null != getSchedulerUserId(request)) {
            addFilter("viewerUserId", getSchedulerUserId(request).toString());
        }

        addFilter(condition, "true");

        setAdditionalFilters(form, request);

        request.setAttribute("view", "list");
        return super.execute(mapping, searchForm, request, response);
    }

    protected void initializeSchedulerUserId(HttpServletRequest request) {
        String paramSchedulerUserId = request.getParameter("schedulerUserId");

        Integer schedulerUserId = getSchedulerUserId(request);

        if (!GenericValidator.isBlankOrNull(paramSchedulerUserId)) {
            if (GenericValidator.isInt(paramSchedulerUserId)) {
                schedulerUserId = new Integer(paramSchedulerUserId);
            } else {
                schedulerUserId = getUserId(request);
            }
        } else {
            if (null == schedulerUserId) {
                schedulerUserId = getUserId(request);
            }
        }

        User user = RequestUtils.getUser(request);
        user.setValue("schedulerUserId", schedulerUserId);

        log.debug("We are working with the calendar of the user: " + schedulerUserId);
    }

    protected DateTimeZone getSchedulerUserDateTimeZone(ActionMapping mapping, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        Integer schedulerUserId = getSchedulerUserId(request);

        if (!getUserId(request).equals(schedulerUserId)) {
            UserAdminCmd userAdminCmd = new UserAdminCmd();
            userAdminCmd.putParam("companyId", user.getValue("companyId"));
            userAdminCmd.putParam("userId", schedulerUserId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(userAdminCmd, request);
                if (resultDTO.isFailure()) {
                    saveErrors(request, MessagesUtil.i.convertToActionErrors(mapping, request, userAdminCmd.getResultDTO()));
                } else if (null != resultDTO.get("timeZone")) {
                    dateTimeZone = DateTimeZone.forID(resultDTO.get("timeZone").toString());
                }
            } catch (AppLevelException e) {
                log.debug("Cannot execute UserAdminCmd to read the scheduler user timeZone, by default use the logged user timeZone.", e);
            }
        }

        log.debug("The schedulerUser: " + schedulerUserId + " use the timeZone: " + dateTimeZone.getID());
        return dateTimeZone;
    }

    protected ActionForward accessRightsValidator(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request) {
        Integer schedulerUserId = getSchedulerUserId(request);
        Integer userId = getUserId(request);

        viewAppointment = SchedulerConstants.PROCESS_ALL_APP;

        if (!userId.equals(schedulerUserId)) {
            Byte publicAppPermission = Functions.getPublicPermission(schedulerUserId, userId);
            Byte privateAppPermission = Functions.getPrivatePermission(schedulerUserId, userId);

            if (SchedulerPermissionUtil.hasPermissions(publicAppPermission)
                    && SchedulerPermissionUtil.hasPermissions(privateAppPermission)) {
                viewAppointment = SchedulerConstants.PROCESS_ALL_APP;
            } else if (SchedulerPermissionUtil.hasPermissions(publicAppPermission)) {
                viewAppointment = SchedulerConstants.PROCESS_ONLYPUBLIC_APP;
            } else if (SchedulerPermissionUtil.hasPermissions(privateAppPermission)) {
                viewAppointment = SchedulerConstants.PROCESS_ONLYPRIVATE_APP;
            } else {
                //user has not permission to view calendar of other user
                ActionErrors errors = new ActionErrors();
                errors.add("VirtualProperty", new ActionError("scheduler.errors.hasNotPermission"));
                saveErrors(request.getSession(), errors);

                return new ActionForwardParameters().add("schedulerUserId", userId.toString())
                        .forward(mapping.findForward("fail"));
            }

            request.setAttribute("publicSchedulerUserPermission", publicAppPermission);
            request.setAttribute("privateSchedulerUserPermission", privateAppPermission);
        }

        return null;
    }

    protected Integer getSchedulerUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        return (Integer) user.getValue("schedulerUserId");
    }

    protected Integer getUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        return (Integer) user.getValue(Constants.USERID);
    }

    protected void setAdditionalFilters(ActionForm form, HttpServletRequest request) {

    }
}