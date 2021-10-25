package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.Translate;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Priority DTO
 *
 * @author Ivan
 * @version $Id: PriorityDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class PriorityDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO, Translate {
    public static final String KEY_PRIORITYID = "priorityId";

    /**
     * Creates an instance.
     */
    public PriorityDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public PriorityDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty SalutationDTO with specified languageId
     */
    public PriorityDTO(Integer priorityId) {
        setPrimKey(priorityId);
    }

    public String getPrimKeyName() {
        return KEY_PRIORITYID;
    }

    /* public String getDTOListName() {
         return KEY_PRIORITYLIST;
     }*/

    public String getJNDIName() {
        return CatalogConstants.JNDI_PRIORITY;
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
        resultDTO.addResultMessage("msg.Duplicated", get("priorityName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("priorityName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("priorityName"));
    }

    public ComponentDTO createDTO() {
        return new PriorityDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("priorityName", "priorityname");
        map.put("type", "type");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_PRIORITYID, "priorityid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_PRIORITY;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_SUPPLIER, "priorityid");
        tables.put(ContactConstants.TABLE_CUSTOMER, "priorityid");
        tables.put(SchedulerConstants.TABLE_APPOINTMENT, "priorityid");
        tables.put(SchedulerConstants.TABLE_TASK, "priorityid");
        tables.put(SupportConstants.TABLE_SUPPORT_CASE, "priorityid");

        return tables;
    }

    public String fieldToTranslate() {
        return "priorityName";
    }

    public String fieldOfLanguaje() {
        return "languageId";
    }

    public String fieldRelatedWithObject() {
        return "langTextId";
    }
}