package com.piramide.elwis.web.bmapp.action.scheduler;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.bmapp.el.Functions;
import com.piramide.elwis.web.bmapp.util.MappingUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.action.AppointmentReminderAction;
import com.piramide.elwis.web.schedulermanager.form.AppointmentForm;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public class AppointmentCreateRESTAction extends AppointmentReminderAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  AppointmentCreateRESTAction..." + request.getParameterMap());

        AppointmentForm appointmentForm = (AppointmentForm) form;
        log.debug("FORM DTO FROM REST:::::::" + appointmentForm.getDtoMap());
        log.debug("IS CREATEEEE:::::::" + isCreate(request));
        log.debug("IS UPDATEEEEE:::::::" + isUpdate(request));


        setDefaultProperties(appointmentForm, request);

        log.debug("FORM DTO AFTER MAPPING::::::::" + appointmentForm.getDtoMap());

        Functions.initializeSchedulerUserId(request);

        return super.execute(mapping, appointmentForm, request, response);
    }

    private void setDefaultProperties(AppointmentForm appointmentForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer userId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        Map dtoMap = getRESTDtoMap(appointmentForm);

        MappingUtil.mappingPropertyBooleanAsOn("isPrivate", dtoMap, appointmentForm);
        MappingUtil.mappingPropertyBooleanAsOn("isRecurrence", dtoMap, appointmentForm);
        MappingUtil.mappingPropertyBooleanAsOn("isAllDay", dtoMap, appointmentForm);

        appointmentForm.setDto("sendAppNotification", "true");
        appointmentForm.setDto("createdById", userId);
        appointmentForm.setDto("userId", userId);
        appointmentForm.setDto("companyId", user.getValue("companyId").toString());
        appointmentForm.setDto("returnType", SchedulerConstants.RETURN_SEARCHLIST);
        appointmentForm.setDto("op", "create");
        appointmentForm.setDto("save", "save");
    }

    private Map getRESTDtoMap(DefaultForm defaultForm) {
        Map dtoMap = new HashMap();
        dtoMap.putAll(defaultForm.getDtoMap());
        return dtoMap;
    }


}
