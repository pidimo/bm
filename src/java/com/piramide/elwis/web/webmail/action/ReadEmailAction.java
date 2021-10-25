package com.piramide.elwis.web.webmail.action;

import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.piramide.elwis.service.exception.webmail.EmailNotFoundException;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.PageSearchUtil;
import com.piramide.elwis.utils.PagingPair;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.delegate.EmailServiceDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.OrderParam;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.persistence.PersistenceConstants;
import org.alfacentauro.fantabulous.persistence.PersistenceManager;
import org.alfacentauro.fantabulous.result.FieldHash;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.alfacentauro.fantabulous.web.FantabulousUtil;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ReadEmailAction extends WebmailDefaultAction {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        boolean createCommunications = Functions.hasAccessRight(request, "COMMUNICATION", "CREATE");
        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("createCommunications", createCommunications);

        Integer mailId = new Integer(defaultForm.getDto("mailId").toString());
        Integer userMailId = (Integer) RequestUtils.getUser(request).getValue(Constants.USERID);

        try {
            EmailServiceDelegate.i.downloadEmailSource(mailId, userMailId);
        } catch (EmailNotFoundException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("emailNotFound", new ActionError("Webmail.emailDeletedFromServer.error"));
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        } catch (ConnectionException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("cannotConnectPOP", new ActionError("Webmail.cannotConnectPop"));
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        addPaginationParameters(request, (DefaultForm) form);
        ActionForward forward = super.execute(mapping, defaultForm, request, response);
        if ("Fail".equals(forward.getName())) {
            return forward;
        }


        return forward;
    }

    private void addPaginationParameters(HttpServletRequest request, DefaultForm defaultForm) {
        String listName = "mailTrayList";
        if (WebMailNavigationUtil.isAvancedSearch(request)) {
            listName = "advancedSearchMailList";
        } else if (WebMailNavigationUtil.isSearch(request)) {
            listName = "searchMailList";
        }
        String mailId = String.valueOf(defaultForm.getDto("mailId"));
        String mailIndex = request.getParameter("mailIndex");
        if (GenericValidator.isBlankOrNull(mailId) ||
                GenericValidator.isBlankOrNull(mailIndex)) {
            return;
        }

        PageSearchUtil pageSearchUtil = new PageSearchUtil();
        Integer pageSize = pageSearchUtil.getGroupSize(Integer.valueOf(mailIndex));
        Integer pageNumber = pageSearchUtil.getGroup(pageSize, Integer.valueOf(mailIndex));

        FantabulousManager fantaManager =
                FantabulousManager.loadFantabulousManager(request.getSession().getServletContext(), request);

        ListStructure fantabulousList;
        try {
            fantabulousList = fantaManager.getList(listName);
        } catch (ListStructureNotFoundException e) {
            log.debug(listName + " List structure not found ", e);
            return;
        }
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);

        Map parametersLoaded = PersistenceManager.
                persistence().loadStatus(userId.toString(), fantabulousList.getListName(), "webmail");
        parametersLoaded.put("userMailId", userId.toString());
        Parameters parameters = new Parameters();
        if (parametersLoaded.size() > 0) {
            // Extract order parameter
            Map<Integer, OrderParam> orderParameters = new TreeMap<Integer, OrderParam>();
            if (parametersLoaded.containsKey(PersistenceConstants.PARAM_ORDER)) {
                orderParameters = FantabulousUtil.getInstance().getOrderParams((String) parametersLoaded.remove(PersistenceConstants.PARAM_ORDER));
            }

            // Execute List with search params and filters..
            parameters.getPageParam().setPageSize(pageSize);
            // Set page for list
            parameters.getPageParam().setPageNumber(pageNumber);
            // Order parameters
            if (!orderParameters.isEmpty()) {
                parameters.setOrderParameters(new ArrayList(orderParameters.values()));
            }
            // Set search parameters...
            parameters.addSearchParameters(FantabulousUtil.getInstance().getParameters(parametersLoaded));
        }
        Collection listFiedls = Controller.getPage(fantabulousList, fantabulousList.getListName(), parameters);
        PagingPair<Integer> pair = findPagingParams(new ArrayList(listFiedls), Integer.parseInt(mailId), pageNumber, pageSize);
        if (pair.getPrevious() != null) {
            request.getParameterMap().put("previousMailId", pair.getPrevious());
        }
        if (pair.getNext() != null) {
            request.getParameterMap().put("nextMailId", pair.getNext());
        }
        if (pair.getIndex() != Integer.MIN_VALUE) {
            request.getParameterMap().put("previousMailIndex", pair.getIndex() - 1);
            request.getParameterMap().put("nextMailIndex", pair.getIndex() + 1);
        }
    }


    private PagingPair<Integer> findPagingParams(List fields, int currentMailId, int pageNumber, int pageSize) {
        PagingPair<Integer> res = new PagingPair<Integer>();
        res.setIndex(Integer.MIN_VALUE);
        Integer previous = Integer.MIN_VALUE;
        int i = 0;
        while (i < fields.size()) {
            FieldHash fs = (FieldHash) fields.get(i);
            if (Integer.parseInt(fs.get("MAILID").toString()) == currentMailId) {
                if (previous > Integer.MIN_VALUE) {
                    res.setPrevious(previous);
                }
                if (i < fields.size() - 1) {
                    res.setNext(new Integer(String.valueOf(((FieldHash) fields.get(i + 1)).get("MAILID"))));
                }
                res.setIndex(((pageNumber - 1) * pageSize) + i);
                i = fields.size();
            } else {
                previous = new Integer(String.valueOf(fs.get("MAILID")));
            }
            i++;
        }
        return (res);
    }
}
