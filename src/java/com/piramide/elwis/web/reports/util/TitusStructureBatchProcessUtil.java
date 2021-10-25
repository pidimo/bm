package com.piramide.elwis.web.reports.util;

import com.piramide.elwis.cmd.reports.TitusStructureRemoveTableBatchCmd;
import com.piramide.elwis.cmd.reports.TitusStructureUpdateBatchCmd;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * Util to execute batch process related to titus configuration, this can be remove fields or remove tables.
 * this should be instanced from BatchProcessServlet.java
 *
 * @author Miky
 * @version $Id: TitusStructureBatchProcessUtil.java 2009-09-17 04:55:41 PM $
 */
public class TitusStructureBatchProcessUtil {
    private Log log = LogFactory.getLog(this.getClass());
    private FantabulousManager fantabulousManager;

    public TitusStructureBatchProcessUtil(ServletContext servletContext) {
        fantabulousManager = FantabulousManager.loadFantabulousManager(servletContext, "/reports");
        if (fantabulousManager == null) {
            throw new RuntimeException("Fantabulos manager canot be loaded...");
        }
    }

    /**
     * fix all reports related with removed fields
     * @param removedFieldList removed fields, List<Map> list of Map with key {table,column}
     */
    public void fixRemovedColumns(List<Map> removedFieldList) {
        log.debug("Executing fixRemovedColumns..." + removedFieldList);

        Set<Integer> columnIdsSet = new HashSet<Integer>();
        Set<Integer> filterIdsSet = new HashSet<Integer>();

        for (Map fieldInfoMap : removedFieldList) {
            String tableRef = (String) fieldInfoMap.get("table");
            String columnRef = (String) fieldInfoMap.get("column");
            log.debug("In field:" + tableRef + "-" + columnRef);

            columnIdsSet.addAll(readReportColumnIds(tableRef, columnRef));
            filterIdsSet.addAll(readReportFilterIds(tableRef, columnRef));
        }
        log.debug("All column ids:" + columnIdsSet);
        log.debug("ALL filter ids:" + filterIdsSet);


        TitusStructureUpdateBatchCmd updaterCmd = new TitusStructureUpdateBatchCmd();
        updaterCmd.putParam(TitusStructureUpdateBatchCmd.COLUMNSIDLIST, new ArrayList<Integer>(columnIdsSet));
        updaterCmd.putParam(TitusStructureUpdateBatchCmd.FILTERSIDLIST, new ArrayList<Integer>(filterIdsSet));

        try {
            BusinessDelegate.i.execute(updaterCmd, null);
        } catch (AppLevelException e) {
            log.error("FAil execute bussines delegate", e);
        }
    }

    /**
     * fix all reports related with removed tables
     * @param removedTableList List<String> removed tables
     */
    public void fixRemovedTables(List<String> removedTableList) {
        log.debug("Executing fixRemovedTables...." + removedTableList);

        TitusStructureRemoveTableBatchCmd removeTableBatchCmd = new TitusStructureRemoveTableBatchCmd();
        removeTableBatchCmd.putParam(TitusStructureRemoveTableBatchCmd.REMOVETABLELIST, removedTableList);

        try {
            BusinessDelegate.i.execute(removeTableBatchCmd, null);
        } catch (AppLevelException e) {
            log.error("-> Execute " + TitusStructureRemoveTableBatchCmd.class.getName() + " FAIL", e);
        }
    }

    /**
     * Get all report column ids related to removed columns. is here executed fantabulous list
     * @param tableReference
     * @param columnReference
     * @return Set of column ids
     */
    private Set<Integer> readReportColumnIds(String tableReference, String columnReference) {
        log.debug("Read column IDS........");
        Set<Integer> columnIds = new HashSet<Integer>();
        String listName = "columnRemoveBatchList";

        //parameters to execute list of manual form
        Parameters parameters = new Parameters();
        parameters.addSearchParameter("tablefield", tableReference + "%" + columnReference);

        //Execute the list
        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
        } catch (ListStructureNotFoundException e) {
            throw new RuntimeException("Read List " + listName + " In Fantabulous structure Fail", e);
        }

        if (list != null) {
            Collection result = Controller.getList(list, parameters);
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) iterator.next();
                //get column defined in fantabulous list
                columnIds.add(Integer.valueOf(fieldHash.get("reportColumnId").toString()));
            }
        }
        log.debug("Column ids...........:" + columnIds);
        return columnIds;
    }

    /**
     * Get all report filter ids related to removed columns. is here executed fantabulous list
     * @param tableReference
     * @param columnReference
     * @return Set of report filter ids
     */
    private Set<Integer> readReportFilterIds(String tableReference, String columnReference) {
        log.debug("Read Filter IDS........");
        Set<Integer> filterIds = new HashSet<Integer>();
        String listName = "filterRemoveBatchList";

        //parameters to execute list of manual form
        Parameters parameters = new Parameters();
        parameters.addSearchParameter("tablefield", tableReference + "%" + columnReference);

        //Execute the list
        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
        } catch (ListStructureNotFoundException e) {
            throw new RuntimeException("Read List " + listName + " In Fantabulous structure Fail", e);
        }

        if (list != null) {
            Collection result = Controller.getList(list, parameters);
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) iterator.next();
                //get column defined in fantabulous list
                filterIds.add(Integer.valueOf(fieldHash.get("reportFilterId").toString()));
            }
        }
        log.debug("Filter ids...........:" + filterIds);
        return filterIds;
    }

}
