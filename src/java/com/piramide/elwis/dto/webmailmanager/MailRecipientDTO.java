package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: MailRecipientDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class MailRecipientDTO extends ComponentDTO {
    public final static String KEY_MAILRECIPIENTID = "mailRecipientId";

    public MailRecipientDTO() {
    }

    public MailRecipientDTO(DTO dto) {
        super.putAll(dto);
    }

    public MailRecipientDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new MailRecipientDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_MAILRECIPIENT;
    }

    public String getPrimKeyName() {
        return KEY_MAILRECIPIENTID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }

    public String getFormattedRecipient() {
        String email = (String) get("email");
        String personal = (String) get("personalName");
        if (null != personal && !"".equals(personal.trim()) && !personal.equals(email)) {
            return processPersonalName(personal) + " <" + email + ">";
        }

        return email;
    }

    private String processPersonalName(String personal) {
        if (personal.startsWith("\"") && personal.endsWith("\"")) {
            return personal;
        }

        return "\"" + personal + "\"";
    }
}
