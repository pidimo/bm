package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.ReportQueryParam;
import com.piramide.elwis.dto.reports.ReportQueryParamDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class ReportQueryParamUpdateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("ReportQueryParamUpdateCmd executing......................... " + paramDTO);

        ReportQueryParamDTO dto = getReportQueryParamDTO();
        ReportQueryParam reportQueryParam = (ReportQueryParam) ExtendedCRUDDirector.i.update(dto, resultDTO, false, false, false, "Fail");

        if (reportQueryParam != null && !resultDTO.isFailure()) {

            Integer filterId = getParamDtoFilterId();
            if (filterId == null && paramDTO.containsKey("filterId")) {
                paramDTO.remove("filterId");
            }

            FilterCmd filterCmd = new FilterCmd();
            filterCmd.putParam(paramDTO);
            filterCmd.setOp(filterId != null ? "update" : "create");
            filterCmd.executeInStateless(ctx);

            //assign filter to query param
            reportQueryParam.setFilterId((Integer) filterCmd.getResultDTO().get("filterId"));

            resultDTO.putAll(filterCmd.getResultDTO());
        }
    }

    private Integer getParamDtoFilterId() {
        Integer filterId = null;
        if (null != paramDTO.get("filterId") && !"".equals(paramDTO.get("filterId").toString().trim())) {
            filterId = new Integer(paramDTO.get("filterId").toString());
        }
        return filterId;
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
