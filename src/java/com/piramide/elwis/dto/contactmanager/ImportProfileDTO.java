package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * <code>DTO</code> Object for <code>ImportProfile</code> Entity Bean.
 *
 * @author Ivan Alban
 * @version 4.2.1
 */
public class ImportProfileDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String IMPORT_PROFILE_PK = "profileId";

    public ImportProfileDTO() {

    }

    public ImportProfileDTO(DTO dto) {
        super.putAll(dto);
    }

    public ImportProfileDTO(Integer id) {
        setPrimKey(id);
    }

    public ComponentDTO createDTO() {
        return new ImportProfileDTO();
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_IMPORTPROFILE;
    }

    public String getPrimKeyName() {
        return IMPORT_PROFILE_PK;
    }

    public String getTableName() {
        return ContactConstants.TABLE_IMPORTPROFILE;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("label"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("label"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }
}
