package com.piramide.elwis.web.financemanager.action.report;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SalesConstants;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Jatun S.R.L.
 * Initialize default values in form
 *
 * @author Miky
 * @version $Id: ContractInvoiceReportForwardAction.java 18-dic-2008 18:32:21 $
 */
public class ContractInvoiceReportForwardAction extends ForwardAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        SearchForm searchForm = (SearchForm) form;
        searchForm.setParameter("fromDate", getFirstDayOfMonth());
        searchForm.setParameter("toDate", getLastDayOfYear());

        searchForm.setParameter("contractTypeParam", SalesConstants.CONTRACTS_TO_BE_INVOICED);

        return super.execute(mapping, searchForm, request, response);
    }

    private Integer getLastDayOfYear() {
        DateTime dateTime = new DateTime(new Date());
        return DateUtils.dateToInteger(dateTime.monthOfYear().withMaximumValue().dayOfMonth().withMaximumValue());
    }

    private Integer getFirstDayOfMonth() {
        DateTime dateTime = new DateTime(new Date());
        return DateUtils.dateToInteger(dateTime.dayOfMonth().withMinimumValue());
    }
}
