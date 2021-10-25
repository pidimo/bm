package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignGenAttachDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignGenAttachDTO extends ComponentDTO {
    public static final String KEY_CAMPAIGNGENATTACHID = "campaignGenAttachPK";

    public CampaignGenAttachDTO() {
    }

    public CampaignGenAttachDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new CampaignGenAttachDTO();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGNGENATTACH;
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNGENATTACHID;
    }

    public String getTableName() {
        return CampaignConstants.TABLE_CAMPAIGNGENATTACH;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
