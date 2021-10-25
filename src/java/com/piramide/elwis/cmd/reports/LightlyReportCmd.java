package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.reportmanager.Column;
import com.piramide.elwis.domain.reportmanager.Report;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : ivan
 * @version : $Id LightlyReportCmd ${time}
 */

public class LightlyReportCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        Map newValues = new HashMap();
        newValues.putAll(paramDTO);
        super.setOp(this.getOp());
        Report report = (Report) super.executeInStateless(ctx, paramDTO, ReportDTO.class);

        //check if the report have chart assigned
        if (null != report && null != report.getCharts() && null != report.getCharts()) {
            resultDTO.put("haveChartAssigned", Boolean.valueOf(true));
        }

        //check if the report have columnsGroups assigned
        if (null != report && null != report.getColumns() && !report.getColumns().isEmpty()) {
            resultDTO.put("haveColumnsAssigned", Boolean.valueOf(true));

            Collection columns = report.getColumns();
            for (Iterator it = columns.iterator(); it.hasNext();) {
                Column column = (Column) it.next();
                if (null != column.getColumnGroup()) {
                    resultDTO.put("haveColumnGroupAssigned", Boolean.valueOf(true));
                    break;
                }
            }
        }

        if (null != report && null != report.getFilters() && !report.getFilters().isEmpty()) {
            resultDTO.put("haveFiltersAssigned", Boolean.valueOf(true));
        }


        resultDTO.put("newValues", newValues);

    }

    public boolean isStateful() {
        return false;
    }
}
