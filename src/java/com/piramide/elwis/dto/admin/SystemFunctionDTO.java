package com.piramide.elwis.dto.admin;

import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: SystemFunctionDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class SystemFunctionDTO extends ComponentDTO {
    public static final String KEY_SYSTEMFUNCTIONID = "functionId";

    /**
     * Creates a new instance
     */
    public SystemFunctionDTO() {
    }

    public SystemFunctionDTO(DTO dto) {
        super.putAll(dto);
    }

    public SystemFunctionDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public ComponentDTO createDTO() {
        return new SystemFunctionDTO();
    }

    public String getJNDIName() {
        return AdminConstants.JNDI_SYSTEMFUNCTION;
    }

    public String getPrimKeyName() {
        return KEY_SYSTEMFUNCTIONID;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("roleName"));
    }
}
