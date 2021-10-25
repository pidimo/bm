package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.domain.reportmanager.Report;
import com.piramide.elwis.domain.reportmanager.ReportFreeTextHome;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: ReportCreateCmd.java 10326 2013-03-15 16:07:03Z miguel $
 */
public class ReportCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReportCreateCmd.......");
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.putAll(paramDTO);

        Object state = paramDTO.get("state");
        Object descriptionText = paramDTO.get("descriptionText");

        reportDTO.put("state", new Boolean(false));
        if (state != null && state.toString().equals(ReportConstants.REPORT_STATUS_READY.toString())) {
            reportDTO.put("state", new Boolean(true));
        }

        if (descriptionText != null && descriptionText.toString().length() > 0) {
            reportDTO.remove("descriptionText");
        }

        Report report = (Report) ExtendedCRUDDirector.i.create(reportDTO, resultDTO, false);

        if (report != null) {
            if (descriptionText != null && descriptionText.toString().length() > 0) {
                FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, report, "DescriptionText", ReportFreeTextHome.class,
                        ReportConstants.JNDI_REPORTFREETEXT, FreeTextTypes.FREETEXT_REPORT, "descriptionText");
            }

            if (ReportConstants.SourceType.JRXML.equal(report.getSourceType())) {
                processAsJrxmlSourceType(report, ctx);
            }
        }

    }

    private void processAsJrxmlSourceType(Report report, SessionContext ctx) {
        ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("fileWrapper");

        if (wrapper != null) {
            FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);

            try {
                FreeText freeText = freeTextHome.create(wrapper.getFileData(), report.getCompanyId(), FreeTextTypes.FREETEXT_REPORT);
                report.setJrxmlFileId(freeText.getFreeTextId());
                report.setJrxmlFileName(wrapper.getFileName());

                createReportQueryParams(report, ctx);
            } catch (CreateException e) {
                log.error("Can't create jrxml freetext....", e);
            }
        }
    }

    private void createReportQueryParams(Report report, SessionContext ctx) {
        List queryParameters = (List) paramDTO.get("queryParameterList");

        if (queryParameters != null) {
            for (int i = 0; i < queryParameters.size(); i++) {
                String parameterName = (String) queryParameters.get(i);
                ReportQueryParamCmd reportQueryParamCmd = new ReportQueryParamCmd();
                reportQueryParamCmd.setOp("create");
                reportQueryParamCmd.putParam("parameterName", parameterName);
                reportQueryParamCmd.putParam("companyId", report.getCompanyId());
                reportQueryParamCmd.putParam("reportId", report.getReportId());
                reportQueryParamCmd.executeInStateless(ctx);
            }
        }
    }
}