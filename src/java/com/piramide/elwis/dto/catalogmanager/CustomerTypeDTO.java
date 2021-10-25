package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a CustomerType DTO
 *
 * @author Ivan
 * @version $Id: CustomerTypeDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class CustomerTypeDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {

    public static final String KEY_CUSTOMERTYPELIST = "customerTypeList";
    public static final String KEY_CUSTOMERTYPEID = "customerTypeId";

    /**
     * Creates an instance.
     */
    public CustomerTypeDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CustomerTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty CustomerTypeDTO with specified customerTypeId
     */
    public CustomerTypeDTO(Integer customerTypeId) {
        setPrimKey(customerTypeId);
    }

    public String getPrimKeyName() {
        return KEY_CUSTOMERTYPEID;
    }

    public String getDTOListName() {
        return KEY_CUSTOMERTYPELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_CUSTOMERTYPE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("customerTypeName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("customerTypeName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("customerTypeName"));
    }

    public ComponentDTO createDTO() {
        return new CustomerTypeDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("customerTypeName", "customertypename");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_CUSTOMERTYPEID, "customertypeid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_CUSTOMERTYPE;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(CatalogConstants.TABLE_CUSTOMER, "customertypeid");

        return tables;
    }
}