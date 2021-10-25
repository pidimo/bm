package com.piramide.elwis.dto.projects;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectActivityDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String PROJECTACTIVITYPK = "activityId";

    public ProjectActivityDTO() {

    }

    public ProjectActivityDTO(DTO dto) {
        super.putAll(dto);
    }

    public ProjectActivityDTO(Integer activityId) {
        setPrimKey(activityId);
    }

    public ComponentDTO createDTO() {
        return new ProjectActivityDTO();
    }

    public String getJNDIName() {
        return ProjectConstants.JNDI_PROJECT_ACTIVITY;
    }

    public String getPrimKeyName() {
        return PROJECTACTIVITYPK;
    }

    public HashMap referencedValues() {
        HashMap map = new HashMap();
        map.put(ProjectConstants.TABLE_PROJECT_TIME, "projectactivityid");
        return map;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        if (null != get("name")) {
            resultDTO.addResultMessage("customMsg.Referenced", get("name"));
        } else {
            resultDTO.addResultMessage("msg.Referenced");
        }
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (null != get("name")) {
            resultDTO.addResultMessage("msg.NotFound", get("name"));
        } else {
            resultDTO.addResultMessage("generalMsg.NotFound");
        }
    }
}
