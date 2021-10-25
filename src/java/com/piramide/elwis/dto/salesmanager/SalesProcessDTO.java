package com.piramide.elwis.dto.salesmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.ReferentialPK;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Action type DTO
 *
 * @author Fernando Montaño
 * @version $Id: SalesProcessDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class SalesProcessDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String SALESPROCESSID = "processId";

    /**
     * Creates an instance.
     */
    public SalesProcessDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SalesProcessDTO(DTO dto) {
        super.putAll(dto);
    }


    public String getPrimKeyName() {
        return SALESPROCESSID;
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_SALESPROCESS;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (get("processName") != null) {
            resultDTO.addResultMessage("msg.NotFound", get("processName"));
        } else {
            resultDTO.addResultMessage("SalesProcess.NotFound");
        }
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("processName"));
    }

    public ComponentDTO createDTO() {
        return new SalesProcessDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {

        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_CONTACT,
                ReferentialPK.create()
                        .addKey("processId", "processid")
                        .addKey("isAction", "isaction"));
        tables.put(SchedulerConstants.TABLE_TASK, "processid");
        tables.put(SalesConstants.TABLE_SALE, "processid");

        return tables;
    }

}