package com.piramide.elwis.web.contactmanager.action.report;

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

/**
 * User: yumi
 * Date: Oct 27, 2005
 * Time: 2:56:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class EmployeeReportAction extends ReportAction {
    private Log log = LogFactory.getLog(EmployeeReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("--- EmployeeReportAction      execute  ....");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        ActionErrors errors = new ActionErrors();
        String cost1, cost2, costHour1, costHour2, hourlyRate1, hourlyRate2 = "";


        if (request.getParameter("parameter(generate)") != null) {
            errors = searchForm.validate(mapping, request); // validate
        }
        saveErrors(request, errors);
        searchForm.getParams().put("country_Id", request.getParameter("parameter(countryId)"));

        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        }


        if (request.getParameter("parameter(cost1)") != null) {
            cost1 = searchForm.getParameter("cost1").toString();
            searchForm.getParams().put("cost1", null);
            searchForm.setParameter("cost1", cost1);
        }
        if (request.getParameter("parameter(cost2)") != null) {
            cost2 = searchForm.getParameter("cost2").toString();
            searchForm.getParams().put("cost2", null);
            searchForm.setParameter("cost2", cost2);
        }
        if (request.getParameter("parameter(costHour1)") != null) {
            costHour1 = searchForm.getParameter("costHour1").toString();
            searchForm.getParams().put("costHour1", null);
            searchForm.getParams().put("costHour1", costHour1);
        }
        if (request.getParameter("parameter(costHour2)") != null) {
            costHour2 = searchForm.getParameter("costHour2").toString();
            searchForm.getParams().put("costHour2", null);
            searchForm.getParams().put("costHour2", costHour2);
        }
        if (request.getParameter("parameter(hourlyRate1)") != null) {
            hourlyRate1 = searchForm.getParameter("hourlyRate1").toString();
            searchForm.getParams().put("hourlyRate1", null);
            searchForm.getParams().put("hourlyRate1", hourlyRate1);
        }
        if (request.getParameter("parameter(hourlyRate2)") != null) {
            hourlyRate2 = searchForm.getParameter("hourlyRate2").toString();
            searchForm.getParams().put("hourlyRate2", null);
            searchForm.getParams().put("hourlyRate2", hourlyRate2);
        }

        if (request.getParameter("parameter(generate)") != null) {
            return super.execute(mapping, searchForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}

