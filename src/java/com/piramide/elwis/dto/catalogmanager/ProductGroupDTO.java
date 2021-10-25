package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Data Transfer Object (DTO)
 *
 * @author Ivan
 * @version id: ProductGroupDTO.java,v 1.33 2004/07/19 21:18:48 ivan Exp $
 */
public class ProductGroupDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_PRODUCTGROUPLIST = "productGroupList";
    public static final String KEY_PRODUCTGROUPID = "groupId";

    /**
     * Creates an instance.
     */
    public ProductGroupDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ProductGroupDTO(DTO dto) {
        super.putAll(dto);
    }

    public ProductGroupDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_PRODUCTGROUPID;
    }

    public String getDTOListName() {
        return KEY_PRODUCTGROUPLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_PRODUCTGROUP;
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
        resultDTO.addResultMessage("msg.Duplicated", get("groupName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("groupName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("groupName"));
    }

    public ComponentDTO createDTO() {
        return new ProductGroupDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        //put the values of having referenced
        tables.put(CatalogConstants.TABLE_PRODUCTGROUP, "parentgroupid");
        tables.put(ProductConstants.TABLE_PRODUCT, "productgroupid");
        return tables;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_PRODUCTGROUP;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("groupName", "groupname");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_PRODUCTGROUPID, "groupid");
        return values;
    }

}
