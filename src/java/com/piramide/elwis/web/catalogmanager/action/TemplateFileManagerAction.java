package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 * @version : $Id TemplateFileManagerAction ${time}
 */
public class TemplateFileManagerAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("execute('" + mapping + "', '" + form + "', '" + request + "', '" + response + "')");


        ActionErrors errors = new ActionErrors();


        if (request.getParameter("dto(templateId)") != null) {

            errors = ForeignkeyValidator.i.validate(CatalogConstants.TABLE_TEMPLATE, "templateid",
                    request.getParameter("dto(templateId)"), errors, new ActionError("template.NotFound"));

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }

        } else {
            errors.add("categoryId", new ActionError("template.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        return super.execute(mapping, form, request, response);
    }
}
