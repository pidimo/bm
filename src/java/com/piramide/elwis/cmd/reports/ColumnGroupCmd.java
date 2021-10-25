package com.piramide.elwis.cmd.reports;

import com.jatun.titus.listgenerator.structure.type.DBType;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.*;
import com.piramide.elwis.dto.reports.ColumnDTO;
import com.piramide.elwis.dto.reports.ColumnGroupDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author : ivan
 * @version : $Id ColumnGroupCmd ${time}
 */
public class ColumnGroupCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    private final Integer[] columnTypes = {DBType.DBTypeNameAsInt.DATE, DBType.DBTypeNameAsInt.DATETIME};
    public final List<Integer> columnTypesList = Arrays.asList(columnTypes);

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(ejb.javax.SessionContext)");
        log.debug("paramDTO : " + paramDTO);

        Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());


        if ("constructStructure".equals(this.getOp())) {
            constructStructureForSelect(reportId, companyId);
        }

        if ("create".equals(this.getOp())) {
            ColumnHome columnHome = (ColumnHome)
                    EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);

            Collection groupedColumns = new ArrayList();
            try {
                groupedColumns = columnHome.findByReportIdOnlyGroupColumns(reportId, companyId);

                if (!groupedColumns.isEmpty()) {
                    update(reportId, companyId, groupedColumns, ctx);
                }

            } catch (FinderException e) {
                create(reportId, companyId);
            }
            if (groupedColumns.isEmpty()) {
                create(reportId, companyId);
            }
        }

        if ("read".equals(this.getOp()) || "".equals(this.getOp().trim())) {
            read(reportId, companyId);
        }

    }

    public void update(Integer reportId, Integer companyId, Collection groupedColumns, SessionContext ctx) {
        log.debug("update('" + reportId + "', '" + companyId + "', '" + groupedColumns + "', ejb.javax.SessionContext)");

        Integer columnGroupTotalVersion = Integer.valueOf(paramDTO.get("columnGroupTotalVersion").toString());

        if (!columnGroupTotalVersion.equals(countColumnGroupVersion(reportId, companyId))) {
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.setForward("Fail");
            resultDTO.put("concurrenceError", Boolean.valueOf(true));
            return;
        }

        List<Map> updateErrorsList;

        Integer resourceListSize = Integer.valueOf((String) paramDTO.get("resourceListSize"));
        List<Integer> columns = saveAndOrderIUColumns(companyId, resourceListSize);
        updateErrorsList = deleteColumnGroup(groupedColumns, columns);

        for (Map map : updateErrorsList) {
            String label = (String) map.get("columnLabel");

            resultDTO.addResultMessage("Chart.common.CannotDelete", label);
            resultDTO.setForward("Fail");
        }
        if (!"Fail".equals(resultDTO.getForward())) {
            orderGroupedColumns(reportId, companyId);
        } else {
            ctx.setRollbackOnly();
            resultDTO.put("cmdError", Boolean.valueOf(true));
        }
    }

    private void read(Integer reportId, Integer companyId) {
        ColumnHome columnHome = (ColumnHome)
                EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);


        List myColumnsId = new ArrayList();
        try {
            Collection columnsWithGroup;
            columnsWithGroup = columnHome.findByReportIdOnlyGroupColumns(reportId, companyId);


            for (Iterator it = columnsWithGroup.iterator(); it.hasNext();) {
                Column column = (Column) it.next();

                myColumnsId.add(column.getColumnGroup().getSequence());

                resultDTO.put("columnId_" + column.getColumnGroup().getSequence(), column.getColumnId());
                resultDTO.put("columnOrder_" + column.getColumnGroup().getSequence(),
                        column.getColumnGroup().getColumnOrder());
                resultDTO.put("version_" + column.getColumnGroup().getSequence(),
                        column.getColumnGroup().getVersion());

                resultDTO.put("columnReferenceSelected_" + column.getColumnGroup().getSequence(), column.getColumnReference());
                resultDTO.put("tableReferenceSelected_" + column.getColumnGroup().getSequence(), column.getTableReference());
                resultDTO.put("labelSelected_" + column.getColumnGroup().getSequence(), column.getLabel());


                resultDTO.put("columnGroupId_" + column.getColumnGroup().getSequence(),
                        column.getColumnGroup().getColumnGroupId());
                if (DBType.DBTypeNameAsInt.DATE == column.getColumnType().intValue() ||
                        DBType.DBTypeNameAsInt.DATETIME == column.getColumnType().intValue()) {
                    resultDTO.put("groupByDate_" + column.getColumnGroup().getSequence(),
                            column.getColumnGroup().getGroupByDate());
                    resultDTO.put("select_" + column.getColumnGroup().getSequence(), Boolean.valueOf(true));
                }
                resultDTO.put("axis_" + column.getColumnGroup().getSequence(), column.getColumnGroup().getAxis());

                resultDTO.put("titusPath_" + column.getColumnGroup().getSequence(), new String(column.getPath()));
            }
        } catch (FinderException e) {
        }

        if (null != paramDTO.get("resourceListSize") && !"".equals(paramDTO.get("resourceListSize").toString())) {
            Integer resourceListSize = Integer.valueOf((String) paramDTO.get("resourceListSize"));
            for (int i = 1; i <= resourceListSize.intValue(); i++) {
                Integer value = new Integer(i);
                if (!myColumnsId.contains(value)) {
                    resultDTO.put("columnId_" + i, null);
                    resultDTO.put("columnOrder_" + i, null);
                    resultDTO.put("version_" + i, null);
                    resultDTO.put("columnGroupId_" + i, null);
                    resultDTO.put("select_" + i, Boolean.valueOf(false));
                    resultDTO.put("axis_" + i, null);
                    resultDTO.put("columnReferenceSelected_" + i, null);
                    resultDTO.put("tableReferenceSelected_" + i, null);
                    resultDTO.put("labelSelected_" + i, null);
                    resultDTO.put("titusPath_" + i, null);
                }
            }
        }

        ReportHome reportHome = (ReportHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORT);

        try {
            Report report = reportHome.findByPrimaryKey(reportId);
            if (ReportConstants.MATRIX_TYPE.equals(report.getReportType())) {
                resultDTO.setForward("MatrixSuccess");
            }

        } catch (FinderException e) {

        }

        resultDTO.put("columnGroupTotalVersion", countColumnGroupVersion(reportId, companyId));
    }

    private void create(Integer reportId, Integer companyId) {

        Integer columnGroupTotalVersion = Integer.valueOf(paramDTO.get("columnGroupTotalVersion").toString());

        if (!columnGroupTotalVersion.equals(countColumnGroupVersion(reportId, companyId))) {
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.setForward("Fail");
            return;
        }


        Integer resourceListSize = Integer.valueOf((String) paramDTO.get("resourceListSize"));

        ColumnHome columnHome =
                (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);

        List columnsSelected = new ArrayList();
        for (int i = 1; i <= resourceListSize.intValue(); i++) {

            if (null != paramDTO.get("columnId_" + i) && !"".equals(paramDTO.get("columnId_" + i).toString())) {
                Integer columnId_i = Integer.valueOf((String) paramDTO.get("columnId_" + i));


                try {

                    String axisValueSelected = (String) paramDTO.get("columnIdisAxis_" + i);

                    Integer axis = null;
                    if (null != axisValueSelected &&
                            !"".equals(axisValueSelected) &&
                            axisValueSelected.indexOf("_X") > -1) {
                        axis = ReportConstants.COLUMN_GROUP_AXIS_X;
                    }

                    if (null != axisValueSelected &&
                            !"".equals(axisValueSelected) &&
                            axisValueSelected.indexOf("_Y") > -1) {
                        axis = ReportConstants.COLUMN_GROUP_AXIS_Y;
                    }

                    Map map = new HashMap();
                    Column column = columnHome.findByPrimaryKey(columnId_i);
                    map.put("position", new Integer(i));
                    map.put("column", column);
                    map.put("axis", axis);
                    columnsSelected.add(map);
                } catch (FinderException e) {
                    resultDTO.addResultMessage("Report.common.deleteColumn");
                    resultDTO.setForward("Fail");
                    return;
                }
            }
        }


        for (int i = 0; i < columnsSelected.size(); i++) {
            ColumnGroupDTO dto = new ColumnGroupDTO();
            Map column = (Map) columnsSelected.get(i);
            Column actual = (Column) column.get("column");
            Integer position = (Integer) column.get("position");
            Integer axis = (Integer) column.get("axis");

            dto.put("companyId", companyId);
            dto.put("columnOrder", Boolean.valueOf((String) paramDTO.get("columnOrder_" + (position.intValue()))));
            dto.put("sequence", position);


            if (DBType.DBTypeNameAsInt.DATE == actual.getColumnType().intValue() ||
                    DBType.DBTypeNameAsInt.DATETIME == actual.getColumnType().intValue()) {
                dto.put("groupByDate", Integer.valueOf((String) paramDTO.get("groupByDate_" + (position.intValue()))));
            }
            dto.put("columnId", actual.getColumnId());

            if (axis != null) {
                dto.put("axis", axis);
            }

            ExtendedCRUDDirector.i.create(dto, resultDTO, false);
        }

        if (!"Fail".equals(resultDTO.getForward())) {
            orderGroupedColumns(reportId, companyId);
        }
    }

    private Integer countColumnGroupVersion(Integer reportId, Integer companyId) {

        Integer result = null;

        int cache = 0;
        ColumnHome columnHome =
                (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);
        Collection groupedColumns = new ArrayList();

        try {
            groupedColumns = columnHome.findByReportIdOnlyGroupColumns(reportId, companyId);
            for (Iterator it = groupedColumns.iterator(); it.hasNext();) {
                Column column = (Column) it.next();

                cache += column.getColumnGroup().getVersion().intValue();
            }
        } catch (FinderException e) {
        }
        result = new Integer(cache);

        return result;
    }

    private void constructStructureForSelect(Integer reportId, Integer companyId) {
        log.debug("constructStructureForSelect('" + reportId + "', '" + companyId + "')");

        Collection structure = new ArrayList();

        Collection tables = getAllTables(reportId, companyId);

        for (Iterator iterator = tables.iterator(); iterator.hasNext();) {
            Map structureMap = new HashMap();
            String referenceTable = (String) iterator.next();
            Collection columns = getColumnsByTable(reportId, referenceTable, companyId);
            structureMap.put("referenceTable", referenceTable);
            structureMap.put("columns", columns);
            structure.add(structureMap);
        }

        resultDTO.put("structureSelect", structure);
    }

    private Collection getAllTables(Integer reportId, Integer companyId) {
        log.debug("getAllTables('" + reportId + "', '" + companyId + "')");

        ColumnHome columnHome = (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);


        Collection tables = new ArrayList();

        try {

            Collection reportColumns = columnHome.findByReportId(reportId, companyId);

            tables = selectTables(reportColumns);

        } catch (FinderException e) {
        }

        return tables;
    }

    private Collection selectTables(Collection columns) {
        log.debug("selectTables('" + columns + "')");
        Collection tables = new ArrayList();

        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            Column column = (Column) iterator.next();

            String tableReference = column.getTableReference();

            if (!tables.contains(tableReference)) {
                tables.add(column.getTableReference());
            }
        }
        return tables;
    }

    private Collection getColumnsByTable(Integer reportId, String referenceTable, Integer companyId) {
        log.debug("getColumnsByTable('" + reportId + "', '" + referenceTable + "', '" + companyId + "')");

        Collection result = new ArrayList();
        ColumnHome columnHome = (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);
        try {

            Collection columnsByTable = columnHome.findByReportIdTableReference(reportId, referenceTable, companyId);
            for (Iterator iterator = columnsByTable.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                ColumnDTO dto = new ColumnDTO();

                dto.put(ColumnDTO.KEY_COLUMNID, column.getColumnId());
                dto.put("label", column.getLabel());
                dto.put("columnType", column.getColumnType());
                dto.put("columnId", column.getColumnId());
                dto.put("tableReference", column.getTableReference());
                dto.put("columnReference", column.getColumnReference());
                dto.put("titusPath", new String(column.getPath()));
                result.add(dto);
            }
        } catch (FinderException e) {

        }


        return result;
    }

    private void orderGroupedColumns(Integer reportId, Integer companyId) {
        log.debug("orderGroupedColumns('" + reportId + "', '" + companyId + "')");

        ColumnHome home = (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);

        try {
            Collection groupedColumns = home.findByReportIdOnlyGroupColumns(reportId, companyId);

            int counter = 1;
            for (Iterator iterator = groupedColumns.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                column.getColumnGroup().setSequence(new Integer(counter));
                counter++;
            }
        } catch (FinderException e) {
            log.debug("Cannot find columns for reportId = " + reportId);
        }
    }

    private List<Integer> saveAndOrderIUColumns(int companyId, int resourceListSize) {
        log.debug("java.util.List saveAndOrderIUColumns('" + companyId + "', '" + resourceListSize + "' )");

        List<Integer> columnsCreated = new ArrayList<Integer>();
        int order = 1;
        ColumnHome h = (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);

        for (int i = 1; i <= resourceListSize; i++) {
            int iuColumnId;
            try {
                iuColumnId = new Integer(paramDTO.get("columnId_" + i).toString());
            } catch (NumberFormatException nfe) {
                continue;
            } catch (NullPointerException npe) {
                continue;
            }

            try {
                Column c = h.findByPrimaryKey(iuColumnId);
                ColumnGroup group = c.getColumnGroup();
                if (null != group) {
                    if (group.getSequence() != order) {
                        group.setSequence(order);
                    }

                    Boolean columnOrder = Boolean.valueOf(paramDTO.get("columnOrder_" + i).toString());
                    group.setColumnOrder(columnOrder);

                    if (columnTypesList.contains(c.getColumnType())) {
                        group.setGroupByDate(Integer.valueOf((String) paramDTO.get("groupByDate_" + (i))));
                    }

                } else {
                    ColumnGroupDTO dto = new ColumnGroupDTO();
                    dto.put("companyId", companyId);
                    dto.put("columnOrder", Boolean.valueOf((String) paramDTO.get("columnOrder_" + i)));
                    dto.put("sequence", order);

                    if (columnTypesList.contains(c.getColumnType())) {
                        dto.put("groupByDate", Integer.valueOf((String) paramDTO.get("groupByDate_" + (i))));
                    }

                    dto.put("columnId", c.getColumnId());

                    ExtendedCRUDDirector.i.create(dto, resultDTO, false);

                }
                columnsCreated.add(c.getColumnId());
            } catch (FinderException e) {
                resultDTO.addResultMessage("Report.common.deleteColumn");
                resultDTO.setForward("Fail");
                break;
            }
            order++;
        }
        return columnsCreated;
    }

    private List<Map> deleteColumnGroup(Collection columnGroups, List<Integer> actualColumnsWithGroup) {
        log.debug("java.util.List deleteColumnGroup('" + columnGroups + "', '" + actualColumnsWithGroup + "')");

        List<Map> errorList = new ArrayList<Map>();
        int i = 1;
        for (Iterator iterator = columnGroups.iterator(); iterator.hasNext();) {
            Column c = (Column) iterator.next();
            if (!actualColumnsWithGroup.contains(c.getColumnId())) {
                try {
                    if (null != c.getColumnGroup().getChartSerie() || null != c.getColumnGroup().getChartCategory()) {
                        Map cannotDeletedMap = new HashMap();
                        cannotDeletedMap.put("columnLabel", paramDTO.get("selectedName_" + i));
                        errorList.add(cannotDeletedMap);
                    } else {
                        c.getColumnGroup().remove();
                    }
                } catch (RemoveException e) {
                    log.error("Cannot remove ColumnGroup for column with columnId= " + c.getColumnId() + " because ", e);
                }
            }
            i++;
        }
        return errorList;
    }

    public boolean isStateful() {
        return false;
    }

}
