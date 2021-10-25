package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: UserMailDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class UserMailDTO extends ComponentDTO {
    public static final String KEY_USERMAILID = "userMailId";

    public UserMailDTO() {

    }

    public UserMailDTO(DTO dto) {
        super.putAll(dto);
    }

    public UserMailDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new UserMailDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_USERMAIL;
    }

    public String getPrimKeyName() {
        return KEY_USERMAILID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }
}

