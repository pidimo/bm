package com.piramide.elwis.cmd.reports;

import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Jatun S.R.L.
 * Cmd to remove reports related to tables removed in titus structure
 *
 * @author Miky
 * @version $Id: TitusStructureRemoveTableBatchCmd.java 10-feb-2009 14:43:36 $
 */
public class TitusStructureRemoveTableBatchCmd extends EJBCommand {
    public static String REMOVETABLELIST = "tableNameList";

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.info("Executing TitusStructureRemoveTableBatchCmd....." + paramDTO);

        //List table names
        List<String> removedTableList = (List<String>) paramDTO.get(REMOVETABLELIST);
        Set<String> reportIdSet = new HashSet<String>();

        for (String tableName : removedTableList) {
            addReportIds(QueryUtil.i.executeQuery(sqlToReportColumn(tableName)), reportIdSet);
            addReportIds(QueryUtil.i.executeQuery(sqlToReportFilter(tableName)), reportIdSet);
            addReportIds(QueryUtil.i.executeQuery(sqlToReport(tableName)), reportIdSet);
        }

        log.debug("IDs to remove........" + reportIdSet);
        log.info("Reports to remove: " + reportIdSet.size());
        int count = 0;
        for (String reportId : reportIdSet) {
            ReportDeleteCmd reportDeleteCmd = new ReportDeleteCmd();
            reportDeleteCmd.putParam("reportId", reportId);
            reportDeleteCmd.executeInStateless(ctx);

            ResultDTO myResultDTO = reportDeleteCmd.getResultDTO();
            if (!myResultDTO.isFailure()) {
                count++;
            }
        }

        log.info("End TitusStructureRemoveTableBatchCmd, removed reports:" + count);
    }


    private String sqlToReportColumn(String tableName) {
        return "SELECT DISTINCT c.reportid as reportid FROM reportcolumn as c WHERE c.tableref = \'" + tableName + "\';";
    }

    private String sqlToReportFilter(String tableName) {
        return "SELECT DISTINCT f.reportid as reportid FROM reportfilter as f  WHERE f.tableref = \'" + tableName + "\';";
    }

    private String sqlToReport(String tableName) {
        return "SELECT DISTINCT r.reportid as reportid FROM report as r  WHERE r.initialtableref = \'" + tableName + "\';";
    }

    private void addReportIds(List sqlResult, Set<String> reportIdSet) {
        for (Object row : sqlResult) {
            Map item = (Map) row;
            reportIdSet.add(item.get("reportid").toString());
        }
    }
}
