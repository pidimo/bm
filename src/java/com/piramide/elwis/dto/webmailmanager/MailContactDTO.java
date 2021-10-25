package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: MailContactDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class MailContactDTO extends ComponentDTO {
    public final static String KEY_MAILCONTACTID = "mailContactId";

    public MailContactDTO() {

    }

    public MailContactDTO(DTO dto) {
        super.putAll(dto);
    }

    public MailContactDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new SignatureDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_MAILCONTACT;
    }

    public String getPrimKeyName() {
        return KEY_MAILCONTACTID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }
}
