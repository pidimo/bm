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
 * Date: May 5, 2005
 * Time: 12:24:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecurExceptionDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_RECUREXCEPTIONID = "recurExceptionId";

    /**
     * Creates an instance.
     */
    public RecurExceptionDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public RecurExceptionDTO(DTO dto) {
        super.putAll(dto);
    }

    public RecurExceptionDTO(Integer id) {
        setPrimKey(id);
    }

    public RecurExceptionDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_RECUREXCEPTIONID;
    }


    public String getJNDIName() {
        return SchedulerConstants.JNDI_RECUREXCEPTION;
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
        return new RecurExceptionDTO();
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



