package com.piramide.elwis.dto.reportmanager;

import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: ReportDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ReportDTO extends ComponentDTO {

    public static final String KEY_REPORTID = "reportId";

    public ReportDTO() {
    }

    public ReportDTO(DTO dto) {
        super.putAll(dto);
    }

    public ReportDTO(Integer reportId) {
        setPrimKey(reportId);
    }

    public String getPrimKeyName() {
        return KEY_REPORTID;
    }


    public String getJNDIName() {
        return ReportConstants.JNDI_REPORT;
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
        resultDTO.addResultMessage("msg.Duplicated", get("name"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("name"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

    public ComponentDTO createDTO() {
        return new com.piramide.elwis.dto.reportmanager.ReportDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ReportConstants.TABLE_REPORT, "reportid");
        return tables;
    }
}
