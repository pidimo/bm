package com.piramide.elwis.web.bmapp.action.product;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.productmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class SalePositionParticipantCreateRESTAction extends DefaultAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  SalePositionParticipantCreateRESTAction..." + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;
        log.debug("FORM DTO FROM REST:::::::" + defaultForm.getDtoMap());

        Object productId = defaultForm.getDto("productId");

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue("userId");
        Integer userAddressId = (Integer) user.getValue("userAddressId");

        ActionErrors errors = validate(productId, userAddressId, request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        //add user id in dto
        defaultForm.setDto("userId", userId);

        return super.execute(mapping, defaultForm, request, response);
    }

    private ActionErrors validate(Object productId, Integer userAddressId, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (productId == null || !Functions.existsProduct(productId)) {
            errors.add("productNotFound", new ActionError("Product.event.notFound"));
            return errors;
        }

        if (com.piramide.elwis.web.bmapp.el.Functions.checkAlreadyIsParticipantInEvent(new Integer(productId.toString()), userAddressId, request)) {
            errors.add("participant", new ActionError("Product.event.userAlreadyIsParticipant"));
        }

        return errors;
    }
}
