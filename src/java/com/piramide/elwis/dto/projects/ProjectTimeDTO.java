package com.piramide.elwis.dto.projects;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProjectTimeDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String PROJECTTIMEPK = "timeId";

    public ProjectTimeDTO() {
    }

    public ProjectTimeDTO(DTO dto) {
        super.putAll(dto);
    }

    public ProjectTimeDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new ProjectTimeDTO();
    }

    public String getJNDIName() {
        return ProjectConstants.JNDI_PROJECT_TIME;
    }

    public String getPrimKeyName() {
        return PROJECTTIMEPK;
    }

    public HashMap referencedValues() {
        return new HashMap();
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        if (null != get("userName")) {
            resultDTO.addResultMessage("customMsg.Referenced", get("userName"));
        } else {
            resultDTO.addResultMessage("msg.Referenced");
        }
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (null != get("userName")) {
            resultDTO.addResultMessage("msg.NotFound", get("userName"));
        } else {
            resultDTO.addResultMessage("generalMsg.NotFound");
        }
    }
}
