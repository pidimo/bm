package com.piramide.elwis.dto.schedulermanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jun 29, 2005
 * Time: 2:06:08 PM
 * To change this template use File | Settings | File Templates.
 */

public class TaskTypeDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_TASKTYPEID = "taskTypeId";

    /**
     * Creates an instance.
     */
    public TaskTypeDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public TaskTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    public TaskTypeDTO(Integer id) {
        setPrimKey(id);
    }

    public TaskTypeDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_TASKTYPEID;
    }


    public String getJNDIName() {
        return SchedulerConstants.JNDI_TASKTYPE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("name"));
    }


    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("name"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

    public ComponentDTO createDTO() {
        return new TaskTypeDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_TASKTYPEID, "tasktypeid");
        return values;
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("name", "name");
        return map;
    }

    public String getTableName() {
        return SchedulerConstants.TABLE_TASKTYPE;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SchedulerConstants.TABLE_TASK, "tasktypeid");
        return tables;
    }
}

