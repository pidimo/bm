package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * this class mantains the categoryId whe the saveAndNew button is pressed
 *
 * @author Ivan
 * @version $Id: CategoryValueCreateDefaultAction.java 9703 2009-09-12 15:46:08Z fernando $
 * @deprecated replaced by CategoryValueManagerAction
 */
public class CategoryValueCreateDefaultAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionForward forward = null;

        DefaultForm myForm = (DefaultForm) form;

        String categoryId = request.getParameter("dto(categoryId)");
        String categoryName = request.getParameter("dto(categoryName)");
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("categoryName", categoryName);

        if (isCancelled(request)) {
            forward = mapping.findForward("Cancel");
        }


        if (null != request.getParameter("create")) {
            forward = super.execute(mapping, form, request, response);
        }


        /*if (request.getParameter("dto(op)") != null)
            return super.execute(mapping, form, request, response);*/


        //if <SAVEANDNEW_BUTTON is pressed>
        if (request.getParameter("SaveAndNew") != null) {
            log.debug("Is SaveAndNew");
            ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
            DefaultForm newForm = (DefaultForm) form;
            //If errors occur in the filling the form
            if (errors != null) {
                if (!errors.isEmpty()) {
                    request.setAttribute(Globals.ERROR_KEY, errors);
                    forward = mapping.getInputForward();
                }
            } else {
                log.debug("Nos vamos por el saveAndNew");
                // save the actual category
                String country = newForm.getDto("categoryId").toString();
                newForm.getDtoMap().clear(); // clear Form
                // put the actual category
                newForm.setDto("categoryId", country);
                log.debug("*******--------------- " + forward);
                forward = mapping.findForward("SaveAndNew");
                log.debug("*******- " + forward);
            }
        }

        return forward;
    }

}
