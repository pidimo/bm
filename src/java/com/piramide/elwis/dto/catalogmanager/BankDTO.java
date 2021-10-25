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
 * Represents a Bank DTO
 *
 * @author Ivan
 * @version $Id: BankDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class BankDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {
    public static final String KEY_BANKLIST = "bankList";
    public static final String KEY_BANKID = "bankId";

    /**
     * Creates an instance.
     */
    public BankDTO() {
    }

    public BankDTO(Integer key) {
        setPrimKey(key);
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public BankDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_BANKID;
    }

    public String getDTOListName() {
        return KEY_BANKLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_BANK;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("bankCode"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("bankName"));

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("bankName"));
    }

    public ComponentDTO createDTO() {
        return new BankDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("bankCode", "bankcode");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_BANKID, "bankid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_BANK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_BANKACCOUNT, "bankid");
        return tables;
    }
}
