package com.piramide.elwis.web.mobile.action.contacts;

import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages request from user in contact manager module.
 * Mainly this class check existence of working addressId, if address has been deleted by other user
 * redirect with error message, otherwise call super and execute respective command.
 *
 * @author Fernando Montaño
 * @version $Id: ContactManagerAction.java 9124 2009-04-17 00:35:24Z fernando $
 */

public class ContactManagerAction extends AbstractDefaultAction {

    /**
     * Check if addressId exists, if exists continue, otherwise cancel operation and return contact search page
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("ContactManagerAction execution...");

        //user in the session
        User user = (User) request.getSession(false).getAttribute(Constants.USER_KEY);

        //cheking if working address was not deleted by other user.

        log.debug("AddressId from parameter  = " + request.getParameter("contactId"));

        ActionErrors errors = new ActionErrors();
        if (request.getParameter("contactId") != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setPrimKey(request.getParameter("contactId"));
            try {
                EJBFactory.i.findEJB(addressDTO); //address already exists
            } catch (EJBFactoryException e) {
                if (e.getCause() instanceof FinderException) {
                    log.debug("The active address was deleted by other user...");
                    errors.add("general", new ActionError("Address.NotFound"));
                    saveErrors(request, errors);
                    return mapping.findForward("ContactsMainSearch");
                }
            }
        } else { //no contact selected
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("ContactsMainSearch");
        }

        /**
         * Add common required values for contact manager
         */
        if (form instanceof DefaultForm) {
            DefaultForm defaultForm = (DefaultForm) form;
            defaultForm.setDto("locale", user.getValue("locale"));
        }

        return super.execute(mapping, form, request, response);
    }
}