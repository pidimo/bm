package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author: ivan
 * Date: 25-10-2006: 01:20:53 PM
 */
public class AttachDTO extends ComponentDTO {
    public static final String KEY_ATTACHID = "attachId";

    public AttachDTO() {
    }

    public AttachDTO(Integer key) {
        super.setPrimKey(key);
    }

    public AttachDTO(DTO dto) {
        super.putAll(dto);
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public ComponentDTO createDTO() {
        return new AttachDTO();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_ATTACH;
    }

    public String getPrimKeyName() {
        return KEY_ATTACHID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (null != get("filename")) {
            resultDTO.addResultMessage("customMsg.NotFound", get("filename"));
        } else {
            resultDTO.addResultMessage("customMsg.NotFound", get("filenameAux"));
        }
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("filename"));
    }
}
