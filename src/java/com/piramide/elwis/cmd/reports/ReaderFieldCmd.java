package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.*;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import com.piramide.elwis.dto.reports.ChartDTO;
import com.piramide.elwis.dto.reports.ColumnDTO;
import com.piramide.elwis.dto.reports.FilterDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author : ivan
 * @version : $Id ReaderFieldCmd ${time}
 */
public class ReaderFieldCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        readColumns(reportId, companyId);
        readFilters(reportId, companyId);
        readReportData(reportId, companyId);
    }

    private void readColumns(Integer reportId, Integer companyId) {
        ColumnHome columnHome =
                (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);

        // all columns that are visible in the report
        Collection columns = new ArrayList();

        List dtos = new ArrayList();

        try {
            columns = columnHome.findByReportId(reportId, companyId);
            columns = orderByColumnSequence(columns.toArray());
        } catch (FinderException e) {

        }

        for (Iterator it = columns.iterator(); it.hasNext();) {
            Column column = (Column) it.next();
            ColumnDTO dto = new ColumnDTO();

            dto.put("columnReference", column.getColumnReference());
            dto.put("companyId", column.getCompanyId());
            dto.put(dto.getPrimKeyName(), column.getColumnId());
            dto.put("path", new String(column.getPath()));
            dto.put("reportId", column.getReportId());
            dto.put("tableReference", column.getTableReference());
            if (null != column.getColumnGroup()) {
                dto.put("isGrouped", Boolean.valueOf(true));
                ColumnGroup columnGroup = column.getColumnGroup();
                dto.put("groupSequence", columnGroup.getSequence());
                dto.put("groupOrder", columnGroup.getColumnOrder());
                dto.put("groupByDate", columnGroup.getGroupByDate());
                dto.put("groupAxis", columnGroup.getAxis());
            } else {
                dto.put("isGrouped", Boolean.valueOf(false));
            }
            dto.put("label", column.getLabel());
            dto.put("isTotalizer", column.getIsTotalizer().toString());
            dto.put("sequence", column.getSequence());
            dto.put("columnType", column.getColumnType());
            if (column.getColumnOrder() != null) {
                dto.put("columnOrder", column.getColumnOrder());
            }
            dtos.add(dto);
        }

        resultDTO.put("columns", dtos);
    }

    private void readFilters(Integer reportId, Integer companyId) {
        FilterHome home =
                (FilterHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_FILTER);

        Collection filters = new ArrayList();

        List dtos = new ArrayList();
        try {
            filters = home.findByReportId(reportId, companyId);
        } catch (FinderException fe) {

        }

        for (Iterator it = filters.iterator(); it.hasNext();) {
            Filter filter = (Filter) it.next();
            FilterDTO dto = new FilterDTO();
            DTOFactory.i.copyToDTO(filter, dto);
            dto.put("path", new String(filter.getPath()));

            //add filter values
            FilterValueHome filterValueHome = (FilterValueHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_FILTERVALUE);
            Collection filterValues = null;
            List valuesList = new LinkedList();
            try {
                filterValues = filterValueHome.findByFilterId(filter.getFilterId()); //find with ordered
            } catch (FinderException e) {
                log.debug("not values to filter:" + filter.getAliasCondition());
            }
            if (filterValues != null) {
                for (Iterator iterator = filterValues.iterator(); iterator.hasNext();) {
                    FilterValue filterValue = (FilterValue) iterator.next();
                    if (filterValue.getFilterValue() != null) {
                        valuesList.add(filterValue.getFilterValue());
                    }
                }
            }

            dto.put("values", valuesList);
            dtos.add(dto);
        }
        resultDTO.put("filters", dtos);
    }

    public boolean isStateful() {
        return false;
    }

    /**
     * Read the data of one report
     *
     * @param reportId
     * @param companyId
     */
    private void readReportData(Integer reportId, Integer companyId) {
        ReportDTO reportDto = new ReportDTO();
        reportDto.put("reportId", reportId);
        reportDto.put("companyId", companyId);

        ResultDTO result_dto = new ResultDTO();
        Report report = (Report) ExtendedCRUDDirector.i.read(reportDto, result_dto, false);
        readTotalizators(report);
        readChart(report);
        resultDTO.put("reportData", result_dto);
    }

    private void readTotalizators(Report report) {
        ArrayList totalizators = new ArrayList(report.getTotalizers());
        ArrayList totalizatorDTOS = new ArrayList();
        for (int i = 0; i < totalizators.size(); i++) {
            Totalize totalize = (Totalize) totalizators.get(i);
            DTO totalizatorDTO = new DTO();
            totalizatorDTO.put("totalizatorId", totalize.getTotalizeId());
            totalizatorDTO.put("totalizatorFormula", totalize.getFormula());
            totalizatorDTO.put("totalizatorName", totalize.getName());
            totalizatorDTO.put("totalizatorType", totalize.getTotalizeType());

            //Read the columns implicated in the totalizator
            ArrayList columnTotalizers = new ArrayList(totalize.getColumnTotalizers());
            ArrayList totalizerColumns = new ArrayList();
            for (int ct = 0; ct < columnTotalizers.size(); ct++) {
                ColumnTotalize columnTotalize = (ColumnTotalize) columnTotalizers.get(ct);
                totalizerColumns.add(columnTotalize.getColumnId());
            }
            totalizatorDTO.put("totalizatorColumnIds", totalizerColumns);
            totalizatorDTOS.add(totalizatorDTO);
        }
        resultDTO.put("totalizators", totalizatorDTOS);
    }

    private void readChart(Report report) {
        if (report != null) {
            Chart chart = report.getCharts();
            ChartDTO chartDTO = new ChartDTO();
            if (chart != null) {
                DTOFactory.i.copyToDTO(chart, chartDTO);
                if (chart.getSerie() != null) {
                    ColumnGroup columnGroup = chart.getSerie();
                    //set in DTO
                    chartDTO.put("seriePath", new String(columnGroup.getColumn().getPath()));
                    chartDTO.put("serieOrder", columnGroup.getColumnOrder());
                    chartDTO.put("serieColumnId", columnGroup.getColumnId());
                }

                if (chart.getCategory() != null) {
                    ColumnGroup columnGroup = chart.getCategory();
                    //set in DTO
                    chartDTO.put("categoryPath", new String(columnGroup.getColumn().getPath()));
                    chartDTO.put("categoryOrder", columnGroup.getColumnOrder());
                    chartDTO.put("categoryColumnId", columnGroup.getColumnId());
                }

                //chart totalizers column path list
                chartDTO.put("totalizersPath", getChartTotalizersColumnPaths(chart));

                //The totalizators
                Integer xTotalizator = chart.getValueXId();
                if (xTotalizator != null) {
                    chartDTO.put("xTotalizatorId", xTotalizator);
                }

                Integer yTotalizator = chart.getValueYId();
                if (yTotalizator != null) {
                    chartDTO.put("yTotalizatorId", yTotalizator);
                }

                Integer zTotalizator = chart.getValueZId();
                if (zTotalizator != null) {
                    chartDTO.put("zTotalizatorId", zTotalizator);
                }

                resultDTO.put("chartData", chartDTO);
            }
        }
    }

    /**
     * ascendent sequence order
     *
     * @param array
     * @return Collection
     */
    public Collection orderByColumnSequence(Object[] array) {
        Collection res;
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                Column column1 = (Column) array[i];
                Column column2 = (Column) array[j];
                if (column1.getSequence().intValue() > column2.getSequence().intValue()) {
                    Object aux = array[i];
                    array[i] = array[j];
                    array[j] = aux;
                }
            }
        }
        res = Arrays.asList(array);

        return res;
    }

    /**
     * get chart totalizers column paths
     *
     * @param chart
     * @return List
     */
    private List getChartTotalizersColumnPaths(Chart chart) {

        Set columnPathSet = new HashSet();

        Totalize xValueTotalize = chart.getXValue();
        Totalize yValueTotalize = chart.getYValue();
        Totalize zValueTotalize = chart.getZValue();

        if (xValueTotalize != null) {
            Collection columnTotalizers = xValueTotalize.getColumnTotalizers();
            for (Iterator iterator = columnTotalizers.iterator(); iterator.hasNext();) {
                ColumnTotalize columnTotalize = (ColumnTotalize) iterator.next();
                Column column = (Column) EJBFactory.i.findEJB(new ColumnDTO(columnTotalize.getColumnId()));
                if (column != null) {
                    columnPathSet.add(new String(column.getPath()));
                }
            }
        }
        if (yValueTotalize != null) {
            Collection columnTotalizers = yValueTotalize.getColumnTotalizers();
            for (Iterator iterator = columnTotalizers.iterator(); iterator.hasNext();) {
                ColumnTotalize columnTotalize = (ColumnTotalize) iterator.next();
                Column column = (Column) EJBFactory.i.findEJB(new ColumnDTO(columnTotalize.getColumnId()));
                if (column != null) {
                    columnPathSet.add(new String(column.getPath()));
                }
            }
        }
        if (zValueTotalize != null) {
            Collection columnTotalizers = zValueTotalize.getColumnTotalizers();
            for (Iterator iterator = columnTotalizers.iterator(); iterator.hasNext();) {
                ColumnTotalize columnTotalize = (ColumnTotalize) iterator.next();
                Column column = (Column) EJBFactory.i.findEJB(new ColumnDTO(columnTotalize.getColumnId()));
                if (column != null) {
                    columnPathSet.add(new String(column.getPath()));
                }
            }
        }

        return ((!columnPathSet.isEmpty()) ? new ArrayList(columnPathSet) : new ArrayList());
    }

}
