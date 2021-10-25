package com.piramide.elwis.dto.financemanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ReminderLevelDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String REMINDERLEVELPK = "reminderLevelId";


    public ReminderLevelDTO() {
    }

    public ReminderLevelDTO(DTO dto) {
        super.putAll(dto);
    }

    public ReminderLevelDTO(Integer id) {
        setPrimKey(id);
    }

    public ComponentDTO createDTO() {
        return new ReminderLevelDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_REMINDERLEVEL;
    }

    public String getPrimKeyName() {
        return REMINDERLEVELPK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(FinanceConstants.TABLE_INVOICEREMINDER, "reminderlevelid");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("name"));
    }
}
