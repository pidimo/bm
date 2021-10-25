package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public class AddressRelationDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {
    public static final String KEY_RELATIONID = "relationId";

    public AddressRelationDTO() {
    }

    public AddressRelationDTO(DTO dto) {
        super.putAll(dto);
    }

    public AddressRelationDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new AddressRelationDTO();
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_ADDRESSRELATION;
    }

    public String getPrimKeyName() {
        return KEY_RELATIONID;
    }

    public String getTableName() {
        return ContactConstants.TABLE_ADDRESSRELATION;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("relatedAddressName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("relatedAddressName"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }

    @Override
    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("AddressRelation.msg.Duplicated", get("relatedAddressName"));
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("addressId", "addressid");
        values.put("relatedAddressId", "relatedaddressid");
        values.put("relationTypeId", "relationtypeid");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_RELATIONID, "relationid");
        return values;
    }

}
