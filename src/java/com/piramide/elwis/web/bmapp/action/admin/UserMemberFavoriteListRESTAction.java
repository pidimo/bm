package com.piramide.elwis.web.bmapp.action.admin;

import com.piramide.elwis.web.bmapp.action.ListRESTAction;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.0.2
 */
public class UserMemberFavoriteListRESTAction extends ListRESTAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  UserMemberFavoriteListRESTAction..." + request.getParameterMap());

        SearchForm listForm = (SearchForm) form;
        //define favorite parameter
        addStaticFilter("isFavoriteWVApp", "1");

        return super.execute(mapping, listForm, request, response);
    }
}
