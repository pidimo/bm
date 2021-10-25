package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class EmailRecipientAddressDTO extends ComponentDTO {
    public final static String EMAIL_RECIPIENT_ADDRESSID = "emailRecipientAddressId";

    public EmailRecipientAddressDTO() {
    }

    public EmailRecipientAddressDTO(DTO dto) {
        super.putAll(dto);
    }

    public EmailRecipientAddressDTO(Integer id) {
        setPrimKey(id);
    }

    public ComponentDTO createDTO() {
        return new EmailRecipientAddressDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_EMAILRECIPIENTADDRESS;
    }

    public String getPrimKeyName() {
        return EMAIL_RECIPIENT_ADDRESSID;
    }
}
