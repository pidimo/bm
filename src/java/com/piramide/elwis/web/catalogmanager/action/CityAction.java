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
 * This class executes de special logic for catalog city
 *
 * @author Ivan
 * @version $Id: CityAction.java 7936 2007-10-27 16:08:39Z fernando $
 */

public class CityAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        log.debug("Executing CityAction...");
        SearchForm searchForm = (SearchForm) form;
        if (searchForm.getParameter("new") != null) {
            DefaultForm cityForm = new DefaultForm();
            cityForm.getDtoMap().put("countryId", searchForm.getParameter("countryId"));
            request.setAttribute("cityForm", cityForm);
            return mapping.findForward("New");
        }
        return super.execute(mapping, form, request, response);

    }
}
