package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.Report;
import com.piramide.elwis.domain.reportmanager.ReportFreeText;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: ReportReadCmd.java 10326 2013-03-15 16:07:03Z miguel $
 */
public class ReportReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReportReadCmd......." + paramDTO);
        Object reportKey = paramDTO.get(ReportDTO.KEY_REPORTID);
        ReportFreeText reportFreeText = null;
        if (reportKey != null) {
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.putAll(paramDTO);
            Report reportBean = (Report) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, reportDTO, resultDTO);
            if (reportBean != null) {
                reportFreeText = reportBean.getDescriptionText();

                if (reportBean.getState().booleanValue()) {
                    resultDTO.put("state", ReportConstants.REPORT_STATUS_READY.toString());
                }
                if (!reportBean.getState().booleanValue()) {
                    resultDTO.put("state", ReportConstants.REPORT_STATUS_PREPARATION.toString());
                }

                if (reportBean.getJrxmlFile() != null) {
                    ArrayByteWrapper wrapper = new ArrayByteWrapper();
                    wrapper.setFileName(reportBean.getJrxmlFileName());
                    wrapper.setFileData(reportBean.getJrxmlFile().getValue());
                    resultDTO.put("fileWrapper", wrapper);
                }
            }
        }
        if (reportFreeText != null) {
            resultDTO.put("descriptionText", new String(reportFreeText.getValue()));
        }
    }
}
