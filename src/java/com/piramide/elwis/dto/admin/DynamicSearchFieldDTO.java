package com.piramide.elwis.dto.admin;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchFieldDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_DYNAMICSEARCHFIELDID = "dynamicSearchFieldId";

    public DynamicSearchFieldDTO() {
    }

    public DynamicSearchFieldDTO(DTO dto) {
        super.putAll(dto);
    }

    public DynamicSearchFieldDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new DynamicSearchFieldDTO();
    }

    public String getJNDIName() {
        return AdminConstants.JNDI_DYNAMICSEARCHFIELD;
    }

    public String getPrimKeyName() {
        return KEY_DYNAMICSEARCHFIELDID;
    }

    public String getTableName() {
        return AdminConstants.TABLE_DYNAMICSEARCHFIELD;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("alias"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("alias"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }
}
