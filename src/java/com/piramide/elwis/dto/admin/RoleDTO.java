package com.piramide.elwis.dto.admin;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;


/**
 * Role DTO
 *
 * @author Fernando Montaño?
 * @version $Id: RoleDTO.java 10153 2011-09-23 00:50:02Z miguel $
 */
public class RoleDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_ROLEID = "roleId";

    /**
     * Creates an instance.
     */
    public RoleDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public RoleDTO(DTO dto) {
        super.putAll(dto);
    }

    public RoleDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_ROLEID;
    }


    public String getJNDIName() {
        return AdminConstants.JNDI_ROLE;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("roleName"));
    }


    public ComponentDTO createDTO() {
        return new RoleDTO();
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(AdminConstants.TABLE_USERROLE, "roleid");
        tables.put(ReportConstants.TABLE_REPORTROLE, "roleid");
        tables.put(AdminConstants.TABLE_ROLEPASSWORDCHANGE, "roleid");
        return tables;
    }


}
