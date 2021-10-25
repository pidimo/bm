package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignGenerationDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignGenerationDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_GENERATIONID = "generationId";

    public CampaignGenerationDTO() {
    }

    public CampaignGenerationDTO(DTO dto) {
        super.putAll(dto);
    }

    public CampaignGenerationDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new CampaignGenerationDTO();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGNGENERATION;
    }

    public String getPrimKeyName() {
        return KEY_GENERATIONID;
    }

    public String getTableName() {
        return CampaignConstants.TABLE_CAMPAIGNGENERATION;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("title"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("title"));
    }

    public HashMap referencedValues() {
        HashMap m = new HashMap();
        m.put(CampaignConstants.TABLE_CAMPAIGNACTIVITYCONTACT, "generationid");
        m.put(CampaignConstants.TABLE_CAMPAIGNGENTEXT, "generationid");

        return m;
    }
}
