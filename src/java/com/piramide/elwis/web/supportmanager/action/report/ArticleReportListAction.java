package com.piramide.elwis.web.supportmanager.action.report;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * User: yumi
 * Date: Oct 5, 2005
 * Time: 5:59:36 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleReportListAction extends ReportAction {
    private Log log = LogFactory.getLog(ArticleReportListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("--- ArticleReportListAction      execute  ....");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        User user = RequestUtils.getUser(request);
        ActionErrors errors = new ActionErrors();
        errors = searchForm.validate(mapping, request);
        saveErrors(request, errors);
        String view1, view2 = "";
        DateTimeZone timeZone;

        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        }

        if (user != null) {
            timeZone = (DateTimeZone) user.getValue("dateTimeZone");
        } else {
            timeZone = DateTimeZone.getDefault();
        }


        request.setAttribute("timeZone", timeZone);
        evaluate("startRange", searchForm.getParams(), timeZone, 00, 00);
        evaluate("endRange", searchForm.getParams(), timeZone, 23, 59);
        evaluate("startChangeRange", searchForm.getParams(), timeZone, 00, 00);
        evaluate("endChangeRange", searchForm.getParams(), timeZone, 23, 59);
        evaluate("startVisitRange", searchForm.getParams(), timeZone, 00, 00);
        evaluate("endVisitRange", searchForm.getParams(), timeZone, 23, 59);

        if (request.getParameter("parameter(view1)") != null) {
            view1 = searchForm.getParameter("view1").toString();
            searchForm.getParams().put("view1", null);
            searchForm.setParameter("view1", view1);
        }
        if (request.getParameter("parameter(view2)") != null) {
            view2 = searchForm.getParameter("view2").toString();
            searchForm.getParams().put("view2", null);
            searchForm.setParameter("view2", view2);
        }

        return super.execute(mapping, searchForm, request, response);
    }

    private void evaluate(String fieldName, Map params, DateTimeZone timeZone, int i, int f) {
        String field = (String) params.get(fieldName);
        if (field != null && field.trim().length() > 0) {
            addFilter(fieldName + "Millis", Long.toString(DateUtils.integerToDateTime(new Integer(field), i, f, timeZone).getMillis()));
            params.put(fieldName, params.remove(fieldName + "_clone"));
        }
    }
}
