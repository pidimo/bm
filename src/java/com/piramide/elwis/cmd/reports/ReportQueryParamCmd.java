package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.*;
import com.piramide.elwis.dto.reports.ReportQueryParamDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class ReportQueryParamCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("ReportQueryParamCmd executing......................... " + paramDTO);

        boolean isRead = true;
        ReportQueryParamDTO dto = getReportQueryParamDTO();

        if ("create".equals(getOp())) {
            isRead = false;
            create(dto);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update(dto);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(dto);
        }
        if ("readAll".equals(getOp())) {
            isRead = false;
            readAll(dto, ctx);
        }
        if ("readReportParamValues".equals(getOp())) {
            isRead = false;
            readAllReportParamValues();
        }
        if ("queryParamFromFilter".equals(getOp())) {
            isRead = false;
            readReportQueryParamRelatedToFilter();
        }
        if ("isAllParamWithFilter".equals(getOp())) {
            isRead = false;
            isAllReportQueryParamsRelatedToFilter();
        }

        if (isRead) {
            boolean checkReferences = (null != paramDTO.get("withReferences") &&
                    "true".equals(paramDTO.get("withReferences")));
            read(dto, checkReferences);
        }
    }

    private void create(ReportQueryParamDTO dto) {
        ExtendedCRUDDirector.i.create(dto, resultDTO, false);
    }

    private void read(ReportQueryParamDTO dto, boolean checkReferences) {
        ExtendedCRUDDirector.i.read(dto, resultDTO, checkReferences);
    }

    private void readAll(ReportQueryParamDTO dto, SessionContext ctx) {
        ReportQueryParam reportQueryParam = (ReportQueryParam) ExtendedCRUDDirector.i.read(dto, resultDTO, false);

        if (reportQueryParam != null) {
            //read associated filter
            if (reportQueryParam.getFilterId() != null) {
                FilterCmd filterCmd = new FilterCmd();
                filterCmd.setOp("read");
                filterCmd.putParam("filterId", reportQueryParam.getFilterId());
                filterCmd.putParam("companyId", reportQueryParam.getCompanyId());
                filterCmd.executeInStateless(ctx);

                resultDTO.putAll(filterCmd.getResultDTO());
            }
        }
    }

    private void readAllReportParamValues() {
        Integer reportId = new Integer(paramDTO.get("reportId").toString());

        ReportHome reportHome = (ReportHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORT);
        Report report = null;
        try {
            report = reportHome.findByPrimaryKey(reportId);
        } catch (FinderException e) {
            log.debug("Error in find report.. " + reportId);
        }

        Map paramValues = new HashMap();
        if (report != null) {
            for (Iterator iterator = report.getReportQueryParams().iterator(); iterator.hasNext();) {
                ReportQueryParam reportQueryParam = (ReportQueryParam) iterator.next();
                paramValues.put(reportQueryParam.getParameterName(), getFilterValue(reportQueryParam.getFilterId()));
            }
        }

        resultDTO.put("queryParamValuesMap", paramValues);
    }

    private void isAllReportQueryParamsRelatedToFilter() {
        Integer reportId = new Integer(paramDTO.get("reportId").toString());

        ReportHome reportHome = (ReportHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORT);
        Report report = null;
        try {
            report = reportHome.findByPrimaryKey(reportId);
        } catch (FinderException e) {
            log.debug("Error in find report.. " + reportId);
        }

        boolean isAllrelatedToFilter = true;
        if (report != null) {
            for (Iterator iterator = report.getReportQueryParams().iterator(); iterator.hasNext();) {
                ReportQueryParam reportQueryParam = (ReportQueryParam) iterator.next();
                if (reportQueryParam.getFilterId() == null) {
                    isAllrelatedToFilter = false;
                    break;
                }
            }
        }

        resultDTO.put("isAllRelatedToFilter", Boolean.valueOf(isAllrelatedToFilter));
    }

    private void readReportQueryParamRelatedToFilter() {
        Integer filterId = new Integer(paramDTO.get("filterId").toString());

        FilterHome filterHome = (FilterHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_FILTER);

            Filter filter = null;
            try {
                filter = filterHome.findByPrimaryKey(filterId);
            } catch (FinderException e) {
                log.debug("Not found filter... " + filterId);
            }

        if (filter != null) {
            ReportQueryParam reportQueryParam = filter.getReportQueryParam();
            if (reportQueryParam != null) {
                ReportQueryParamDTO queryParamDTO = new ReportQueryParamDTO();
                DTOFactory.i.copyToDTO(reportQueryParam, queryParamDTO);

                resultDTO.put("reportQueryParamMap", queryParamDTO);
            }
        }
    }

    private String getFilterValue(Integer filterId) {
        String value = null;
        FilterHome filterHome = (FilterHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_FILTER);

        if (filterId != null) {
            Filter filter = null;
            try {
                filter = filterHome.findByPrimaryKey(filterId);
                List filterValues = new ArrayList(filter.getFilterValues());
                if (!filterValues.isEmpty()) {
                    FilterValue filterValue = (FilterValue) filterValues.get(0);
                    value = filterValue.getFilterValue();
                }
            } catch (FinderException e) {
                log.debug("Not found filter... " + filterId);
            }
        }
        return value;
    }

    private void update(ReportQueryParamDTO dto) {
        ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");
    }

    private void delete(ReportQueryParamDTO dto) {
        ExtendedCRUDDirector.i.delete(dto, resultDTO, true, "Fail");
    }

    private ReportQueryParamDTO getReportQueryParamDTO() {
        ReportQueryParamDTO queryParamDTO = new ReportQueryParamDTO();
        Integer reportQueryParamId = null;
        if (null != paramDTO.get("reportQueryParamId") &&
                !"".equals(paramDTO.get("reportQueryParamId").toString().trim())) {
            try {
                reportQueryParamId = Integer.valueOf(paramDTO.get("reportQueryParamId").toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse reportQueryParamId=" + paramDTO.get("reportQueryParamId") + " FAIL");
            }
        }

        queryParamDTO.put("reportQueryParamId", reportQueryParamId);
        queryParamDTO.put("parameterName", paramDTO.get("parameterName"));
        queryParamDTO.put("companyId", paramDTO.get("companyId"));
        queryParamDTO.put("reportId", paramDTO.get("reportId"));
        queryParamDTO.put("version", paramDTO.get("version"));
        queryParamDTO.put("withReferences", paramDTO.get("withReferences"));

        log.debug("-> Working on ReportQueryParamDTO=" + queryParamDTO + " OK");
        return queryParamDTO;
    }

    public boolean isStateful() {
        return false;
    }
}
