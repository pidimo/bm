package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.struts.action.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 * Action to forward form to create invoices based in sale positions and contracts related to sale
 *
 * @author Miky
 * @version $Id: ContractInvoiceFromSaleAction.java 19-nov-2008 13:43:18 $
 */
public class ContractInvoiceFromSaleAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContractInvoiceFromSaleAction........" + request.getParameterMap());

        ActionForward forward;
        //concurrence validation
        if (null != (forward = validateElementExistence(request, mapping))) {
            return forward;
        }

        String saleId = request.getParameter("saleId");
        ActionErrors errors = processContractsAndSalePositionsToInvoice(request, saleId);
        if (errors.isEmpty()) {
            DefaultForm defaultForm = (DefaultForm) form;
            Functions.setDefaultOptionsForInvoice(defaultForm, request);
        } else {
            saveErrors(request.getSession(), errors);
            return composeFailForward(request, mapping);
        }
        return mapping.findForward("Success");
    }

    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsSale(request.getParameter("saleId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("SaleNotFound", new ActionError("Sale.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainSaleList");
        }

        return null;
    }

    protected ActionForward composeFailForward(HttpServletRequest request, ActionMapping mapping) {
        String saleId = request.getParameter("saleId");

        ActionForwardParameters failParameters = new ActionForwardParameters();
        failParameters.add("dto(saleId)", saleId);
        return failParameters.forward(mapping.findForward("Fail"));
    }

    private ActionErrors processContractsAndSalePositionsToInvoice(HttpServletRequest request, String saleId) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        String companyId = user.getValue(Constants.COMPANYID).toString();

        FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(getServlet().getServletContext(), "/sales");

        boolean hasContractToInvoice = executeListContractToInvoiceFromSale(request, saleId, companyId, fantabulousManager);
        boolean hasSalePositionToInvoice = executeListSalePositionToInvoice(request, saleId, companyId, fantabulousManager);

        //validate result list
        ActionErrors errors = new ActionErrors();
        if (!hasContractToInvoice && !hasSalePositionToInvoice) {
            errors.add("empty", new ActionError("Sale.toInvoice.emptyError"));
        }
        return errors;
    }

    /**
     * Execute list and set in request contract ids list to be invoiced
     *
     * @param request
     * @param saleId
     * @param companyId
     * @param fantabulousManager
     * @return
     */
    private boolean executeListContractToInvoiceFromSale(HttpServletRequest request, String saleId, String companyId, FantabulousManager fantabulousManager) {
        log.debug("Read Contract IDS........");
        String listName = "contractToInvoiceSaleList";

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        String currentDateFilter = DateUtils.dateToInteger(new DateTime((DateTimeZone) user.getValue("dateTimeZone"))).toString();

        //parameters to execute list of manual form
        Parameters parameters = new Parameters();
        parameters.addSearchParameter(Constants.COMPANYID, companyId);
        parameters.addSearchParameter("currentDate", currentDateFilter);
        parameters.addSearchParameter("saleId", saleId);

        //Execute the list to read all ids related to sale
        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
        } catch (ListStructureNotFoundException e) {
            log.debug("->Read List " + listName + " In Fantabulous structure Fail");
        }

        List idsList = new ArrayList();
        if (list != null) {
            Collection result = Controller.getList(list, parameters);
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) iterator.next();
                //get column defined in fantabulous list
                idsList.add(fieldHash.get("contractInvoiceId"));
            }
        }
        log.debug("Contracts related to sale...........:" + idsList);

        request.setAttribute("contractIdsList", idsList);
        request.setAttribute("dateFilterList", currentDateFilter);

        return (!idsList.isEmpty());
    }

    /**
     * Execute list an set sale position ids list to be invoiced in request
     *
     * @param request
     * @param saleId
     * @param companyId
     * @param fantabulousManager
     * @return
     */
    private boolean executeListSalePositionToInvoice(HttpServletRequest request, String saleId, String companyId, FantabulousManager fantabulousManager) {

        log.debug("Read Sale position IDS........");
        String listName = "salePositionToInvoiceList";

        //parameters to execute list of manual form
        Parameters parameters = new Parameters();
        parameters.addSearchParameter(Constants.COMPANYID, companyId);
        parameters.addSearchParameter("saleId", saleId);

        //Execute the list to read all ids related to sale
        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
        } catch (ListStructureNotFoundException e) {
            log.debug("->Read List " + listName + " In Fantabulous structure Fail");
        }

        List idsList = new ArrayList();
        if (list != null) {
            Collection result = Controller.getList(list, parameters);
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) iterator.next();
                //get column defined in fantabulous list
                idsList.add(fieldHash.get("salePositionInvoiceId"));
            }
        }
        log.debug("Sale positions to invoice related to sale...........:" + idsList);

        request.setAttribute("salePositionIdsList", idsList);

        return (!idsList.isEmpty());
    }

}