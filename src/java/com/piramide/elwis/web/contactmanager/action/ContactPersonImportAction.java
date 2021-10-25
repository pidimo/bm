package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.contactmanager.form.AddressContactPersonHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Action used to import a contact person, after import, it put the predefined telecomtypes
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonImportAction.java 9124 2009-04-17 00:35:24Z fernando $
 */

public class ContactPersonImportAction extends ContactManagerAction {
    private Log log = LogFactory.getLog(ContactPersonImportAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing contact person import action");
        ActionForward forward = super.execute(mapping, form, request, response); //execute the command
        if ("Success".equals(forward.getName())) {
            DefaultForm defaultForm = (DefaultForm) form;
            defaultForm.setDto("contactPersonTelecomMap", AddressContactPersonHelper.getDefaultTelecomTypes(request));
        }
        return forward;
    }
}
