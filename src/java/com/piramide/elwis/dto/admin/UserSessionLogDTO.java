package com.piramide.elwis.dto.admin;

import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 23, 2005
 * Time: 3:24:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserSessionLogDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_USERID = "userId";

    /**
     * Creates an instance.
     */
    public UserSessionLogDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public UserSessionLogDTO(DTO dto) {
        super.putAll(dto);
    }

    public UserSessionLogDTO(Integer id) {
        setPrimKey(id);
    }

    public UserSessionLogDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_USERID;
    }


    public String getJNDIName() {
        return AdminConstants.JNDI_USERSESSIONLOG;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public ComponentDTO createDTO() {
        return new UserSessionLogDTO();
    }
}


