package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.contactmanager.delegate.dataimport.DataImportDelegate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2.4
 */
public class ImportDuplicateEmptyAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing  ImportDuplicateEmptyAction..." + request.getParameterMap());

        Integer profileId = Integer.valueOf(request.getParameter("profileId"));

        DataImportDelegate.i.emptyImportProfileDuplicates(profileId);

        return mapping.findForward("Success");
    }
}