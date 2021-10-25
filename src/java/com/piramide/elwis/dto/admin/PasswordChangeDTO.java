package com.piramide.elwis.dto.admin;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class PasswordChangeDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_PASSWORDCHANGEID = "passwordChangeId";

    public PasswordChangeDTO() {
    }

    public PasswordChangeDTO(DTO dto) {
        super.putAll(dto);
    }

    public PasswordChangeDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new PasswordChangeDTO();
    }

    public String getJNDIName() {
        return AdminConstants.JNDI_PASSWORDCHANGE;
    }

    public String getPrimKeyName() {
        return KEY_PASSWORDCHANGEID;
    }

    public String getTableName() {
        return AdminConstants.TABLE_PASSWORDCHANGE;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("description"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("description"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }
}
