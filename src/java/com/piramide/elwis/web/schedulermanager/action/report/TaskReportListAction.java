package com.piramide.elwis.web.schedulermanager.action.report;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * User: yumi
 * Date: Oct 7, 2005
 * Time: 2:54:53 PM
 * To change this template use File | Settings | File Templates.
 */

public class TaskReportListAction extends ReportAction {

    private Log log = LogFactory.getLog(TaskReportListAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("    ...  TaskReportListAction  execute    ...    ");
        super.initializeFilter();

        SearchForm searchForm = (SearchForm) form;
        User user = RequestUtils.getUser(request);
        DateTimeZone timeZone;
        ActionForward forward = mapping.findForward("Success");
        if (user != null) {
            timeZone = (DateTimeZone) user.getValue("dateTimeZone");
        } else {
            timeZone = DateTimeZone.getDefault();
        }

        if (request.getParameter("parameter(generate)") != null) {

            ActionErrors errors = new ActionErrors();
            errors = searchForm.validate(mapping, request);
            saveErrors(request, errors);

            if (errors.isEmpty()) {

                if (searchForm.getParameter("percent2") != null && !"".equals(request.getParameter("parameter(percent2"))
                        && !"".equals(searchForm.getParameter("percent2")) && request.getParameter("parameter(percent2)") != null) {
                    if ((new Integer(searchForm.getParameter("percent2").toString())).intValue() > 100) {
                        errors.add("percent2", new ActionError("errors.percent_range",
                                JSPHelper.getMessage(request, "Task.percent")));
                        saveErrors(request, errors);
                        return mapping.findForward("Success");
                    }
                }

                addFilter("userId", user.getValue("userId").toString());
                request.setAttribute("timeZone", timeZone);
                evaluate("startRange", searchForm.getParams(), timeZone, 00, 00);
                evaluate("endRange", searchForm.getParams(), timeZone, 23, 59);
                evaluate("startFinishRange", searchForm.getParams(), timeZone, 00, 00);
                evaluate("endFinishRange", searchForm.getParams(), timeZone, 23, 59);
            } else {
                return forward;
            }
            /*  notification    */
            if ("1".equals(request.getParameter("parameter(notification)"))) {
                addFilter("withNotification", SchedulerConstants.TRUE_VALUE);
            } else if ("2".equals(request.getParameter("parameter(notification)"))) {
                addFilter("withoutNotification", SchedulerConstants.TRUE_VALUE);
            }
            return super.execute(mapping, searchForm, request, response);

        } else {
            return forward;
        }
    }

    private void evaluate(String fieldName, Map params, DateTimeZone timeZone, int i, int f) {
        String field = (String) params.get(fieldName);
        if (field != null && field.trim().length() > 0) {
            addFilter(fieldName + "Millis", Long.toString(DateUtils.integerToDateTime(new Integer(field), i, f, timeZone).getMillis()));
            params.put(fieldName, params.remove(fieldName + "_clone"));
        }
    }
}