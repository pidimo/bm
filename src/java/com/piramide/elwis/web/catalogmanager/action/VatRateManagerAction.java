package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 * @version : $Id VatRateManagerAction ${time}
 */
public class VatRateManagerAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Executing execute method with ...\n" +
                "mapping  = " + mapping + "\n" +
                "form     = " + form + "\n" +
                "request  = " + request + "\n" +
                "response = " + response);


        ActionErrors errors = new ActionErrors();

        if (request.getParameter("dto(vatId)") != null) {

            errors = ForeignkeyValidator.i.validate(CatalogConstants.TABLE_VAT, "vatid",
                    request.getParameter("dto(vatId)"), errors, new ActionError("customMsg.NotFound",
                            request.getParameter("dto(vatLabel)")));

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }

        } else {
            errors.add("vatid", new ActionError("customMsg.NotFound",
                    request.getParameter("dto(vatLabel)")));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        return super.execute(mapping, form, request, response);
    }
}
