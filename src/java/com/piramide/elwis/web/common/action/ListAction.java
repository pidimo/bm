package com.piramide.elwis.web.common.action;

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
import java.util.HashMap;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: ListAction.java 10569 2015-04-30 19:14:24Z miguel $
 */


public class ListAction extends FantabulousAction {
    protected static Log log = LogFactory.getLog(ListAction.class);
    protected Integer userId;

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }
        log.debug("Master ListAction");
        SearchForm listForm = (SearchForm) form;

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        log.debug("User Values:" + user.getValueMap());
        userId = new Integer(user.getValue(Constants.USERID).toString());

        addConfig(ROWS_PER_PAGE, getRowsPerPage(request).toString());
        addStaticFilter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());
        addStaticFilter(Constants.USERID, user.getValue(Constants.USERID).toString());
        addStaticFilter(Constants.USER_ADDRESSID, user.getValue(Constants.USER_ADDRESSID).toString());
        addContactSearchNameFixedFilter(listForm);

        setUser(user.getValue(Constants.USERID).toString());

        ///////////////////////////////////////////////////////////
        //todo: logs for verify fantabulous params (show all rows in some cases)
        Map previousParams = previousTestListParameters(mapping, form, request);
        ///////////////////////////////////////////////////////////

        ActionForward forward = super.execute(mapping, form, request, response);

        ///////////////////////////////////////////////////////////
        //todo: logs for verify fantabulous params (show all rows in some cases)
        testParametersExecuteLogs(mapping, form, request, previousParams);
        ///////////////////////////////////////////////////////////

        return forward;
    }

    protected Integer getRowsPerPage(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        return new Integer(user.getValue("rowsPerPage").toString());
    }

    @Override
    public ListStructure getListStructure() throws Exception {
        return processAccessRightDataLevelSecurity(super.getListStructure());
    }

    private ListStructure processAccessRightDataLevelSecurity(ListStructure sourceListStructure) {
        return AccessRightDataLevelSecurity.i.processAddressAccessRightByList(sourceListStructure, userId);
    }

    protected void saveErrors(HttpSession session, ActionErrors errors) {
        if (errors == null || errors.isEmpty()) {
            session.removeAttribute(Globals.ERROR_KEY);
            return;
        } else {
            session.setAttribute(Globals.ERROR_KEY, errors);
            return;
        }
    }

    protected void addContactSearchNameFixedFilter(SearchForm listForm) {
        Functions.addContactSearchNameFixedFilter("contactSearchName", listForm, this);
        Functions.addContactSearchNameFixedFilter("contactSearchName@_contactPersonOfSearchName", listForm, this);
    }

    //todo: test method, should be deleted when ELWIS-2772 is solved
    private void testParametersExecuteLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request, Map previousParams) {
        SearchForm searchForm = (SearchForm) form;
        if ((searchForm.getParams() == null || searchForm.getParams().isEmpty())
                && (getFilter() == null || getFilter().isEmpty())
                && (getStaticFilter() == null || getStaticFilter().isEmpty())) {
            log.info("FANTABULOUS WITHOUT SEARCH PARAMS AFTER EXECUTE.........................." + mapping.getParameter());
            log.info("PREVIOUS PARAMS BEFORE EXECUTE LIST .........................." + previousParams);
            log.info("REQUEST......" + request.getParameterMap());
            //log.info("FILTERS......." + getFilter());
            //log.info("STATIC FILTERS......." + getStaticFilter());
        }
    }

    //todo: test method, should be deleted when ELWIS-2772 is solved
    private Map previousTestListParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request) {
        SearchForm searchForm = (SearchForm) form;

        Map map = new HashMap();
        map.put("SearchParameters", searchForm.getParams());
        map.put("filter", getFilter());
        map.put("staticFilter", getStaticFilter());
        return map;
    }

}
