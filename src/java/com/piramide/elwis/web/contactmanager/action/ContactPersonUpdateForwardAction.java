package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class ContactPersonUpdateForwardAction extends ContactManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("ContactPersonUpdateForwardAction executing..." +request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;

        if (!userWithAccessOnDataLevel(defaultForm, request)) {
            ActionErrors errors = new ActionErrors();
            errors.add("accessError", new ActionError("Contact.accessRight.error.access"));
            saveErrors(request, errors);
            return withoutAccessActionForward(mapping, request);
        }

        return super.execute(mapping, form, request, response); //execute the command
    }

    protected boolean userWithAccessOnDataLevel(DefaultForm defaultForm, HttpServletRequest request) {
        boolean userAccessAddress = true;
        boolean userAccessContactPerson = true;

        Object addressId = defaultForm.getDto("addressId");
        Object contactPersonId = defaultForm.getDto("contactPersonId");

        if (addressId != null && !"".equals(addressId.toString())) {
            userAccessAddress = AccessRightDataLevelSecurity.i.userWithAccessToAddressOnDataLevel(new Integer(addressId.toString()), request);
        }
        if (contactPersonId != null && !"".equals(contactPersonId.toString())) {
            userAccessContactPerson = AccessRightDataLevelSecurity.i.userWithAccessToAddressOnDataLevel(new Integer(contactPersonId.toString()), request);
        }
        return (userAccessAddress && userAccessContactPerson);
    }

    protected ActionForward withoutAccessActionForward(ActionMapping mapping, HttpServletRequest request) {
        return mapping.findForward("MainSearch");
    }

}
