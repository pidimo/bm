package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.reportmanager.Chart;
import com.piramide.elwis.domain.reportmanager.Report;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import com.piramide.elwis.dto.reports.ChartDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Nov 16, 2005
 * Time: 12:14:53 PM
 * To change this template use File | Settings | File Templates.
 */

public class ChartCmd extends GeneralCmd {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ChartCmd................" + paramDTO);

        Integer reportId = new Integer(paramDTO.get("reportId").toString());
        Report report = (Report) EJBFactory.i.findEJB(new ReportDTO(reportId));

        if (report.getReportType().equals(ReportConstants.SINGLE_TYPE)) {
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.setForward("Single");
            return;
        }

        if ("read".equals(getOp()) && paramDTO.get("chartId") == null) {
            Chart chart = report.getCharts();
            if (chart != null) {
                DTOFactory.i.copyToDTO(chart, resultDTO);
            }

        } else if ("create".equals(getOp()) && report.getCharts() != null) {
            DTOFactory.i.copyToDTO(report.getCharts(), resultDTO);
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.setForward("Fail");

        } else {
            ChartDTO chartDTO = new ChartDTO(paramDTO);
            if (paramDTO.get("isEnable") != null) {
                chartDTO.put("isEnable", new Boolean(true));
            } else {
                chartDTO.put("isEnable", new Boolean(false));
            }

            if (paramDTO.get("showOnlyChart") != null) {
                chartDTO.put("showOnlyChart", new Boolean(true));
            } else {
                chartDTO.put("showOnlyChart", new Boolean(false));
            }

            //execute cmd
            super.setOp(this.getOp());
            super.checkDuplicate = false;
            super.isClearingForm = false;
            Chart chart = (Chart) super.execute(ctx, chartDTO);
        }
    }

    private void readChartFields(SessionContext ctx) {

        List totalizeList = new ArrayList(0);
        List columnGroupList = new ArrayList(0);

        ReaderChartFieldCmd chartFieldCmd = new ReaderChartFieldCmd();
        chartFieldCmd.putParam("reportId", paramDTO.get("reportId").toString());
        chartFieldCmd.putParam("companyId", paramDTO.get("companyId").toString());
        chartFieldCmd.executeInStateless(ctx);

        totalizeList = (List) chartFieldCmd.getResultDTO().get("totalizeList");
        columnGroupList = (List) chartFieldCmd.getResultDTO().get("columnList");

        resultDTO.put("totalizeList", totalizeList);
        resultDTO.put("columnList", columnGroupList);
    }
}
