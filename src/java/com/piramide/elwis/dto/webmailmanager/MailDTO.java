package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: MailDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class MailDTO extends ComponentDTO {
    public final static String KEY_MAILID = "mailId";

    public MailDTO() {

    }

    public MailDTO(DTO dto) {
        super.putAll(dto);
    }

    public MailDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new MailDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_MAIL;
    }

    public String getPrimKeyName() {
        return KEY_MAILID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }
}
