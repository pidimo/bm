package com.piramide.elwis.web.bmapp.action.scheduler;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.bmapp.el.Functions;
import com.piramide.elwis.web.bmapp.util.MappingUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.action.AppointmentDeleteAction;
import com.piramide.elwis.web.schedulermanager.form.AppointmentForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5.7
 */
public class AppointmentDeleteRESTAction extends AppointmentDeleteAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  AppointmentDeleteRESTAction......" + request.getParameterMap());

        Functions.initializeSchedulerUserId(request);

        AppointmentForm appointmentForm = (AppointmentForm) form;
        setDefaultProperties(appointmentForm, request);

        return super.execute(mapping, form, request, response);
    }

    private void setDefaultProperties(AppointmentForm appointmentForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer userId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        MappingUtil.mappingPropertyBooleanAsOn("isPrivate", appointmentForm.getDtoMap(), appointmentForm);

        appointmentForm.setDto("returnType", ""); //empty to return Success forward
        appointmentForm.setDto("op", "delete");
        //appointmentForm.setDto("save", "save");
    }

}
