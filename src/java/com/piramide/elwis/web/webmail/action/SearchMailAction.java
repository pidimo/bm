package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.webmail.form.SearchResultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class helps to the view of the mail tray
 *
 * @author Alvaro
 * @version $Id: SearchMailAction.java 10248 2012-07-27 15:10:14Z miguel $
 */
public class SearchMailAction extends MailTrayAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        SearchResultForm f = (SearchResultForm) form;

        ////FOR THE SEARCHS/////
        Object returning = request.getParameter("returning");
        Object searchText = f.getSearchText();
        Object searchFolder = f.getSearchFolder();
        Object searchFilter = f.getSearchFilter();
        if (searchText != null && returning == null) {
            if (searchFilter != null) {
                if (searchFilter.toString().equals(WebMailConstants.SEARCH_ALL_MESSAGE)) {
                    f.setParameter("mailPersonalFrom", searchText.toString());
                    f.setParameter("mailFrom", searchText.toString());
                    f.setParameter("mailSubject", searchText.toString());
                    f.setParameter("email", searchText.toString());
                    f.setParameter("body", searchText.toString());
                    f.setParameter("enableRecipientFilter", "true");
                } else if (searchFilter.toString().equals(WebMailConstants.SEARCH_ONLY_FROM)) {
                    f.setParameter("mailPersonalFrom", searchText.toString());
                    f.setParameter("mailFrom", searchText.toString());
                } else if (searchFilter.toString().equals(WebMailConstants.SEARCH_ONLY_SUBJECT)) {
                    f.setParameter("mailSubject", searchText.toString());
                } else if (searchFilter.toString().equals(WebMailConstants.SEARCH_ONLY_TO)) {
                    f.setParameter("email", searchText.toString());
                    f.setParameter("personal", searchText.toString());
                    f.setParameter("enableRecipientFilter", "true");
                } else if (searchFilter.toString().equals(WebMailConstants.SEARCH_ONLY_CONTENT)) {
                    f.setParameter("body", searchText.toString());
                }
            }
        }
        if (searchFolder != null && returning == null) {
            if (!searchFolder.toString().equals(WebMailConstants.SEARCH_ALL_FOLDERS)) {
                f.setParameter("mailFolderId_fk", searchFolder.toString());
                super.setSearchFolderId(searchFolder.toString());
            } else {
                //to compose column view as FROM/TO
                super.setSearchFolderId(null);
            }
        }

        ////END OF 'FOR THE SEARCHS'/////
        super.setIsSearch(true);
        return (super.execute(mapping, form, request, response));
    }
}
