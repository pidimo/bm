package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.domain.reportmanager.*;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import com.piramide.elwis.dto.reports.FilterValueDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.FreeTextTypes;
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
 * @version $Id: ReportUpdateCmd.java 10338 2013-03-28 01:00:43Z miguel $
 */
public class ReportUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReportUpdateCmd......." + paramDTO);


        Integer reportId = new Integer((String) paramDTO.get("reportId"));

        ReportHome reportHome = (ReportHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORT);

        boolean haveChanges = false;

        Integer actualCompanyId = null;
        try {

            Report actualReport = reportHome.findByPrimaryKey(reportId);

            Integer actualRerpotType = actualReport.getReportType();
            String actualModule = actualReport.getModule();
            String actualInitialTableReference = actualReport.getInitialTableReference();
            actualCompanyId = actualReport.getCompanyId();

            if (!actualRerpotType.equals(new Integer((String) paramDTO.get("reportType"))) ||
                    !actualModule.equals(paramDTO.get("module")) ||
                    !actualInitialTableReference.equals(paramDTO.get("initialTableReference")))

            {
                haveChanges = true;
            }

        } catch (FinderException e) {
            ReportReadCmd cmd = new ReportReadCmd();
            cmd.putParam(paramDTO);
            cmd.executeInStateless(ctx);
            resultDTO.putAll(cmd.getResultDTO());
        }


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

        Report report = (Report) ExtendedCRUDDirector.i.update(reportDTO, resultDTO, false, true, false, "Fail");

        if (report != null) {
            if (descriptionText != null && descriptionText.toString().length() > 0) {
                FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, report, "DescriptionText", ReportFreeTextHome.class,
                        ReportConstants.JNDI_REPORTFREETEXT, FreeTextTypes.FREETEXT_REPORT, "descriptionText");
            }

            if (ReportConstants.SourceType.JRXML.equal(report.getSourceType())) {
                boolean isJrxmlUpdate = updateJrxmlFile(report, ctx);
                boolean isCompanyChanged = isCompanyChanged(actualCompanyId);
                if (isJrxmlUpdate || isCompanyChanged) {
                    haveChanges = true;
                    paramDTO.put("haveChangeColumn", true);
                }

                if (isCompanyChanged) {
                    setChangedCompanyInQueryParam(report);
                    setChangedCompanyInArtifact(report);
                }
            }
        }

        if (!resultDTO.isFailure() && haveChanges) {

            if (null != report && null != paramDTO.get("haveChangeType") &&
                    true == Boolean.valueOf(paramDTO.get("haveChangeType").toString()).booleanValue()) {
                removeColumnGroupAndChart(report);
            }

            if (null != report && null != paramDTO.get("haveChangeColumn") &&
                    true == Boolean.valueOf(paramDTO.get("haveChangeColumn").toString()).booleanValue()) {

                removeColumnGroupAndChart(report);

                Collection columns = new ArrayList(report.getColumns());
                for (Iterator it = columns.iterator(); it.hasNext();) {
                    Column column = (Column) it.next();

                    if (null != column.getColumnTotalizers()) {
                        Collection columnTotalizers = new ArrayList(column.getColumnTotalizers());
                        for (Iterator itt = columnTotalizers.iterator(); itt.hasNext();) {
                            ColumnTotalize columnTotalize = (ColumnTotalize) itt.next();

                            try {
                                columnTotalize.remove();
                            } catch (RemoveException e) {
                                log.error("Cannot remove columnTotalize assigned to columnId = " +
                                        column.getColumnId() + "caused by ", e);
                            }
                        }
                    }

                    try {
                        column.remove();

                    } catch (RemoveException e) {
                        log.error("Cannot remove column assigned to reportId = " +
                                report.getReportId() + " caused by ", e);
                    }
                }

                Collection myTotalizers = new ArrayList(report.getTotalizers());

                for (Iterator it = myTotalizers.iterator(); it.hasNext();) {
                    Totalize totalize = (Totalize) it.next();
                    try {

                        totalize.remove();
                    } catch (RemoveException e) {
                        log.error("Cannot remove totalize assigned to reportId = " +
                                report.getReportId() + " caused by ", e);
                    }
                }

                setNullFilterQueryParam(report);
                Collection myFilters = new ArrayList(report.getFilters());

                for (Iterator it = myFilters.iterator(); it.hasNext();) {
                    Filter filter = (Filter) it.next();

                    //remove values
                    Collection values = filter.getFilterValues();
                    if (!values.isEmpty()) {
                        Object[] objArray = values.toArray();
                        for (int i = 0; i < objArray.length; i++) {
                            FilterValue filterValue = (FilterValue) objArray[i];
                            ExtendedCRUDDirector.i.delete(new FilterValueDTO(filterValue.getFilterValueId()), resultDTO, false, null);
                        }
                    }

                    try {
                        filter.remove();
                    } catch (RemoveException e) {
                        log.error("Cannot remove filter to reportId = " +
                                report.getReportId() + " caused by ", e);
                    }
                }

            }
        } else {

            ReportReadCmd cmd = new ReportReadCmd();
            cmd.putParam(paramDTO);
            cmd.executeInStateless(ctx);
            resultDTO.putAll(cmd.getResultDTO());
        }

    }

    private boolean removeColumnGroupAndChart(Report report) {

        boolean result = true;

        Chart chart = report.getCharts();


        if (null != chart)

        {
            try {
                chart.remove();
            } catch (RemoveException e) {
                result = false;
            }
        }


        Collection columns = new ArrayList(report.getColumns());
        for (Iterator it = columns.iterator(); it.hasNext();) {
            Column column = (Column) it.next();
            try {
                if (null != column.getColumnGroup()) {
                    column.getColumnGroup().remove();
                }
            } catch (RemoveException e) {
                result = false;
            }
        }

        return result;
    }

    private boolean isCompanyChanged(Integer actualCompanyId) {
        return paramDTO.get("companyId") != null && !Integer.valueOf(paramDTO.get("companyId").toString()).equals(actualCompanyId);
    }

    private boolean updateJrxmlFile(Report report, SessionContext ctx) {
        boolean isUpdated = false;
        ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("fileWrapper");

        if (wrapper != null) {
            report.getJrxmlFile().setValue(wrapper.getFileData());
            report.setJrxmlFileName(wrapper.getFileName());

            createReportQueryParams(report, ctx);

            isUpdated = true;
        }
        return isUpdated;
    }

    private void setNullFilterQueryParam(Report report) {
        for (Iterator iterator = report.getReportQueryParams().iterator(); iterator.hasNext();) {
            ReportQueryParam reportQueryParam = (ReportQueryParam) iterator.next();
            reportQueryParam.setFilterId(null);
        }
    }

    private void setChangedCompanyInQueryParam(Report report) {
        for (Iterator iterator = report.getReportQueryParams().iterator(); iterator.hasNext();) {
            ReportQueryParam reportQueryParam = (ReportQueryParam) iterator.next();
            reportQueryParam.setCompanyId(report.getCompanyId());
        }
    }

    private void setChangedCompanyInArtifact(Report report) {
        for (Iterator iterator = report.getReportArtifacts().iterator(); iterator.hasNext();) {
            ReportArtifact reportArtifact = (ReportArtifact) iterator.next();
            reportArtifact.setCompanyId(report.getCompanyId());
        }
    }

    private void createReportQueryParams(Report report, SessionContext ctx) {
        List queryParameters = (List) paramDTO.get("queryParameterList");

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