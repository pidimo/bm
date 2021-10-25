package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.web.common.action.ListAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * This class executes de special logic for catalog city
 *
 * @author Ivan
 * @version $Id: HolidayAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class HolidayAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        log.debug("Executing HolidayAction...");
        SearchForm searchForm = (SearchForm) form;
        if (searchForm.getParameter("new") != null) {
            DefaultForm holidayForm = new DefaultForm();
            holidayForm.getDtoMap().put("countryId", searchForm.getParameter("countryId"));
            request.setAttribute("HolidayForm", holidayForm);
            return mapping.findForward("New");
        }
        super.initializeFilter();
        String countryParam = (String) searchForm.getParameter("countryId");
        if (countryParam != null && countryParam.equals("indepent")) {
            addFilter("indepent", "true");
        } else {
            addFilter("normal", "true");
        }

        return super.execute(mapping, form, request, response);

    }

    public Map interceptParametersLoaded(Map map) {
        String countryParam = (String) map.get("countryId");
        if (countryParam != null && countryParam.equals("indepent")) {
            map.put("indepent", "true");
            map.put("normal", "false");
        } else {
            map.put("normal", "true");
        }
        return map;
    }

}
