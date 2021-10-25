package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * @author: ivan
 * Date: 01-11-2006: 01:54:40 PM
 */
public class CampaignActivityContactDTO extends ComponentDTO {
    public static final String KEY_CAMPAIGNACTIVITYCONTACT = "campaignActivityContactPK";


    public CampaignActivityContactDTO() {
    }


    public CampaignActivityContactDTO(DTO dto) {
        super(dto);
    }

    public ComponentDTO createDTO() {
        return new CampaignTextDTO();
    }

    public CampaignActivityContactDTO(Integer key) {

    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGNACTIVITYCONTACT;
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNACTIVITYCONTACT;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}
