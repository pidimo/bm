package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class EmailAccountErrorDetailDTO extends ComponentDTO {
    public static final String KEY_EMAILACCOUNTERRORDETAILID = "emailAccountErrorDetailId";

    public EmailAccountErrorDetailDTO() {
    }

    public EmailAccountErrorDetailDTO(DTO dto) {
        super.putAll(dto);
    }

    public EmailAccountErrorDetailDTO(Integer pk) {
        setPrimKey(pk);
    }

    public ComponentDTO createDTO() {
        return new EmailAccountErrorDetailDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_EMAILACCOUNTERROR;
    }

    public String getPrimKeyName() {
        return KEY_EMAILACCOUNTERRORDETAILID;
    }

    public String getTableName() {
        return WebMailConstants.TABLE_EMAILACCOUNTERRORDETAIL;
    }

}
