package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.alfacentauro.fantabulous.web.action.FantabulousAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: ProductPictureListAction.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class ProductPictureListAction extends FantabulousAction {
    protected static Log log = LogFactory.getLog(ProductPictureListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("product picture List Action...");
        log.debug("...................................................................................................");

        User user = (User) request.getSession(false).getAttribute(Constants.USER_KEY);

        //cheking if working product was not deleted by other user.
        log.debug("productId for user session  = " + request.getParameter("productId"));
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

        addConfig(ROWS_PER_PAGE, "1");
        addStaticFilter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());
        addStaticFilter(Constants.USERID, user.getValue(Constants.USERID).toString());
        setUser(user.getValue(Constants.USERID).toString());

        addFilter("productId", request.getParameter("productId"));
        return super.execute(mapping, form, request, response);

    }
}
