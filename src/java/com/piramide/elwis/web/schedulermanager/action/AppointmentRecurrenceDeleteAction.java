package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.schedulermanager.AppointmentRecurrenceDeleteCmd;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.schedulermanager.form.AppointmentForm;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 20, 2005
 * Time: 11:54:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentRecurrenceDeleteAction extends DefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--------  AppointmentRecurrenceDeleteAction executeFunction  --------");

        AppointmentForm appointmentForm = (AppointmentForm) form;
        ActionErrors errors = new ActionErrors();
        String type = request.getParameter("type");//tipo de interface que llega: day, week...
        String calendar = request.getParameter("calendar");//fecha en la que se encuentra al appointment.
        ActionForward returnSuccess = mapping.findForward("Cancel");
        AppointmentRecurrenceDeleteCmd cmd = new AppointmentRecurrenceDeleteCmd();

        cmd.putParam(appointmentForm.getDtoMap());
        BusinessDelegate.i.execute(cmd, request);
        errors = MessagesUtil.i.convertToActionErrors(mapping, request, cmd.getResultDTO());
        saveErrors(request, errors);
        if (type != null && !cmd.getResultDTO().isFailure()) {
            if (type.equals("1")) {
                returnSuccess = mapping.findForward("ReturnDay");
            } else if (type.equals("2")) {
                returnSuccess = mapping.findForward("ReturnWeek");
            } else if (type.equals("3")) {
                returnSuccess = mapping.findForward("ReturnMonth");
            } else if (type.equals("4")) {
                returnSuccess = mapping.findForward("ReturnYear");
            }
        }
        return new ActionForwardParameters().add("calendar", calendar)
                .add("oldType", type)
                .forward(returnSuccess);
    }
}
