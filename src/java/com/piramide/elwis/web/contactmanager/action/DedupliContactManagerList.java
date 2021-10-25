package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.ListAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DedupliContactManagerList extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        addFilter("dedupliContactId", request.getParameter("dedupliContactId"));
        setModuleId(request.getParameter("dedupliContactId"));
        return super.execute(mapping, form, request, response);
    }
}
