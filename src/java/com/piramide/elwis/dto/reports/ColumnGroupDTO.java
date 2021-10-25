package com.piramide.elwis.dto.reports;

import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author : ivan
 * @version : $Id ColumnGroupDTO ${time}
 */
public class ColumnGroupDTO extends ComponentDTO {
    public static final String KEY_COLUMNGROUPID = "columnGroupId";

    public ColumnGroupDTO() {
    }

    public ColumnGroupDTO(DTO dto) {
        super.putAll(dto);
    }

    public ColumnGroupDTO(Integer columnId) {
        setPrimKey(columnId);
    }

    public ComponentDTO createDTO() {
        return new ColumnGroupDTO();
    }

    public String getJNDIName() {
        return ReportConstants.JNDI_COLUMNGROUP;
    }

    public String getPrimKeyName() {
        return KEY_COLUMNGROUPID;
    }

    public String getTableName() {
        return ReportConstants.TABLE_COLUMNGROUP;
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
