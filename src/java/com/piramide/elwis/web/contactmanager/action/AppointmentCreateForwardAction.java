package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.web.contactmanager.el.Functions;
import com.piramide.elwis.web.schedulermanager.action.AppointmentAction;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ivan Alban
 * @version 4.3.7
 */
public class AppointmentCreateForwardAction extends AppointmentAction {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        if (!Functions.existsAddress(request.getParameter("contactId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        return null;
    }

    @Override
    protected void preProcessDefaultForm(DefaultForm defaultForm, HttpServletRequest request) {
        super.preProcessDefaultForm(defaultForm, request);

        ResultDTO resultDTO = readAddressInformation(request);
        if (null != resultDTO) {
            defaultForm.setDto("addressId", request.getParameter("contactId"));
            defaultForm.setDto("contact", resultDTO.get("addressName"));
        }
    }

    @Override
    protected Integer getSchedulerUserId(HttpServletRequest request) {
        return getUserId(request);
    }

    private ResultDTO readAddressInformation(HttpServletRequest request) {
        LightlyAddressCmd cmd = new LightlyAddressCmd();
        cmd.putParam("addressId", request.getParameter("contactId"));
        try {
            return BusinessDelegate.i.execute(cmd, request);
        } catch (AppLevelException e) {
            log.debug("Cannot read the address information for addressId: " + request.getParameter("contactId"));
        }

        return null;
    }
}
