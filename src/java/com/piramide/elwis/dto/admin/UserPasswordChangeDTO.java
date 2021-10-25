package com.piramide.elwis.dto.admin;

import com.piramide.elwis.domain.admin.UserPasswordChangePK;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.5.3
 */
public class UserPasswordChangeDTO extends ComponentDTO {
    public static final String KEY_USERPASSWORDCHANGEID = "userPasswordChangePK";

    public UserPasswordChangeDTO() {
    }

    public UserPasswordChangeDTO(DTO dto) {
        super.putAll(dto);
    }

    public UserPasswordChangeDTO(UserPasswordChangePK key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new UserPasswordChangeDTO();
    }

    public String getJNDIName() {
        return AdminConstants.JNDI_USERPASSWORDCHANGE;
    }

    public String getPrimKeyName() {
        return KEY_USERPASSWORDCHANGEID;
    }

    public String getTableName() {
        return AdminConstants.TABLE_USERPASSWORDCHANGE;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
