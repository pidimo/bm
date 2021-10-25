package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignActivityDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignActivityDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_ACTIVITYID = "activityId";

    public CampaignActivityDTO() {
    }

    public CampaignActivityDTO(DTO dto) {
        super.putAll(dto);
    }

    public CampaignActivityDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new CampaignActivityDTO();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGN_ACTIVITY;
    }

    public String getPrimKeyName() {
        return KEY_ACTIVITYID;
    }

    public String getTableName() {
        return CampaignConstants.TABLE_CAMPAIGNACTIVITY;
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
        /*m.put(CampaignConstants.TABLE_CAMPAIGNACTIVITYUSER, "activityid");*/
        m.put(CampaignConstants.TABLE_CAMPAIGNACTIVITYCONTACT, "activityid");
        /*m.put(CampaignConstants.TABLE_CAMPAIGNCONTACT, "activityid");*/
        m.put(SchedulerConstants.TABLE_TASK, "activityid");
        m.put(SalesConstants.TABLE_SALESPROCESS, "campactivityid");

        return m;
    }
}
