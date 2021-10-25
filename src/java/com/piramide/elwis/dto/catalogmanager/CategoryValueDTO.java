package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a CategoryValue DTO
 *
 * @author Ivan
 * @version $Id: CategoryValueDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CategoryValueDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_CATEGORYVALUELIST = "categoryValueList";
    public static final String KEY_CATEGORYVALUEID = "categoryValueId";

    /**
     * Creates an instance.
     */
    public CategoryValueDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CategoryValueDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty CityDTO with specified languageId
     */
    public CategoryValueDTO(Integer CategoryValueId) {
        setPrimKey(CategoryValueId);
    }

    public String getPrimKeyName() {
        return KEY_CATEGORYVALUEID;
    }

    public String getDTOListName() {
        return KEY_CATEGORYVALUELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_CATEGORYVALUE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("categoryValueName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("categoryValueName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("categoryValueName"));
    }

    public ComponentDTO createDTO() {
        return new CategoryValueDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("categoryValueName", "categoryvaluename");
        map.put("categoryId", "categoryid");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_CATEGORYVALUEID, "categoryvalueid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_CATEGORYVALUE;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(CatalogConstants.TABLE_CATEGORYFIELDVALUE, "categoryvalueid");
        return tables;
    }
}