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
 * @version id: ProductTypeDTO.java,v 1.33 2004/07/19 21:18:48 ivan Exp $
 */
public class ProductTypeDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_PRODUCTTYPELIST = "productTypeList";
    public static final String KEY_PRODUCTTYPEID = "typeId";

    /**
     * Creates an instance.
     */
    public ProductTypeDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ProductTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    public ProductTypeDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_PRODUCTTYPEID;
    }

    public String getDTOListName() {
        return KEY_PRODUCTTYPELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_PRODUCTTYPE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("typeName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("typeName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("typeName"));
    }

    public ComponentDTO createDTO() {
        return new ProductTypeDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ProductConstants.TABLE_PRODUCT, "producttypeid");

        //put the values of having referenced
        return tables;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_PRODUCTTYPE;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("typeName", "typename");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_PRODUCTTYPEID, "typeid");
        return values;
    }

}
