package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.8
 */
public class CampaignCriterionValueDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_CAMPCRITERIONVALUEID = "campCriterionValueId";

    public CampaignCriterionValueDTO() {
    }

    public CampaignCriterionValueDTO(DTO dto) {
        super.putAll(dto);
    }

    public CampaignCriterionValueDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new CampaignCriterionValueDTO();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGNCRITERIONVALUE;
    }

    public String getPrimKeyName() {
        return KEY_CAMPCRITERIONVALUEID;
    }

    public String getTableName() {
        return CampaignConstants.TABLE_CAMPAIGNCRITERIONVALUE;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("campCriterionValueId"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("campCriterionValueId"));
    }

    public HashMap referencedValues() {
        HashMap m = new HashMap();
        return m;
    }
}
