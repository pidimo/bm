package com.piramide.elwis.dto.reports;

import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author : ivan
 * @version : $Id TotalizeDTO ${time}
 */
public class TotalizeDTO extends ComponentDTO {
    public static final String KEY_TOTALIZEID = "totalizeId";

    public TotalizeDTO() {
    }

    public TotalizeDTO(DTO dto) {
        super.putAll(dto);
    }

    public TotalizeDTO(Integer columnId) {
        setPrimKey(columnId);
    }

    public ComponentDTO createDTO() {
        return new TotalizeDTO();
    }

    public String getJNDIName() {
        return ReportConstants.JNDI_TOTALIZE;
    }

    public String getPrimKeyName() {
        return KEY_TOTALIZEID;
    }

    public String getTableName() {
        return ReportConstants.TABLE_TOTALIZE;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {

    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("Common.DuplicatedEntry.message");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("generalMsg.NotFound");
    }
}
