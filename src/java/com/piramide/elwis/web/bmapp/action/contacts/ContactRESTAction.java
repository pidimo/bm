package com.piramide.elwis.web.bmapp.action.contacts;

import com.piramide.elwis.web.bmapp.action.DefaultRESTAction;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class ContactRESTAction extends DefaultRESTAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  ContactRESTAction..." + request.getParameterMap());

        ActionForward forward = mapping.findForward("List");

        if (!GenericValidator.isBlankOrNull(request.getParameter("dto(addressId)"))) {
            forward = mapping.findForward("AddressRest");
        }

        if (!GenericValidator.isBlankOrNull(request.getParameter("dto(contactPersonId)"))) {
            forward = mapping.findForward("ContactPersonRest");
        }

        return forward;
    }
}
