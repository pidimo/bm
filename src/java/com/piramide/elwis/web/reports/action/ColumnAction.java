package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.reports.el.Functions;
import com.piramide.elwis.web.reports.form.ColumnForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Jatun s.r.l.
 *
 * @author miky
 * @version $Id: ColumnAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ColumnAction extends ReportManagerAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ColumnAction................");

        ActionForward forward;
        forward = null;
        ActionErrors errors = new ActionErrors();
        ColumnForm columnForm = (ColumnForm) form;

        //validate available columns to generate report
        errors = vaidateColumnsRequired(columnForm);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        forward = super.execute(mapping, form, request, response);

        //set message if columns to delete have relations
        if (forward != null && "Success".equals(forward.getName())) {
            log.debug("After of cmd execute...................." + request.getAttribute("dto"));
            Map values = (Map) request.getAttribute("dto");
            if (columnForm.getDto(ReportConstants.KEY_RELATION) != null) {
                List columnRelationList = (List) columnForm.getDto(ReportConstants.KEY_RELATION);
                for (Iterator iterator = columnRelationList.iterator(); iterator.hasNext();) {
                    Map map = (Map) iterator.next();
                    String label = (map.get(ReportConstants.KEY_LABEL) != null) ? map.get(ReportConstants.KEY_LABEL).toString() : null;
                    String composeLabel;

                    if (map.containsKey(ReportConstants.KEY_RELATION_COLUMNGROUP)) {
                        composeLabel = Functions.composeColumnLabelByTitusPath(map.get(ReportConstants.KEY_RELATION_COLUMNGROUP).toString(), label, request);
                        errors.add("Rel_grouping", new ActionError("Report.columns.error.deleted", composeLabel, JSPHelper.getMessage(request, "Report.columns.msg.columnGrouping")));
                    }
                    if (map.containsKey(ReportConstants.KEY_RELATION_COLUMNTOTALIZE)) {
                        composeLabel = Functions.composeColumnLabelByTitusPath(map.get(ReportConstants.KEY_RELATION_COLUMNTOTALIZE).toString(), label, request);
                        errors.add("Rel_totalize", new ActionError("Report.columns.error.deleted", composeLabel, JSPHelper.getMessage(request, "Report.columns.msg.totalizeColumns")));
                    }
                    if (map.containsKey(ReportConstants.KEY_RELATION_CHART)) {
                        composeLabel = Functions.composeColumnLabelByTitusPath(map.get(ReportConstants.KEY_RELATION_CHART).toString(), label, request);
                        errors.add("Rel_chart", new ActionError("Report.columns.error.deleted", composeLabel, JSPHelper.getMessage(request, "Report.columns.msg.reportChart")));
                    }
                }
                saveErrors(request, errors);
            }
        }
        return forward;
    }

    /**
     * Validate columns required. If report has defined groupings, the report should have
     * defined "reportGroups + 1" columns
     *
     * @param columnForm
     * @return errors
     */
    private ActionErrors vaidateColumnsRequired(ColumnForm columnForm) {
        ActionErrors errors = new ActionErrors();
        List reportColumns = (List) columnForm.getDto("listColumns");
        Integer reportId = new Integer(columnForm.getDto("reportId").toString());
        int reportGroupingSize = Functions.getReportColumnGroupsSize(reportId);

        if (reportColumns.size() <= reportGroupingSize && reportGroupingSize > 0) {
            errors.add("columns", new ActionError("Report.columns.error.columnsRequired", reportGroupingSize + 1));
        }
        return errors;
    }


}
