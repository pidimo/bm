package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Data Transfer Object (DTO)
 *
 * @author Ivan
 * @version id: ProductUnitDTO.java,v 1.33 2004/07/19 21:18:48 ivan Exp $
 */
public class ProductUnitDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_PRODUCTUNITLIST = "productUnitList";
    public static final String KEY_PRODUCTUNITID = "unitId";

    /**
     * Creates an instance.
     */
    public ProductUnitDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ProductUnitDTO(DTO dto) {
        super.putAll(dto);
    }

    public ProductUnitDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_PRODUCTUNITID;
    }

    public String getDTOListName() {
        return KEY_PRODUCTUNITLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_PRODUCTUNIT;
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
        resultDTO.addResultMessage("msg.Duplicated", get("unitName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("unitName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("unitName"));
    }

    public ComponentDTO createDTO() {
        return new ProductUnitDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ProductConstants.TABLE_PRODUCTSUPPLIER, "unitid");
        tables.put(ProductConstants.TABLE_PRODUCT, "unitid");
        tables.put(SalesConstants.TABLE_ACTIONPOSITION, "unitid");
        return tables;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_PRODUCTUNIT;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("unitName", "unitname");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_PRODUCTUNITID, "unitid");
        return values;
    }
}
