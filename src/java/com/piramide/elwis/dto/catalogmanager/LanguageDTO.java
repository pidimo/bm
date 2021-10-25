package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Language DTO
 *
 * @author Ivan
 * @version $Id: LanguageDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class LanguageDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_LANGUAGELIST = "languageList";
    public static final String KEY_LANGUAGEID = "languageId";

    /**
     * Creates an instance.
     */
    public LanguageDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public LanguageDTO(DTO dto) {
        super.putAll(dto);
    }

    public LanguageDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_LANGUAGEID;
    }

    public String getDTOListName() {
        return KEY_LANGUAGELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_LANGUAGE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("languageName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("languageName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("languageName"));
    }

    public ComponentDTO createDTO() {
        return new LanguageDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {

        HashMap tables = new HashMap();
        tables.put(CatalogConstants.TABLE_ADDRESS, "languageid");
        tables.put(CatalogConstants.TABLE_LANGTEXT, "languageid");
        tables.put(CatalogConstants.TABLE_TEMPLATETEXT, "languageid");
        tables.put(CatalogConstants.TABLE_PAYCONDITIONTEXT, "languageid");
        tables.put(FinanceConstants.TABLE_INVOICETEXT, "languageid");
        tables.put(FinanceConstants.TABLE_REMINDERTEXT, "languageid");
        tables.put(ProductConstants.TABLE_PRODUCTTEXT, "languageid");

        return tables;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_LANGUAGE;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("languageName", "languagename");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_LANGUAGEID, "languageid");
        return values;
    }
}