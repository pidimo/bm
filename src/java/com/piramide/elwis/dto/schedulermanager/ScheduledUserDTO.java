package com.piramide.elwis.dto.schedulermanager;

import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 26, 2005
 * Time: 2:01:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledUserDTO extends ComponentDTO {//implements IntegrityReferentialDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_SCHEDULEDID = "scheduledUserId";

    /**
     * Creates an instance.
     */
    public ScheduledUserDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ScheduledUserDTO(DTO dto) {
        super.putAll(dto);
    }

    public ScheduledUserDTO(Integer id) {
        setPrimKey(id);
    }

    public ScheduledUserDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_SCHEDULEDID;
    }


    public String getJNDIName() {
        return SchedulerConstants.JNDI_SCHEDULEDUSER;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("participantName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("participantName"));
    }

    public ComponentDTO createDTO() {
        return new ScheduledUserDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    /*public HashMap referencedValues() {
        HashMap tables = new HashMap();

        tables.put(SchedulerConstants.TABLE_SCHEDULEDUSER, ReferentialPK.create()
                .addKey("userGroupId", "usergroupid")
                .addKey("userId", "userid"));

        return tables;
    }
*/
}
