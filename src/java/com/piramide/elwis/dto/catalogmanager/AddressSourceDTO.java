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
 * Represents a AddressSource DTO
 *
 * @author Ivan
 * @version $Id: AddressSourceDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class AddressSourceDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {
    public static final String KEY_ADDRESSSOURCELIST = "addressSourceList";
    public static final String KEY_ADDRESSSOURCEID = "addressSourceId";

    /**
     * Creates an instance.
     */
    public AddressSourceDTO() {
    }

    public AddressSourceDTO(Integer key) {
        setPrimKey(key);
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public AddressSourceDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_ADDRESSSOURCEID;
    }

    public String getDTOListName() {
        return KEY_ADDRESSSOURCELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_ADDRESSSOURCE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("addressSourceName"));
    }


    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("addressSourceName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("addressSourceName"));
    }

    public ComponentDTO createDTO() {
        return new AddressSourceDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }


    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("addressSourceName", "sourcename");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_ADDRESSSOURCEID, "sourceid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_ADDRESSSOURCE;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_CUSTOMER, "sourceid");
        return tables;
    }
}
