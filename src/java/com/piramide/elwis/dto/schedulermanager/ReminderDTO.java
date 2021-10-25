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
 * Time: 10:15:30 AM
 * To change this template use File | Settings | File Templates.
 */

public class ReminderDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_REMINDERID = "reminderId";

    /**
     * Creates an instance.
     */
    public ReminderDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ReminderDTO(DTO dto) {
        super.putAll(dto);
    }

    public ReminderDTO(Integer id) {
        setPrimKey(id);
    }

    public ReminderDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_REMINDERID;
    }


    public String getJNDIName() {
        return SchedulerConstants.JNDI_REMINDER;
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
        resultDTO.addResultMessage("customMsg.NotFound", get(""));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get(""));
    }

    public ComponentDTO createDTO() {
        return new ReminderDTO();
    }

    public void parseValues() {
/*
        if ("".equals(this.get("contactPersonId")))
            this.put("contactPersonId", null);
        if ("".equals(this.get("remarkValue")))
            this.put("remark", null);
*/
        super.convertPrimKeyToInteger();
    }

}


