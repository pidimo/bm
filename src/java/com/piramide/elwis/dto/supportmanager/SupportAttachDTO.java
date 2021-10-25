package com.piramide.elwis.dto.supportmanager;

import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 12, 2005
 * Time: 10:14:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class SupportAttachDTO extends ComponentDTO {
    public static final String KEY_SUPPORT_ATTACHID = "attachId";

    /**
     * Creates an instance.
     */
    public SupportAttachDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SupportAttachDTO(DTO dto) {
        super.putAll(dto);
    }

    public SupportAttachDTO(Integer id) {
        setPrimKey(id);
    }

    public SupportAttachDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_SUPPORT_ATTACHID;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_SUPPORT_ATTACH;
    }


    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("supportAttachName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("supportAttachName"));
    }

    public ComponentDTO createDTO() {
        return new SupportAttachDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

}

