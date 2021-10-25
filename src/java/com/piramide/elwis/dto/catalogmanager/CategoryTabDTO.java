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
public class CategoryTabDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_CATEGORYTAB = "categoryTabId";

    public CategoryTabDTO() {
    }

    public CategoryTabDTO(DTO dto) {
        super.putAll(dto);
    }

    public CategoryTabDTO(Integer pk) {
        setPrimKey(pk);
    }

    public ComponentDTO createDTO() {
        return new CategoryTabDTO();
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_CATEGORYTAB;
    }

    public String getPrimKeyName() {
        return KEY_CATEGORYTAB;
    }

    public HashMap referencedValues() {
        HashMap references = new HashMap();
        references.put(CatalogConstants.TABLE_CATEGORYGROUP, "categorytabid");
        return references;
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

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("label"));
    }
}
