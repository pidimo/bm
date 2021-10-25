package com.piramide.elwis.dto.projects;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.ReferentialPK;
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
public class ProjectAssigneeDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String PROJECT_ASSIGNEE_PK = "projectAssigneePK";

    public ProjectAssigneeDTO() {
    }

    public ProjectAssigneeDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new ProjectAssigneeDTO();
    }

    public String getJNDIName() {
        return ProjectConstants.JNDI_PROJECT_ASSIGNEE;
    }

    public String getPrimKeyName() {
        return PROJECT_ASSIGNEE_PK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap(0);
        tables.put(ProjectConstants.TABLE_PROJECT_TIME,
                ReferentialPK.create()
                        .addKey("addressId", "assigneeid")
                        .addKey("projectId", "projectid"));
        return tables;
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
