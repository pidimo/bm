package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * @author Ivan Alban
 * @version 4.3.6
 */
public class SignatureImageDTO extends ComponentDTO {
    public final static String KEY_SIGNATURE_IMAGE_ID = "signatureImageId";

    public SignatureImageDTO() {
    }

    public SignatureImageDTO(DTO dto) {
        super.putAll(dto);
    }

    public SignatureImageDTO(Integer key) {
        setPrimKey(key);
    }

    @Override
    public ComponentDTO createDTO() {
        return new SignatureImageDTO();
    }

    @Override
    public String getJNDIName() {
        return WebMailConstants.JNDI_SIGNATURE_IMAGE;
    }

    @Override
    public String getPrimKeyName() {
        return KEY_SIGNATURE_IMAGE_ID;
    }
}
