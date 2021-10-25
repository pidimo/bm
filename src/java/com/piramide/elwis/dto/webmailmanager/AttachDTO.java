package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: AttachDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class AttachDTO extends ComponentDTO {
    public static final String KEY_ATTACHID = "attachId";

    public AttachDTO() {
    }

    public AttachDTO(DTO dto) {
        super.putAll(dto);
    }

    public AttachDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new AttachDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_ATTACH;
    }

    public String getPrimKeyName() {
        return KEY_ATTACHID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }
}
