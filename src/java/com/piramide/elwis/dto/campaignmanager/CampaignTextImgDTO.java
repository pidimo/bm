package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miky
 * @version $Id: CampaignTextImgDTO.java 2009-06-23 05:27:58 PM $
 */
public class CampaignTextImgDTO extends ComponentDTO {
    public static final String KEY_CAMPAIGNTEXTIMGID = "campaignTextImgPK";

    public CampaignTextImgDTO() {
    }

    public CampaignTextImgDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new CampaignTextImgDTO();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGNTEXTIMG;
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNTEXTIMGID;
    }

    public String getTableName() {
        return CampaignConstants.TABLE_CAMPAIGNTEXTIMG;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
