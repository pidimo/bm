package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.domain.contactmanager.RecordColumnPK;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class RecordColumnDTO extends ComponentDTO {
    public static final String KEY_RECORDCOLUMNID = "recordColumnPK";

    public RecordColumnDTO() {
    }

    public RecordColumnDTO(DTO dto) {
        super.putAll(dto);
    }

    public RecordColumnDTO(RecordColumnPK key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new RecordColumnDTO();
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_RECORDCOLUMN;
    }

    public String getPrimKeyName() {
        return KEY_RECORDCOLUMNID;
    }

    public String getTableName() {
        return ContactConstants.TABLE_RECORDCOLUMN;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
