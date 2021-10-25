package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignTextDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignTextDTO extends ComponentDTO {


    public static final String KEY_CAMPAIGNTEXTLIST = "campaignTextList";
    public static final String KEY_CAMPAIGNTEXTID = "campaignTextPK";

    /**
     * Creates an instance.
     */
    public CampaignTextDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CampaignTextDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty TemplateTextDTO with specified templatetextid
     */
    public CampaignTextDTO(Integer campaignTextId) {
        setPrimKey(campaignTextId);
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNTEXTID;
    }

    public String getDTOListName() {
        return KEY_CAMPAIGNTEXTLIST;
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGN_TEXT;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {
    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {
    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {
    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", "CampaignText.id");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", "CampaignText");
    }

    public ComponentDTO createDTO() {
        return new CampaignTextDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}
