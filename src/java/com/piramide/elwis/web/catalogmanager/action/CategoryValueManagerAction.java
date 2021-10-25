package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 * @version : $Id CategoryValueManagerAction ${time}
 */
public class CategoryValueManagerAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();

        if (request.getParameter("dto(categoryId)") != null) {
            errors = ForeignkeyValidator.i.validate(CatalogConstants.TABLE_CATEGORY, "categoryid",
                    request.getParameter("dto(categoryId)"), errors, new ActionError("customMsg.NotFound",
                            request.getParameter("dto(categoryName)")));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }
        } else {
            errors.add("categoryId", new ActionError("customMsg.NotFound",
                    request.getParameter("dto(categoryName)")));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        return super.execute(mapping, form, request, response);
    }
}
