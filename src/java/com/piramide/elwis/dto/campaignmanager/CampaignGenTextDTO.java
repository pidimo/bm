package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;


/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignGenTextDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignGenTextDTO extends ComponentDTO {
    public static final String KEY_CAMPAIGNGENTEXTID = "campaignGenTextId";

    public CampaignGenTextDTO() {
    }

    public CampaignGenTextDTO(DTO dto) {
        super.putAll(dto);
    }

    public CampaignGenTextDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new CampaignGenTextDTO();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGNGENTEXT;
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNGENTEXTID;
    }

    public String getTableName() {
        return CampaignConstants.TABLE_CAMPAIGNGENTEXT;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
