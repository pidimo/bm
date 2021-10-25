package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryGroupDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_CATEGORYGROUPID = "categoryGroupId";

    public CategoryGroupDTO() {
    }

    public CategoryGroupDTO(DTO dto) {
        super.putAll(dto);
    }

    public CategoryGroupDTO(Integer pk) {
        setPrimKey(pk);
    }

    public ComponentDTO createDTO() {
        return new CategoryGroupDTO();
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_CATEGORYGROUP;
    }

    public String getPrimKeyName() {
        return KEY_CATEGORYGROUPID;
    }

    public HashMap referencedValues() {
        HashMap references = new HashMap();
        references.put(CatalogConstants.TABLE_CATEGORY, "categorygroupid");
        return references;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("label"));
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("label"));
    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("label"));
    }
}
