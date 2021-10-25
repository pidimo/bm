package com.piramide.elwis.dto.salesmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Action type DTO
 *
 * @author Fernando Montaño
 * @version $Id: ActionTypeDTO.java 9122 2009-04-17 00:31:07Z fernando $
 */
public class ActionTypeDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String ACTIONTYPEID = "actionTypeId";

    /**
     * Creates an instance.
     */
    public ActionTypeDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ActionTypeDTO(DTO dto) {
        super.putAll(dto);
    }


    public String getPrimKeyName() {
        return ACTIONTYPEID;
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_ACTIONTYPE;
    }


    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("actionTypeName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("actionTypeName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("actionTypeName"));
    }

    public ComponentDTO createDTO() {
        return new ActionTypeDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {

        HashMap tables = new HashMap();
        tables.put(SalesConstants.TABLE_ACTION, "actiontypeid");
        return tables;
    }

}