package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.dto.productmanager.ProductDTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Titus
 * @version ProductListAction.java, v 2.0 Aug 24, 2004 5:09:51 PM
 */
public class ProductListAction extends com.piramide.elwis.web.common.action.ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("product list action execution..." + request.getParameterMap());

        //cheking if working product was not deleted by other user.
        log.debug("productId for user request= " + request.getParameter("productId"));
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
            errors.add("productSessionNotFound", new ActionError("Product.NotFound"));
            saveErrors(request.getSession(), errors);
            return new ActionForward(mapping.findForward("MainSearch").getPath(), true); //new forward with redirect true
        }

        addFilter("productId", request.getParameter("productId")); //adding module productId filter value.
        setModuleId(request.getParameter("productId"));
        return super.execute(mapping, form, request, response);
    }
}
