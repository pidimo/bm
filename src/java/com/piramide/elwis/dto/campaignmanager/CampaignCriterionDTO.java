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
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignCriterionDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class CampaignCriterionDTO extends ComponentDTO implements IntegrityReferentialDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_CAMPAIGNCRITERIONID = "campaignCriterionId";

    /**
     * Creates an instance.
     */
    public CampaignCriterionDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CampaignCriterionDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public CampaignCriterionDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNCRITERIONID;
    }


    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGNCRITERION;
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

        resultDTO.addResultMessage("campCriteriaMsg.NotFound");
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }

    public void parseValues() {

        if ("".equals(this.get("numberHits"))) {
            this.put("numberHits", null);
        }

        if ("".equals(this.get("categoryId"))) {
            this.put("categoryId", null);
        }

        if (" ".equals(this.get("campCriterionValueId")) || "".equals(this.get("campCriterionValueId"))) {
            this.put("campCriterionValueId", null);
        }

        super.convertPrimKeyToInteger();
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Referenced");
    }
}
