package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.domain.contactmanager.RecordDuplicatePK;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class RecordDuplicateDTO extends ComponentDTO {
    public static final String KEY_RECORDDUPLICATEID = "recordDuplicatePK";

    public RecordDuplicateDTO() {
    }

    public RecordDuplicateDTO(DTO dto) {
        super.putAll(dto);
    }

    public RecordDuplicateDTO(RecordDuplicatePK key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new RecordDuplicateDTO();
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_RECORDDUPLICATE;
    }

    public String getPrimKeyName() {
        return KEY_RECORDDUPLICATEID;
    }

    public String getTableName() {
        return ContactConstants.TABLE_RECORDUPLICATE;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
