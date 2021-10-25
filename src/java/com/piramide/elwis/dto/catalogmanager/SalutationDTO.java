package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Salutation DTO
 *
 * @author yumi
 * @version $Id: SalutationDTO.java 9310 2009-05-29 23:43:06Z miguel $
 */

public class SalutationDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {

    public static final String KEY_SALUTATIONLIST = "salutationList";
    public static final String KEY_SALUTATIONID = "salutationId";

    /**
     * Creates an instance.
     */
    public SalutationDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SalutationDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty SalutationDTO with specified languageId
     */
    public SalutationDTO(Integer salutationId) {
        setPrimKey(salutationId);
    }

    public String getPrimKeyName() {
        return KEY_SALUTATIONID;
    }

    public String getDTOListName() {
        return KEY_SALUTATIONLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_SALUTATION;
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
        resultDTO.addResultMessage("msg.Duplicated", get("salutationLabel"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("salutationLabel"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("salutationLabel"));
    }

    public ComponentDTO createDTO() {
        return new SalutationDTO();
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("salutationLabel", "salutationlabel");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_SALUTATIONID, "salutationid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_SALUTATION;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_ADDRESS, "salutationid");
        tables.put(ContactConstants.TABLE_COMPANY, "salutationid");

        return tables;
    }

}