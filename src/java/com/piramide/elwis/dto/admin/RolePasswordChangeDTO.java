package com.piramide.elwis.dto.admin;

import com.piramide.elwis.domain.admin.RolePasswordChangePK;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class RolePasswordChangeDTO extends ComponentDTO {
    public static final String KEY_ROLEPASSWORDCHANGEID = "rolePasswordChangePK";

    public RolePasswordChangeDTO() {
    }

    public RolePasswordChangeDTO(DTO dto) {
        super.putAll(dto);
    }

    public RolePasswordChangeDTO(RolePasswordChangePK key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new RolePasswordChangeDTO();
    }

    public String getJNDIName() {
        return AdminConstants.JNDI_ROLEPASSWORDCHANGE;
    }

    public String getPrimKeyName() {
        return KEY_ROLEPASSWORDCHANGEID;
    }

    public String getTableName() {
        return AdminConstants.TABLE_ROLEPASSWORDCHANGE;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
