package com.piramide.elwis.dto.schedulermanager;

import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 7, 2005
 * Time: 10:15:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecurrenceDTO extends ComponentDTO {


    public static final String KEY_APPOINTMENTID = "appointmentId";

    /**
     * Creates an instance.
     */
    public RecurrenceDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public RecurrenceDTO(DTO dto) {
        super.putAll(dto);
    }

    public RecurrenceDTO(Integer id) {
        setPrimKey(id);
    }

    public RecurrenceDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_APPOINTMENTID;
    }


    public String getJNDIName() {
        return SchedulerConstants.JNDI_RECURRENCE;
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
        return new RecurrenceDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

}


