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
 * Represents a Template DTO
 *
 * @author yumi
 * @version $Id: TitleDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class TitleDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_TITLELIST = "titleList";
    public static final String KEY_TITLEID = "titleId";

    /**
     * Creates an instance.
     */
    public TitleDTO(Integer key) {
        setPrimKey(key);
    }

    public TitleDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public TitleDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_TITLEID;
    }

    public String getDTOListName() {
        return KEY_TITLELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_TITLE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("titleName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("titleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("titleName"));
    }

    public ComponentDTO createDTO() {
        return new TitleDTO();
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_ADDRESS, "titleid");
        return tables;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("titleName", "titletext");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_TITLEID, "titleid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_TITLE;
    }
}
