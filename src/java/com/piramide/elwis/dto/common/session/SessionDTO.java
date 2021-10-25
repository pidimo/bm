package com.piramide.elwis.dto.common.session;

import com.piramide.elwis.utils.Constants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * User session DTO
 */
public class SessionDTO extends ComponentDTO {
    public SessionDTO(DTO dto) {
        super(dto);
    }

    public SessionDTO() {
    }

    public ComponentDTO createDTO() {
        return new SessionDTO();
    }

    public String getJNDIName() {
        return Constants.JNDI_USERSESSION;
    }

    public String getPrimKeyName() {
        return null;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}
