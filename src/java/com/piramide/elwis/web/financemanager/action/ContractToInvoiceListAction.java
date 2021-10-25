package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
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
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 * Action to manage contract to invoice list,
 * this also recovery all contracts ids to option 'invoice for all contracts'
 *
 * @author Miky
 * @version $Id: ContractToInvoiceListAction.java 19-nov-2008 13:43:18 $
 */
public class ContractToInvoiceListAction extends ListAction {
    protected static Log log = LogFactory.getLog(ContractToInvoiceListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContractToInvoiceListAction........" + request.getParameterMap());
        SearchForm listForm = (SearchForm) form;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        //add current date as filter if is necessary
        if (GenericValidator.isBlankOrNull((String) listForm.getParameter("currentDate"))) {
            listForm.setParameter("currentDate", DateUtils.dateToInteger(new DateTime((DateTimeZone) user.getValue("dateTimeZone"))).toString());
        }
        //add list date filter in request
        request.setAttribute("listDateFilter", listForm.getParameter("currentDate"));

        //parameters to execute list of manual form
        Parameters parameters = new Parameters();
        parameters.addSearchParameters(getParameters(listForm.getParams()));
        //add companyId
        parameters.addSearchParameter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());


        ActionForward forward = super.execute(mapping, listForm, request, response);

        //read ids to create for all action process
        String listName = "contractToInvoiceList";
        executeListToReadAllContractIds(request, listName, parameters);
        return forward;
    }

    private void executeListToReadAllContractIds(HttpServletRequest request, String listName, Parameters parameters) {
        log.debug("Read Contract IDS........");
        String ids = "";
        //Execute again the list to read all ids
        FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(getServlet().getServletContext(), "/sales");

        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
            //add access right security because this is for invoice all list result
            list = AccessRightDataLevelSecurity.i.processAddressAccessRightByList(list, request);
        } catch (ListStructureNotFoundException e) {
            log.debug("->Read List " + listName + " In Fantabulous structure Fail");
        }

        if (list != null) {
            Collection result = Controller.getList(list, parameters);
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) iterator.next();
                //get column defined in fantabulous list
                ids += fieldHash.get("contractInvoiceId").toString();
                if (iterator.hasNext()) {
                    ids += ",";
                }
            }
        }
        request.setAttribute("allListComposeIds", ids);
    }
}
