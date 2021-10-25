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
public class ImportRecordDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_IMPORTRECORDID = "importRecordId";

    public ImportRecordDTO() {
    }

    public ImportRecordDTO(DTO dto) {
        super.putAll(dto);
    }

    public ImportRecordDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new ImportRecordDTO();
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_IMPORTRECORD;
    }

    public String getPrimKeyName() {
        return KEY_IMPORTRECORDID;
    }

    public String getTableName() {
        return ContactConstants.TABLE_IMPORTRECORD;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("recordIndex"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("recordIndex"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }
}
