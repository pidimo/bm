package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.utils.Constants;
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
import java.util.Iterator;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.4
 */
public class IncomingInvoicePrintListAction extends ListAction {
    protected static Log log = LogFactory.getLog(IncomingInvoicePrintListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing IncomingInvoicePrintListAction........" + request.getParameterMap());

        //parameters to execute list of manual form to print all
        SearchForm searchForm = (SearchForm) form;
        Parameters parameters = new Parameters();
        parameters.addSearchParameters(getParameters(searchForm.getParams()));
        //add companyId
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        parameters.addSearchParameter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());

        ActionForward forward = super.execute(mapping, form, request, response);

        //read ids to all print action
        String listName = "incomingInvoicePrintList";
        executeListToReadAllDocumentId(request, listName, parameters, form);
        return forward;

    }

    private void executeListToReadAllDocumentId(HttpServletRequest request, String listName, Parameters parameters, ActionForm form) {
        log.debug("Read document IDS........");

        String ids = "";
        //Execute again the list to read all documents id, this to print all
        FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(getServlet().getServletContext(), request);

        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
            //because this is applied to all result list
            list = AccessRightDataLevelSecurity.i.processAddressAccessRightByList(list, request);
        } catch (ListStructureNotFoundException e) {
            log.debug("->Read List " + listName + " In Fantabulous structure Fail");
        }

        if (list != null) {
            Collection result = Controller.getList(list, parameters);
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) iterator.next();
                ids += fieldHash.get("DOCUMENTID").toString();
                if (iterator.hasNext()) {
                    ids += ",";
                }
            }
        }

        request.setAttribute("allPrintIds", ids);
    }

}
