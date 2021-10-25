package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.common.action.ListAction;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: InvoiceToRemindListAction.java 31-oct-2008 14:42:28 $
 */
public class InvoiceToRemindListAction extends ListAction {
    protected static Log log = LogFactory.getLog(InvoiceToRemindListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing InvoiceToRemindListAction........" + request.getParameterMap());
        SearchForm listForm = (SearchForm) form;

        //add current date as filter
        listForm.setParameter("currentDate", DateUtils.dateToInteger(new Date()).toString());

        //add reminder level filter
        Object reminderLevel = listForm.getParameter("toRemindLevel");
        if (reminderLevel != null && SalesConstants.INVOICE_NOT_REMINDERED_YET.equals(reminderLevel)) {
            listForm.setParameter("isNotRemindered", "true");
        } else {
            listForm.setParameter("isNotRemindered", ""); //remove
            listForm.setParameter("reminderLevel", reminderLevel);
        }

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        //parameters to execute list of manual form to create reminder for all invoices
        Parameters parameters = new Parameters();
        parameters.addSearchParameters(getParameters(listForm.getParams()));
        //add companyId
        parameters.addSearchParameter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());
        parameters.addSearchParameter("isAllCreate", "true");


        ActionForward forward = super.execute(mapping, listForm, request, response);

        //read ids to create for all action
        String listName = "invoiceToRemindList";
        executeListToReadAllInvoiceId(request, listName, parameters, form);
        return forward;
    }

    private void executeListToReadAllInvoiceId(HttpServletRequest request, String listName, Parameters parameters, ActionForm form) {
        log.debug("Read Invoice IDS........");
        String ids = "";
        //Execute again the list to read all invoice id
        FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(getServlet().getServletContext(), request);

        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
            //because this is for remind to all result list
            list = AccessRightDataLevelSecurity.i.processAddressAccessRightByList(list, request);
        } catch (ListStructureNotFoundException e) {
            log.debug("->Read List " + listName + " In Fantabulous structure Fail");
        }

        if (list != null) {
            Collection result = Controller.getList(list, parameters);
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) iterator.next();
                ids += fieldHash.get("invoiceId").toString();
                if (iterator.hasNext()) {
                    ids += ",";
                }
            }
        }
        request.setAttribute("allListInvoiceIds", ids);
    }


}
