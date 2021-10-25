package com.piramide.elwis.dto.supportmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 12, 2005
 * Time: 9:56:01 AM
 * To change this template use File | Settings | File Templates.
 */

public class SupportCaseDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_SUPPORT_CASEID = "caseId";

    /**
     * Creates an instance.
     */
    public SupportCaseDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SupportCaseDTO(DTO dto) {
        super.putAll(dto);
    }

    public SupportCaseDTO(Integer id) {
        setPrimKey(id);
    }

    public SupportCaseDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_SUPPORT_CASEID;
    }

    public String getJNDIName() {
        return SupportConstants.JNDI_SUPPORT_CASE;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("caseTitle"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SupportConstants.TABLE_SUPPORT_ATTACH, "caseid");
        tables.put(SupportConstants.TABLE_SUPPORT_CONTACT, "caseid");
        tables.put(SupportConstants.TABLE_SUPPORT_CASE_ACTIVITY, "caseid");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("caseTitle"));
    }

    public ComponentDTO createDTO() {
        return new SupportCaseDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}

