package com.piramide.elwis.web.bmapp.action.scheduler;

import com.piramide.elwis.cmd.schedulermanager.AppointmentReadCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.bmapp.el.Functions;
import com.piramide.elwis.web.bmapp.util.MappingUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.action.AppointmentReadAction;
import com.piramide.elwis.web.schedulermanager.action.AppointmentReminderAction;
import com.piramide.elwis.web.schedulermanager.form.AppointmentForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public class AppointmentUpdateRESTAction extends AppointmentReminderAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  AppointmentUpdateRESTAction..." + request.getParameterMap());

        Functions.initializeSchedulerUserId(request);

        AppointmentForm appointmentForm = (AppointmentForm) form;
        Map dtoMap = getRESTDtoMap(appointmentForm);

        ActionErrors readErrors = readCurrentAppointmentInfo(appointmentForm, mapping, request);

        if (!readErrors.isEmpty()) {
            saveRESTErrors(appointmentForm, readErrors, request);
            return mapping.findForward("Fail");
        }

        mappingRESTValues(dtoMap, appointmentForm, request);

        return super.execute(mapping, appointmentForm, request, response);
    }

    @Override
    protected List<String> getExceptionList(HttpServletRequest request) {
        List<String> result = new ArrayList<String>();

        String datePattern = JSPHelper.getMessage(request, "datePattern").trim();

        List<LabelValueBean> exceptionList = (List<LabelValueBean>) request.getAttribute("array2");
        for (LabelValueBean labelValueBean : exceptionList) {
            result.add(DateUtils.dateToInteger((DateUtils.formatDate(labelValueBean.getValue(), datePattern))).toString());
        }
        return result;
    }

    private Map getRESTDtoMap(DefaultForm defaultForm) {
        Map dtoMap = new HashMap();
        dtoMap.putAll(defaultForm.getDtoMap());
        return dtoMap;
    }

    private void mappingRESTValues(Map dtoMap, AppointmentForm appointmentForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer sessionUserId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        MappingUtil.mappingProperty("title", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("addressId", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("contactPersonId", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("contact", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("appointmentTypeId", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("priorityId", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("location", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("startDate", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("startHour", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("startMin", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("endDate", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("endHour", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("endMin", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("descriptionText", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("freeTextId", dtoMap, appointmentForm);

        MappingUtil.mappingProperty("reminder", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("reminderType", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("timeBefore_1", dtoMap, appointmentForm);
        MappingUtil.mappingProperty("timeBefore_2", dtoMap, appointmentForm);

        MappingUtil.mappingPropertyBooleanAsOn("isAllDay", dtoMap, appointmentForm);
        MappingUtil.mappingPropertyBooleanAsOn("isPrivate", dtoMap, appointmentForm);
        MappingUtil.mappingPropertyBooleanAsOn("isRecurrence", dtoMap, appointmentForm);

        MappingUtil.mappingProperty("version", dtoMap, appointmentForm);

        appointmentForm.setDto("sendAppNotification", "true");
        appointmentForm.setDto("returnType", SchedulerConstants.RETURN_SEARCHLIST);
        appointmentForm.setDto("op", "update");
        appointmentForm.setDto("save", "save");
    }

    private ActionErrors readCurrentAppointmentInfo(AppointmentForm appointmentForm, ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        Integer appointmentId = new Integer(appointmentForm.getDto("appointmentId").toString());
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        Integer schedulerUserId = (Integer) user.getValue("schedulerUserId");

        AppointmentReadCmd cmd = new AppointmentReadCmd();
        cmd.putParam("appointmentId", appointmentId);
        cmd.putParam("userSessionId", userId);
        cmd.putParam("viewerUserId", (schedulerUserId != null) ? schedulerUserId : userId);
        cmd.putParam("title", appointmentForm.getDto("title"));

        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(cmd, null);
        } catch (AppLevelException e) {
            log.error("Error executing TelecomUtilCmd cmd", e);
        }

        if (resultDTO != null) {
            errors = processResuldDtoErrors(resultDTO, mapping, request);
            if (errors.isEmpty()) {

                //recurremnce default values
                processResultDTO(resultDTO, appointmentForm, request, null);

                appointmentForm.getDtoMap().putAll(MappingUtil.getAllPropertiesAsString(resultDTO));

                MappingUtil.mappingDateProperty("startDate", resultDTO, appointmentForm, request);
                MappingUtil.mappingDateProperty("endDate", resultDTO, appointmentForm, request);
                MappingUtil.mappingPropertyAsString("reminder", resultDTO, appointmentForm);

                //default recurrence values
                mappingDefaultRecurrenceValues(resultDTO, appointmentForm, request);

                //remove version
                appointmentForm.getDtoMap().remove("version");
            }
        }

        return errors;
    }

    private void mappingDefaultRecurrenceValues(ResultDTO resultDTO, AppointmentForm appointmentForm, HttpServletRequest request) {
        //week days
        MappingUtil.mappingPropertyBooleanAsOn("1", resultDTO, appointmentForm);
        MappingUtil.mappingPropertyBooleanAsOn("2", resultDTO, appointmentForm);
        MappingUtil.mappingPropertyBooleanAsOn("3", resultDTO, appointmentForm);
        MappingUtil.mappingPropertyBooleanAsOn("4", resultDTO, appointmentForm);
        MappingUtil.mappingPropertyBooleanAsOn("5", resultDTO, appointmentForm);
        MappingUtil.mappingPropertyBooleanAsOn("6", resultDTO, appointmentForm);
        MappingUtil.mappingPropertyBooleanAsOn("7", resultDTO, appointmentForm);

        //range recurrence en date
        MappingUtil.mappingDateProperty("rangeValueDate", resultDTO, appointmentForm, request);
    }


    public void processResultDTO(ResultDTO resultDTO,
                                 DefaultForm defaultForm,
                                 HttpServletRequest request,
                                 String reminderType) {

        boolean isPrivate = ((Boolean) resultDTO.get("isPrivate"));

        defaultForm.setDto("returnType", request.getParameter("type"));

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
            request.setAttribute("array2", AppointmentReadAction.getExceptionList(exceptions, request));
            if (!"true".equals(request.getParameter("fail"))) {
                defaultForm.getDtoMap().putAll(resultDTO);
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

    private ActionErrors processResuldDtoErrors(ResultDTO resultDTO, ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (resultDTO.isFailure()) {
            errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
        }
        return errors;
    }

    private void saveRESTErrors(DefaultForm defaultForm, ActionErrors errors, HttpServletRequest request) {
        saveErrors(request, errors);
    }

}
