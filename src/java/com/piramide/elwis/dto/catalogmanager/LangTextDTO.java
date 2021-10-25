package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Represents a LangText DTO
 *
 * @author Ivan
 * @version $Id: LangTextDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class LangTextDTO extends ComponentDTO {

    public static final String KEY_LANGTEXTLIST = "langTextList";
    public static final String KEY_LANGTEXTID = "langTextId";


    /**
     * Creates an instance.
     */
    public LangTextDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public LangTextDTO(DTO dto) {
        super.putAll(dto);
    }

    public LangTextDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_LANGTEXTID;
    }

    public String getDTOListName() {
        return KEY_LANGTEXTLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_LANGTEXT;
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
        resultDTO.addResultMessage("msg.Duplicated", "LangText.id");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", "LangText");
    }

    public ComponentDTO createDTO() {
        return new LangTextDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}
