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
 * Represents a PayMorality DTO
 *
 * @author Ivan
 * @version $Id: PayMoralityDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class PayMoralityDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {
    public static final String KEY_PAYMORALITYLIST = "payMoralityList";
    public static final String KEY_PAYMORALITYID = "payMoralityId";

    /**
     * Creates an instance.
     */
    public PayMoralityDTO() {
    }

    public PayMoralityDTO(Integer key) {
        setPrimKey(key);
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public PayMoralityDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_PAYMORALITYID;
    }

    public String getDTOListName() {
        return KEY_PAYMORALITYLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_PAYMORALITY;
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
        resultDTO.addResultMessage("msg.Duplicated", get("payMoralityName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("payMoralityName"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_CUSTOMER, "paymoralityid");

        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("payMoralityName"));
    }

    public ComponentDTO createDTO() {
        return new PayMoralityDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("payMoralityName", "paymoralityname");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_PAYMORALITYID, "paymoralityid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_PAYMORALITY;
    }
}
