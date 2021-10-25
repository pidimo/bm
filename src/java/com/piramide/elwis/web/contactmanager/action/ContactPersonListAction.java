package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import org.alfacentauro.fantabulous.persistence.PersistenceManager;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Action will be used in lists of contacts persons.
 *
 * @author Fernando Montaño
 * @version ContactPersonListAction.java, v 2.0 Sept 15, 2004 10:48:26 AM
 */

public class ContactPersonListAction extends ContactListAction {
    private Log log = LogFactory.getLog(ContactPersonListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("ContactPerson forward list, the when tab is choosen");
        User user = (User) request.getSession(false).getAttribute(Constants.USER_KEY);
        SearchForm searchForm = (SearchForm) form;
        Map map = (Map) request.getSession().getAttribute("Fantabulous.Modules");
        if (map != null && !request.getParameter("contactId").equals(map.get("/contacts"))) { //defining default value when is showing another contact
            searchForm.setParameter("active", "1");
        } else { //if it is the same address check if the list status contains active, if no pass as parameter, otherwise skip it.
            Map listParameters = PersistenceManager.persistence().loadStatus(user.getValue("userId").toString(),
                    "contactPersonList", "contacts");
            if (!listParameters.containsKey("active")) {
                searchForm.setParameter("active", "1");
            }
        }
        return super.execute(mapping, form, request, response);
    }
}