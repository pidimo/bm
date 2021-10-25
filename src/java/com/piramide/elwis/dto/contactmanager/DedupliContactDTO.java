package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DedupliContactDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_DEDUPLICONTACTID = "dedupliContactId";

    public DedupliContactDTO() {
    }

    public DedupliContactDTO(DTO dto) {
        super.putAll(dto);
    }

    public DedupliContactDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new DedupliContactDTO();
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_DEDUPLICONTACT;
    }

    public String getPrimKeyName() {
        return KEY_DEDUPLICONTACTID;
    }

    public String getTableName() {
        return ContactConstants.TABLE_DEDUPLICONTACT;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("startTime"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("startTime"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }
}
