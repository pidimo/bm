package com.piramide.elwis.web.bmapp.action.contacts;

import com.piramide.elwis.web.bmapp.action.ListRESTAction;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.6
 */
public class ContactMainListRESTAction extends ListRESTAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  ContactMainListRESTAction..." + request.getParameterMap());

        //define default order
        SearchForm listForm = (SearchForm) form;
        listForm.setOrderParam("1", "addressName1-true");

        return super.execute(mapping, listForm, request, response);
    }

}
