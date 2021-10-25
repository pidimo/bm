package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class EmailAccountErrorDTO extends ComponentDTO {
    public static final String EMAIL_ACCOUNT_ERROR_PK = "emailAccountErrorId";

    public EmailAccountErrorDTO() {
    }

    public EmailAccountErrorDTO(DTO dto) {
        super.putAll(dto);
    }

    public EmailAccountErrorDTO(Integer pk) {
        setPrimKey(pk);
    }

    public ComponentDTO createDTO() {
        return new EmailAccountErrorDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_EMAILACCOUNTERROR;
    }

    public String getPrimKeyName() {
        return EMAIL_ACCOUNT_ERROR_PK;
    }
}
