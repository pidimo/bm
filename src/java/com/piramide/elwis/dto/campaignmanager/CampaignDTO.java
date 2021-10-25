package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

/**
 * @author Yumi
 * @version $Id: CampaignDTO.java 10285 2012-11-28 05:59:23Z miguel $
 */
public class CampaignDTO extends ComponentDTO implements IntegrityReferentialDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_CAMPAIGNID = "campaignId";

    /**
     * Creates an instance.
     */
    public CampaignDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CampaignDTO(DTO dto) {
        super.putAll(dto);
    }

    public CampaignDTO(Integer id) {
        setPrimKey(id);
    }

    public CampaignDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNID;
    }


    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGN;
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
        resultDTO.addResultMessage("customMsg.NotFound", get("campaignName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("campaignName"));
    }

    public HashMap referencedValues() {
        HashMap m = new HashMap();
        m.put(CampaignConstants.TABLE_CAMPAIGNACTIVITYCONTACT, "campaignid");
        m.put(CampaignConstants.TABLE_CAMPAIGNGENERATION, "campaignid");
        /*m.put(CampaignConstants.TABLE_CAMPAIGNACTIVITY, "campaignid");*/
        return m;
    }

    public ComponentDTO createDTO() {
        return new CampaignDTO();
    }


    public void parseValues() {
        if ("".equals(this.get("contactPersonId"))) {
            this.put("contactPersonId", null);
        }
        if ("".equals(this.get("remarkValue"))) {
            this.put("remark", null);
        }
        super.convertPrimKeyToInteger();
    }

}

