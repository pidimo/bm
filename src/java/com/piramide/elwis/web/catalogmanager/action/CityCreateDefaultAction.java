package com.piramide.elwis.web.catalogmanager.action;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * this action is especial for the city Create action,
 * maintain the country combo list Id for the "save and new"
 * button
 *
 * @author Ivan
 * @version $Id: CityCreateDefaultAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CityCreateDefaultAction extends net.java.dev.strutsejb.web.DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Elwis CityCreateDefaultAction executing...");

        //if <CANCEL_BUTTON> is pressed
        if (isCancelled(request)) {
            log.debug("Is Cancel");
            return (mapping.findForward("Cancel"));
        } else {
            log.debug("Execute..");
            ActionForward forward = super.execute(mapping, form, request, response);

            //if <SAVEANDNEW_BUTTON is pressed>
            if (request.getParameter("SaveAndNew") != null) {
                log.debug("Is SaveAndNew");
                ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
                DefaultForm newForm = (DefaultForm) form;

                //If errors occur in the filling the form
                if (errors != null) {
                    if (!errors.isEmpty()) {
                        request.setAttribute(Globals.ERROR_KEY, errors);
                        return mapping.getInputForward();
                    }
                } else {
                    // save the actual country
                    String country = newForm.getDto("countryId").toString();
                    newForm.getDtoMap().clear(); // clear Form
                    // put the actual country
                    newForm.setDto("countryId", country);
                    forward = mapping.findForward("SaveAndNew");
                }
            }

            return forward;
        }
    }
}
