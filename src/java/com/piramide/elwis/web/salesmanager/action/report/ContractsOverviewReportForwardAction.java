package com.piramide.elwis.web.salesmanager.action.report;

import com.piramide.elwis.utils.SalesConstants;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * Initialize default values in form
 *
 * @author Miky
 * @version $Id: ContractsOverviewReportForwardAction.java 04-mar-2009 16:05:11 $
 */
public class ContractsOverviewReportForwardAction extends ForwardAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        SearchForm searchForm = (SearchForm) form;
        searchForm.setParameter("contractTypeParam", SalesConstants.CONTRACTS_TO_BE_INVOICED);

        return super.execute(mapping, searchForm, request, response);
    }
}
