package com.piramide.elwis.web.supportmanager.action.report;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.report.ReportAction;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * User: yumi
 * Date: Oct 5, 2005
 * Time: 6:22:25 PM
 * To change this template use File | Settings | File Templates.
 */

public class CaseReportListAction extends ReportAction {
    private Log log = LogFactory.getLog(CaseReportListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("...  CaseReportListAction  EXECUTE  ...");
        initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        ActionErrors errors = searchForm.validate(mapping, request);
        saveErrors(request, errors);
        String totalHours1, totalHours2 = "";

        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        }

        if (request.getParameter("parameter(totalHours1)") != null) {
            totalHours1 = request.getParameter("parameter(totalHours1)");
            searchForm.getParams().put("totalHours1", null);
            searchForm.setParameter("totalHours1", totalHours1);
        }

        if (request.getParameter("parameter(totalHours2)") != null) {
            totalHours2 = request.getParameter("parameter(totalHours2)");
            searchForm.getParams().put("totalHours2", null);
            searchForm.setParameter("totalHours2", totalHours2);
        }

        String userId = user.getValue("userId").toString();
        Map params = searchForm.getParams();
        String toUserId = (String) params.remove("toUserId");
        String openByUserId = (String) params.remove("openByUserId");
        boolean hasToUserId = toUserId != null && toUserId.length() > 0;
        boolean hasOpenByUserId = openByUserId != null && openByUserId.length() > 0;
        boolean userIdEqualToUserId = userId.equals(toUserId);
        boolean userIdEqualOpenByUserId = userId.equals(openByUserId);

        log.debug("hasToUserId:" + hasToUserId + " - userId = toUser:" + userIdEqualToUserId + " - hasOpenBiId:" + hasOpenByUserId + " - uI==OBOU:" + userIdEqualOpenByUserId);

        String condition = "onlyUser";
        searchForm.setParameter("userId", userId);

        if (!hasToUserId && !hasOpenByUserId) {
            condition = "bothUser";
            searchForm.setParameter("toUserId", userId);
            searchForm.setParameter("openByUserId", userId);
        } else if (!userIdEqualToUserId && !userIdEqualOpenByUserId && hasToUserId && hasOpenByUserId) {
            condition = "bothOtherUser";
            searchForm.setParameter("toUserId", toUserId);
            searchForm.setParameter("openByUserId", openByUserId);
        } else if (hasToUserId && !userIdEqualToUserId && !hasOpenByUserId) {
            condition = "otherToUser";
            searchForm.setParameter("toUserId", toUserId);
        } else if (hasToUserId && !userIdEqualToUserId) {
            condition = "otherToUser_";
            searchForm.setParameter("toUserId", toUserId);
        } else if (hasOpenByUserId && !userIdEqualOpenByUserId && !hasToUserId) {
            condition = "otherOpenByUser";
            searchForm.setParameter("openByUserId", openByUserId);
        } else if (hasOpenByUserId && !userIdEqualOpenByUserId) {
            condition = "otherOpenByUser_";
            searchForm.setParameter("openByUserId", openByUserId);
        } else if (userIdEqualOpenByUserId && userIdEqualToUserId) {
            condition = "userIdEqualUserId";
        } else if (userIdEqualToUserId) {
            condition = "userIdEqualToUserId";

        } else if (userIdEqualOpenByUserId) {
            condition = "userIdEqualOpenByUserId";

        }

        log.debug(" ... option ... " + condition);
        searchForm.setParameter(condition, "true");
        return super.execute(mapping, searchForm, request, response);
    }
}
