package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class CampaignSentLogDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_CAMPAIGNSENTLOGID = "campaignSentLogId";

    public CampaignSentLogDTO() {
    }

    public CampaignSentLogDTO(DTO dto) {
        super.putAll(dto);
    }

    public CampaignSentLogDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new CampaignSentLogDTO();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGNSENTLOG;
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNSENTLOGID;
    }

    public String getTableName() {
        return CampaignConstants.TABLE_CAMPAIGNSENTLOG;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("generalMsg.NotFound");
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Referenced");
    }

    public HashMap referencedValues() {
        HashMap m = new HashMap();
        return m;
    }
}
