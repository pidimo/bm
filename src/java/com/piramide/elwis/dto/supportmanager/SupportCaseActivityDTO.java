package com.piramide.elwis.dto.supportmanager;

import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 12, 2005
 * Time: 10:11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class SupportCaseActivityDTO extends ComponentDTO {
    public static final String KEY_CASE_ACTIVITYID = "activityId";

    /**
     * Creates an instance.
     */
    public SupportCaseActivityDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SupportCaseActivityDTO(DTO dto) {
        super.putAll(dto);
    }

    public SupportCaseActivityDTO(Integer id) {
        setPrimKey(id);
    }

    public SupportCaseActivityDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_CASE_ACTIVITYID;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_SUPPORT_CASE_ACTIVITY;
    }


    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", null);
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", null);
    }

    public ComponentDTO createDTO() {
        return new SupportCaseActivityDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

}

