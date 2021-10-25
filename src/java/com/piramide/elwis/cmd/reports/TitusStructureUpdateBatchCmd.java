package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.*;
import com.piramide.elwis.dto.reports.TotalizeDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 * cmd to fix report columns and filters related to data base remove fields from titus structure
 *
 * @author Miky
 * @version $Id: TitusStructureUpdateBatchCmd.java 11-jul-2008 16:07:00 $
 */
public class TitusStructureUpdateBatchCmd extends EJBCommand {
    public static String COLUMNSIDLIST = "columnIds";
    public static String FILTERSIDLIST = "filterIds";

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing TitusStructureUpdateBatchCmd....." + paramDTO);

        List<Integer> columnIdList = (List<Integer>) paramDTO.get(COLUMNSIDLIST);
        List<Integer> filterIdList = (List<Integer>) paramDTO.get(FILTERSIDLIST);

        try {
            removeReportColumn(columnIdList);
            removeReportFilter(filterIdList);
        } catch (RemoveException e) {
            log.error("Error in delete report column...", e);
            ctx.setRollbackOnly();
            resultDTO.setResultAsFailure();
        }

        log.info("End TitusStructureUpdateBatchCmd.........");
    }


    private void removeReportColumn(List<Integer> columnIdList) throws RemoveException {
        ColumnHome columnHome = (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);

        for (Integer columnId : columnIdList) {
            Column column = null;
            try {
                column = columnHome.findByPrimaryKey(columnId);
            } catch (FinderException e) {
                log.debug("Not found column... " + columnId);
                continue;
            }

            if (column.getColumnGroup() != null) {
                removeColumnGroup(column.getColumnGroup());
            }

            if (!column.getColumnTotalizers().isEmpty()) {
                Object[] columnTotalizers = column.getColumnTotalizers().toArray();
                for (int i = 0; i < columnTotalizers.length; i++) {
                    ColumnTotalize columnTotalize = (ColumnTotalize) columnTotalizers[i];
                    removeColumnTotalize(columnTotalize);
                }
            }

            column.remove();
        }
    }

    private void removeColumnGroup(ColumnGroup columnGroup) throws RemoveException {
        if (columnGroup.getChartCategory() != null) {
            removeChart(columnGroup.getChartCategory());
        }
        if (columnGroup.getChartSerie() != null) {
            removeChart(columnGroup.getChartSerie());
        }

        Integer reportId = columnGroup.getColumn().getReportId();
        Integer companyId = columnGroup.getCompanyId();
        columnGroup.remove();

        //update sequence in columns groups
        ColumnHome columnHome = (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);
        try {
            Collection columns = columnHome.findByReportIdOnlyGroupColumns(reportId, companyId);
            int sequence = 1;
            for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                if (column.getColumnGroup() != null) {
                    column.getColumnGroup().setSequence(sequence);
                    sequence++;
                }
            }
        } catch (FinderException e) {
            log.debug("Not found columns...");
        }
    }

    private void removeChart(Chart chart) throws RemoveException {
        log.debug("Remove chart..... " + chart.getTitle());
        chart.remove();
    }

    private void removeColumnTotalize(ColumnTotalize columnTotalize) throws RemoveException {
        Integer totalizeId = columnTotalize.getTotalizeId();
        columnTotalize.remove();
        removeReportTotalize(totalizeId);
    }

    private void removeReportTotalize(Integer totalizeId) throws RemoveException {
        Totalize totalize = (Totalize) ExtendedCRUDDirector.i.read(new TotalizeDTO(totalizeId), new ResultDTO(), false);
        if (totalize != null) {
            if (totalize.getChartXValue() != null) {
                removeChart(totalize.getChartXValue());
            }
            if (totalize.getChartYValue() != null) {
                removeChart(totalize.getChartYValue());
            }
            if (totalize.getChartZValue() != null) {
                removeChart(totalize.getChartZValue());
            }
            totalize.remove();
        }
    }

    private void removeReportFilter(List<Integer> filterIdList) throws RemoveException {
        FilterHome filterHome = (FilterHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_FILTER);

        for (Integer filterId : filterIdList) {
            Filter filter = null;
            try {
                filter = filterHome.findByPrimaryKey(filterId);
            } catch (FinderException e) {
                log.debug("Not found filter... " + filterId);
                continue;
            }

            if (!filter.getFilterValues().isEmpty()) {
                Object[] filterValuesObj = filter.getFilterValues().toArray();
                for (int j = 0; j < filterValuesObj.length; j++) {
                    FilterValue filterValue = (FilterValue) filterValuesObj[j];
                    removeFilterValue(filterValue);
                }
            }
            filter.remove();
        }
    }

    private void removeFilterValue(FilterValue filterValue) throws RemoveException {
        filterValue.remove();
    }
}
