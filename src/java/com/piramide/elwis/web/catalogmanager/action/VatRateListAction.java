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
 * @version $Id: VatRateListAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class VatRateListAction extends com.piramide.elwis.web.common.action.ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        boolean pushNew = false;
        log.debug("VatRate List Action ....");
        SearchForm searchForm = (SearchForm) form;
        if (searchForm.getParameter("new") != null) {
            DefaultForm vatRateForm = new DefaultForm();
            vatRateForm.getDtoMap().put("vatId", searchForm.getParameter("vatRateVatId"));
            request.setAttribute("vatRateForm", vatRateForm);
            pushNew = true;
        }
        ActionForward action = super.execute(mapping, form, request, response);
        if (pushNew) {
            return mapping.findForward("New");
        } else {
            return action;
        }
    }
}
