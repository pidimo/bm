package com.piramide.elwis.dto.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miky
 * @version $Id: CampaignGenTextImgDTO.java 2009-06-26 06:12:14 PM $
 */
public class CampaignGenTextImgDTO extends ComponentDTO {
    public static final String KEY_CAMPAIGNGENTEXTIMGID = "campaignGenTextImgPK";

    public CampaignGenTextImgDTO() {
    }

    public CampaignGenTextImgDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new CampaignTextImgDTO();
    }

    public String getJNDIName() {
        return CampaignConstants.JNDI_CAMPAIGNGENTEXTIMG;
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNGENTEXTIMGID;
    }

    public String getTableName() {
        return CampaignConstants.TABLE_CAMPAIGNGENTEXTIMG;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
