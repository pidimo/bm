package com.piramide.elwis.web.supportmanager.action.report;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * Time: 6:22:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuestionReportListAction extends ReportAction {

    private Log log = LogFactory.getLog(QuestionReportListAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("    ...  questionReportListAction  execute    ...    ");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        User user = RequestUtils.getUser(request);
        DateTimeZone timeZone;
        if (user != null) {
            timeZone = (DateTimeZone) user.getValue("dateTimeZone");
        } else {
            timeZone = DateTimeZone.getDefault();
        }

        if ("0".equals(request.getParameter("parameter(published)"))) {
            addFilter("isPublished", SupportConstants.TRUE_VALUE);
        } else if ("1".equals(request.getParameter("parameter(published)"))) {
            addFilter("not_published", SupportConstants.TRUE_VALUE);
            addFilter("user_createId", user.getValue("userId").toString());
        } else {
            addFilter("published_null", SupportConstants.TRUE_VALUE);
            addFilter("user_createId", user.getValue("userId").toString());
        }

        request.setAttribute("timeZone", timeZone);
        evaluate("startRange", searchForm.getParams(), timeZone, 00, 00);
        evaluate("endRange", searchForm.getParams(), timeZone, 23, 59);
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