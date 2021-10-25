package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.Column;
import com.piramide.elwis.domain.reportmanager.Report;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 * Cmd to read number of column groups defined in an report
 *
 * @author Miky
 * @version $Id: ReportColumnGroupCountCmd.java, v1.0 17-jun-2008 16:04:37 Miky Exp $
 */
public class ReportColumnGroupCountCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReportColumnGroupCountCmd................" + paramDTO);

        Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());

        Report report = (Report) ExtendedCRUDDirector.i.read(new ReportDTO(reportId), resultDTO, false);
        if (report != null) {
            int count = 0;
            for (Iterator iterator = report.getColumns().iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                if (column.getColumnGroup() != null) {
                    count++;
                }
            }
            resultDTO.put("columnGroupsSize", count);
        }
    }
}
