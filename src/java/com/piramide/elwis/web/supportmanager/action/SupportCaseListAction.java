package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.alfacentauro.fantabulous.controller.OrderParam;
import org.alfacentauro.fantabulous.persistence.PersistenceManager;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fernando Montaño
 * @version SupportCaseListAction.java, v 2.0 Aug 24, 2004 5:09:51 PM
 */

public class SupportCaseListAction extends ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Support case list action execution...");
        String caseId = "";
        User user = RequestUtils.getUser(request);

        if (request.getParameter("caseId") != null && !"".equals(request.getParameter("caseId"))) {
            caseId = request.getParameter("caseId");
        } else {
            caseId = request.getParameter("dto(caseId)");
        }

        if (!validateSupportCase(caseId, request)) {
            return mapping.findForward("CaseMainSearch");
        }

        addFilter("caseId", caseId);
        setModuleId("case", caseId);

        if ("1".equals(request.getParameter("n"))) {
            List<OrderParam> orders = new ArrayList<OrderParam>();
            Map map1 = new HashMap();
            Map mapParameter = new HashMap();
            orders.add(new OrderParam("closeDateActivityId", false, 1));
            map1.put("pageNumber", "0");
            PersistenceManager.persistence().saveStatus(user.getValue("userId").toString(), "caseActivityList", "support", mapParameter, orders, map1);
        }
        return super.execute(mapping, form, request, response);
    }

    private boolean validateSupportCase(String caseId, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        errors = ForeignkeyValidator.i.validate(SupportConstants.TABLE_SUPPORT_CASE, "caseId",
                caseId, errors, new ActionError("msg.NotFound", request.getParameter("dto(caseTitle)")));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return errors.isEmpty();
    }
}
