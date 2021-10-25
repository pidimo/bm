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
public class DynamicSearchDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_DYNAMICSEARCHID = "dynamicSearchId";

    public DynamicSearchDTO() {
    }

    public DynamicSearchDTO(DTO dto) {
        super.putAll(dto);
    }

    public DynamicSearchDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new DynamicSearchDTO();
    }

    public String getJNDIName() {
        return AdminConstants.JNDI_DYNAMICSEARCH;
    }

    public String getPrimKeyName() {
        return KEY_DYNAMICSEARCHID;
    }

    public String getTableName() {
        return AdminConstants.TABLE_DYNAMICSEARCH;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("name"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }
}
