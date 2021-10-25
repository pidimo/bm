package com.piramide.elwis.dto.admin;

import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: AccessRightsDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class AccessRightsDTO extends ComponentDTO {

    public AccessRightsDTO() {
    }

    public AccessRightsDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new AccessRightsDTO();
    }

    public String getJNDIName() {
        return AdminConstants.JNDI_ACCESSRIGHTS;
    }

    public String getPrimKeyName() {
        return null;
    }
}
