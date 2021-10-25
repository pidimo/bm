package com.piramide.elwis.dto.admin;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 19, 2005
 * Time: 12:07:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class UserGroupDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {

    public static final String KEY_USERGROUPID = "userGroupId";

    /**
     * Creates an instance.
     */
    public UserGroupDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public UserGroupDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty SalutationDTO with specified languageId
     */
    public UserGroupDTO(Integer groupId) {
        setPrimKey(groupId);
    }

    public String getPrimKeyName() {
        return KEY_USERGROUPID;
    }

    public String getJNDIName() {
        return AdminConstants.JNDI_USERGROUP;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("groupName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("groupName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("groupName"));
    }

    public ComponentDTO createDTO() {
        return new UserGroupDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("groupName", "groupname");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_USERGROUPID, "usergroupid");
        return values;
    }

    public String getTableName() {
        return AdminConstants.TABLE_USERGROUP;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SchedulerConstants.TABLE_SCHEDULEDUSER, "usergroupid");
        tables.put(AdminConstants.TABLE_USEROFGROUP, "usergroupid");
        tables.put(ContactConstants.TABLE_USERADDRESSACCESS, "usergroupid");
        tables.put(SupportConstants.TABLE_USERARTICLEACCESS, "usergroupid");
        return tables;
    }
}
