package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.common.action.SecondSimpleAdvancedListAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CommunicationSimpleAdvancedListAction.java 13-mar-2009 14:54:34 $
 */
public class CommunicationSimpleAdvancedListAction extends SecondSimpleAdvancedListAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CommunicationSimpleAdvancedListAction ...");
        //cheking if working address was not deleted by other user.
        log.debug("AddressId for user session  = " + request.getParameter("contactId"));

        ActionErrors errors = new ActionErrors();
        if (request.getParameter("contactId") != null) {
            errors = ForeignkeyValidator.i.validate(ContactConstants.TABLE_ADDRESS, "addressid",
                    request.getParameter("contactId"), errors, new ActionError("Address.NotFound"));

            if (!errors.isEmpty()) {
                log.debug("The active address was deleted by other user... ");
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }
        } else {
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        log.debug("Final AddSeach:");
        addFilter("addressId", request.getParameter("contactId"));

        setModuleId(request.getParameter("contactId"));
        return super.execute(mapping, form, request, response);
    }
}
