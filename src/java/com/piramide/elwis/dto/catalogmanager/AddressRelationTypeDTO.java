package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public class AddressRelationTypeDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {

    public static final String KEY_RELATIONTYPEID = "relationTypeId";

    public AddressRelationTypeDTO() {
    }

    public AddressRelationTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    public AddressRelationTypeDTO(Integer relationTypeId) {
        setPrimKey(relationTypeId);
    }

    public String getPrimKeyName() {
        return KEY_RELATIONTYPEID;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_ADDRESSRELATIONTYPE;
    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("title"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("title"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("title"));
    }

    public ComponentDTO createDTO() {
        return new AddressRelationTypeDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }


    public String getTableName() {
        return CatalogConstants.TABLE_ADDRESSRELATIONTYPE;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("title", "title");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_RELATIONTYPEID, "relationtypeid");
        return values;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_ADDRESSRELATION, "relationtypeid");
        return tables;
    }
}