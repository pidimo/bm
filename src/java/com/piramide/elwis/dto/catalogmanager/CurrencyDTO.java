package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Currency DTO
 *
 * @author Ivan
 * @version $Id: CurrencyDTO.java 9439 2009-07-09 22:22:25Z ivan $
 */

public class CurrencyDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_CURRENCYLIST = "currencyList";
    public static final String KEY_CURRENCYID = "currencyId";

    /**
     * Creates an instance.
     */
    public CurrencyDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CurrencyDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty CurrencyDTO with specified currencyId
     */
    public CurrencyDTO(Integer currencyId) {
        setPrimKey(currencyId);
    }

    public String getPrimKeyName() {
        return KEY_CURRENCYID;
    }

    public String getDTOListName() {
        return KEY_CURRENCYLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_CURRENCY;
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
        resultDTO.addResultMessage("msg.Duplicated", get("currencyName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("currencyName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("currencyName"));
    }

    public ComponentDTO createDTO() {
        return new CurrencyDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(CatalogConstants.TABLE_COUNTRY, "currencyid");
        tables.put(FinanceConstants.TABLE_INVOICE, "currencyid");
        tables.put(SalesConstants.TABLE_ACTION, "currencyid");
        tables.put(SalesConstants.TABLE_SALE, "currencyid");
        return tables;
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("currencyName", "currencyname");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_CURRENCYID, "currencyid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_CURRENCY;
    }

}
