package com.piramide.elwis.dto.schedulermanager;

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
 * Date: Apr 7, 2005
 * Time: 10:17:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentTypeDTO extends ComponentDTO implements IntegrityReferentialDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_APPOINTMENTTYPEID = "appointmentTypeId";

    /**
     * Creates an instance.
     */
    public AppointmentTypeDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public AppointmentTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    public AppointmentTypeDTO(Integer id) {
        setPrimKey(id);
    }

    public AppointmentTypeDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_APPOINTMENTTYPEID;
    }


    public String getJNDIName() {
        return SchedulerConstants.JNDI_APPOINTMENTTYPE;
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
        resultDTO.addResultMessage("customMsg.NotFound", get("name"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

    public ComponentDTO createDTO() {
        return new AppointmentTypeDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_APPOINTMENTTYPEID, "apptypeid");
        return values;
    }

    public String getTableName() {
        return SchedulerConstants.TABLE_APPOINTMENT;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SchedulerConstants.TABLE_APPOINTMENT, "apptypeid");
        return tables;
    }
}

