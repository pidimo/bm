package com.piramide.elwis.dto.reports;

import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author : ivan
 * @version : $Id ColumnDTO ${time}
 */
public class ColumnDTO extends ComponentDTO {
    public static final String KEY_COLUMNID = "columnId";

    public ColumnDTO() {
    }

    public ColumnDTO(DTO dto) {
        super.putAll(dto);
    }

    public ColumnDTO(Integer columnId) {
        setPrimKey(columnId);
    }

    public ComponentDTO createDTO() {
        return new ColumnDTO();
    }

    public String getJNDIName() {
        return ReportConstants.JNDI_COLUMN;
    }

    public String getPrimKeyName() {
        return KEY_COLUMNID;
    }

    public String getTableName() {
        return ReportConstants.TABLE_COLUMN;
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
