package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FinanceConstants;
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
 * @version id: VatDTO.java,v 1.33 2004/07/19 21:18:48 ivan Exp $
 */
public class VatDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_VATLIST = "vatList";
    public static final String KEY_VATID = "vatId";

    /**
     * Creates an instance.
     */
    public VatDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public VatDTO(DTO dto) {
        super.putAll(dto);
    }

    public VatDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_VATID;
    }

    public String getDTOListName() {
        return KEY_VATLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_VAT;
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
        resultDTO.addResultMessage("msg.Duplicated", get("vatLabel"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("vatLabel"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("vatLabel"));
    }

    public ComponentDTO createDTO() {
        return new VatDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(CatalogConstants.TABLE_VATRATE, "vatid");
        tables.put(FinanceConstants.TABLE_INVOICEPOSITION, "vatid");
        tables.put(FinanceConstants.TABLE_INVOICEVAT, "vatid");
        tables.put(SalesConstants.TABLE_PRODUCTCONTRACT, "vatid");
        tables.put(SalesConstants.TABLE_SALEPOSITION, "vatid");
        tables.put(ProductConstants.TABLE_PRODUCT, "vatid");
        return tables;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_VAT;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("vatLabel", "label");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_VATID, "vatid");
        return values;
    }
}
