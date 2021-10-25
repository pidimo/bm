package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public class AdditionalAddressDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {
    public static final String KEY_ADDITIONALADDRESSID = "additionalAddressId";

    public AdditionalAddressDTO() {
    }

    public AdditionalAddressDTO(DTO dto) {
        super.putAll(dto);
    }

    public AdditionalAddressDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new AdditionalAddressDTO();
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_ADDITIONALADDRESS;
    }

    public String getPrimKeyName() {
        return KEY_ADDITIONALADDRESSID;
    }

    public String getTableName() {
        return ContactConstants.TABLE_ADDITIONALADDRESS;
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
        tables.put(ContactConstants.TABLE_CONTACT, "addaddressid");
        tables.put(ContactConstants.TABLE_CONTACTPERSON, "addaddressid");
        tables.put(ContactConstants.TABLE_CUSTOMER, "addaddressid");
        tables.put(SalesConstants.TABLE_SALE, "addaddressid");
        tables.put(SalesConstants.TABLE_PRODUCTCONTRACT, "addaddressid");
        tables.put(FinanceConstants.TABLE_INVOICE, "addaddressid");

        return tables;
    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("name"));
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("addressId", "addressid");
        values.put("name", "name");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_ADDITIONALADDRESSID, "addaddressid");
        return values;
    }
}
