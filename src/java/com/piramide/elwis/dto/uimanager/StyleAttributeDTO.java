package com.piramide.elwis.dto.uimanager;

import com.piramide.elwis.utils.UIManagerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleAttributeDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class StyleAttributeDTO extends ComponentDTO {
    public final static String KEY_STYLEATTRIBUTEID = "attributeId";

    public StyleAttributeDTO() {
    }

    public StyleAttributeDTO(DTO dto) {
        super.putAll(dto);
    }

    public StyleAttributeDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new StyleAttributeDTO();
    }

    public String getJNDIName() {
        return UIManagerConstants.JNDI_STYLEATTRIBUTE;
    }

    public String getPrimKeyName() {
        return KEY_STYLEATTRIBUTEID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }
}
