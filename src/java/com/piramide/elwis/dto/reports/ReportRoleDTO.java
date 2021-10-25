package com.piramide.elwis.dto.reports;

import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class ReportRoleDTO extends ComponentDTO {
    public static final String KEY_REPORTROLE_PK = "reportRolePk";

    public ReportRoleDTO() {
    }

    public ReportRoleDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new ReportRoleDTO();
    }

    public String getJNDIName() {
        return ReportConstants.JNDI_REPORTROLE;
    }

    public String getPrimKeyName() {
        return KEY_REPORTROLE_PK;
    }


}
