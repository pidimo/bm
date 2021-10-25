package com.piramide.elwis.dto.admin;

import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class UserRoleDTO extends ComponentDTO {
    public static final String KEY_USERROLEID = "userRolePK";

    /**
     * Creates an instance.
     */
    public UserRoleDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public UserRoleDTO(DTO dto) {
        super.putAll(dto);
    }

    public UserRoleDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_USERROLEID;
    }


    public String getJNDIName() {
        return AdminConstants.JNDI_USERROLE;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }


    public ComponentDTO createDTO() {
        return new UserRoleDTO();
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}
