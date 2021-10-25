package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages request from user in product manager module.
 * Mainly this class check existence of working productId, if product has been deleted by other user
 * redirect with error message, otherwise call super and execute respective command.
 *
 * @author Titus
 * @version ProductManagerAction.java, v 2.0 Aug 23, 2004 11:13:27 AM
 */
public class ProductManagerAction extends AbstractDefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        if (null != (forward = validateElementExistence((DefaultForm) form, request, mapping))) {
            return forward;
        }

        ((DefaultForm) form).setDto("isProduct", "true");

        User user = RequestUtils.getUser(request);
        if (form instanceof DefaultForm) {
            if (!(((DefaultForm) form).getDto("companyId") != null)) {
                ((DefaultForm) form).setDto("companyId", user.getValue("companyId"));
            }
        }

        forward = super.execute(mapping, form, request, response);

        return processForward(forward, (DefaultForm) form, mapping, request);
    }

    protected ActionForward validateElementExistence(DefaultForm defaultForm,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {
        log.debug("productId for user request  = " + request.getParameter("productId"));
        //cheking if working product was not deleted by other user.
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("productId") != null) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setPrimKey(request.getParameter("productId"));
            try {
                EJBFactory.i.findEJB(productDTO); //product already exists
            } catch (EJBFactoryException e) {
                if (e.getCause() instanceof FinderException) {
                    log.debug("The active product was deleted by other user...");
                    /*errors.add("general", new ActionError("customMsg.NotFound", user.getValue("productName")));*/
                    errors.add("general", new ActionError("Product.NotFound"));
                }
                saveErrors(request.getSession(), errors);
                return new ActionForward(mapping.findForward("MainSearch").getPath(), true); //new forward with redirect true
            }

        } else { //no product selected
            errors.add("productNotFound", new ActionError("Product.NotFound"));
            saveErrors(request.getSession(), errors);
            return new ActionForward(mapping.findForward("MainSearch").getPath(), true); //new forward with redirect true
        }

        return null;
    }

    protected ActionForward processForward(ActionForward forward,
                                           DefaultForm defaultForm,
                                           ActionMapping mapping,
                                           HttpServletRequest request) {
        return forward;
    }
}
