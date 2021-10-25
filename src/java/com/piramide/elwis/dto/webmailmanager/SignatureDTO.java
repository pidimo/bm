package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: SignatureDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class SignatureDTO extends ComponentDTO {
    public final static String KEY_SIGNATUREID = "signatureId";

    public SignatureDTO() {

    }

    public SignatureDTO(DTO dto) {
        super.putAll(dto);
    }

    public SignatureDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new SignatureDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_SIGNATURE;
    }

    public String getPrimKeyName() {
        return KEY_SIGNATUREID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }
}
