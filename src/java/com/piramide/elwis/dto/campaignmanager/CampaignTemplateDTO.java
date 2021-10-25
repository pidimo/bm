package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author: ivan
 * Date: 30-10-2006: 04:26:59 PM
 */
public class CampaignTemplateDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_CAMPAIGNTEMPLATEID = "templateId";

    public CampaignTemplateDTO() {
    }

    public CampaignTemplateDTO(DTO dto) {
        super.putAll(dto);
    }

    public CampaignTemplateDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new CampaignTextDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGN_TEMPLATE;
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNTEMPLATEID;
    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("description"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("description"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("description"));
    }

    public HashMap referencedValues() {
        HashMap m = new HashMap();
        m.put(CampaignConstants.TABLE_CAMPAIGNGENERATION, "templateid");
        return m;
    }
}
