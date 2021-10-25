package com.piramide.elwis.web.bmapp.action.scheduler;

import com.piramide.elwis.web.schedulermanager.action.AppointmentParticipantImportAction;
import com.piramide.elwis.web.schedulermanager.form.ParticipantForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.7
 */
public class AppointmentParticipantImportRESTAction extends AppointmentParticipantImportAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  AppointmentParticipantImportRESTAction..." + request.getParameterMap());

        ParticipantForm participantForm = (ParticipantForm) form;
        setDefaultProperties(participantForm, request);

        return super.execute(mapping, participantForm, request, response);
    }

    private void setDefaultProperties(ParticipantForm participantForm, HttpServletRequest request) {

        participantForm.setDto("type", "user");
    }


}
