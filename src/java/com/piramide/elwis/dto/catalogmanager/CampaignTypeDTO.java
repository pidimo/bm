package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ivan
 * Date: 23-10-2006: 03:50:55 PM
 */
public class CampaignTypeDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {
    public static final String KEY_CAMPAIGNTYPEID = "campaignTypeId";

    public CampaignTypeDTO() {
    }

    public CampaignTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    public CampaignTypeDTO(Integer id) {
        setPrimKey(id);
    }

    public ComponentDTO createDTO() {
        return new CampaignTypeDTO();
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_CAMPAIGNTYPE;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public String getPrimKeyName() {
        return KEY_CAMPAIGNTYPEID;
    }

    public HashMap referencedValues() {
        Map m = new HashMap();
        m.put(CampaignConstants.TABLE_CAMPAIGN, "typeid");
        return ((HashMap) m);
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("title"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("title"));
    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("title"));
    }


    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("title", "title");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_CAMPAIGNTYPE;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_CAMPAIGNTYPEID, "camtypeid");
        return values;
    }
}
