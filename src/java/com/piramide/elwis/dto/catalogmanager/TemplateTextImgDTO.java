package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miky
 * @version $Id: TemplateTextImgDTO.java 2009-06-19 06:57:22 PM $
 */
public class TemplateTextImgDTO extends ComponentDTO {
    public static final String KEY_TEMPLATETEXTIMGID = "templateTextImgPK";

    public TemplateTextImgDTO() {
    }

    public TemplateTextImgDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new TemplateTextImgDTO();
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_TEMPLATETEXTIMG;
    }

    public String getPrimKeyName() {
        return KEY_TEMPLATETEXTIMGID;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_TEMPLATETEXTIMG;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
