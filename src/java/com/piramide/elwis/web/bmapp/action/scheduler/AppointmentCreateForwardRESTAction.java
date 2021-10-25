package com.piramide.elwis.web.bmapp.action.scheduler;

import com.piramide.elwis.web.bmapp.el.Functions;
import com.piramide.elwis.web.schedulermanager.action.AppointmentAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.5
 */
public class AppointmentCreateForwardRESTAction extends AppointmentAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  AppointmentCreateForwardRESTAction..." + request.getParameterMap());

        Functions.initializeSchedulerUserId(request);

        return super.execute(mapping, form, request, response);
    }


}
