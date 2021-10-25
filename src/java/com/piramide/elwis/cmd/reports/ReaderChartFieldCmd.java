package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.*;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import com.piramide.elwis.dto.reports.ColumnDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ReaderChartFieldCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ReaderChartFieldCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReaderChartFieldCmd................" + paramDTO);
        Integer reportId = new Integer(paramDTO.get("reportId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        ColumnHome columnHome = (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);
        List totalizeList = new ArrayList();
        List columnList = new ArrayList();

        Report report = (Report) ExtendedCRUDDirector.i.read(new ReportDTO(reportId), resultDTO, false);

        if (report != null) {
            //column totalizers
            Collection totalizers = report.getTotalizers();
            for (Iterator iterator = totalizers.iterator(); iterator.hasNext();) {
                Totalize totalize = (Totalize) iterator.next();

                DTO totalizatorDTO = new DTO();
                totalizatorDTO.put("totalizeId", totalize.getTotalizeId());
                totalizatorDTO.put("totalizeType", totalize.getTotalizeType());
                if (totalize.getName() != null) {
                    totalizatorDTO.put("name", totalize.getName());
                }

                if (!ReportConstants.TOTALIZER_TYPE_CUSTOM.equals(totalize.getTotalizeType()) &&
                        !ReportConstants.TOTALIZER_TYPE_SUMRECORDS.equals(totalize.getTotalizeType())) {
                    Collection columnTotalizers = totalize.getColumnTotalizers();
                    for (Iterator iterator2 = columnTotalizers.iterator(); iterator2.hasNext();) {
                        ColumnTotalize columnTotalize = (ColumnTotalize) iterator2.next();
                        Column column = (Column) ExtendedCRUDDirector.i.read(new ColumnDTO(columnTotalize.getColumnId()), resultDTO, false);
                        if (column != null) {
                            totalizatorDTO.put("path", new String(column.getPath()));
                        }
                    }
                }
                totalizeList.add(totalizatorDTO);
            }

            //column groups
            Collection columns = null;
            try {
                columns = columnHome.findByReportIdOnlyGroupColumns(reportId, companyId);
            } catch (FinderException e) {
                log.debug("... collection columns .. not found ... !!!");
            }

            for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                if (column.getColumnGroup() != null) {
                    ColumnDTO columnDTO = new ColumnDTO();
                    DTOFactory.i.copyToDTO(column, columnDTO);
                    columnDTO.put("path", new String(column.getPath()));
                    columnDTO.put("columnGroupId", column.getColumnGroup().getColumnGroupId());

                    columnList.add(columnDTO);
                }
            }
        }

        resultDTO.put("totalizeList", totalizeList);
        resultDTO.put("columnList", columnList);
    }

    public boolean isStateful() {
        return false;
    }
}
