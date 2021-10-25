package com.piramide.elwis.dto.salesmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.ReferentialPK;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Action type DTO
 *
 * @author Fernando Montaño
 * @version $Id: ActionDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ActionDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String ACTIONPK = "actionPK";

    /**
     * Creates an instance.
     */
    public ActionDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ActionDTO(DTO dto) {
        super.putAll(dto);
    }


    public String getPrimKeyName() {
        return ACTIONPK;
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_ACTION;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("note"));
    }


    public ComponentDTO createDTO() {
        return new ActionDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SalesConstants.TABLE_SALE,
                ReferentialPK.create()
                        .addKey("processId", "processid")
                        .addKey("contactId", "contactid"));
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("note"));
    }
}