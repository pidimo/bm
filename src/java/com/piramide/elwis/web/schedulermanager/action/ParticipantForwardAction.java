package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jul 28, 2005
 * Time: 4:01:31 PM
 * To change this template use File | Settings | File Templates.
 */

public class ParticipantForwardAction extends ForwardAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--------  ParticipantForwardAction executeFunction  --------");
        ActionErrors errors = new ActionErrors();
        DefaultForm participantForm = (DefaultForm) form;
        String id = null;

        if (request.getParameter("appointmentId") != null && !"".equals(request.getParameter("appointmentId"))) {
            id = request.getParameter("appointmentId");
        } else {
            id = request.getParameter("dto(appointmentId)");
        }
        errors = ForeignkeyValidator.i.validate(SchedulerConstants.TABLE_APPOINTMENT, "appointmentid",
                id, errors, new ActionError("msg.NotFound", request.getParameter("dto(title)")));
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return new ActionForwardParameters().add("simple", "true")
                    .forward(mapping.findForward("MainSearch"));
        }
        //para verificar si pertenece a un grupo o es usuario independiente.
        if (request.getParameter("userGroupId") != null && !"".equals(request.getParameter("userGroupId")) && !"group".equals(request.getParameter("flag"))) {

            String type = request.getParameter("type");
            if (type == null) {
                type = "";
            }
            return new ActionForwardParameters()
                    .add("calendar", request.getParameter("calendar"))
                    .add("type", type)
                    .add("userType", request.getParameter("userType"))
                    .add("userGroupId", request.getParameter("userGroupId"))
                    .add("index", request.getParameter("index"))
                    .add("appointmentId", request.getParameter("appointmentId"))
                    .add("scheduledUserId", request.getParameter("dto(scheduledUserId)"))
                    .add("participantName", request.getParameter("dto(participantName)"))
                    .add("appointmentGroupName", request.getParameter("dto(appointmentGroupName)"))
                    .forward(mapping.findForward("DeleteGroup"));
        }

        return super.execute(mapping, participantForm, request, response);
    }
}
