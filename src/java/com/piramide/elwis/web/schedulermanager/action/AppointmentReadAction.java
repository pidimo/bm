package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.schedulermanager.AppointmentReadCmd;
import com.piramide.elwis.domain.schedulermanager.RecurException;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.schedulermanager.el.Functions;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Jatun S.R.L
 *
 * @author yumi
 */

@SuppressWarnings(value = "unchecked")
public class AppointmentReadAction extends AppointmentGeneralAction {
    private Byte publicAppPermission = null;
    private Byte privateAppPermission = null;


    @Override
    protected boolean accessRightsValidatorCondition(Byte publicAppPermission,
                                                     Byte privateAppPermission,
                                                     DefaultForm defaultForm,
                                                     HttpServletRequest request) {
        this.publicAppPermission = publicAppPermission;
        this.privateAppPermission = privateAppPermission;

        boolean isPrivate = isPrivateAppointment(defaultForm, request);
        return Functions.hasPermissions(publicAppPermission, privateAppPermission, !isPrivate)
                && Functions.hasReadAppointmentPermission(publicAppPermission, privateAppPermission, !isPrivate);
    }

    @Override
    protected ActionError getAccessRightsValidatorErrorMessage(DefaultForm defaultForm, HttpServletRequest request) {
        return new ActionError("scheduler.permission.error",
                JSPHelper.getMessage(request, "Scheduler.grantAccess.read"));
    }

    @Override
    protected ActionForward commandExecution(ActionMapping mapping,
                                             DefaultForm defaultForm,
                                             HttpServletRequest request) throws Exception {
        if ("true".equals(request.getParameter("fail"))) {
            return mapping.findForward("Success");
        }

        Integer appointmentId = getAppointmentId(request);

        AppointmentReadCmd cmd = new AppointmentReadCmd();
        cmd.putParam("userSessionId", getUserId(request));
        cmd.putParam("appointmentId", appointmentId);
        cmd.putParam(defaultForm.getDtoMap());

        cmd.putParam("viewerUserId", getViewerUserId(request));

        ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
        ActionErrors aErrors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
        saveErrors(request, aErrors);

        String reminderType = null;
        if (defaultForm.getDto("reminderType") != null) {
            reminderType = defaultForm.getDto("reminderType").toString();
        }

        if (!resultDTO.isFailure()) {
            processResultDTO(resultDTO, defaultForm, request, reminderType);
            request.setAttribute("appointmentId", appointmentId);

            String reload = (String) request.getAttribute("reload");
            if (reload != null) {
                defaultForm.setDto("rangeValueText", null);
                defaultForm.setDto("rangeValueDate", null);

                if (reload.equals(SchedulerConstants.TRUE_VALUE)) {
                    aErrors.add("reload", new ActionError("Common.error.concurrency"));
                    saveErrors(request, aErrors);
                    return mapping.findForward("Success");
                }
            }
        } else {
            aErrors = MessagesUtil.i.convertToActionErrors(mapping, request, cmd.getResultDTO());
            saveErrors(request, aErrors);
            return mapping.findForward("Fail");
        }

        return new ActionForwardParameters()
                .add("calendar", request.getParameter("calendar"))
                .add("type", request.getParameter("type"))
                .add("currentDate", request.getParameter("currentDate"))
                .forward(mapping.findForward("Success"));
    }


    private void processResultDTO(ResultDTO resultDTO,
                                  DefaultForm defaultForm,
                                  HttpServletRequest request,
                                  String reminderType) {
        ActionErrors aErrors = new ActionErrors();
        boolean isPrivate = ((Boolean) resultDTO.get("isPrivate"));

        defaultForm.setDto("returnType", request.getParameter("type"));

        if (!"delete".equals(request.getParameter("operation"))
                && !SchedulerConstants.TRUE_VALUE.equals(request.getParameter("onlyView"))
                && !hasOnlyReadPermission(publicAppPermission, privateAppPermission, isPrivate)) {
            request.setAttribute("jsLoad", "onLoad=\"generalDisable(" + resultDTO.get("isAllDay") + "," + resultDTO.get("reminder") + "," + resultDTO.get("rangeType") + ")\"");

        } else if ("delete".equals(request.getParameter("operation"))) {
            if (((Integer) resultDTO.get("SchedulerUsersNumber")) > 1) {
                aErrors.add("participant", new ActionError("Appointment.deleteParticipant.message",
                        resultDTO.get("SchedulerUsersNumber")));
                request.setAttribute("message", SchedulerConstants.TRUE_VALUE);
            }
            if (((Boolean) resultDTO.get("isRecurrence")) && SchedulerConstants.TRUE_VALUE.equals(request.getParameter("fromView"))) {
                aErrors.add("deleteRecurrence", new ActionError("Appointment.delete.recurreceApp", resultDTO.get("title")));
                request.setAttribute("message", SchedulerConstants.TRUE_VALUE);
            }
            saveErrors(request, aErrors);
        }

        if ("59".equals(resultDTO.get("endMin").toString())) {
            resultDTO.put("endMin", "45");
        }
        if (!((Boolean) resultDTO.get("reminder"))) {
            resultDTO.put("reminderType", "1");
        }

        defaultForm.getDtoMap().putAll(resultDTO);
        if (reminderType != null) {
            defaultForm.getDtoMap().put("reminderType", reminderType);
        }

        /*---------  Recurrence  -------------*/
        Date date = DateUtils.integerToDate((Integer) resultDTO.get("startDate"));

        resultDTO.put("date", date);
        Integer rangeType = -1;
        Integer ruleType = -1;

        if (((Boolean) resultDTO.get("isRecurrence"))) {
            if (resultDTO.get("rangeType") != null) {
                rangeType = (Integer) resultDTO.get("rangeType");
            }
            if (resultDTO.get("ruleType") != null) {
                ruleType = (Integer) resultDTO.get("ruleType");
            }
            Integer ruleValueType = null;
            if (resultDTO.get("ruleValueType") != null) {
                ruleValueType = (Integer) resultDTO.get("ruleValueType");
            }
            if (rangeType == 2) {
                resultDTO.put("rangeValueText", resultDTO.get("rangeValue"));
            } else if (rangeType == 3) {
                resultDTO.put("rangeValueDate", resultDTO.get("rangeValue"));
            }
            if (ruleType == 2) {  /*for Week*/
                String days = (String) resultDTO.get("ruleValue");
                StringTokenizer tokenizer = new StringTokenizer(days, SchedulerConstants.RECURRENCE_VALUES_SEPARATOR);
                while (tokenizer.hasMoreTokens()) {
                    String a = tokenizer.nextToken();
                    resultDTO.put(a, true);
                }
            } else if (ruleType == 3) {/* for Month*/
                if (ruleValueType != null) {
                    if (ruleValueType == 2) {
                        String days = (String) resultDTO.get("ruleValue");
                        StringTokenizer tokenizer = new StringTokenizer(days, SchedulerConstants.RECURRENCE_VALUES_SEPARATOR);
                        resultDTO.put("ruleValueWeek", tokenizer.nextToken());
                        resultDTO.put("daysWeek", tokenizer.nextToken());
                    } else {
                        resultDTO.put("ruleValueDay", resultDTO.get("ruleValue"));
                    }
                }
                resultDTO.put("ruleValueTypeMonth", ruleValueType);
            } else if (ruleType == 4) {  /*for year*/
                resultDTO.put("ruleValueTypeYear", ruleValueType);
            }
            List exceptions = new ArrayList(0);
            if (resultDTO.get("exceptions") != null) {
                exceptions = (List) resultDTO.get("exceptions");
            }
            request.setAttribute("array2", getExceptionList(exceptions, request));
            if (!"true".equals(request.getParameter("fail"))) {
                defaultForm.getDtoMap().putAll(resultDTO);
            }
            if (new Integer(1).equals(resultDTO.get("rangeType"))
                    && !"delete".equals(request.getParameter("operation"))
                    && !SchedulerConstants.TRUE_VALUE.equals(request.getParameter("onlyView"))
                    && !hasOnlyReadPermission(publicAppPermission, privateAppPermission, isPrivate)) {
                request.setAttribute("jsLoad", "onLoad=\"deshabilitaEmptyRangeType()\"");
            }
        } else {
            defaultForm.setDto("exceptionValue", new ArrayList(0));
            defaultForm.setDto("rangeType", 1);
            defaultForm.setDto("recurEveryDay", 1);
            defaultForm.setDto("recurEveryWeek", 1);
            defaultForm.setDto("recurEveryMonth", 1);
            defaultForm.setDto("recurEveryYear", 1);
            request.setAttribute("array2", new ArrayList(0));
        }
    }

    @Override
    protected void preProcessDefaultForm(DefaultForm defaultForm, HttpServletRequest request) {
        if ("true".equals(request.getParameter("fail"))) {
            if ("on".equals(request.getParameter("dto(isRecurrence)"))) {
                defaultForm.setDto("isRecurrence", true);
            }
            ArrayList exceptionList = new ArrayList(0);
            if (request.getParameterValues("array2") != null) {
                List list = Arrays.asList(request.getParameterValues("array2"));
                for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                    String date = (String) iterator.next();
                    exceptionList.add(new LabelValueBean(date, date));
                }
            }
            request.setAttribute("array2", exceptionList);
        }
    }

    protected Integer getViewerUserId(HttpServletRequest request) {
        if (null != getSchedulerUserId(request)) {
            return getSchedulerUserId(request);
        }

        return getUserId(request);
    }

    public static ArrayList getExceptionList(List e, HttpServletRequest request) {
        ArrayList exceptionList = new ArrayList(0);
        Date d;
        for (Iterator iterator = e.iterator(); iterator.hasNext();) {
            RecurException recurException = (RecurException) iterator.next();
            d = DateUtils.integerToDate(recurException.getDateValue());
            exceptionList.add(new LabelValueBean(DateUtils.parseDate(d, JSPHelper.getMessage(request, "datePattern")),
                    DateUtils.parseDate(d, JSPHelper.getMessage(request, "datePattern"))));
        }
        return exceptionList;
    }

    private boolean hasOnlyReadPermission(Byte publicPermission, Byte privatePermission, boolean isPrivateAppointment) {
        String permissionAsStr;

        if (isPrivateAppointment) {
            permissionAsStr = (privatePermission != null ? privatePermission.toString() : null);
        } else {
            permissionAsStr = (publicPermission != null ? publicPermission.toString() : null);
        }
        return String.valueOf(SchedulerPermissionUtil.READ).equals(permissionAsStr);
    }

    private boolean isPrivateAppointment(DefaultForm defaultForm, HttpServletRequest request) {
        return "true".equals(defaultForm.getDto("private"));
    }
}