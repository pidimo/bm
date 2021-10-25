package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9.1
 */
public class ProductContractContactListAction extends ContactListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Excecuting ProductContractContactListAction......" + request.getParameterMap());

        SearchForm listForm = (SearchForm) form;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        //add currentDate filter..
        listForm.setParameter("currentDate", DateUtils.dateToInteger(new DateTime((DateTimeZone) user.getValue("dateTimeZone"))).toString());

        return super.execute(mapping, form, request, response);
    }
}
