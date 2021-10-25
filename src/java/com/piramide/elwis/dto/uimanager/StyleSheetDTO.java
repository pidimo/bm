package com.piramide.elwis.dto.uimanager;

import com.piramide.elwis.utils.UIManagerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleSheetDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class StyleSheetDTO extends ComponentDTO {
    public static final String KEY_STYLESHEETID = "styleSheetId";

    public StyleSheetDTO() {
    }

    public StyleSheetDTO(DTO dto) {
        super.putAll(dto);
    }

    public StyleSheetDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new StyleSheetDTO();
    }

    public String getJNDIName() {
        return UIManagerConstants.JNDI_STYLESHEET;
    }

    public String getPrimKeyName() {
        return KEY_STYLESHEETID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        //resultDTO.addResultMessage("customMsg.NotFound",get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }
}
