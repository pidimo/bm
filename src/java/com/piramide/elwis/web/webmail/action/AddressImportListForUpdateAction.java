package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.web.webmail.form.SearchMailAddressForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Alfacentauro Team
 * <p/>
 * this class help to list address for update
 *
 * @author miky
 * @version $Id: AddressImportListForUpdateAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class AddressImportListForUpdateAction extends WebmailListAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("ElwisWebmail AddressImportListForUpdateAction executing.............");

        String listName = "webmailSearchAddressList";
        if ("contactPerson".equals(request.getParameter("dto(searchType)"))) {
            listName = "campaign@queryContactPerson";
        } else {
            listName = "contacts@webmailSearchAddressList";
        }
        SearchMailAddressForm searchForm = (SearchMailAddressForm) form;
        ActionMapping actionMapping = new ActionMapping();
        ActionForward forward = new ActionForward();
        actionMapping.setInput("/webmail/Settings.jsp");
        actionMapping.setParameter(listName);
        actionMapping.setPath("/Mail/Forward/AddressImportListForUpdate");
        actionMapping.setType("com.piramide.elwis.web.webmail.action.AddressImportListForUpdateAction");
        actionMapping.setName("listForm");
        actionMapping.setScope("request");
        actionMapping.setValidate(false);

        forward.setName("Success");
        forward.setPath("/AddressImportListForUpdate.jsp?dto(searchType)=" + request.getParameter("dto(searchType)"));
        forward.setRedirect(false);
        forward.setContextRelative(false);

        actionMapping.addForwardConfig(forward);
        searchForm.setSearchType(request.getParameter("dto(searchType)"));
        request.setAttribute(org.apache.struts.Globals.MAPPING_KEY, actionMapping);
        return super.execute(actionMapping, searchForm, request, response);

    }

}
