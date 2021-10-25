package com.piramide.elwis.dto.salesmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a sales process Priority DTO
 *
 * @author Fernando Montaño
 * @version $Id: PriorityDTO.java 9122 2009-04-17 00:31:07Z fernando $
 */

public class PriorityDTO extends ComponentDTO implements IntegrityReferentialDTO {

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


    public String getJNDIName() {
        return SalesConstants.JNDI_PRIORITY;
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

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SalesConstants.TABLE_SALESPROCESS, "priorityid");
        return tables;
    }
}