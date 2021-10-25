package com.piramide.elwis.dto.admin;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 19, 2005
 * Time: 12:07:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserOfGroupDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_USEROFGROUPID = "userOfGroupPK";

    public UserOfGroupDTO() {
    }

    public UserOfGroupDTO(DTO dto) {
        super.putAll(dto);
    }

    public UserOfGroupDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new UserOfGroupDTO();
    }

    public String getJNDIName() {
        return AdminConstants.JNDI_USEROFGROUP;
    }

    public String getPrimKeyName() {
        return KEY_USEROFGROUPID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("name"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();

        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }
}

