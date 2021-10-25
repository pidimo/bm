package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.web.common.action.ListAction;
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
 * @version $Id: CategoryListAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CategoryListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        boolean pushNew = false;
        log.debug("Executing execute method on CategoryListAction ");

        SearchForm searchForm = (SearchForm) form;
        if (null != searchForm.getParameter("new")) {
            DefaultForm categoryForm = new DefaultForm();
            categoryForm.getDtoMap().put("table", searchForm.getParameter("tableId"));
            request.setAttribute("categoryForm", categoryForm);
            pushNew = true;
            searchForm.getParams().remove("new");
        }

        ActionForward action = super.execute(mapping, form, request, response);
        if (pushNew) {
            return mapping.findForward("New");
        } else {
            return action;
        }
    }
}