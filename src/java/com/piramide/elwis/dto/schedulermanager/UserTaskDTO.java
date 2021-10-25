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
 * Date: May 31, 2005
 * Time: 2:39:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserTaskDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_SCHEDULEDUSERID = "scheduledUserId";

    /**
     * Creates an instance.
     */
    public UserTaskDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public UserTaskDTO(DTO dto) {
        super.putAll(dto);
    }

    public UserTaskDTO(Integer id) {
        setPrimKey(id);
    }

    public UserTaskDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_SCHEDULEDUSERID;
    }


    public String getJNDIName() {
        return SchedulerConstants.JNDI_USERTASK;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    /*public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("title"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("title"));
    }
*/
    public ComponentDTO createDTO() {
        return new TaskDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}
