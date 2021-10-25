package com.piramide.elwis.dto.financemanager;

import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ReminderTextDTO extends ComponentDTO {
    public static final String REMINDERTEXTPK = "reminderTextPK";

    public ReminderTextDTO() {
    }

    public ReminderTextDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new ReminderTextDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_REMINDERTEXT;
    }

    public String getPrimKeyName() {
        return REMINDERTEXTPK;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("TemplateFile.error.find");
    }
}
