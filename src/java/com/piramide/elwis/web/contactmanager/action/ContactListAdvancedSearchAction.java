package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.alfacentauro.fantabulous.persistence.PersistenceManager;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: ContactListAdvancedSearchAction.java 13-feb-2009 12:33:12
 */
public class ContactListAdvancedSearchAction extends ContactListAction {
    private final String ADVANCED_SEARCH_KEY = "advancedSearchId";

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        updateAdvancedSearchStatus(request);

        return super.execute(mapping, form, request, response);
    }

    private void updateAdvancedSearchStatus(HttpServletRequest request) {
        String contactId = request.getParameter("contactId");

        String advancedSearchId = (String) request.getSession().getAttribute(ADVANCED_SEARCH_KEY);

        if (null == advancedSearchId) {
            request.getSession().setAttribute(ADVANCED_SEARCH_KEY, contactId);
        } else {
            if (!advancedSearchId.equals(contactId)) {
                User user = RequestUtils.getUser(request);
                String userId = user.getValue(Constants.USERID).toString();

                PersistenceManager.persistence().deleteStatus(userId,
                        "communicationAdvancedSearchList",
                        "contacts");

                request.getSession().setAttribute(ADVANCED_SEARCH_KEY, contactId);
            }
        }
    }
}
