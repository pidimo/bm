package com.piramide.elwis.dto.projects;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Project entity DTO
 *
 * @author Fernando Montao
 * @version $Id: ProjectDTO.java 2009-02-21 15:15:11 $
 */
public class ProjectDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String PROJECT_ID = "projectId";

    public ProjectDTO() {
    }

    public ProjectDTO(DTO dto) {
        super(dto);
    }

    public String getPrimKeyName() {
        return PROJECT_ID;
    }

    public String getJNDIName() {
        return ProjectConstants.JNDI_PROJECT;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (get("name") != null) {
            resultDTO.addResultMessage("msg.NotFound", get("name"));
        } else {
            resultDTO.addResultMessage("Project.NotFound");
        }
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

    public ComponentDTO createDTO() {
        return new ProjectDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        return new HashMap(0);
    }

}
