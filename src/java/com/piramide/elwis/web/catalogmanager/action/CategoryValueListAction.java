package com.piramide.elwis.web.catalogmanager.action;

import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryValueListAction extends com.piramide.elwis.web.common.action.ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        SearchForm searchForm = (SearchForm) form;

        String categoryId = null != request.getParameter("categoryId") ?
                request.getParameter("categoryId") :
                request.getParameter("dto(categoryId)");

        addFilter("category", categoryId);
        setModuleId(categoryId);
        return super.execute(mapping, searchForm, request, response);
    }
}
