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
 * Date: Apr 7, 2005
 * Time: 10:07:40 AM
 * To change this template use File | Settings | File Templates.
 */

public class TaskDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_TASKID = "taskId";

    /**
     * Creates an instance.
     */
    public TaskDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public TaskDTO(DTO dto) {
        super.putAll(dto);
    }

    public TaskDTO(Integer id) {
        setPrimKey(id);
    }

    public TaskDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_TASKID;
    }


    public String getJNDIName() {
        return SchedulerConstants.JNDI_TASK;
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

        resultDTO.addResultMessage("customMsg.NotFound", get("title"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("title"));


    }

    public ComponentDTO createDTO() {
        return new TaskDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

}

