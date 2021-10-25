package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.common.ModuleEntriesLimitUtilCmd;
import com.piramide.elwis.cmd.common.UserInfoCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.schedulermanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class AppointmentGeneralAction extends AbstractDefaultAction {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward inputForward = gotoInputForward((DefaultForm) form, request, mapping);
        if (null != inputForward) {
            return inputForward;
        }

        ActionForward entriesLimitForward = entriesLimitValidator((DefaultForm) form, request, mapping);
        if (null != entriesLimitForward) {
            return entriesLimitForward;
        }

        ActionForward existenceForward;
        if (null != (existenceForward = validateElementExistence(request, mapping))) {
            return existenceForward;
        }

        ActionForward accessRightsForward = accessRightsValidator(mapping, (DefaultForm) form, request);
        if (null != accessRightsForward) {
            return accessRightsForward;
        }

        ActionForward formValidationForward = formValidator((DefaultForm) form, request, mapping);
        if (null != formValidationForward) {
            return formValidationForward;
        }

        preProcessDefaultForm((DefaultForm) form, request);
        return commandExecution(mapping, (DefaultForm) form, request);
    }

    protected ActionForward gotoInputForward(DefaultForm defaultForm,
                                             HttpServletRequest request,
                                             ActionMapping mapping) {
        return null;
    }

    protected ActionForward formValidator(DefaultForm defaultForm,
                                          HttpServletRequest request,
                                          ActionMapping mapping) {
        return null;
    }

    protected ActionForward entriesLimitValidator(DefaultForm defaultForm,
                                                  HttpServletRequest request,
                                                  ActionMapping mapping) {
        if (isEnabledEntriesLimitValidation(defaultForm, request)) {
            User user = RequestUtils.getUser(request);

            ModuleEntriesLimitUtilCmd limitUtilCmd = new ModuleEntriesLimitUtilCmd();
            limitUtilCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
            limitUtilCmd.putParam("mainTable", "appointment");
            limitUtilCmd.putParam("functionality", "APPOINTMENT");

            try {
                BusinessDelegate.i.execute(limitUtilCmd, request);
            } catch (AppLevelException e) {
                throw new RuntimeException("Cannot execute EntriesLimit validation.", e);
            }

            if (!limitUtilCmd.getResultDTO().isFailure()) {
                if (!((Boolean) limitUtilCmd.getResultDTO().get("canCreate"))) {
                    ActionErrors errors = new ActionErrors();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Common.message.checkEntries"));
                    saveErrors(request, errors);

                    return getEntriesLimitFailureActionForward(mapping, request);
                }
            }
        }

        return null;
    }

    protected boolean isEnabledEntriesLimitValidation(DefaultForm defaultForm, HttpServletRequest request) {
        return false;
    }

    protected ActionForward getEntriesLimitFailureActionForward(ActionMapping mapping, HttpServletRequest request) {
        return mapping.findForward("Fail");
    }

    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionErrors errors = new ActionErrors();

        errors = ForeignkeyValidator.i.validate(SchedulerConstants.TABLE_APPOINTMENT,
                "appointmentid",
                getAppointmentId(request),
                errors,
                new ActionError("msg.NotFound", request.getParameter("dto(title)")));
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return new ActionForwardParameters().add("simple", "true")
                    .forward(mapping.findForward("MainSearch"));
        }

        return null;
    }

    protected ActionForward accessRightsValidator(ActionMapping mapping,
                                                  DefaultForm form,
                                                  HttpServletRequest request) {
        Integer schedulerUserId = getSchedulerUserId(request);
        Integer userId = getUserId(request);

        if (!schedulerUserId.equals(userId)) {
            Map<String, Byte> permissionMap = Functions.getUserPermissions(schedulerUserId, userId);
            Byte publicAppPermission = permissionMap.get("public");
            Byte privateAppPermission = permissionMap.get("private");

            if (accessRightsValidatorCondition(publicAppPermission,
                    privateAppPermission,
                    form,
                    request)) {
                request.setAttribute("publicSchedulerUserPermission", publicAppPermission);
                request.setAttribute("privateSchedulerUserPermission", privateAppPermission);
            } else {
                ActionErrors errors = new ActionErrors();
                errors.add("VirtualProperty", getAccessRightsValidatorErrorMessage(form, request));
                saveErrors(request, errors);
                return getAccessRightsFailureActionForward(mapping, request);
            }
        }

        return null;
    }

    protected ActionError getAccessRightsValidatorErrorMessage(DefaultForm defaultForm, HttpServletRequest request) {
        return new ActionError("scheduler.errors.hasNotPermission");
    }

    protected ActionForward getAccessRightsFailureActionForward(ActionMapping mapping, HttpServletRequest request) {
        return new ActionForwardParameters()
                .add("schedulerUserId", getUserId(request).toString())
                .forward(mapping.findForward("Fail"));
    }

    protected boolean accessRightsValidatorCondition(Byte publicAppPermission,
                                                     Byte privateAppPermission,
                                                     DefaultForm defaultForm,
                                                     HttpServletRequest request) {
        return false;
    }

    @Deprecated
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {

    }

    protected ActionForward commandExecution(ActionMapping mapping,
                                             DefaultForm defaultForm,
                                             HttpServletRequest request) throws Exception {
        return mapping.findForward("Success");
    }

    protected void preProcessDefaultForm(DefaultForm defaultForm, HttpServletRequest request) {

    }

    protected DateTimeZone getDateTimeZone(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        Integer schedulerUserId = getSchedulerUserId(request);
        if (!getUserId(request).equals(schedulerUserId)) {
            UserInfoCmd userInfoCmd = new UserInfoCmd();
            userInfoCmd.putParam("userId", schedulerUserId);
            userInfoCmd.putParam("create", "true");
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(userInfoCmd, request);
                String timezone = (String) resultDTO.get("timeZone");
                if (null != timezone) {
                    dateTimeZone = DateTimeZone.forID(timezone);
                }
            } catch (AppLevelException e) {
                log.debug("Cannot execute UserInfoCmd to read the schedulerUser timeZone, by default use the logged user timeZone.");
            }
        }

        return dateTimeZone;
    }

    protected Integer getSchedulerUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        return (Integer) user.getValue("schedulerUserId");
    }

    protected Integer getUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        return (Integer) user.getValue(Constants.USERID);
    }

    protected Integer getAppointmentId(HttpServletRequest request) {
        String id = request.getParameter("appointmentId");
        if (request.getParameter("dto(appointmentId)") != null
                && !"".equals(request.getParameter("dto(appointmentId)"))) {
            id = request.getParameter("dto(appointmentId)");
        }

        return Integer.valueOf(id);
    }

}
