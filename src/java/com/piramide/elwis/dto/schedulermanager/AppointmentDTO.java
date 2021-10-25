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
 * Time: 9:49:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_APPOINTMENTID = "appointmentId";

    /**
     * Creates an instance.
     */
    public AppointmentDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public AppointmentDTO(DTO dto) {
        super.putAll(dto);
    }

    public AppointmentDTO(Integer id) {
        setPrimKey(id);
    }

    public AppointmentDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_APPOINTMENTID;
    }


    public String getJNDIName() {
        return SchedulerConstants.JNDI_APPOINTMENT;
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
        return new AppointmentDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

}

