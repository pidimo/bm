package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.Globals;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * this action is especial for the city Create action,
 * maintain the country combo list Id for the "save and new"
 * button
 *
 * @author Ivan
 * @version $Id: PricingCreateDefaultAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class PricingCreateDefaultAction extends net.java.dev.strutsejb.web.DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {


        User user = (User) request.getSession(false).getAttribute(Constants.USER_KEY);

        log.debug("productId for user request  = " + request.getParameter("productId"));
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("productId") != null) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setPrimKey(request.getParameter("productId"));
            try {
                EJBFactory.i.findEJB(productDTO); //product already exists
            } catch (EJBFactoryException e) {
                if (e.getCause() instanceof FinderException) {
                    log.debug("The active product was deleted by other user...");

                    errors.add("general", new ActionError("Product.NotFound"));
                }
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }

        } else { //no product selected
            errors.add("productSessionNotFound", new ActionError("Product.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }


        log.debug("Elwis PricingCreateDefaultAction executing...");

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
                errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
                DefaultForm newForm = (DefaultForm) form;

                //If errors occur in the filling the form
                if (errors != null) {
                    if (!errors.isEmpty()) {
                        request.setAttribute(Globals.ERROR_KEY, errors);
                        return mapping.getInputForward();
                    }
                } else {
                    // save the actual unitName

                    String unit = newForm.getDto("unitName") != null ? newForm.getDto("unitName").toString() : "";
                    newForm.getDtoMap().clear(); // clear Form
                    // put the actual unitName
                    newForm.setDto("unitName", unit);
                    request.setAttribute("dto(unitName)", unit);
                    forward = mapping.findForward("SaveAndNew");
                }
            }

            return forward;
        }
    }
}
