package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.ReferentialPK;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignActivityUserDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignActivityUserDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_CAMPAIGNACTIVITYUSERID = "activityUserPK";

    /**
     * Creates an instance.
     */
    public CampaignActivityUserDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CampaignActivityUserDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty CampaignActivityUserDTO with specified Id
     */
    public CampaignActivityUserDTO(Integer userId) {
        setPrimKey(userId);
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNACTIVITYUSERID;
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGN_ACTIVITY_USER;
    }

    public String getTableName() {
        return CampaignConstants.TABLE_CAMPAIGNACTIVITYUSER;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {
    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("userName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("userName"));
    }

    public ComponentDTO createDTO() {
        return new CampaignActivityUserDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(CampaignConstants.TABLE_CAMPAIGNCONTACT,
                ReferentialPK.create()
                        .addKey("activityId", "activityid")
                        .addKey("userId", "userid"));
        return tables;
    }
}