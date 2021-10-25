package com.piramide.elwis.web.catalogmanager.action;

import net.java.dev.strutsejb.web.DefaultForm;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: CategoryValueAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CategoryValueAction extends com.piramide.elwis.web.common.action.ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        boolean pushNew = false;
        log.debug("Category Value  Action ....");
        SearchForm searchForm = (SearchForm) form;

        String categoryId = null != request.getParameter("categoryId") ?
                request.getParameter("categoryId") :
                request.getParameter("dto(categoryId)");

        if (searchForm.getParameter("new") != null) {
            DefaultForm categoryValueForm = new DefaultForm();
            categoryValueForm.getDtoMap().put("categoryId", categoryId);
            request.setAttribute("categoryValueForm", categoryValueForm);
            pushNew = true;
            searchForm.getParams().remove("new");
        }
        addFilter("category", categoryId);
        setModuleId(categoryId);
        ActionForward action = super.execute(mapping, searchForm, request, response);
        if (pushNew) {
            return mapping.findForward("New");
        } else {
            return action;
        }
    }
}
