package com.piramide.elwis.web.schedulermanager.action.report;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SchedulerConstants;
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
 * Date: Oct 7, 2005
 * Time: 2:54:32 PM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentReportListAction extends ReportAction {

    private Log log = LogFactory.getLog(AppointmentReportListAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("    ...  AppointmentReportListAction  execute    ...    ");
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
                addFilter("userId", user.getValue("userId").toString());
                request.setAttribute("timeZone", timeZone);
                evaluate("startRange", searchForm.getParams(), timeZone, 00, 00);
                evaluate("endRange", searchForm.getParams(), timeZone, 23, 59);
                evaluate("startFinishRange", searchForm.getParams(), timeZone, 00, 00);
                evaluate("endFinishRange", searchForm.getParams(), timeZone, 23, 59);
            } else {
                return forward;
            }
            /*recurrence*/
            if ("1".equals(request.getParameter("parameter(isRecurrence)"))) {
                addFilter("withRecur", SchedulerConstants.TRUE_VALUE);
            } else if ("2".equals(request.getParameter("parameter(isRecurrence)"))) {
                addFilter("withoutRecur", SchedulerConstants.TRUE_VALUE);
            }
            /*reminder*/
            if ("1".equals(request.getParameter("parameter(isReminder)"))) {
                addFilter("withReminder", SchedulerConstants.TRUE_VALUE);
            } else if ("2".equals(request.getParameter("parameter(isReminder)"))) {
                addFilter("withoutReminder", SchedulerConstants.TRUE_VALUE);
            }
            /*is AllDAY*/
            if ("on".equals(request.getParameter("parameter(allDay)"))) {
                addFilter("isAllDay", "1");
            }
            return super.execute(mapping, searchForm, request, response);
        } else {
            return forward;
        }
        /*return super.execute(mapping, searchForm, request, response);*/
    }

    private void evaluate(String fieldName, Map params, DateTimeZone timeZone, int intIni, int intFin) {
        String field = (String) params.get(fieldName);
        if (field != null && field.trim().length() > 0) {
            addFilter(fieldName + "Millis", Long.toString(DateUtils.integerToDateTime(new Integer(field), intIni, intFin, timeZone).getMillis()));
            params.put(fieldName, params.remove(fieldName + "_clone"));
        }
    }
}