package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DataImportManagerAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        if (null != (forward = validateDataImportExistence(request, mapping))) {
            return forward;
        }

        forward = super.execute(mapping, form, request, response);
        return forward;
    }

    protected ActionForward validateDataImportExistence(HttpServletRequest request, ActionMapping mapping) {

        Object profileId = request.getParameter("profileId");
        if (profileId != null) {
            ActionErrors errors = new ActionErrors();
            errors = ForeignkeyValidator.i.validate(ContactConstants.TABLE_IMPORTPROFILE, "profileid", profileId, errors, new ActionError("DataImport.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
                return mapping.findForward("ImportMainSearch");
            }
        } else {
            ActionErrors errors = new ActionErrors();
            errors.add("general", new ActionError("DataImport.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("ImportMainSearch");
        }

        return null;
    }
}
