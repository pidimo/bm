package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.contactmanager.el.Functions;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DataImportManagerListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if (null != (forward = validateElementExistence(request, mapping))) {
            return forward;
        }

        addFilter("profileId", request.getParameter("profileId"));
        setModuleId(request.getParameter("profileId"));
        return super.execute(mapping, form, request, response);
    }

    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {

        String profileId = request.getParameter("profileId");
        if (profileId == null || !Functions.existsDataImport(profileId)) {
            ActionErrors errors = new ActionErrors();
            errors.add("ImportProfile", new ActionError("DataImport.NotFound"));
            saveErrors(request.getSession(), errors);

            //return main list
            return mapping.findForward("ImportMainSearch");
        }

        return null;
    }
}
