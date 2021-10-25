package com.piramide.elwis.dto.supportmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.Translate;
import com.piramide.elwis.dto.common.ValidateCatalog;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 12, 2005
 * Time: 10:09:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class StateDTO extends ComponentDTO implements IntegrityReferentialDTO, ValidateCatalog, Translate {
    public static final String KEY_STATEID = "stateId";

    /**
     * Creates an instance.
     */
    public StateDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public StateDTO(DTO dto) {
        super.putAll(dto);
    }

    public StateDTO(Integer id) {
        setPrimKey(id);
    }

    public StateDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_STATEID;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_STATE;
    }


    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("stateName"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SupportConstants.TABLE_SUPPORT_CASE, "stateid");
        tables.put(SupportConstants.TABLE_CASE_ACTIVITY, "stateid");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        if (!containsKey("checkEditReference")) {
            resultDTO.addResultMessage("customMsg.Referenced", get("stateName"));
        } else {
            resultDTO.addResultMessage("SupportCase.state.edit.Referenced", get("stateName"));
        }
    }

    public ComponentDTO createDTO() {
        return new StateDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public boolean evaluation() {
        boolean evaluation = false;
        if ("update".equals(getAsString("op"))) {
            String oldType = getAsString("oldStageType");
            String newType = getAsString("stageType");
            evaluation = !newType.equals(oldType);
            put("checkEditReference", "true");
        }
        return evaluation;
    }


    public String fieldToTranslate() {
        return "stateName";
    }

    public String fieldOfLanguaje() {
        return "languageId";
    }

    public String fieldRelatedWithObject() {
        return "langTextId";
    }
}

