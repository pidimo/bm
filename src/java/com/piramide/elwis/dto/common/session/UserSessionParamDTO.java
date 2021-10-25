package com.piramide.elwis.dto.common.session;

import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Oct 11, 2004
 * Time: 11:46:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserSessionParamDTO extends ComponentDTO {
    private static final String KEY_USERID = "userId";
    public static final String JNDI = "userId";

    public UserSessionParamDTO(DTO dto) {
        super(dto);
    }

    public ComponentDTO createDTO() {
        return null;
    }

    public String getJNDIName() {
        return null;
    }

    public String getPrimKeyName() {
        return JNDI;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}
