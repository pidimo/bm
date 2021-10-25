package com.piramide.elwis.dto.uimanager;

import com.piramide.elwis.utils.UIManagerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class StyleDTO extends ComponentDTO {
    public final static String KEY_STYLEID = "styleId";

    public StyleDTO() {
    }

    public StyleDTO(DTO dto) {
        super.putAll(dto);
    }

    public StyleDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new StyleDTO();
    }

    public String getJNDIName() {
        return UIManagerConstants.JNDI_STYLE;
    }

    public String getPrimKeyName() {
        return KEY_STYLEID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }
}
