package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.Translate;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a TelecomType DTO
 *
 * @author yumi
 * @version $Id: TelecomTypeDTO.java 10285 2012-11-28 05:59:23Z miguel $
 */

public class TelecomTypeDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO, Translate {

    public static final String KEY_TELECOMTYPELIST = "telecomTypeList";
    public static final String KEY_TELECOMTYPEID = "telecomTypeId";

    /**
     * Creates an instance.
     */
    public TelecomTypeDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public TelecomTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty TelecomTypeDTO with specified languageId
     */
    public TelecomTypeDTO(Integer telecomTypeId) {
        setPrimKey(telecomTypeId);
    }

    public String getPrimKeyName() {
        return KEY_TELECOMTYPEID;
    }

    public String getDTOListName() {
        return KEY_TELECOMTYPELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_TELECOMTYPE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("telecomTypeName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("telecomTypeName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("telecomTypeName"));
    }

    public ComponentDTO createDTO() {
        return new TelecomTypeDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_TELECOMTYPEID, "telecomtypeid");
        return values;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("telecomTypeName", "telecomtypename");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_TELECOMTYPE;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_TELECOM, "telecomtypeid");
        tables.put(CampaignConstants.TABLE_CAMPAIGNGENERATION, "telecomtypeid");
        return tables;
    }


    public String fieldToTranslate() {
        return "telecomTypeName";
    }

    public String fieldOfLanguaje() {
        return "languageId";
    }

    public String fieldRelatedWithObject() {
        return "langTextId";
    }
}