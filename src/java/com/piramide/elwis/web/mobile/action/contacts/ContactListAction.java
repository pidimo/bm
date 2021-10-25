package com.piramide.elwis.web.mobile.action.contacts;

import com.piramide.elwis.dto.contactmanager.AddressDTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for contacts lists. When an specific contact is being used.
 *
 * @author Fernando
 */

public class ContactListAction extends com.piramide.elwis.web.mobile.action.ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {


        log.debug("AddressId for user session  = " + request.getParameter("contactId"));
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("contactId") != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setPrimKey(request.getParameter("contactId"));
            try {
                EJBFactory.i.findEJB(addressDTO); //address already exists
            } catch (EJBFactoryException e) {
                if (e.getCause() instanceof FinderException) {
                    log.debug("The active address was deleted by other user... show errors and " +
                            "return to contact search page");

                    errors.add("general", new ActionError("Address.NotFound"));
                    saveErrors(request, errors);
                    return mapping.findForward("ContactsMainSearch");
                }
            }
            //put addressId as list default param

        } else { //if addressId not found
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("ContactsMainSearch");
        }


        addFilter("addressId", request.getParameter("contactId"));

        setModuleId(request.getParameter("contactId"));
        return super.execute(mapping, form, request, response);
    }
}