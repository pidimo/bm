package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.Column;
import com.piramide.elwis.domain.reportmanager.Report;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import com.piramide.elwis.dto.reports.ColumnDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun s.r.l.
 *
 * @author miky
 * @version $Id: ColumnCmd.java 8468 2008-09-11 21:10:05Z miguel $
 */
public class ColumnCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ColumnCmd................" + paramDTO);

        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());
        List listColumns = (List) paramDTO.get("listColumns");
        List existColumnIdList = new ArrayList();
        List previousColumnsList = (List) paramDTO.get("previousColumns");
        List columnRelationList = new ArrayList();

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.put("reportId", reportId);
        Report report = (Report) EJBFactory.i.findEJB(reportDTO);

        //recurrence validate
        if (report != null) {
            Collection columns = (Collection) report.getColumns();
            if (columns.size() == previousColumnsList.size()) {
                boolean modified = false;
                for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                    Column actualColumn = (Column) iterator.next();

                    boolean existColumn = false;
                    for (Iterator iterator2 = previousColumnsList.iterator(); iterator2.hasNext();) {
                        String prevText = (String) iterator2.next();
                        String columnId = columnTextParser(prevText, ReportConstants.KEY_COLUMNSEPARATOR_ID);
                        String version = columnTextParser(prevText, ReportConstants.KEY_COLUMNSEPARATOR_VERSION);
                        if (columnId != null && version != null) {
                            if (actualColumn.getColumnId().equals(new Integer(columnId))
                                    && actualColumn.getVersion().equals(new Integer(version))) {
                                existColumn = true;
                            }
                        } else {
                            modified = true;
                            break;
                        }
                    }
                    if (!existColumn) {
                        modified = true;
                        break;
                    }
                }
                if (modified) {
                    log.debug("report has modified..........");
                    //modified
                    resultDTO.addResultMessage("Common.error.concurrency");
                    resultDTO.setForward("Fail");
                    return;
                }
            } else {
                log.debug("column has deleted for other user........");
                //modified
                resultDTO.addResultMessage("Common.error.concurrency");
                resultDTO.setForward("Fail");
                return;
            }

        } else {
            log.debug("report deleted.........");
            //report deleted
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.setForward("FailReport");
            return;
        }

        log.debug("*****************pass validate********************");
        //create or update columns
        int sequence = 0;
        Column column = null;
        for (Iterator iterator = listColumns.iterator(); iterator.hasNext();) {
            String text = (String) iterator.next();

            if (text.trim().length() > 0) {
                ColumnDTO columnDTO = new ColumnDTO();

                String columnId = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_ID);
                String columnRef = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_COLUMNREF);
                String path = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_PATH);
                String label = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_LABEL);
                String type = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_TYPE);
                String tableRef = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_TABLEREF);
                String version = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_VERSION);
                String isTotalizer = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_ISTOTALIZER);
                String columnOrder = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_ORDER);
                String categoryId = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_CATEGORYID);

                columnDTO.put("columnReference", columnRef);
                columnDTO.put("columnType", type);
                columnDTO.put("companyId", companyId);
                columnDTO.put("tempPath", path);//used to set from bean
                columnDTO.put("reportId", reportId);
                columnDTO.put("sequence", new Integer(sequence));
                columnDTO.put("tableReference", tableRef);
                columnDTO.put("categoryId", categoryId);
                if (isTotalizer != null && isTotalizer.equals("true")) {
                    columnDTO.put("isTotalizer", new Boolean(true));
                } else {
                    columnDTO.put("isTotalizer", new Boolean(false));
                }

                if (columnId != null) {
                    //is update
                    columnDTO.put("columnId", new Integer(columnId));
                    columnDTO.put("version", new Integer(Integer.parseInt(version) + 1));

                    log.debug("DTO*UP**********" + columnDTO);
                    try {
                        column = (Column) ExtendedCRUDDirector.i.update(columnDTO, resultDTO, false, false, false, "Fail");    //update
                        //set label
                        if (label != null) {
                            column.setLabel(label.trim());
                        } else {
                            column.getLabel();
                            column.setLabel(null);
                        }
                        //set column order
                        if (columnOrder != null) {
                            column.setColumnOrder(Boolean.valueOf(columnOrder));
                        } else {
                            column.getColumnOrder();
                            column.setColumnOrder(null);
                        }
                    } catch (Exception e) {
                        log.debug("Error to update column......");
                        ctx.setRollbackOnly();
                        resultDTO.addResultMessage("Common.error.concurrency");
                        resultDTO.setForward("Fail");
                        return;
                    }
                } else {
                    try {
                        columnDTO.put("label", (label != null) ? label.trim() : null);
                        columnDTO.put("columnOrder", (columnOrder != null) ? Boolean.valueOf(columnOrder) : null);
                        log.debug("DTO*CRE**********" + columnDTO);
                        column = (Column) EJBFactory.i.createEJB(columnDTO);    //create
                    } catch (Exception e) {
                        log.debug("Error to create column......");
                        ctx.setRollbackOnly();
                        resultDTO.addResultMessage("Common.error.concurrency");
                        resultDTO.setForward("Fail");
                        return;
                    }
                }
                if (column != null) {
                    existColumnIdList.add(column.getColumnId());
                }
                sequence++;
            }
        }

        //delete not exist columns
        try {
            report = (Report) EJBFactory.i.findEJB(reportDTO);
        } catch (Exception e) {
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.setForward("FailReport");
            return;
        }
        if (!report.getColumns().isEmpty()) {
            Collection columns = (Collection) report.getColumns();
            Object[] ob = columns.toArray();
            for (int i = 0; i < ob.length; i++) {
                Column removeColumn = (Column) ob[i];
                boolean existColumn = false;
                for (Iterator iterator2 = existColumnIdList.iterator(); iterator2.hasNext();) {
                    Integer existColumnId = (Integer) iterator2.next();
                    if (existColumnId.equals(removeColumn.getColumnId())) {
                        existColumn = true;
                        break;
                    }
                }
                if (!existColumn) {
                    try {
                        if (!columnHaveRelation(removeColumn, columnRelationList)) {
                            EJBFactory.i.removeEJB(removeColumn);
                        } else {
                            //update sequence
                            removeColumn.setSequence(new Integer(sequence));
                            sequence++;
                        }
                    } catch (Exception e) {
                        log.debug("Error, column deleted.....");
                        resultDTO.addResultMessage("Common.error.concurrency");
                        resultDTO.setForward("Fail");
                        return;
                    }
                }
            }
        }

        //if exist column relation
        if (!columnRelationList.isEmpty()) {
            resultDTO.put(ReportConstants.KEY_RELATION, columnRelationList);
        }
    }

    /**
     * parser data of column, get substring that be between the parserId
     *
     * @param text     text to parser
     * @param parserId key to parser
     * @return String column data
     */
    private String columnTextParser(String text, String parserId) {
        String res = null;
        int firstIndex = text.indexOf(parserId);
        int lastIindex = text.lastIndexOf(parserId);
        if (firstIndex != lastIindex) {
            res = text.substring((firstIndex + parserId.length()), lastIindex).trim();
        }

        return res;
    }

    private boolean columnHaveRelation(Column column, List columnRelationList) {
        boolean res = false;
        if (column.getColumnGroup() != null) {
            res = true;
            Map map = new HashMap();
            map.put(ReportConstants.KEY_RELATION_COLUMNGROUP, new String(column.getPath()));
            map.put(ReportConstants.KEY_LABEL, column.getLabel());
            columnRelationList.add(map);
        }
        if (column.getColumnTotalizers() != null && !column.getColumnTotalizers().isEmpty()) {
            res = true;
            Map map = new HashMap();
            map.put(ReportConstants.KEY_RELATION_COLUMNTOTALIZE, new String(column.getPath()));
            map.put(ReportConstants.KEY_LABEL, column.getLabel());
            columnRelationList.add(map);
        }
        /*if (column.getChartX() != null || column.getChartY() != null) {
            res = true;
            Map map = new HashMap();
            map.put(ReportConstants.KEY_RELATION_CHART, new String(column.getPath()));
            map.put(ReportConstants.KEY_LABEL, column.getLabel());
            columnRelationList.add(map);
        }*/ //todo:revise charts

        return res;
    }

}
