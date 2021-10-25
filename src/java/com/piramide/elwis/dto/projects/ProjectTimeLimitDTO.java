package com.piramide.elwis.dto.projects;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class ProjectTimeLimitDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String PROJECTTIMELIMITPK = "timeLimitId";

    public ProjectTimeLimitDTO() {
    }

    public ProjectTimeLimitDTO(DTO dto) {
        super.putAll(dto);
    }

    public ProjectTimeLimitDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new ProjectTimeLimitDTO();
    }

    public String getJNDIName() {
        return ProjectConstants.JNDI_PROJECT_TIME_LIMIT;
    }

    public String getPrimKeyName() {
        return PROJECTTIMELIMITPK;
    }

    public String getTableName() {
        return ProjectConstants.TABLE_PROJECT_TIME_LIMIT;
    }

    public HashMap referencedValues() {
        return new HashMap();
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Referenced");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("generalMsg.NotFound");
    }
}
