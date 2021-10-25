package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.schedulermanager.AppointmentCreateCmd;
import com.piramide.elwis.cmd.schedulermanager.AppointmentUpdateCmd;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.schedulermanager.el.Functions;
import com.piramide.elwis.web.schedulermanager.util.AppointmentNotificationUtil;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author yumi
 */

public class AppointmentReminderAction extends AppointmentGeneralAction {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (isCancelled(request)) {
            request.setAttribute("simple", "true");

            String returnType = (String) ((DefaultForm) form).getDto("returnType");
            String calendar = request.getParameter("calendar");
            String oldType = request.getParameter("oldType");
            if (calendar == null) {
                calendar = "";
            }

            if (oldType != null) {
                ActionForward returnSuccess = mapping.findForward("Cancel");

                if (oldType.equals("1")) {
                    returnSuccess = mapping.findForward("ReturnDay");
                } else if (oldType.equals("2")) {
                    returnSuccess = mapping.findForward("ReturnWeek");
                } else if (oldType.equals("3")) {
                    returnSuccess = mapping.findForward("ReturnMonth");
                } else if (oldType.equals("4")) {
                    returnSuccess = mapping.findForward("ReturnYear");
                } else if (oldType.equals("5")) {
                    returnSuccess = mapping.findForward("ReturnSearchList");
                } else if (oldType.equals("6")) {
                    returnSuccess = mapping.findForward("ReturnAdvancedSearchList");
                } else if (returnType.equals("7")) {
                    returnSuccess = mapping.findForward("ReturnTabParticipants");
                }
                if (!"5".equals(oldType) && !"6".equals(oldType)) {
                    return new ActionForwardParameters().add("calendar", calendar)
                            .add("oldType", oldType)
                            .forward(returnSuccess);
                } else {
                    return mapping.findForward("Cancel");
                }
            } else {
                return mapping.findForward("Cancel");
            }
        }

        return super.execute(mapping, form, request, response);
    }

    @Override
    protected boolean accessRightsValidatorCondition(Byte publicAppPermission,
                                                     Byte privateAppPermission,
                                                     DefaultForm defaultForm,
                                                     HttpServletRequest request) {
        boolean isPrivate = null != defaultForm.getDto("isPrivate");

        if (isCreate(request)) {
            return Functions.hasAddAppointmentPermission(publicAppPermission, privateAppPermission, !isPrivate);
        }

        if (isUpdate(request)) {
            return Functions.hasEditAppointmentPermission(publicAppPermission, privateAppPermission, !isPrivate);
        }

        return super.accessRightsValidatorCondition(publicAppPermission, privateAppPermission, defaultForm, request);
    }

    @Override
    protected ActionError getAccessRightsValidatorErrorMessage(DefaultForm defaultForm, HttpServletRequest request) {
        if (isCreate(request)) {
            return new ActionError("scheduler.permission.error",
                    JSPHelper.getMessage(request, "Scheduler.grantAccess.add"));
        }

        if (isUpdate(request)) {
            return new ActionError("scheduler.permission.error",
                    JSPHelper.getMessage(request, "Scheduler.grantAccess.edit"));
        }

        return super.getAccessRightsValidatorErrorMessage(defaultForm, request);
    }

    @Override
    protected void preProcessDefaultForm(DefaultForm defaultForm, HttpServletRequest request) {
        DateTimeZone dateTimeZone = getDateTimeZone(request);
        defaultForm.setDto("userTimeZone", dateTimeZone);
    }


    @Override
    protected ActionForward formValidator(DefaultForm defaultForm, HttpServletRequest request, ActionMapping mapping) {
        if (request.getParameter("dto(save)") != null || request.getParameter("dto(onlySave)") != null) {
            ActionErrors errors = defaultForm.validate(mapping, request);

            boolean isAllDay = "on".equals(defaultForm.getDto("isAllDay"));
            boolean isReminder = null != defaultForm.getDto("reminder");

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                request.setAttribute("jsLoad", "onLoad=\"generalDisable(" + isAllDay + "," + isReminder + "," + defaultForm.getDto("rangeType") + ")\"");
                return mapping.findForward("fail");
            }

            List<ActionError> errorList = new ArrayList<ActionError>();

            Integer startDate = (Integer) defaultForm.getDto("startDate");
            Integer endDate = (Integer) defaultForm.getDto("endDate");
            if (startDate > endDate) {
                errorList.add(new ActionError("Contact.Contract.compareDates",
                        JSPHelper.getMessage(request, "Task.startDate"),
                        JSPHelper.getMessage(request, "Appointment.endDate")));
            }

            if (!isAllDay && startDate.equals(endDate)) {
                StringBuffer startT = new StringBuffer()
                        .append(defaultForm.getDto("startHour"))
                        .append(defaultForm.getDto("startMin"));

                StringBuffer endT = new StringBuffer()
                        .append(defaultForm.getDto("endHour"))
                        .append(defaultForm.getDto("endMin"));

                if (new Integer(startT.toString()) >= new Integer(endT.toString())) {
                    errorList.add(new ActionError("Appointment.greater",
                            JSPHelper.getMessage(request, "Appointment.startTime"),
                            JSPHelper.getMessage(request, "Appointment.endTime")));
                }
            }

            List list = new ArrayList(0);
            if (request.getParameterValues("array2") != null) {
                boolean isDate = true;

                list = Arrays.asList(request.getParameterValues("array2"));
                String pattern = JSPHelper.getMessage(request, "datePattern").trim();

                for (Object element : list) {
                    String s = (String) element;
                    if (!GenericValidator.isDate(s, pattern, false)) {
                        isDate = false;
                        break;
                    }
                }

                if (!isDate) {
                    errorList.add(new ActionError("Appointment.Recurrence.dateExceptionFail", pattern));
                }
            }

            if (!errorList.isEmpty()) {
                errors = new ActionErrors();
                for (int i = 0; i < errorList.size(); i++) {
                    ActionError error = errorList.get(i);
                    errors.add("AppointmentError_" + i, error);
                }
                saveErrors(request, errors);

                request.setAttribute("array2", getExceptionList(list));
                return mapping.findForward("fail");
            }
        }

        return null;
    }


    @Override
    protected ActionForward commandExecution(ActionMapping mapping,
                                             DefaultForm defaultForm,
                                             HttpServletRequest request) throws Exception {
        ActionForward returnSuccess = mapping.findForward("Success");

        String calendar = request.getParameter("calendar");
        String oldType = request.getParameter("oldType");
        String returnType = (String) defaultForm.getDto("returnType");

        if (request.getParameter("dto(save)") != null) {
            if (returnType.equals("1")) {
                returnSuccess = mapping.findForward("ReturnDay");
            } else if (returnType.equals("2")) {
                returnSuccess = mapping.findForward("ReturnWeek");
            } else if (returnType.equals("3")) {
                returnSuccess = mapping.findForward("ReturnMonth");
            } else if (returnType.equals("4")) {
                returnSuccess = mapping.findForward("ReturnYear");
            } else if (returnType.equals("5")) {
                returnSuccess = mapping.findForward("ReturnSearchList");
            } else if (returnType.equals("6")) {
                returnSuccess = mapping.findForward("ReturnAdvancedSearchList");
            } else if (returnType.equals("7")) {
                returnSuccess = mapping.findForward("ReturnTabParticipants");
            }

            returnSuccess = new ActionForwardParameters().add("calendar", calendar)
                    .add("oldType", oldType)
                    .forward(returnSuccess);
        }

        if (request.getParameter("dto(save)") != null || request.getParameter("dto(onlySave)") != null) {
            if (isCreate(request)) {
                AppointmentCreateCmd cmdCreate = new AppointmentCreateCmd();
                cmdCreate.putParam(defaultForm.getDtoMap());
                cmdCreate.putParam("exceptionList", getExceptionList(request));
                BusinessDelegate.i.execute(cmdCreate, request);

                ActionErrors aErrors = MessagesUtil.i.convertToActionErrors(mapping, request, cmdCreate.getResultDTO());
                if (SchedulerConstants.TRUE_VALUE.equals(cmdCreate.getResultDTO().get("sDateChangeMessage"))) {
                    aErrors.add("dateFail", new ActionError("Scheduler.Appointment.startDateChanged.msg"));
                }
                saveErrors(request.getSession(), aErrors);

                if (!cmdCreate.getResultDTO().isFailure()) {
                    //send notification email if is required
                    if (isWithSendNotification(request)) {
                        Integer userId = new Integer(defaultForm.getDto("userId").toString());
                        Integer appointmentId = (Integer) cmdCreate.getResultDTO().get("appointmentId");

                        if (appointmentId != null) {
                            AppointmentNotificationUtil.sendNotificationEmail(userId, appointmentId, request);
                        }
                    }

                    return new ActionForwardParameters().add("appointmentId", (cmdCreate.getResultDTO().get("appointmentId")).toString())
                            .forward(returnSuccess);
                } else {
                    return mapping.findForward("fail");
                }
            }

            if (isUpdate(request)) {
                AppointmentUpdateCmd cmdUpdate = new AppointmentUpdateCmd();
                cmdUpdate.putParam("exceptionList", getExceptionList(request));
                cmdUpdate.putParam(defaultForm.getDtoMap());

                BusinessDelegate.i.execute(cmdUpdate, request);
                ActionErrors aErrors = MessagesUtil.i.convertToActionErrors(mapping, request, cmdUpdate.getResultDTO());
                saveErrors(request, aErrors);

                if (cmdUpdate.getResultDTO().isFailure()) {
                    request.setAttribute("reload", SchedulerConstants.TRUE_VALUE);
                    return mapping.findForward("ConcurrencyFail");
                } else {
                    ActionErrors errors = new ActionErrors();
                    if (SchedulerConstants.TRUE_VALUE.equals(cmdUpdate.getResultDTO().get("sDateChangeMessage"))) {
                        errors.add("dateFail", new ActionError("Scheduler.Appointment.startDateChanged.msg"));
                    }
                    saveErrors(request.getSession(), errors);
                    return returnSuccess;
                }
            }
        }

        log.debug("Return to default forward: " + returnSuccess.getName());
        return returnSuccess;
    }

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        if (isUpdate(request)) {
            return super.validateElementExistence(request, mapping);
        }

        return null;
    }

    @Override
    protected boolean isEnabledEntriesLimitValidation(DefaultForm defaultForm, HttpServletRequest request) {
        return isCreate(request);
    }

    @Override
    protected ActionForward gotoInputForward(DefaultForm defaultForm, HttpServletRequest request, ActionMapping mapping) {
        if (request.getParameter("dto(save)") != null || request.getParameter("dto(onlySave)") != null) {
            return null;
        }

        boolean isAllDay = "on".equals(defaultForm.getDto("isAllDay"));
        boolean isReminder = null != defaultForm.getDto("reminder");
        request.setAttribute("jsLoad", "onLoad=\"generalDisable(" + isAllDay + "," + isReminder + "," + defaultForm.getDto("rangeType") + ")\"");

        return mapping.findForward("fail");
    }

    private ArrayList getExceptionList(List list) {
        ArrayList arrayList = new ArrayList(0);
        if (list != null) {
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                String date = (String) iterator.next();
                arrayList.add(new LabelValueBean(date, date));
            }
        }
        return arrayList;
    }

    protected boolean isCreate(HttpServletRequest request) {
        return "create".equals(request.getParameter("dto(op)"));
    }

    protected boolean isUpdate(HttpServletRequest request) {
        return "update".equals(request.getParameter("dto(op)"));
    }

    protected List<String> getExceptionList(HttpServletRequest request) {
        List<String> result = new ArrayList<String>();

        if (request.getParameterValues("array2") != null) {
            List elements = Arrays.asList(request.getParameterValues("array2"));
            String pattern = JSPHelper.getMessage(request, "datePattern").trim();

            for (Object element : elements) {
                String dateElement = (String) element;
                result.add(DateUtils.dateToInteger(GenericTypeValidator.formatDate(dateElement, pattern, false)).toString());
            }
        }

        return result;
    }

    private boolean isWithSendNotification(HttpServletRequest request) {
        return "true".equals(request.getParameter("dto(sendAppNotification)"));
    }
}
