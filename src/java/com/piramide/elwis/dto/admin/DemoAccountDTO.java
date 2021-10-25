package com.piramide.elwis.dto.admin;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public class DemoAccountDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_DEMOACCOUNTID = "demoAccountId";

    public DemoAccountDTO() {
    }

    public DemoAccountDTO(DTO dto) {
        super.putAll(dto);
    }

    public DemoAccountDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new DemoAccountDTO();
    }

    public String getJNDIName() {
        return AdminConstants.JNDI_DEMOACCOUNT;
    }

    public String getPrimKeyName() {
        return KEY_DEMOACCOUNTID;
    }

    public String getTableName() {
        return AdminConstants.TABLE_DEMOACCOUNT;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("lastName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("lastName"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }
}
