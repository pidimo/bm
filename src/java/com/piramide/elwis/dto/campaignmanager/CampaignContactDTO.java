package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Yumi
 * @version $Id: CampaignContactDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class CampaignContactDTO extends ComponentDTO {

    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_CAMPAIGNCONTACTID = "campaignContactPK";

    /**
     * Creates an instance.
     */
    public CampaignContactDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CampaignContactDTO(DTO dto) {
        super.putAll(dto);
    }

    public CampaignContactDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNCONTACTID;
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGNCONTACT;
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
        resultDTO.addResultMessage("customMsg.NotFound", get("contactName"));
    }

    public ComponentDTO createDTO() {
        return new CampaignContactDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}

