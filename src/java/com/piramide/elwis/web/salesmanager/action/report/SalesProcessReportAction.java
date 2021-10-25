package com.piramide.elwis.web.salesmanager.action.report;

import com.jatun.titus.web.form.ReportGeneratorForm;
import com.jatun.titus.web.util.SortGroupColumn;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: yumi
 * Date: Oct 20, 2005
 * Time: 10:15:17 AM
 * To change this template use File | Settings | File Templates.
 */

public class SalesProcessReportAction extends ReportAction {
    private Log log = LogFactory.getLog(SalesProcessReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--- SalesProcessReportAction      execute  ....");

        super.initializeFilter();
        ReportGeneratorForm reportGeneratorForm = (ReportGeneratorForm) form;
        ActionErrors errors = new ActionErrors();
        String value1, value2, probality1, probality2 = "";
        boolean rangeError = false;

        errors = reportGeneratorForm.validate(mapping, request);
        saveErrors(request, errors);

        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        } else {
            if (reportGeneratorForm.getParameter("probability2") != null && !"".equals(request.getParameter("parameter(probability2"))
                    && !"".equals(reportGeneratorForm.getParameter("probability2")) && request.getParameter("parameter(probability2)") != null) {
                if ((new Integer(reportGeneratorForm.getParameter("probability2").toString())).intValue() > 100) {
                    rangeError = true;
                }
            }
            if (reportGeneratorForm.getParameter("probability1") != null && !"".equals(request.getParameter("parameter(probability1"))
                    && !"".equals(reportGeneratorForm.getParameter("probability1")) && request.getParameter("parameter(probability1)") != null) {
                if ((new Integer(reportGeneratorForm.getParameter("probability1").toString())).intValue() > 100) {
                    rangeError = true;
                }
            }

            if (rangeError) {
                errors.add("discount2", new ActionError("errors.percent_range",
                        JSPHelper.getMessage(request, "SalesProcess.probability")));
                saveErrors(request, errors);
                return mapping.findForward("Success");
            }

            if (request.getParameter("parameter(value1)") != null) {
                value1 = reportGeneratorForm.getParameter("value1").toString();
                reportGeneratorForm.getParams().put("value1", null);
                reportGeneratorForm.setParameter("value1", value1);
            }
            if (request.getParameter("parameter(value2)") != null) {
                value2 = reportGeneratorForm.getParameter("value2").toString();
                reportGeneratorForm.getParams().put("value2", null);
                reportGeneratorForm.setParameter("value2", value2);
            }
            if (request.getParameter("parameter(probability1)") != null) {
                probality1 = reportGeneratorForm.getParameter("probability1").toString();
                reportGeneratorForm.getParams().put("probability1", null);
                reportGeneratorForm.setParameter("probability1", probality1);
            }
            if (request.getParameter("parameter(probability2)") != null) {
                probality2 = reportGeneratorForm.getParameter("probability2").toString();
                reportGeneratorForm.getParams().put("probability2", null);
                reportGeneratorForm.setParameter("probability2", probality2);
            }
        }
        if (request.getParameter("parameter(generate)") != null) {
            //Remove endDateCopy if was not selected as grouping column
            if (reportGeneratorForm.getReportSGParamMap().size() > 0) {
                boolean exists = false;
                for (Iterator<Map.Entry<Integer, SortGroupColumn>> iterator = reportGeneratorForm.getReportSGParamMap().entrySet().iterator(); iterator.hasNext() && !exists;) {
                    Map.Entry<Integer, SortGroupColumn> integerSortGroupColumnEntry = iterator.next();
                    exists = integerSortGroupColumnEntry.getValue().getColumnName().equals("endDate") &&
                            integerSortGroupColumnEntry.getValue().getGroupingColumn();
                }
                if (!exists) {
                    int i = 0;
                    ArrayList reportFieldParams = reportGeneratorForm.getReportFieldParams();
                    while (i < reportFieldParams.size()) {
                        HashMap reportField = (HashMap) reportFieldParams.get(i);
                        if (reportField.get("name").equals("endDateCopy")) {
                            reportFieldParams.remove(reportField);
                            i = reportFieldParams.size() + 5;
                        }
                        i++;
                    }
                }
            }
            return super.execute(mapping, reportGeneratorForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}