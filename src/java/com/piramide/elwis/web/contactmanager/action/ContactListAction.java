package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.dto.contactmanager.AddressDTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action will be used in lists of contacts, this checks if session addressId
 * was not deleted by other user.
 *
 * @author Titus
 * @version ContactListAction.java, v 2.0 May 13, 2004 10:48:26 AM
 */

public class ContactListAction extends com.piramide.elwis.web.common.action.ListAction {

    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Contact list action execution...");
        //User user = (User) request.getSession(false).getAttribute(Constants.USER_KEY);

        //cheking if working address was not deleted by other user.
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
                    return mapping.findForward("MainSearch");
                }
            }
            //put addressId as list default param

        } else { //if addressId not found
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        addFantabulousFilters(request);
        return super.execute(mapping, form, request, response);
    }

    protected void addFantabulousFilters(HttpServletRequest request) {
        addFilter("addressId", request.getParameter("contactId"));
        setModuleId(request.getParameter("contactId"));
    }
}