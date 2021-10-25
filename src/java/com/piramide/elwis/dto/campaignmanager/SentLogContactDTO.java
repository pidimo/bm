package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class SentLogContactDTO extends ComponentDTO {
    public static final String KEY_SENTLOGCONTACTID = "sentLogContactId";

    public SentLogContactDTO() {
    }

    public SentLogContactDTO(DTO dto) {
        super.putAll(dto);
    }

    public SentLogContactDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new SentLogContactDTO();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_SENTLOGCONTACT;
    }

    public String getPrimKeyName() {
        return KEY_SENTLOGCONTACTID;
    }

    public String getTableName() {
        return CampaignConstants.TABLE_SENTLOGCONTACT;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("generalMsg.NotFound");
    }

}
