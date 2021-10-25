package com.piramide.elwis.dto.reports;

import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class ReportQueryParamDTO extends ComponentDTO {
    public static final String KEY_REPORTQUERYPARAMID = "reportQueryParamId";

    public ReportQueryParamDTO() {
    }

    public ReportQueryParamDTO(DTO dto) {
        super.putAll(dto);
    }

    public ReportQueryParamDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new ReportQueryParamDTO();
    }

    public String getJNDIName() {
        return ReportConstants.JNDI_REPORTQUERYPARAM;
    }

    public String getPrimKeyName() {
        return KEY_REPORTQUERYPARAMID;
    }

    public String getTableName() {
        return ReportConstants.TABLE_REPORTQUERYPARAM;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("parameterName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("parameterName"));
    }
}
