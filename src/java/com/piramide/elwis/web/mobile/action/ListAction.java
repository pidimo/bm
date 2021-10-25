package com.piramide.elwis.web.mobile.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.contactmanager.el.Functions;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.action.FantabulousAction;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Jatun S.R.L.
 * This class defines the default values for all lists defined
 * in the mobile module.
 *
 * @author Fernando
 * @version $Id: ListAction.java 10522 2015-03-16 23:06:32Z miguel $
 */


public class ListAction extends FantabulousAction {
    protected static Log log = LogFactory.getLog(com.piramide.elwis.web.common.action.ListAction.class);
    private Integer userId;

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        SearchForm listForm = (SearchForm) form;

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        userId = new Integer(user.getValue(Constants.USERID).toString());


        addConfig(ROWS_PER_PAGE, "5");//by default limit to 10
        addStaticFilter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());
        addStaticFilter(Constants.USERID, user.getValue(Constants.USERID).toString());
        addContactSearchNameFixedMobileFilter(listForm);

        setUser(user.getValue(Constants.USERID).toString());
        return super.execute(mapping, form, request, response);
    }


    protected void saveErrors(HttpSession session, ActionErrors errors) {
        if (errors == null || errors.isEmpty()) {
            session.removeAttribute(Globals.ERROR_KEY);
        } else {
            session.setAttribute(Globals.ERROR_KEY, errors);
        }
    }

    @Override
    public ListStructure getListStructure() throws Exception {
        return AccessRightDataLevelSecurity.i.processAddressAccessRightByList(super.getListStructure(), userId);
    }

    protected void addContactSearchNameFixedMobileFilter(SearchForm listForm) {
        Functions.addContactSearchNameFixedFilter("contactSearchName", listForm, this);
    }

}