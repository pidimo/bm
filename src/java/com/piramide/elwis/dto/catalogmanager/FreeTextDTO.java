package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a FreeText DTO
 *
 * @author Ivan
 * @version $Id: FreeTextDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */

public class FreeTextDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_FREETEXTLIST = "freeTextList";
    public static final String KEY_FREETEXTID = "freeTextId";

    /**
     * Creates an instance.
     */
    public FreeTextDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public FreeTextDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty FreeTextDTO with specified languageId
     */
    public FreeTextDTO(Integer freetextId) {
        setPrimKey(freetextId);
    }

    public String getPrimKeyName() {
        return KEY_FREETEXTID;
    }

    public String getDTOListName() {
        return KEY_FREETEXTLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_FREETEXT;
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
        resultDTO.addResultMessage("msg.Duplicated", "FreeText.id");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", "FreeText");
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", "FreeText");
    }

    public ComponentDTO createDTO() {
        return new FreeTextDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public String getTableName() {
        return CatalogConstants.TABLE_FREETEXT;
    }

    public HashMap referencedValues() {

        HashMap tables = new HashMap();
        tables.put(CatalogConstants.TABLE_TEMPLATETEXT, "freetextid");
        tables.put(ContactConstants.TABLE_CONTACT, "freetextid");
        tables.put(CampaignConstants.TABLE_CAMPAIGNTEXT, "freetextid");
        tables.put(CampaignConstants.TABLE_CAMPAIGNCRITERION, "valueid");
        tables.put(CampaignConstants.TABLE_CAMPAIGN, "remark");
        return tables;
    }

}