package com.piramide.elwis.dto.salesmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Status DTO
 *
 * @author Fernando Montaño
 * @version $Id: StatusDTO.java 9122 2009-04-17 00:31:07Z fernando $
 */
public class StatusDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String STATUSID = "statusId";

    /**
     * Creates an instance.
     */
    public StatusDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public StatusDTO(DTO dto) {
        super.putAll(dto);
    }


    public String getPrimKeyName() {
        return STATUSID;
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_STATUS;
    }


    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("statusName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("statusName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("statusName"));
    }

    public ComponentDTO createDTO() {
        return new StatusDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {

        HashMap tables = new HashMap();
        tables.put(SalesConstants.TABLE_SALESPROCESS, "statusid");
        return tables;
    }
}