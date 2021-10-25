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
 * cmd to read report configuration
 *
 * @author Miky
 * @version $Id: ReportExecuteReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ReportExecuteReadCmd extends EJBCommand {
    Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("Executing ReportExecuteReadCmd................" + paramDTO);

        Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.put("reportId", reportId);
        Report report = (Report) ExtendedCRUDDirector.i.read(reportDTO, resultDTO, false);
        if (resultDTO.isFailure()) {
            resultDTO.addResultMessage("Report.NotFound");
            resultDTO.setForward("MainSearch");
        }

    }

    public boolean isStateful() {
        return false;
    }
}
