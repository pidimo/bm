package com.piramide.elwis.dto.reports;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10.1
 */
public class ReportArtifactDTO extends ComponentDTO implements DuplicatedEntryDTO {
    public static final String KEY_REPORTARTIFACTID = "artifactId";

    public ReportArtifactDTO() {
    }

    public ReportArtifactDTO(DTO dto) {
        super.putAll(dto);
    }

    public ReportArtifactDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new ReportArtifactDTO();
    }

    public String getJNDIName() {
        return ReportConstants.JNDI_REPORTARTIFACT;
    }

    public String getPrimKeyName() {
        return KEY_REPORTARTIFACTID;
    }

    public String getTableName() {
        return ReportConstants.TABLE_REPORTARTIFACT;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("fileName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("fileName"));
    }

    @Override
    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("fileName"));
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("fileName", "filename");
        values.put("reportId", "reportid");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_REPORTARTIFACTID, "artifactid");
        return values;
    }
}
