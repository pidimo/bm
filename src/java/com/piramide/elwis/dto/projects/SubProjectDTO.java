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
public class SubProjectDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String SUBPROJECTPK = "subProjectId";

    public SubProjectDTO() {
    }

    public SubProjectDTO(DTO dto) {
        super.putAll(dto);
    }

    public SubProjectDTO(Integer subProjectId) {
        setPrimKey(subProjectId);
    }

    public ComponentDTO createDTO() {
        return new SubProjectDTO();
    }

    public String getJNDIName() {
        return ProjectConstants.JNDI_SUB_PROJECT;
    }

    public String getPrimKeyName() {
        return SUBPROJECTPK;
    }

    public HashMap referencedValues() {
        HashMap map = new HashMap();
        map.put(ProjectConstants.TABLE_PROJECT_TIME, "subprojectid");
        map.put(ProjectConstants.TABLE_PROJECT_TIME_LIMIT, "subprojectid");
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
