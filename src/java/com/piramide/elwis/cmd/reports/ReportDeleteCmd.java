package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.*;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import com.piramide.elwis.dto.reports.FilterValueDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: ReportDeleteCmd.java 10338 2013-03-28 01:00:43Z miguel $
 */
public class ReportDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());


    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReportDeleteCmd.......");
        Object reportKey = paramDTO.get(ReportDTO.KEY_REPORTID);
        if (reportKey != null) {
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.putAll(paramDTO);
            Report report = (Report) ExtendedCRUDDirector.i.read(reportDTO, resultDTO, false);

            if (resultDTO.isFailure()) {
                resultDTO.setForward("Fail");
            }

            if (null != report) {
                //remove description text for report
                if (null != report.getDescriptionText()) {
                    try {
                        report.getDescriptionText().remove();
                    } catch (RemoveException e) {
                    }
                }

                //remove jrxml file for report
                if (null != report.getJrxmlFile()) {
                    try {
                        report.getJrxmlFile().remove();
                    } catch (RemoveException e) {
                    }
                }

                //remove queryparams
                deleteReportQueryParams(report);
                //remove artifacts
                deleteReportArtifacts(report, ctx);

                //remove chart
                if (report.getCharts() != null) {
                    try {
                        report.getCharts().remove();
                    } catch (RemoveException e) {
                        log.debug("Error in chart delete.....");
                    }
                }

                //remove filter values if exist
                Collection filters = report.getFilters();
                if (!filters.isEmpty()) {
                    for (Iterator iterator = filters.iterator(); iterator.hasNext();) {
                        Filter filter = (Filter) iterator.next();
                        //remove values
                        Collection values = filter.getFilterValues();
                        if (!values.isEmpty()) {
                            Object[] objArray = values.toArray();
                            for (int i = 0; i < objArray.length; i++) {
                                FilterValue filterValue = (FilterValue) objArray[i];
                                ExtendedCRUDDirector.i.delete(new FilterValueDTO(filterValue.getFilterValueId()), resultDTO, false, null);
                            }
                        }
                    }
                }

                //remove reportRoles if exists
                deleteReportRoles(report.getReportId(), report.getCompanyId());

                //remove report
                try {
                    report.remove();
                } catch (RemoveException e) {
                    ctx.setRollbackOnly();
                }
            }
        }
    }

    private void deleteReportRoles(Integer reportId, Integer companyId) {
        ReportRoleHome home = (ReportRoleHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORTROLE);
        try {
            Collection reportRoles = home.findByReportId(reportId, companyId);
            if (null != reportRoles) {
                List deleteElements = new ArrayList(reportRoles);
                for (Object object : deleteElements) {
                    ReportRole reportRole = (ReportRole) object;
                    try {
                        reportRole.remove();
                    } catch (RemoveException e) {
                        log.debug("Cannot delete reportRole with reportId=" + reportId +
                                " and roleId=" + reportRole.getRoleId());
                    }
                }
            }

        } catch (FinderException e) {
            log.debug("Report cannot assignded report roles");
        }
    }

    private void deleteReportQueryParams(Report report) {
        //first delete previous
        Collection queryParams = new ArrayList(report.getReportQueryParams());
        for (Iterator it = queryParams.iterator(); it.hasNext();) {
            ReportQueryParam reportQueryParam = (ReportQueryParam) it.next();
            try {
                reportQueryParam.remove();
            } catch (RemoveException e) {
                log.error("Cannot remove reportQueryParam assigned to reportId = " +
                        report.getReportId() + " caused by ", e);
            }
        }
    }

    private void deleteReportArtifacts(Report report, SessionContext ctx) {
        Collection artifacts = new ArrayList(report.getReportArtifacts());
        for (Iterator it = artifacts.iterator(); it.hasNext();) {
            ReportArtifact reportArtifact = (ReportArtifact) it.next();

            ReportArtifactCmd artifactCmd = new ReportArtifactCmd();
            artifactCmd.setOp("delete");
            artifactCmd.putParam("artifactId", reportArtifact.getArtifactId());
            artifactCmd.executeInStateless(ctx);
        }
    }

    public boolean isStateful() {
        return false;
    }

}