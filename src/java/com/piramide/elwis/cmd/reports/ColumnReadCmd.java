package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.domain.reportmanager.Column;
import com.piramide.elwis.domain.reportmanager.Report;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
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
 * @version $Id: ColumnReadCmd.java 7936 2007-10-27 16:08:39Z fernando $
 */

public class ColumnReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ColumnReadCmd................" + paramDTO);

        Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());
        List columnList = new ArrayList();

        Report report;
        try {
            report = (Report) EJBFactory.i.callFinder(new ReportDTO(), "findByPrimaryKey", new Object[]{reportId});
        } catch (Exception e) {
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.setForward("FailReport");
            return;
        }

        if (report != null) {
            //read columns
            Collection columns = report.getColumns();
            Collection orderedColumns = orderBySequence(columns.toArray());

            for (Iterator iterator = orderedColumns.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                Map map = new HashMap();
                StringBuffer strValue = new StringBuffer();
                strValue.append(ReportConstants.KEY_COLUMNSEPARATOR_ID + column.getColumnId() + ReportConstants.KEY_COLUMNSEPARATOR_ID);
                strValue.append(ReportConstants.KEY_COLUMNSEPARATOR_COLUMNREF + column.getColumnReference() + ReportConstants.KEY_COLUMNSEPARATOR_COLUMNREF);
                strValue.append(ReportConstants.KEY_COLUMNSEPARATOR_PATH + new String(column.getPath()) + ReportConstants.KEY_COLUMNSEPARATOR_PATH);
                //strValue.append(ReportConstants.KEY_COLUMNSEPARATOR_SEQUENCE + column.getSequence() + ReportConstants.KEY_COLUMNSEPARATOR_SEQUENCE);
                strValue.append(ReportConstants.KEY_COLUMNSEPARATOR_TABLEREF + column.getTableReference() + ReportConstants.KEY_COLUMNSEPARATOR_TABLEREF);
                strValue.append(ReportConstants.KEY_COLUMNSEPARATOR_TYPE + column.getColumnType() + ReportConstants.KEY_COLUMNSEPARATOR_TYPE);
                strValue.append(ReportConstants.KEY_COLUMNSEPARATOR_VERSION + column.getVersion() + ReportConstants.KEY_COLUMNSEPARATOR_VERSION);
                if (column.getIsTotalizer().booleanValue()) {
                    strValue.append(ReportConstants.KEY_COLUMNSEPARATOR_ISTOTALIZER + column.getIsTotalizer().booleanValue() + ReportConstants.KEY_COLUMNSEPARATOR_ISTOTALIZER);
                }

                if (column.getLabel() != null) {
                    //add in value
                    strValue.append(ReportConstants.KEY_COLUMNSEPARATOR_LABEL + column.getLabel() + ReportConstants.KEY_COLUMNSEPARATOR_LABEL);
                    map.put("label", column.getLabel());
                }

                if (column.getColumnOrder() != null) {
                    strValue.append(ReportConstants.KEY_COLUMNSEPARATOR_ORDER + column.getColumnOrder().booleanValue() + ReportConstants.KEY_COLUMNSEPARATOR_ORDER);
                    map.put("columnOrder", column.getColumnOrder());
                }

                map.put("value", strValue);
                map.put("table", column.getTableReference());
                map.put("field", column.getColumnReference());
                map.put("path", new String(column.getPath()));
                columnList.add(map);
            }

            //read init table
            resultDTO.put("initialTableReference", report.getInitialTableReference());
        }
        resultDTO.put("columnList", columnList);
    }

    /**
     * order Column objects by sequence
     *
     * @param arrayObj Column array
     * @return Collection columns ordered
     */
    private Collection orderBySequence(Object[] arrayObj) {
        Collection res;
        for (int i = 0; i < arrayObj.length - 1; i++) {
            for (int j = i + 1; j < arrayObj.length; j++) {
                Column column1 = (Column) arrayObj[i];
                Column column2 = (Column) arrayObj[j];
                if (column2.getSequence().intValue() < column1.getSequence().intValue()) {
                    Object aux = arrayObj[i];
                    arrayObj[i] = arrayObj[j];
                    arrayObj[j] = aux;
                }
            }
        }
        res = Arrays.asList(arrayObj);

        return res;
    }
}
