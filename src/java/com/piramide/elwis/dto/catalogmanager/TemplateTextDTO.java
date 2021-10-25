package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: TemplateTextDTO.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 09-jun-2004 15:33:44 Ivan Exp $
 */

public class TemplateTextDTO extends ComponentDTO {

    public static final String KEY_TEMPLATETEXTLIST = "templateTextList";
    public static final String KEY_TEMPLATETEXTID = "templateTextId";

    /**
     * Creates an instance.
     */
    public TemplateTextDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public TemplateTextDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty TemplateTextDTO with specified templatetextid
     */
    public TemplateTextDTO(Integer templateTextId) {
        setPrimKey(templateTextId);
    }

    public String getPrimKeyName() {
        return KEY_TEMPLATETEXTID;
    }

    public String getDTOListName() {
        return KEY_TEMPLATETEXTLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_TEMPLATETEXT;
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
        resultDTO.addResultMessage("msg.Duplicated", "TemplateText.id");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("TemplateFile.error.find");
    }

    public ComponentDTO createDTO() {
        return new TemplateTextDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}