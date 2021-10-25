package com.piramide.elwis.dto.financemanager;

import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author: ivan
 * <p/>
 * Jatun S.R.L
 */
public class InvoiceReminderDTO extends ComponentDTO {
    public static final String INVOICEREMINDERPK = "reminderId";

    public InvoiceReminderDTO() {
    }

    public InvoiceReminderDTO(DTO dto) {
        super.putAll(dto);
    }

    public InvoiceReminderDTO(Integer reminderId) {
        setPrimKey(reminderId);
    }

    public ComponentDTO createDTO() {
        return new InvoiceReminderDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_INVOICEREMINDER;
    }

    public String getPrimKeyName() {
        return INVOICEREMINDERPK;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("remindelLevelName"));
    }
}
