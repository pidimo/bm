package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.Report;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * Cmd to read number of column defined in an report
 *
 * @author Miky
 * @version $Id: ReportColumnCountCmd.java, v1.0 16-jun-2008 16:27:46 Miky Exp $
 */
public class ReportColumnCountCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReportColumnCountCmd................" + paramDTO);

        Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());

        Report report = (Report) ExtendedCRUDDirector.i.read(new ReportDTO(reportId), resultDTO, false);
        if (report != null) {
            resultDTO.put("columnsSize", report.getColumns().size());
        }
    }
}
